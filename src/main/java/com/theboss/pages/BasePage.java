package com.theboss.pages;

import com.theboss.utils.ConfigReader;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final AppiumDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds", 20)));
    }

    /**
     * Waits for the element at {@code locator} to be visible and returns it.
     *
     * @param locator the element locator
     * @return the visible {@link WebElement}
     */
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the element at {@code locator} to be clickable and returns it.
     *
     * @param locator the element locator
     * @return the clickable {@link WebElement}
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for the element to be clickable, then taps it.
     *
     * @param locator the element locator
     */
    protected void tap(By locator) {
        waitForClickable(locator).click();
    }

    /**
     * Clears the input at {@code locator} and types {@code text} into it.
     *
     * @param locator the input element locator
     * @param text    the text to enter
     */
    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Checks whether at least one element matching {@code locator} is present
     * and visible. Does not wait; returns {@code false} immediately if nothing
     * matches.
     *
     * @param locator the element locator
     * @return {@code true} if a matching element is currently displayed
     */
    protected boolean isDisplayed(By locator) {
        List<WebElement> matches = driver.findElements(locator);
        return !matches.isEmpty() && matches.get(0).isDisplayed();
    }

    /**
     * Waits for the element at {@code locator} to be visible and returns its
     * displayed text.
     *
     * @param locator the element locator
     * @return the visible text of the element
     */
    protected String textOf(By locator) {
        return waitForVisible(locator).getText();
    }


    /**
     * Shortcut for building an accessibility-id locator.
     *
     * @param id the accessibility id exposed by the app
     * @return a {@link By} matching the given accessibility id
     */
    protected static By accessibility(String id) {
        return AppiumBy.accessibilityId(id);
    }

    /** Element whose visible text equals the given string (Android UiAutomator). */
    protected static By androidText(String text) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + text + "\")");
    }

    /** Element whose visible text contains the given substring (Android UiAutomator). */
    protected static By androidTextContains(String text) {
        return AppiumBy.androidUIAutomator("new UiSelector().textContains(\"" + text + "\")");
    }
}
