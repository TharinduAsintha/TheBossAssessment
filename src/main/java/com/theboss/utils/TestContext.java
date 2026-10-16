package com.theboss.utils;

/**
 * Shared, cross-test holder for values produced in one test and consumed in
 * another (e.g. an email generated during sign-up and reused during login).
 */
public final class TestContext {

    private static volatile String signupEmail;

    private TestContext() {}

    public static void setSignupEmail(String email) {
        signupEmail = email;
    }

    public static String getSignupEmail() {
        return signupEmail;
    }
}
