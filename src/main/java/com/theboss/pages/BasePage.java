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

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void tap(By locator) {
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> matches = driver.findElements(locator);
        return !matches.isEmpty() && matches.get(0).isDisplayed();
    }

    protected String textOf(By locator) {
        return waitForVisible(locator).getText();
    }


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
