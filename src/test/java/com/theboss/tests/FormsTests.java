package com.theboss.tests;

import com.theboss.base.BaseTest;
import com.theboss.listeners.ExtentTestListener;
import com.theboss.pages.AlertDialog;
import com.theboss.pages.FormsScreen;
import com.theboss.pages.NavBar;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import org.slf4j.*;

public class FormsTests extends BaseTest {

    private static final Logger logger =
            LoggerFactory.getLogger(FormsTests.class);

    /**
     * Navigates to the Forms tab from the bottom nav bar.
     *
     * @return the active {@link FormsScreen}
     */
    private FormsScreen openForms() {
        logger.info("Navigating to the Forms screen");
        return new NavBar(driver).openForms();
    }

    @Test(description = "Typed text is echoed back into the result label")
    public void typedTextIsEchoedBack() {
        String input = "Hello WDIO Demo";
        logger.info("Starting test: typed text is echoed back");

        ExtentTestListener.getTest().info("Typing '" + input + "' into the text input");
        FormsScreen forms = openForms().typeIntoTextInput(input);

        String actualText = forms.echoedText();
        logger.info("Expected text: '{}', Actual text: '{}'", input, actualText);
        ExtentTestListener.getTest().info("Echoed label value: '" + actualText + "'");

        assertEquals(actualText, input,
                "Echoed label should mirror the typed text");

        logger.info("Test passed: typed text is echoed back");
    }

    @Test(description = "Toggling the switch flips its descriptive label")
    public void switchToggleUpdatesLabel() {

        logger.info("Starting test: switch toggle updates label");

        FormsScreen forms = openForms();
        String labelBefore = forms.switchLabel();

        logger.info("Switch label before toggle: {}", labelBefore);
        ExtentTestListener.getTest().info("Switch label before toggle: '" + labelBefore + "'");

        assertEquals(labelBefore, "Click to turn the switch ON",
                "Switch should start in the OFF state");

        ExtentTestListener.getTest().info("Toggling the switch");
        forms.toggleSwitch();

        String labelAfter = forms.switchLabel();
        logger.info("Switch label after toggle: {}", labelAfter);
        ExtentTestListener.getTest().info("Switch label after toggle: '" + labelAfter + "'");

        assertEquals(labelAfter, "Click to turn the switch OFF",
                "After toggling, label should reflect the ON state");

        logger.info("Test passed: switch toggle updates label");
    }

    @Test(description = "The 'Active' button opens a native alert")
    public void activeButtonOpensNativeAlert() {
        logger.info("Starting test: active button opens native alert");

        ExtentTestListener.getTest().info("Tapping the 'Active' button");
        openForms().tapActiveButton();

        AlertDialog alert = new AlertDialog(driver).waitUntilShown();
        String actualMessage = alert.message();

        logger.info("Native alert message: {}", actualMessage);
        ExtentTestListener.getTest().info("Native alert message: '" + actualMessage + "'");

        assertEquals(alert.message(), "This button is active");

        ExtentTestListener.getTest().info("Dismissing the native alert");
        alert.dismiss();

        logger.info("Test passed: native alert opened and dismissed");
    }
}
