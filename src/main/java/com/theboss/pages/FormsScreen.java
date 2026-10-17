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

    /**
     * Blocks until the Forms screen is fully rendered.
     *
     * @return this {@link FormsScreen} for fluent chaining
     */
    public FormsScreen waitUntilReady() {
        waitForVisible(TEXT_INPUT);
        return this;
    }

    /**
     * Types the given text into the text input field.
     *
     * @param text the text to enter
     * @return this {@link FormsScreen} for fluent chaining
     */
    public FormsScreen typeIntoTextInput(String text) {
        type(TEXT_INPUT, text);
        return this;
    }

    /**
     * @return the text currently rendered in the echo label below the input
     */
    public String echoedText() {
        return textOf(TEXT_RESULT);
    }

    /**
     * @return the descriptive label next to the switch (reflects its on/off state)
     */
    public String switchLabel() {
        return textOf(SWITCH_LABEL);
    }

    /**
     * Taps the switch to flip its state.
     *
     * @return this {@link FormsScreen} for fluent chaining
     */
    public FormsScreen toggleSwitch() {
        tap(SWITCH);
        return this;
    }

    /**
     * Taps the "Active" button, which triggers the native alert.
     *
     * @return this {@link FormsScreen} for fluent chaining
     */
    public FormsScreen tapActiveButton() {
        tap(ACTIVE_BUTTON);
        return this;
    }
}
