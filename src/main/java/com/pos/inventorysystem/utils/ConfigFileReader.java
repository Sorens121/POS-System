package com.pos.inventorysystem.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {
    private static final Properties properties = new Properties();

    static {
        try(FileInputStream fileInputStream = new FileInputStream("C:/Users/TROJAN HORSE/Java Project/POS Inventory System/src/main/resources/config.properties")){
            properties.load(fileInputStream);
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Config.properties file not found");
            properties.setProperty("database.url", "jdbc:mysql://localhost/pos_system");
            properties.setProperty("database.username", "root");
            properties.setProperty("database.password", "password123#");
            properties.setProperty("customer_table.flag","false");
            properties.setProperty("employee_table.flag","false");
            properties.setProperty("product_table.flag","false");
            properties.setProperty("sales_table.flag","false");
            properties.setProperty("supplier_table.flag","false");
            properties.setProperty("reports_table.flag","false");
        }
    }

    public static String getDatabaseUrl() {
        return properties.getProperty("database.url");
    }

    public static String getDatabaseUsername() {
        return properties.getProperty("database.username");
    }

    public static String getDatabasePassword() {
        return properties.getProperty("database.password");
    }

    public static Boolean getCustomerTableFlag() {
        String booleanValueAsString = properties.getProperty("customer_table.flag");
        return Boolean.parseBoolean(booleanValueAsString);
    }
    public static Boolean getEmployeeTableFlag() {
        String booleanValueAsString = properties.getProperty("employee_table.flag");
        return Boolean.parseBoolean(booleanValueAsString);
    }
    public static Boolean getProductTableFlag() {
        String booleanValueAsString = properties.getProperty("product_table.flag");
        return Boolean.parseBoolean(booleanValueAsString);
    }
    public static Boolean getSalesTableFlag() {
        String booleanValueAsString = properties.getProperty("sales_table.flag");
        return Boolean.parseBoolean(booleanValueAsString);
    }
    public static Boolean getSupplierTableFlag() {
        String booleanValueAsString = properties.getProperty("supplier_table.flag");
        return Boolean.parseBoolean(booleanValueAsString);
    }
    public static Boolean getReportsTableFlag() {
        String booleanValueAsString = properties.getProperty("reports_table.flag");
        return Boolean.parseBoolean(booleanValueAsString);
    }

}
