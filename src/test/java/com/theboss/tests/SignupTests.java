package com.theboss.tests;

import com.theboss.base.BaseTest;
import com.theboss.pages.AlertDialog;
import com.theboss.pages.LoginScreen;
import com.theboss.pages.NavBar;
import com.theboss.utils.TestContext;

import org.slf4j.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

public class SignupTests extends BaseTest {

        private static final Logger logger =
            LoggerFactory.getLogger(FormsTests.class);

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
        TestContext.setSignupEmail(uniqueEmail);

        logger.info("Generated a unique email for signup");

        openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password123")
                .submitSignup();

        AlertDialog alert = new AlertDialog(driver).waitUntilShown();

        logger.info("Signup alert title: {}", alert.title());
        logger.info("Signup alert message: {}", alert.message());

        assertEquals(alert.title(), "Signed Up!");
        assertEquals(alert.message(), "You successfully signed up!");
        alert.dismiss();

         logger.info("Successful signup test completed");
    }

    @Test(description = "Signing up with a malformed email shows an inline validation error")
    public void invalidEmailIsRejected() {
        logger.info("Starting invalid email signup test");

        String uniqueEmail = "new.user"+ ThreadLocalRandom.current().nextInt(100, 1000);
        LoginScreen screen = openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password123")
                .submitSignup();

        assertTrue(screen.invalidEmailMessageShown(),
                "Expected the 'Please enter a valid email address' validation message");

        logger.info("Invalid email validation test passed");
    }

    @Test(description = "Signing up with mismatched passwords shows an inline validation error")
    public void mismatchedPasswordsAreRejected() {
        logger.info("Starting mismatched passwords signup test");

        String uniqueEmail = "new.user"+ ThreadLocalRandom.current().nextInt(100, 1000)+ "@example.com";
        LoginScreen screen = openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password999")
                .submitSignup();

        assertTrue(screen.passwordMismatchMessageShown(),
                "Expected the 'Please enter the same password' validation message");
        assertFalse(screen.invalidEmailMessageShown(),
                "Email was valid; no email error should appear");

        logger.info("Mismatched passwords validation test passed");
    }
}
