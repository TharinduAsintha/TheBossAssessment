package com.theboss.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    /**
     * Returns the value for the given key, giving precedence to a JVM system
     * property (e.g. {@code -Dkey=value}) over the value in config.properties.
     *
     * @param key the property key to look up
     * @return the resolved value, or {@code null} if neither source defines it
     */
    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    /**
     * Same as {@link #get(String)} but returns {@code defaultValue} when the
     * key is undefined.
     *
     * @param key          the property key to look up
     * @param defaultValue the value to return when the key is not set
     * @return the resolved value or {@code defaultValue} if unset
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Reads an integer-valued property.
     *
     * @param key          the property key to look up
     * @param defaultValue the value to return when the key is not set
     * @return the parsed integer, or {@code defaultValue} if the key is unset
     * @throws NumberFormatException if the value cannot be parsed as an int
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value.trim());
    }

    /**
     * Reads a boolean-valued property.
     *
     * @param key          the property key to look up
     * @param defaultValue the value to return when the key is not set
     * @return the parsed boolean, or {@code defaultValue} if the key is unset
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }
}
