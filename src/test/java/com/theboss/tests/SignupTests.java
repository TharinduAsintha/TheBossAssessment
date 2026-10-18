package com.theboss.tests;

import com.theboss.base.BaseTest;
import com.theboss.listeners.ExtentTestListener;
import com.theboss.pages.AlertDialog;
import com.theboss.pages.LoginScreen;
import com.theboss.pages.NavBar;

import org.slf4j.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

public class SignupTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(SignupTests.class);

    /**
     * Navigates to the Login tab and switches to its SIGN UP sub-tab.
     *
     * @return the {@link LoginScreen} with the SIGN UP sub-tab active
     */
    private LoginScreen openSignup() {
        return new NavBar(driver).openLogin().selectSignupTab();
    }

    @Test(description = "Signing up with matching passwords shows the success dialog")
    public void successfulSignupShowsSuccessDialog() {
        logger.info("Starting successful signup test");

        String uniqueEmail = "new.user" + ThreadLocalRandom.current().nextInt(100, 1000) + "@example.com";

        logger.info("Generated a unique email for signup");
        ExtentTestListener.getTest().info("Generated unique signup email: '" + uniqueEmail + "'");

        ExtentTestListener.getTest().info("Submitting signup with matching passwords");
        openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password123")
                .submitSignup();

        AlertDialog alert = new AlertDialog(driver).waitUntilShown();

        logger.info("Signup alert title: {}", alert.title());
        logger.info("Signup alert message: {}", alert.message());
        ExtentTestListener.getTest().info("Signup alert title: '" + alert.title() + "'");
        ExtentTestListener.getTest().info("Signup alert message: '" + alert.message() + "'");

        assertEquals(alert.title(), "Signed Up!");
        assertEquals(alert.message(), "You successfully signed up!");

        ExtentTestListener.getTest().info("Dismissing the signup alert");
        alert.dismiss();

         logger.info("Successful signup test completed");
    }

    @Test(description = "Signing up with a malformed email shows an inline validation error")
    public void invalidEmailIsRejected() {
        logger.info("Starting invalid email signup test");

        String uniqueEmail = "new.user"+ ThreadLocalRandom.current().nextInt(100, 1000);
        ExtentTestListener.getTest().info("Submitting signup with malformed email '" + uniqueEmail + "'");
        LoginScreen screen = openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password123")
                .submitSignup();

        ExtentTestListener.getTest().info("Verifying the invalid-email validation message is shown");
        assertTrue(screen.invalidEmailMessageShown(),
                "Expected the 'Please enter a valid email address' validation message");

        logger.info("Invalid email validation test passed");
    }

    @Test(description = "Signing up with mismatched passwords shows an inline validation error")
    public void mismatchedPasswordsAreRejected() {
        logger.info("Starting mismatched passwords signup test");

        String uniqueEmail = "new.user"+ ThreadLocalRandom.current().nextInt(100, 1000)+ "@example.com";
        ExtentTestListener.getTest().info("Submitting signup with mismatched passwords for '" + uniqueEmail + "'");
        LoginScreen screen = openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password999")
                .submitSignup();

        ExtentTestListener.getTest().info("Verifying the password-mismatch validation message is shown");
        assertTrue(screen.passwordMismatchMessageShown(),
                "Expected the 'Please enter the same password' validation message");

        ExtentTestListener.getTest().info("Verifying no email validation error is shown");
        assertFalse(screen.invalidEmailMessageShown(),
                "Email was valid; no email error should appear");

        logger.info("Mismatched passwords validation test passed");
    }
}
