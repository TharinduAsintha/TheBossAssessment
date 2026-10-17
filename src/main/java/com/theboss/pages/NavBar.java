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

    /**
     * Taps the "Login" bottom tab and returns the resulting screen once it is
     * ready for interaction.
     *
     * @return a {@link LoginScreen} wrapping the now-active Login tab
     */
    public LoginScreen openLogin() {
        tap(LOGIN_TAB);
        return new LoginScreen(driver).waitUntilReady();
    }

    /**
     * Taps the "Forms" bottom tab and returns the resulting screen once it is
     * ready for interaction.
     *
     * @return a {@link FormsScreen} wrapping the now-active Forms tab
     */
    public FormsScreen openForms() {
        tap(FORMS_TAB);
        return new FormsScreen(driver).waitUntilReady();
    }
}
