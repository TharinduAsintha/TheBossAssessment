package com.theboss.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * The "Forms" bottom tab. Exposes a text input (echoed below), a labelled
 * switch, and an "Active" button that triggers a native alert.
 */
public class FormsScreen extends BasePage {

    private static final By TEXT_INPUT = accessibility("text-input");
    private static final By TEXT_RESULT = accessibility("input-text-result");

    private static final By SWITCH = accessibility("switch");
    private static final By SWITCH_LABEL = accessibility("switch-text");

    private static final By ACTIVE_BUTTON = accessibility("button-Active");

    public FormsScreen(AppiumDriver driver) {
        super(driver);
    }

    public FormsScreen waitUntilReady() {
        waitForVisible(TEXT_INPUT);
        return this;
    }

    public FormsScreen typeIntoTextInput(String text) {
        type(TEXT_INPUT, text);
        return this;
    }

    public String echoedText() {
        return textOf(TEXT_RESULT);
    }

    public String switchLabel() {
        return textOf(SWITCH_LABEL);
    }

    public FormsScreen toggleSwitch() {
        tap(SWITCH);
        return this;
    }

    public FormsScreen tapActiveButton() {
        tap(ACTIVE_BUTTON);
        return this;
    }
}
