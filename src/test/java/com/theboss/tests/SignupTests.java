package com.theboss.tests;

import com.theboss.base.BaseTest;
import com.theboss.pages.AlertDialog;
import com.theboss.pages.LoginScreen;
import com.theboss.pages.NavBar;
import com.theboss.utils.TestContext;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

public class SignupTests extends BaseTest {

    private LoginScreen openSignup() {
        return new NavBar(driver).openLogin().selectSignupTab();
    }

    @Test(description = "Signing up with matching passwords shows the success dialog")
    public void successfulSignupShowsSuccessDialog() {
        String uniqueEmail = "new.user" + ThreadLocalRandom.current().nextInt(100, 1000) + "@example.com";
        TestContext.setSignupEmail(uniqueEmail);
        openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password123")
                .submitSignup();

        AlertDialog alert = new AlertDialog(driver).waitUntilShown();
        assertEquals(alert.title(), "Signed Up!");
        assertEquals(alert.message(), "You successfully signed up!");
        alert.dismiss();
    }

    @Test(description = "Signing up with a malformed email shows an inline validation error")
    public void invalidEmailIsRejected() {
        String uniqueEmail = "new.user"+ ThreadLocalRandom.current().nextInt(100, 1000);
        LoginScreen screen = openSignup()
                .enterEmail(uniqueEmail)
                .enterPassword("Password123")
                .enterRepeatPassword("Password123")
                .submitSignup();

        assertTrue(screen.invalidEmailMessageShown(),
                "Expected the 'Please enter a valid email address' validation message");
    }

    @Test(description = "Signing up with mismatched passwords shows an inline validation error")
    public void mismatchedPasswordsAreRejected() {
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
    }
}
