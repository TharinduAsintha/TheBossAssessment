package com.theboss.tests;

import com.theboss.base.BaseTest;
import com.theboss.pages.AlertDialog;
import com.theboss.pages.FormsScreen;
import com.theboss.pages.NavBar;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class FormsTests extends BaseTest {

    /**
     * Navigates to the Forms tab from the bottom nav bar.
     *
     * @return the active {@link FormsScreen}
     */
    private FormsScreen openForms() {
        return new NavBar(driver).openForms();
    }

    @Test(description = "Typed text is echoed back into the result label")
    public void typedTextIsEchoedBack() {
        String input = "Hello WDIO Demo";
        FormsScreen forms = openForms().typeIntoTextInput(input);

        assertEquals(forms.echoedText(), input,
                "Echoed label should mirror the typed text");
    }

    @Test(description = "Toggling the switch flips its descriptive label")
    public void switchToggleUpdatesLabel() {
        FormsScreen forms = openForms();

        String labelBefore = forms.switchLabel();
        assertEquals(labelBefore, "Click to turn the switch ON",
                "Switch should start in the OFF state");

        forms.toggleSwitch();

        String labelAfter = forms.switchLabel();
        assertEquals(labelAfter, "Click to turn the switch OFF",
                "After toggling, label should reflect the ON state");
    }

    @Test(description = "The 'Active' button opens a native alert")
    public void activeButtonOpensNativeAlert() {
        openForms().tapActiveButton();

        AlertDialog alert = new AlertDialog(driver).waitUntilShown();
        assertEquals(alert.message(), "This button is active");
        alert.dismiss();
    }
}
