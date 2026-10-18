package com.theboss.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.theboss.utils.ExtentReportManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener {

    private static final ThreadLocal<ExtentTest> extentTest =
            new ThreadLocal<>();

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();

        ExtentTest test = ExtentReportManager.getInstance()
                .createTest(
                        description != null && !description.isBlank()
                                ? description
                                : result.getMethod().getMethodName()
                );

        extentTest.set(test);
        test.info("Test execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().skip("Test skipped");

        if (result.getThrowable() != null) {
            extentTest.get().skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.getInstance().flush();
        extentTest.remove();
    }
}