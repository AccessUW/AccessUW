# AccessUW Developer Guide

## Table of Contents
- [Installing/Running the App](#installing/running-the-app)
    - [Get Set Up with Android Studio](#get-set-up-with-android-studio)
    - [Get Source Code](#get-source-code)
    - [Build/Run](#build/run)
- [Building & Testing the System](#building-&-testing-the-system)
    - [How to Add New Tests](#how-to-add-new-tests)
    - [Currently Un-automated Tasks](#currently-un-automated-tasks)

## Installing/Running the App
### Get Set Up with Android Studio
- (General steps listed below, follow [Android Developer's guide](https://developer.android.com/studio/run/emulator) for more detailed steps and troubleshooting)
- Download Android Studio (version 4.1 or newer)
- Download SDK Tools 26.1.1 or higher, preferably the most updated version
- Set up an Android environment:
    - If you have an Android phone operating at 9.0 or newer:
        - Open Settings > Developer options
        - Enable "USB debugging"
    - Otherwise, use an emulator:
        - (General steps listed below, follow [Android Developer's guide](https://developer.android.com/studio/run/emulator) for more detailed steps and troubleshooting)
            - Download SDK Tools 26.1.1 or higher
            - Download an Android Emulator of Android 9.0 or newer

### Get Source Code
- Clone AccessUW repository in Android Studio in one of two ways:
    - Menus:
        - Click the menu labeled "VCS" at the top of Android Studio.
        - Click "Get from Version Control...".
        - Copy and paste "https://github.com/code-dot-org/code-dot-org.git" into the "URL" text entry.
        - Change "Directory" to the desired folder where you want to clone the repository to.
        - Click "Clone".
    - Terminal:
        - Click the terminal tab in the bottom left of Android Studio.
        - Navigate to the desired folder where you want to clone the repository to.
        - Run "git clone https://github.com/code-dot-org/code-dot-org.git".
- Make sure the "Project" tab on the left side of the screen is open, showing the file layout.
- Just above the files, there should be a dropdown. If it is not already, change it to "Project".
- At the top of the screen, there should be 2 dropdowns to the right of a green hammer icon. If it is not already, change the left dropdown to "app".
- (If you're using a physical device) Plug your device into the computer.
- If it is not already, change the right dropdown to your device:
    - If using a physical device, select that device after it has been plugged in
    - If using an emulator, select the emulated device you set up previously

### Build/Run
- Click the green hammer icon at the top of Android Studio to build
- Click the green arrow icon at the top of Android Studio to run
- Building an .apk can be accomplished by going to the Build menu, choosing Build Bundle(s)/APK(s), and choosing Build APK(s). The resulting output apk can be found at directory app/build/outputs/apk/debug.

## Building & Testing the System
Building & testing the system is relatively automatic, with [Travis CI](https://travis-ci.com/github/AccessUW/AccessUW) automatically running our current unit testing scripts on each commit.

For manual building/testing, it's advised to use a Unix terminal.
When run in the project's root directory, `./gradlew build` builds the system and `./gradlew test` runs unit tests.

If successful, `./gradlew build` should build successfully, with 61 actionable tasks. Likewise, `./gradlew test` should have 32 actionable tasks.

Instrumentation tests can be performed by starting an emulator using Android Studio and then running `./gradlew cAT` in the project's root directory.

Building an APK is accomplishable using gradle task `./gradlew :app:assembleDebug`.

### How to Add New Tests
Adding new unit tests can be done by adding JUnit test Java files to /app/src/test/java. They will be automatically tested through `./gradlew test`.

Instrumentation tests can be added at /app/src/androidTest/java/ instead to be run automatically through `./gradlew cAT`.

### Currently Un-automated Tasks
The only thing that is currently unautomated is setting up an emulator (lots has been tried on Travis CI, to no avail). Android Studio can be used to set up an emulator instead in order for `./gradlew cAT` and `./gradlew build cC` to be run.

Builds currently run/take place on android API version 30. Be sure that scrolling and the keyboard automatically hiding after typing is complete is functional on non-emulator devices.