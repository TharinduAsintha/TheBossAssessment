package com.theboss.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * The native Android alert used by the demo app for success dialogs and the
 * "Active" button on the Forms screen.
 */
public class AlertDialog extends BasePage {

    private static final By TITLE = By.id("com.wdiodemoapp:id/alert_title");
    private static final By MESSAGE = By.id("android:id/message");
    private static final By OK_BUTTON = By.id("android:id/button1");

    public AlertDialog(AppiumDriver driver) {
        super(driver);
    }

    public AlertDialog waitUntilShown() {
        waitForVisible(TITLE);
        return this;
    }

    public String title() {
        return textOf(TITLE);
    }

    public String message() {
        return textOf(MESSAGE);
    }

    public void dismiss() {
        tap(OK_BUTTON);
    }
}
