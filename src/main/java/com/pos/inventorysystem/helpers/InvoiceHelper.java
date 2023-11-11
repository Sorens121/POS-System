package com.pos.inventorysystem.helpers;

import com.pos.inventorysystem.Model.Items;
import com.pos.inventorysystem.Model.Product;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class InvoiceHelper {
    public static Items getItem(ObservableList<Product> product, String quantity, String invoice_id) {
        Items newItem = createItem(product);
        Double totalPrice = getTotalPrice(quantity, newItem.getUnitPrice());
        newItem.setQuantity(Integer.parseInt(quantity));
        newItem.setTotalPrice(totalPrice);
        newItem.setInvoiceId(invoice_id);

        return newItem;
    }

    // create item of items model from product model
    private static Items createItem(ObservableList<Product> product) {
        Items item = new Items();
        item.setItemName(product.get(0).getProductName());
        item.setItemId(product.get(0).getBarcode());
        item.setUnitPrice(Double.parseDouble(product.get(0).getPrice()));

        return item;
    }

    private static Double getTotalPrice (String quantity, Double unitPrice) {
        return Double.parseDouble(quantity) * unitPrice;
    }

    public static Double getTotalBill(ObservableList<Items> itemsList) {
        double total_bill = 0.0;
        if(itemsList.size() > 0) {
            for(Items item : itemsList) {
                total_bill += item.getTotalPrice();
            }
        }

        return total_bill;
    }

    public static Map<String, Object> consolidateItems(Object... keyValuePairs) {
        String[] keyData = {"invoice_id", "customer_id", "invoice_date", "total_amount", "bucket_id"};
        Map<String, Object> parameters = new HashMap<>();
        for(int i = 0; i < keyValuePairs.length; i ++) {
            String key = keyData[i];
            Object value = keyValuePairs[i];

            parameters.put(key, value);
        }

        return parameters;
    }
}
