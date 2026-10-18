package com.theboss.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public final class ExtentReportManager {

    private static final ExtentReports extent = createReport();

    private ExtentReportManager() {
    }

    private static ExtentReports createReport() {
        ExtentSparkReporter spark =
                new ExtentSparkReporter("target/ExtentReport.html");

        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("Appium Test Execution");

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(spark);
        reports.setSystemInfo("Framework", "Appium + TestNG");
        reports.setSystemInfo("Language", "Java");

        return reports;
    }

    public static ExtentReports getInstance() {
        return extent;
    }
}