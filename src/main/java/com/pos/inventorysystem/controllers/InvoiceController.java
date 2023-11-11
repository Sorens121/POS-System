package com.pos.inventorysystem.controllers;

import com.pos.inventorysystem.Model.Items;
import com.pos.inventorysystem.Model.Product;
import com.pos.inventorysystem.actions.InvoiceActions;
import com.pos.inventorysystem.constants.InvoiceConstants;
import com.pos.inventorysystem.helpers.InvoiceHelper;
import com.pos.inventorysystem.utils.ConfigFileManager;
import com.pos.inventorysystem.utils.DialogBoxUtility;
import com.pos.inventorysystem.utils.GenericUtils;
import com.pos.inventorysystem.utils.TableUtility;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class InvoiceController {
    @FXML
    private AnchorPane anchorSpace;

    @FXML
    private TextField balanceAmtField;

    @FXML
    private TextField customer_name;

    @FXML
    private TextField customer_no;

    @FXML
    private TextField totalAmtField;

    @FXML
    private TextField paidAmtField;

    @FXML
    private TextField quantity_field;

    @FXML
    private TextField search_customer_field;

    @FXML
    private TextField search_product_field_1;

    @FXML
    private Button search_product_btn_1;

    @FXML
    private Label invoiceNo;

    @FXML
    private Label onDateLabel;

    @FXML
    private Label unitPriceLabel;

    @FXML
    private StackPane tableArea;

    @FXML
    private Button addToCartBtn;

    @FXML
    private Button checkoutBtn;

    @FXML
    private Button removeAllBtn;

    @FXML
    private Button removeBtn;

    private final TableView<Items> invoiceItemsTable = new TableView<>();

    private final ObservableList<Items> itemList = FXCollections.observableArrayList();

    private boolean isTotalAmtCalculated = false;

    private DialogBoxUtility dialogBoxUtility = new DialogBoxUtility();

    private String bucket_id = "";

    private Stage mainStage;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    void initialize() throws SQLException {
        ConfigFileManager configFileManager = new ConfigFileManager();

        try {
            boolean tableCheckFlag = Boolean.parseBoolean(configFileManager.getProperty("invoice_table.flag"));

            if(!tableCheckFlag) {
                TableUtility.createTable(InvoiceConstants.CREATE_INVOICE_TABLE);
                TableUtility.createTable(InvoiceConstants.CREATE_INVOICE_ITEMS_TABLE);
                configFileManager.setProperty("invoice_table.flag", "true");
                configFileManager.setProperty("items_table.flag", "true");
                System.out.println("Table create successfully");
            } else {
                System.out.println("Table already exists");
            }
        } catch (SQLException e) {
            System.out.println("Database error occurred " + e.getMessage());
        }

        customer_no.setDisable(true);
        customer_name.setDisable(true);
        customer_no.setStyle("-fx-text-fill: #1a1aff; -fx-opacity: 1.0");
        customer_name.setStyle("-fx-text-fill: #1a1aff; -fx-opacity: 1.0");
        totalAmtField.setStyle("-fx-text-fill: #1a1aff; -fx-opacity: 1.0");

        //initializing items table
        initializeTable();
        updateButtonState();
        createInvoice();

        // set quantity field when row is selected
        invoiceItemsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                quantity_field.setText(String.valueOf(newValue.getQuantity()));
                unitPriceLabel.setText(String.valueOf(newValue.getUnitPrice()));
            }
        });

        //search customer
        search_customer_field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        String customerSearchInput = search_customer_field.getText();
                        //System.out.println("Enter button pressed and the value is : " + customerSearchInput);
                        customerPopUP(customerSearchInput);

                        search_customer_field.clear();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        //search product field
        search_product_field_1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchString = search_product_field_1.getText().trim();
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    searchPopUP(searchString);

                    search_product_field_1.clear();
                }
            }
        });

        //search product button 1
        search_product_btn_1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String searchString = search_product_field_1.getText().trim();
                searchPopUP(searchString);

                search_product_field_1.clear();
            }
        });

        // quantity
        quantity_field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    int setQuantity = Integer.parseInt(quantity_field.getText().trim());
                    Items selectedItem = invoiceItemsTable.getSelectionModel().getSelectedItem();
                    //System.out.println("quantity : " + setQuantity);
                    if (selectedItem != null) {
                        updateProductList(selectedItem, setQuantity);
                    }
                    quantity_field.clear();
                }
            }
        });

        //amount paid field to calculate balance
        paidAmtField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                double balance = 0.0;
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    double totalAmt = Double.parseDouble(totalAmtField.getText().trim());
                    double paidAmt = Double.parseDouble(paidAmtField.getText().trim());

                    if (paidAmt > totalAmt && totalAmt != 0.0) {
                        balance = paidAmt - totalAmt;
                    }

                    balanceAmtField.setText(String.valueOf(balance));
                }
            }
        });

    }

    private void createInvoice () {
        // generate invoice id
        String invoice_id = invoiceNo.getText().trim();
        if(invoice_id.isEmpty() || invoice_id.isBlank() || invoice_id.equals("00")) {
            invoice_id = GenericUtils.GenerateInvoiceNo();
            invoiceNo.setText(invoice_id);
            // set invoice date
            String date = GenericUtils.GenerateDate();
            onDateLabel.setText(date);
        }

    }

    private void updateButtonState() {
        if(itemList.size() == 0) {
            addToCartBtn.setDisable(true);
            removeBtn.setDisable(true);
            removeAllBtn.setDisable(true);
            checkoutBtn.setDisable(true);
        } else {
            addToCartBtn.setDisable(false);
            removeBtn.setDisable(false);
            removeAllBtn.setDisable(false);
        }

        if(isTotalAmtCalculated) {
            checkoutBtn.setDisable(false);
        }
    }

    private void clearFields () {
        customer_no.clear();
        customer_name.clear();
        quantity_field.clear();
        totalAmtField.setText("00");
        paidAmtField.setText("00");
        onDateLabel.setText("00-00-00");
        balanceAmtField.setText("00");
        unitPriceLabel.setText("00");
        invoiceNo.setText("00");

        itemList.clear();
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() { // <- this should be list of items
        TableColumn<Items, Integer> serial_no = new TableColumn<>("serial no".toUpperCase());
        TableColumn<Items, String> product_name = TableUtility.createTableColumn("product name".toUpperCase(), Items::getItemName);
        TableColumn<Items, String> item_id = TableUtility.createTableColumn("barcode".toUpperCase(), Items::getItemId);
        TableColumn<Items, Integer> quantity = TableUtility.createTableColumn("qty".toUpperCase(), Items::getQuantity);
        TableColumn<Items, Double> unit_price = TableUtility.createTableColumn("Unit price".toUpperCase(), Items::getUnitPrice);
        TableColumn<Items, Double> total_price = TableUtility.createTableColumn("total price".toUpperCase(), Items::getTotalPrice);

        serial_no.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(invoiceItemsTable.getItems().indexOf(cellData.getValue()) + 1));

        serial_no.setPrefWidth(80);
        product_name.setPrefWidth(200);
        item_id.setPrefWidth(150);
        quantity.setPrefWidth(60);
        unit_price.setPrefWidth(100);
        total_price.setPrefWidth(100);

        invoiceItemsTable.getColumns().addAll(serial_no, product_name, item_id, quantity, unit_price, total_price);
        tableArea.getChildren().add(invoiceItemsTable);
    }


    @FXML
    void onAddToCart() {
        // create a bucket_id
        if(bucket_id.isEmpty() || bucket_id.isBlank() || Objects.equals(bucket_id, "")){
            bucket_id = GenericUtils.GenerateBucketId();
        }

        //System.out.println("bucket_id: " + bucket_id);

        double totalPrice = InvoiceHelper.getTotalBill(itemList);
        totalAmtField.setText(String.valueOf(totalPrice));
        isTotalAmtCalculated = true;
        updateButtonState();
    }

    @FXML
    void onPayAndPrint(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        try {
            InvoiceActions actions = new InvoiceActions();
            String invoice_id = invoiceNo.getText().trim();
            String customer_id = customer_no.getText().trim();
            String date = onDateLabel.getText().trim();
            double total_amount = Double.parseDouble(totalAmtField.getText().trim());

            //System.out.println("Invoice id: " + invoice_id + ", Customer Id: " + customer_id + ", Date: " + date + ", Total Amount: " + total_amount + "bucket_id: " + bucket_id);
            Map<String, Object> invoiceData = InvoiceHelper.consolidateItems(invoice_id, customer_id, date, total_amount, bucket_id);

            /*
                Print the resulting Map
                for (Map.Entry<String, Object> entry : invoiceData.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                }
            */

            int result = actions.createNewRecord(invoiceData, itemList);

            if (result == 1) {
                System.out.println("Successfully inserted the data");
                dialogBoxUtility.showDialogBox(1);
            } else {
                System.out.println("Error inserting data");
                dialogBoxUtility.showDialogBox(0);
            }

            clearFields();
            createInvoice();
            checkoutBtn.setDisable(true);
            bucket_id = "";
        } catch (SQLException | IOException e) {
            System.out.println("Error running query");
            e.printStackTrace();
        }
    }

    @FXML
    void onRemove(ActionEvent event) {
        Items selectedItem = invoiceItemsTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            // Remove the selected item from the data model and recalculate total bill
            itemList.remove(selectedItem);

            double total_bill = InvoiceHelper.getTotalBill(itemList);
            totalAmtField.setText(String.valueOf(total_bill));
        }

        if(itemList.size() == 0) {
            isTotalAmtCalculated = false;
            clearFields();
        }
        updateButtonState();
    }

    @FXML
    void onRemoveAll(ActionEvent event) {
        itemList.clear();
        // recalculate total bill
        double total_bill = InvoiceHelper.getTotalBill(itemList);
        totalAmtField.setText(String.valueOf(total_bill));
        isTotalAmtCalculated = false;
        updateButtonState();
        clearFields();
    }

    private void searchPopUP(String searchProduct) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pos/inventorysystem/popupTable.fxml"));
        Parent popupTable = null;
        try {
            popupTable = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Error loading table layout");
        }

        PopUpTableController popUpTableController = loader.<PopUpTableController>getController();
        popUpTableController.searchProduct(searchProduct);
        popUpTableController.setInvoiceController(this);

        Stage popUpTableStage = new Stage();
        popUpTableStage.initModality(Modality.WINDOW_MODAL);
        popUpTableStage.initOwner(mainStage); // set the parent stage as the owner to make it a modal
        popUpTableStage.setTitle("Search Product");
        popUpTableStage.setScene(new Scene(popupTable));
        popUpTableStage.show();
    }

    // method to open child UI where invoice controller is the parent controller
    private void customerPopUP(String searchInput) throws SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pos/inventorysystem/customerPopUp.fxml"));
        Parent popupTable = null;

        try{
            popupTable = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Error loading table layout");
        }

        CustomerPopUpController customerPopUpController = loader.<CustomerPopUpController>getController();
        customerPopUpController.setInvoiceController(this); // Pass reference to the parent controller
        customerPopUpController.searchCustomer(searchInput);

        Stage popUpTableStage = new Stage();
        popUpTableStage.initModality(Modality.WINDOW_MODAL);
        popUpTableStage.initOwner(mainStage); // Set the parent stage as the owner to make it modal
        popUpTableStage.setTitle("Search Customer");
        popUpTableStage.setScene(new Scene(popupTable));
        popUpTableStage.showAndWait();
    }

    // Method to receive selected data from CustomerPopup
    public void setSelectedData(String id, String name) {
        customer_no.setText(id);
        customer_name.setText(name);
    }

    public void addToProductList(ObservableList<Product> product) {
        String quantity = quantity_field.getText().trim();

        if(quantity.isBlank() || quantity.isEmpty()) {
            quantity = "1";
        }

        String invoice_id = invoiceNo.getText().trim();
        Items newItem = InvoiceHelper.getItem(product, quantity, invoice_id);
        itemList.add(newItem);

        invoiceItemsTable.setItems(itemList);
        updateButtonState();
    }

    public void updateProductList(Items product, int quantity) {
        double update = product.getUnitPrice() * quantity;
        product.setTotalPrice(update);
        product.setQuantity(quantity);

        invoiceItemsTable.refresh();
        invoiceItemsTable.getSelectionModel().clearSelection();

        quantity_field.setText(String.valueOf(quantity));
    }

}
