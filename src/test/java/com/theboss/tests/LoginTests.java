package com.theboss.tests;

import com.theboss.base.BaseTest;
import com.theboss.pages.AlertDialog;
import com.theboss.pages.LoginScreen;
import com.theboss.pages.NavBar;
import com.theboss.utils.TestContext;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginTests extends BaseTest {

    /**
     * Navigates to the Login tab and switches to its LOGIN sub-tab.
     *
     * @return the {@link LoginScreen} with the LOGIN sub-tab active
     */
    private LoginScreen openLogin() {
        return new NavBar(driver).openLogin().selectLoginTab();
    }

    @Test(description = "Logging in with a valid email and 8+ char password shows the success dialog")
    public void successfulLoginShowsSuccessDialog() {
        String email = TestContext.getSignupEmail();
        if (email == null) {
            email = "sample@gmail.com";
            
        }else{
            throw new IllegalStateException(
                    "No signup email in TestContext");
        }
        openLogin()
                .enterEmail(email)
                .enterPassword("Password123")
                .submitLogin();

        AlertDialog alert = new AlertDialog(driver).waitUntilShown();
        assertEquals(alert.title(), "Success");
        assertEquals(alert.message(), "You are logged in!");
        alert.dismiss();
    }

    @Test(description = "Logging in with a malformed email shows an inline validation error")
    public void invalidEmailIsRejected() {
        LoginScreen screen = openLogin()
                .enterEmail("not-an-email")
                .enterPassword("Password123")
                .submitLogin();

        assertTrue(screen.invalidEmailMessageShown(),
                "Expected the 'Please enter a valid email address' validation message");
    }

    @Test(description = "Logging in with a password shorter than 8 chars shows an inline validation error")
    public void shortPasswordIsRejected() {
        String email = TestContext.getSignupEmail();
        if (email == null) {
             email = "sample@gmail.com";
        } else{

               throw new IllegalStateException(
                    "No signup email in TestContext — run SignupTests.successfulSignupShowsSuccessDialog first");
        }
        LoginScreen screen = openLogin()
                .enterEmail(email)
                .enterPassword("short")
                .submitLogin();

        assertTrue(screen.shortPasswordMessageShown(),
                "Expected the 'Please enter at least 8 characters' validation message");
    }
}
