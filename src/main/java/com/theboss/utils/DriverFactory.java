package com.theboss.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
    }

    /**
     * Resolves an app artifact path to an absolute path and verifies that the file exists.
     *
     * @param rawPath the configured app artifact path
     * @return the absolute path if provided, or null if the path is empty
     * @throws IllegalStateException if the app artifact does not exist
     */
    private static String resolveAppPath(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            return null;
        }
        File asFile = new File(rawPath);
        if (!asFile.isAbsolute()) {
            asFile = Paths.get(System.getProperty("user.dir"), rawPath).toFile();
        }
        if (!asFile.exists()) {
            throw new IllegalStateException(
                    "App artefact not found at: " + asFile.getAbsolutePath()
                            + " (set android.app / ios.app in config.properties or via -D)");
        }
        return asFile.getAbsolutePath();
    }

     /**
     * Creates an Appium driver for the specified platform.
     *
     * @param platform the target platform (android or ios)
     * @return the initialized Android or iOS Appium driver
     */
    public static AppiumDriver create(String platform) {
        try {
            URL serverUrl = new URL(ConfigReader.get("appium.server.url"));
            String target = platform == null ? "android" : platform.toLowerCase();

            return switch (target) {
                case "android" -> new AndroidDriver(serverUrl, buildAndroidOptions());
                default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
            };
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium server URL", e);
        }
    }

     /**
     * Builds Android-specific Appium capabilities using the configured device,
     * automation settings, and app details.
     *
     * @return the configured UiAutomator2 options
     */
    private static UiAutomator2Options buildAndroidOptions() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(ConfigReader.get("android.platformName", "Android"))
                .setPlatformVersion(ConfigReader.get("android.platformVersion"))
                .setDeviceName(ConfigReader.get("android.deviceName"))
                .setAutomationName(ConfigReader.get("android.automationName", "UiAutomator2"))
                .setNoReset(ConfigReader.getBoolean("android.noReset", false))
                .setNewCommandTimeout(Duration.ofSeconds(
                        ConfigReader.getInt("android.newCommandTimeout", 120)));

        String app = resolveAppPath(ConfigReader.get("android.app"));
        if (app != null) {
            options.setApp(app);
        }

        String appPackage = ConfigReader.get("android.appPackage");
        if (appPackage != null && !appPackage.isBlank()) {
            options.setAppPackage(appPackage);
        }

        String appActivity = ConfigReader.get("android.appActivity");
        if (appActivity != null && !appActivity.isBlank()) {
            options.setAppActivity(appActivity);
        }

        return options;
    }
}
