# Bug reports — WDIO Native Demo App v2.2.0 (Android)

Bugs found during exploratory testing while building the automated suite.
Scope is limited to the Login, Sign up and Forms screens that are in the
automation suite. Severity uses: **Low / Medium / High**.

Environment (common to all reports):
- App: `android.wdio.native.app.v2.2.0.apk`
- Platform: Android 14, Pixel emulator / Galaxy S20 FE physical device
- Appium 2.11, UiAutomator2 driver
- Date observed: 2026-10-18

---

## BUG-001 — Success dialog titles are inconsistent between Sign up and Login

**Severity:** Low (UX / copy consistency)

**Area:** Sign up screen, Login screen

**Steps to reproduce:**
1. Launch the app.
2. Open the **Login** bottom tab.
3. Switch to the **SIGN UP** sub-tab, enter a valid email and matching
   passwords (e.g. `test@example.com` / `Password123` / `Password123`),
   tap **SIGN UP**. Observe the dialog title → **"Signed Up!"**.
4. Dismiss the dialog, switch to the **LOGIN** sub-tab, enter any valid
   email + 8+ char password, tap **LOGIN**. Observe the dialog title →
   **"Success"**.

**Expected:** The two success dialogs should use a consistent title style —
either both event-labelled (*"Signed Up!"* / *"Logged In!"*) or both
generic (*"Success"*). Two different conventions in two adjacent flows
reads as polish debt.

**Actual:** Sign up uses *"Signed Up!"*, Login uses *"Success"*.

**Notes:** Automated by `SignupTests.successfulSignupShowsSuccessDialog`
and `LoginTests.successfulLoginShowsSuccessDialog` — assertions pin the
current (inconsistent) strings so a copy change will show up as a test
failure.

---

## BUG-002 — "Active" button alert has no title; the title string lives in the message body

**Severity:** Low (UX / native-dialog usage)

**Area:** Forms screen → "Active" button

**Steps to reproduce:**
1. Launch the app.
2. Open the **Forms** bottom tab.
3. Scroll to the **Active / Inactive button** group and tap **Active**.
4. Inspect the native alert that appears.

**Expected:** The native `AlertDialog` renders a bold **title** (e.g.
*"Button pressed"*) and a descriptive **message** (e.g. *"This button is
active"*). This matches Material guidelines and how the Sign up / Login
dialogs are built in the same app.

**Actual:** The alert has no title view at all (`com.wdiodemoapp:id/alert_title`
is absent). The sentence *"This button is active"* is shown as the message
body only, with the OK button directly beneath it.

**Impact:** Screen readers announce the button with no leading heading, so
the context ("why is this alert here?") only arrives mid-sentence. Visually
it also looks truncated compared to the Signup / Login success dialogs in
the same app.

**Notes:** Caught while writing `FormsTests.activeButtonOpensNativeAlert` —
the test asserts only `alert.message()` because `alert.title()` throws
`NoSuchElementException` on this screen.

---

## BUG-003 — Login accepts any email + 8-char password with no backend check

**Severity:** Informational (expected for a demo app, documented so a
reviewer isn't surprised)

**Area:** Login screen

**Steps to reproduce:**
1. Launch the app, go to **Login**.
2. Enter an email that has never been signed up (e.g.
   `does.not.exist.9999@example.com`) and any 8+ char password.
3. Tap **LOGIN**.

**Expected (for a real app):** Rejected with an "unknown user / wrong
password" style error.

**Actual:** Shows the generic *"You are logged in!"* success dialog.

**Impact:** None for the demo (it has no backend); flagged so test design
decisions downstream are visible — the login happy-path test does not
need a preceding signup, which is why the suite doesn't share state
between `SignupTests` and `LoginTests`.
