package com.pos.inventorysystem.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileManager {
    private final Properties properties;
    private final String path = "C:/Users/TROJAN HORSE/Java Project/POS Inventory System/src/main/resources/config.properties";

    public ConfigFileManager() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try(FileInputStream fileInputStream = new FileInputStream(path)){
            properties.load(fileInputStream);
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("config.properties file not found");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    private void saveProperties() {
        try(FileOutputStream fileOutputStream = new FileOutputStream(path)){
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("config.properties file not found");
        }
    }
}
