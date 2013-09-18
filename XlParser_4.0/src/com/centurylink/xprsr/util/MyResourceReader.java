package com.centurylink.xprsr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyResourceReader {
    private Properties properties;
    private static MyResourceReader reader = null;

    private MyResourceReader(String requiredDatabase) {
        properties = new Properties();
        InputStream is = null;
        if (requiredDatabase.equalsIgnoreCase("Derby")) {
            is = getClass().getResourceAsStream(
                    "/" + "derbyDatabase.properties");
        } else if (requiredDatabase.equalsIgnoreCase("Oracle")) {
            is = getClass().getResourceAsStream(
                    "/" + "oracleDatabase.properties");
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            System.out.println("File not found!");
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public static MyResourceReader getInstance(String requiredDatabase) {
        if (reader == null) {
            reader = new MyResourceReader(requiredDatabase);
            return reader;
        } else
            return reader;
    }
}
