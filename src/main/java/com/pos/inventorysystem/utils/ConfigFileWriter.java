package com.pos.inventorysystem.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileWriter {

    private ConfigFileWriter() {
        // prevent object creation by declaring constructor private
    }
    private final static Properties properties = new Properties();

    private static void saveProperties() {
        try(FileOutputStream fileOutputStream = new FileOutputStream("C:/Users/TROJAN HORSE/Java Project/POS Inventory System/src/main/resources/config.properties")){
            properties.store(fileOutputStream, "update properties");
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("config.properties file not found");
        }
    }

    public static void setTableFlag(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

}
