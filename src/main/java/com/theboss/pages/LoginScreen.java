package com.theboss.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

import java.time.Duration;

/**
 * The "Login" bottom tab. The screen hosts two sub-tabs (LOGIN and SIGN UP)
 * backed by the same input fields; which action the button performs depends on
 * which sub-tab is active.
 */
public class LoginScreen extends BasePage {

    private static final By LOGIN_SUBTAB =
            By.xpath("//android.widget.Button[@content-desc=\"Login\"]");

    private static final By SIGNUP_SUBTAB =
            By.xpath("//android.widget.TextView[@text=\"Sign up\"]");

    private static final By EMAIL_INPUT = accessibility("input-email");
    private static final By PASSWORD_INPUT = accessibility("input-password");
    private static final By REPEAT_PASSWORD_INPUT = accessibility("input-repeat-password");

    private static final By LOGIN_BUTTON = accessibility("button-LOGIN");
    private static final By SIGNUP_BUTTON = accessibility("button-SIGN UP");

    // Validation messages rendered as plain Text nodes below the inputs.
    private static final By INVALID_EMAIL_MSG =
            androidText("Please enter a valid email address");
    private static final By SHORT_PASSWORD_MSG =
            androidText("Please enter at least 8 characters");
    private static final By PASSWORD_MISMATCH_MSG =
            androidText("Please enter the same password");

    private static final Duration VALIDATION_MSG_TIMEOUT = Duration.ofSeconds(5);

    public LoginScreen(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Blocks until the Login screen's sub-tabs are visible.
     *
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen waitUntilReady() {
        waitForVisible(LOGIN_SUBTAB);
        return this;
    }

    /**
     * Switches to the LOGIN sub-tab and waits for the login button to render.
     *
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen selectLoginTab() {
        tap(LOGIN_SUBTAB);
        waitForVisible(LOGIN_BUTTON);
        return this;
    }

    /**
     * Switches to the SIGN UP sub-tab and waits for the signup button to render.
     *
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen selectSignupTab() {
        tap(SIGNUP_SUBTAB);
        waitForVisible(SIGNUP_BUTTON);
        return this;
    }

    /**
     * Types the given value into the email field.
     *
     * @param email the email address to enter
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen enterEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }

    /**
     * Types the given value into the password field.
     *
     * @param password the password to enter
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Types the given value into the "repeat password" field used on the signup sub-tab.
     *
     * @param password the password to repeat
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen enterRepeatPassword(String password) {
        type(REPEAT_PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Submits the login form by tapping the LOGIN button.
     *
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen submitLogin() {
        tap(LOGIN_BUTTON);
        return this;
    }

    /**
     * Submits the signup form by tapping the SIGN UP button.
     *
     * @return this {@link LoginScreen} for fluent chaining
     */
    public LoginScreen submitSignup() {
        tap(SIGNUP_BUTTON);
        return this;
    }

    /**
     * @return {@code true} if the "please enter a valid email address" message is shown
     */
    public boolean invalidEmailMessageShown() {
        return isDisplayedWithin(INVALID_EMAIL_MSG, VALIDATION_MSG_TIMEOUT);
    }

    /**
     * @return {@code true} if the "please enter at least 8 characters" message is shown
     */
    public boolean shortPasswordMessageShown() {
        return isDisplayedWithin(SHORT_PASSWORD_MSG, VALIDATION_MSG_TIMEOUT);
    }

    /**
     * @return {@code true} if the "please enter the same password" message is shown
     */
    public boolean passwordMismatchMessageShown() {
        return isDisplayedWithin(PASSWORD_MISMATCH_MSG, VALIDATION_MSG_TIMEOUT);
    }
}
