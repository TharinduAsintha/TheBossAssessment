package com.theboss.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Bottom tab bar. Each tab is exposed via a stable accessibility id by the demo app.
 */
public class NavBar extends BasePage {

    private static final By LOGIN_TAB = accessibility("Login");
    private static final By FORMS_TAB = accessibility("Forms");

    public NavBar(AppiumDriver driver) {
        super(driver);
    }

    public LoginScreen openLogin() {
        tap(LOGIN_TAB);
        return new LoginScreen(driver).waitUntilReady();
    }

    public FormsScreen openForms() {
        tap(FORMS_TAB);
        return new FormsScreen(driver).waitUntilReady();
    }
}
