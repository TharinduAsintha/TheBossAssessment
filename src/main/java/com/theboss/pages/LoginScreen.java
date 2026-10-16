package com.theboss.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

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

    public LoginScreen(AppiumDriver driver) {
        super(driver);
    }

    public LoginScreen waitUntilReady() {
        waitForVisible(LOGIN_SUBTAB);
        return this;
    }

    public LoginScreen selectLoginTab() {
        tap(LOGIN_SUBTAB);
        waitForVisible(LOGIN_BUTTON);
        return this;
    }

    public LoginScreen selectSignupTab() {
        tap(SIGNUP_SUBTAB);
        waitForVisible(SIGNUP_BUTTON);
        return this;
    }

    public LoginScreen enterEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }

    public LoginScreen enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    public LoginScreen enterRepeatPassword(String password) {
        type(REPEAT_PASSWORD_INPUT, password);
        return this;
    }

    public LoginScreen submitLogin() {
        tap(LOGIN_BUTTON);
        return this;
    }

    public LoginScreen submitSignup() {
        tap(SIGNUP_BUTTON);
        return this;
    }

    public boolean invalidEmailMessageShown() {
        return isDisplayed(INVALID_EMAIL_MSG);
    }

    public boolean shortPasswordMessageShown() {
        return isDisplayed(SHORT_PASSWORD_MSG);
    }

    public boolean passwordMismatchMessageShown() {
        return isDisplayed(PASSWORD_MISMATCH_MSG);
    }
}
