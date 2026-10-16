package com.theboss.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
    }

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

    public static AppiumDriver create(String platform) {
        try {
            URL serverUrl = new URL(ConfigReader.get("appium.server.url"));
            String target = platform == null ? "ios" : platform.toLowerCase();

            return switch (target) {
                case "android" -> new AndroidDriver(serverUrl, buildAndroidOptions());
                case "ios" -> new IOSDriver(serverUrl, buildIosOptions());
                default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
            };
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium server URL", e);
        }
    }

    private static XCUITestOptions buildIosOptions() {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName(ConfigReader.get("ios.platformName", "iOS"))
                .setPlatformVersion(ConfigReader.get("ios.platformVersion"))
                .setDeviceName(ConfigReader.get("ios.deviceName"))
                .setAutomationName(ConfigReader.get("ios.automationName", "XCUITest"))
                .setNoReset(ConfigReader.getBoolean("ios.noReset", false))
                .setNewCommandTimeout(Duration.ofSeconds(
                        ConfigReader.getInt("ios.newCommandTimeout", 120)));

        String app = resolveAppPath(ConfigReader.get("ios.app"));
        if (app != null) {
            options.setApp(app);
        }

        String bundleId = ConfigReader.get("ios.bundleId");
        if (bundleId != null && !bundleId.isBlank()) {
            options.setBundleId(bundleId);
        }

        return options;
    }

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
