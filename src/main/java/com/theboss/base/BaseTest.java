package com.theboss.base;

import com.theboss.utils.ConfigReader;
import com.theboss.utils.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

public abstract class BaseTest {

    protected AppiumDriver driver;

    /**
     * Sets up the WebDriver before each test method.
     * Resolves the target platform from the system property or TestNG parameter,
     * creates the driver, and configures the implicit wait if enabled.
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters("platform")
    public void setUp(@Optional("android") String platform) {
        String resolved = System.getProperty("platform", platform);
        driver = DriverFactory.create(resolved);
        int implicitWait = ConfigReader.getInt("implicit.wait.seconds", 0);
        if (implicitWait > 0) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        }
    }

   /**
    * Quits the WebDriver after each test method and clears the driver reference.
    */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
