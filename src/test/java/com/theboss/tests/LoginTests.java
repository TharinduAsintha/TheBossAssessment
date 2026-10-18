package com.theboss.tests;

import com.theboss.base.BaseTest;
import com.theboss.listeners.ExtentTestListener;
import com.theboss.pages.AlertDialog;
import com.theboss.pages.LoginScreen;
import com.theboss.pages.NavBar;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.slf4j.*;

public class LoginTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginTests.class);

    /**
     * Navigates to the Login tab and switches to its LOGIN sub-tab.
     *
     * @return the {@link LoginScreen} with the LOGIN sub-tab active
     */
    private LoginScreen openLogin() {
        logger.info("Navigating to the Login screen");
        return new NavBar(driver).openLogin().selectLoginTab();
    }

    @Test(description = "Logging in with a valid email and 8+ char password shows the success dialog")
    public void successfulLoginShowsSuccessDialog() {
        logger.info("Starting successful login test");

        String email = "user" + ThreadLocalRandom.current().nextInt(100, 1000) + "@example.com";
        ExtentTestListener.getTest().info("Submitting login as '" + email + "'");
        openLogin()
                .enterEmail(email)
                .enterPassword("Password123")
                .submitLogin();

        AlertDialog alert = new AlertDialog(driver).waitUntilShown();

        logger.info("Success dialog title: {}", alert.title());
        logger.info("Success dialog message: {}", alert.message());
        ExtentTestListener.getTest().info("Success dialog title: '" + alert.title() + "'");
        ExtentTestListener.getTest().info("Success dialog message: '" + alert.message() + "'");

        assertEquals(alert.title(), "Success");
        assertEquals(alert.message(), "You are logged in!");

        ExtentTestListener.getTest().info("Dismissing the success dialog");
        alert.dismiss();

        logger.info("Successful login test completed");
    }

    @Test(description = "Logging in with a malformed email shows an inline validation error")
    public void invalidEmailIsRejected() {
        logger.info("Starting invalid email validation test");

        ExtentTestListener.getTest().info("Submitting login with malformed email 'not-an-email'");
        LoginScreen screen = openLogin()
                .enterEmail("not-an-email")
                .enterPassword("Password123")
                .submitLogin();

        ExtentTestListener.getTest().info("Verifying the invalid-email validation message is shown");
        assertTrue(screen.invalidEmailMessageShown(),
                "Expected the 'Please enter a valid email address' validation message");

        logger.info("Invalid email validation test passed");
    }

    @Test(description = "Logging in with a password shorter than 8 chars shows an inline validation error")
    public void shortPasswordIsRejected() {
        logger.info("Starting short password validation test");

        String email = "user" + ThreadLocalRandom.current().nextInt(100, 1000) + "@example.com";
        ExtentTestListener.getTest().info("Submitting login as '" + email + "' with a 5-character password");
        LoginScreen screen = openLogin()
                .enterEmail(email)
                .enterPassword("short")
                .submitLogin();

        ExtentTestListener.getTest().info("Verifying the short-password validation message is shown");
        assertTrue(screen.shortPasswordMessageShown(),
                "Expected the 'Please enter at least 8 characters' validation message");

        logger.info("Short password validation test passed");
    }
}
