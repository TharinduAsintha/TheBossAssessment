# WDIO Native Demo App — Mobile QA Take-home

Automated UI tests for the sign-up, login and forms screens of the
[WebdriverIO Native Demo App v2.2.0](https://github.com/webdriverio/native-demo-app/releases/tag/v2.2.0).

Platform: **Android** (required by the exercise).
Stack: **Appium 2 + Java 17 + TestNG + Maven**, driven through the UiAutomator2 driver.

---

## Why this stack

- **Appium** — the industry-standard mobile automation protocol. It drives the
  prebuilt APK as-is (no source rebuild) and the same page objects can be
  retargeted at the iOS simulator build included in the same release.
- **Java + TestNG** — mature, strongly typed, well suited to a page-object
  design. TestNG's suite XML lets us group tests per screen area and parameterise
  the target platform.
- **UiAutomator2** — the recommended Android automation backend for React Native
  apps. The demo app exposes `accessibilityLabel` / `testID` on every
  interactive element, so we locate by accessibility id — stable across layout
  tweaks — and only fall back to `UiSelector().text(...)` for the plain `<Text>`
  validation messages that have no id.

I considered Maestro (quick to write YAML flows) and Appium + WebdriverIO
(TypeScript). Appium + Java was the best fit here because the exercise asks for
reliability over quantity and the page-object + explicit-wait pattern is easiest
to argue about in a Java/TestNG codebase at review time.

---

## Prerequisites

- JDK 17 or newer (tested on Temurin 21)
- Maven 3.9+
- Node 18+ and Appium Server 2.x running at `http://127.0.0.1:4723`
  ```bash
  npm install -g appium
  appium driver install uiautomator2
  appium   # leave running in a second terminal
  ```
- Android SDK with an emulator (or a connected real device) booted and visible
  to `adb devices`
- The WDIO Native Demo App APK v2.2.0, placed at
  `apps/android.wdio.native.app.v2.2.0.apk`. The APK is ~128 MB (above
  GitHub's 100 MB per-file limit) so it is not committed — download it once with:
  ```bash
  mkdir -p apps
  curl -L -o apps/android.wdio.native.app.v2.2.0.apk \
    https://github.com/webdriverio/native-demo-app/releases/download/v2.2.0/android.wdio.native.app.v2.2.0.apk
  ```

Minimum Android API tested against: API 30 (Android 11). Any API ≥ 24 should work.

---

## Run the tests

Boot an Android emulator (any API ≥ 24) and start the Appium server, then from
the project root:

```bash
mvn clean test
```

Android is the **default** Maven profile, so no extra flags are needed. The
`BeforeMethod` hook in `BaseTest` installs the APK and starts a fresh app
session before every test, and `AfterMethod` quits it — tests are independent
and can run in any order.

### Overrides

Any `config.properties` key can be overridden on the command line with `-D`:

```bash
mvn test -Dandroid.deviceName="Pixel_7_API_34"
mvn test -Dandroid.app=/absolute/path/to/some.apk
mvn test -Dappium.server.url=http://127.0.0.1:4724
```

### Run a single test class

```bash
mvn test -Dtest=LoginTests
```

### iOS (optional)

The scaffold still carries an iOS profile. Drop the simulator `.app` bundle
into `apps/` and set `ios.app` / `ios.bundleId` in `config.properties`, then:

```bash
mvn clean test -Pios -Dplatform=ios
```

I have **not** validated the iOS flow against the current page objects (the
exercise is Android-first), so expect to tweak a couple of locators.

---

## What is tested

| Area    | Scenarios                                                                                                   | Expected result                                                             |
| ------- | ----------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| Sign up | Valid email + matching passwords                                                                            | Native "Success" dialog with the text *"You successfully signed up!"*       |
| Sign up | Malformed email                                                                                             | Inline error: *"Please enter a valid email address"*                        |
| Sign up | Passwords that don't match                                                                                  | Inline error: *"Please enter the same password"*                            |
| Login   | Valid email + 8+ char password                                                                              | Native "Success" dialog with the text *"You are logged in!"*                |
| Login   | Malformed email                                                                                             | Inline error: *"Please enter a valid email address"*                        |
| Login   | Password shorter than 8 characters                                                                          | Inline error: *"Please enter at least 8 characters"*                        |
| Forms   | Typed text is echoed in the result label                                                                    | Result label text equals the typed string                                   |
| Forms   | Switch toggles                                                                                              | Label flips from *"Click to turn the switch ON"* → *"Click to turn the switch OFF"* |
| Forms   | "Active" button                                                                                             | Native alert with title *"This button is active"*                           |

Every assertion checks a user-visible string (dialog text, label, inline error)
rather than internal state — that is what the exercise asks for and what a
human tester would eyeball.

---

## Project layout

```
├── apps/
│   └── android.wdio.native.app.v2.2.0.apk    # app under test (checked in for reproducibility)
├── pom.xml                                   # Maven build (android profile active by default)
├── testng.xml                                # suite: one <test> per screen area
└── src/
    ├── main/java/com/theboss/
    │   ├── base/BaseTest.java                # TestNG lifecycle: fresh driver per test
    │   ├── pages/
    │   │   ├── BasePage.java                 # wait helpers + accessibility-id / UiAutomator locator helpers
    │   │   ├── NavBar.java                   # bottom tabs (Login, Forms, …)
    │   │   ├── LoginScreen.java              # Login + Sign Up sub-tabs (share the same screen)
    │   │   ├── FormsScreen.java              # text input, switch, Active button
    │   │   └── AlertDialog.java              # native Android alert
    │   └── utils/
    │       ├── ConfigReader.java             # config.properties + -D override resolver
    │       └── DriverFactory.java            # Android / iOS driver assembly
    └── test/
        ├── java/com/theboss/tests/
        │   ├── SignupTests.java              # 3 sign-up scenarios
        │   ├── LoginTests.java               # 3 login scenarios
        │   └── FormsTests.java               # 3 forms scenarios
        └── resources/
            ├── config.properties             # capabilities + app path
            └── logback.xml
```

### Design notes

- **One driver session per test** — `@BeforeMethod` / `@AfterMethod` reinstalls
  the app, so no test depends on another's state. The suite is safe to re-order
  or run a subset.
- **Explicit waits only.** `BasePage` wraps `WebDriverWait` for visibility and
  clickability. Implicit wait is set to `0` in `config.properties` — mixing
  implicit and explicit waits is a well-known footgun (compounded timeouts,
  flaky stale-element behaviour).
- **Stable locators first.** Every interactive element in the demo app ships
  with an `accessibilityLabel`, so page objects reach for `accessibility id`
  before anything else. The three inline validation messages are plain `<Text>`
  nodes with no id — those are the only locators that match by exact text via
  `UiSelector().text(...)`.
- **Page objects are small on purpose.** Each returns `this` for a fluent
  arrange-act flow in tests, keeping scenarios short and declarative.
- **The APK is checked into `apps/`** so the reviewer doesn't need to chase a
  release URL. ~120 MB, below GitHub's 100 MB per-file hard limit after Git LFS
  would normally kick in — in a real team repo I'd store it externally and
  fetch in CI, but for a take-home it's one less step.

---

## What I'd do next with more time

- **CI** — a GitHub Actions workflow that boots an Android emulator (e.g.
  `reactivecircus/android-emulator-runner`) and runs the suite on every push.
  Attach the TestNG report and Appium server log as artefacts.
- **Reporting** — add Allure (or at least surface the TestNG HTML report),
  attach a screenshot + `driver.getPageSource()` on failure via a TestNG
  `ITestListener`. This drastically shortens the "why did this fail?" loop.
- **Parallelism** — TestNG `parallel="classes"` with a device pool, so Sign
  up / Login / Forms run on three emulators concurrently. The current page
  objects are already stateless-per-driver, so this is a config-only change.
- **iOS parity** — validate the same page objects against the simulator `.app`
  included in the v2.2.0 release and add an `ios` CI job.
- **A couple more scenarios that genuinely add signal**:
  - Sign up with an email that is already visually valid but has leading/trailing
    whitespace — confirms the app trims (or doesn't) and documents the behaviour.
  - Submitting the login form with the on-screen keyboard IME action instead of
    tapping the button — different code path, same expected result.
  - Rotating the device during the Forms test to confirm state (typed text,
    switch position) survives.
- **Data cleanup** — the demo app has no backend, so this is a no-op here, but
  in a real app I'd add a per-test cleanup hook rather than relying on `noReset`.
- **Bug reports** — if I'd found real defects I'd file them with
  reproduction steps, device/OS matrix, expected vs actual, logs/screenshot,
  and a short note on severity + user impact.

---

## Known limitations / honest caveats

- Compile was **not** verified locally (my sandbox doesn't have Maven and
  blocked downloading the Maven wrapper). The code was reviewed by eye against
  the Appium Java client 9.x API; if anything doesn't compile for you, please
  let me know and I'll fix forward quickly.
- The APK path is resolved relative to `user.dir` (the directory you run
  `mvn` from). Running Maven from a sub-directory will fail with a clear
  "App artefact not found" error pointing at the resolved path.
- The locators assume the WDIO demo app v2.2.0 exactly. Later releases have
  renamed `button-login-container` → something else; pin the APK version if you
  upgrade.

---

## AI usage disclosure

I used Anthropic's **Claude Code** (Opus 4.7) as a pair-programmer for the
following, which is the honest extent of the AI's contribution:

- Scaffolding the Maven / TestNG / Appium project layout from a short brief.
- Drafting the page-object classes and TestNG tests against the WDIO demo
  app's known accessibility ids.
- Writing this README.

I reviewed every file by hand, picked the test scenarios and assertions, chose
the locator strategy and the one-session-per-test discipline, decided to
disable the implicit wait, and own the final design and any bugs.
