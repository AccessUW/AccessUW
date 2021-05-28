# AccessUW Developer Guide

## Table of Contents
- [Installing/Running the App](#installing/running-the-app)
    - [Get Source Code](#get-source-code)
- [How to Use](#how-to-use)
- [Bugs & Issues](#bugs-&-issues)
    - [Bug reporting for the users](#bug-reporting-for-the-users:)
    - [Known Bugs](#known-bugs)

## Installing/Running the App
### Get Set Up with Android Studio
- (General steps listed below, follow [Android Developer's guide](https://developer.android.com/studio/run/emulator) for more detailed steps and troubleshooting)
- Download Android Studio (version 4.1 or newer)
- Download SDK Tools 26.1.1 or higher
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

## Building & Testing the system
Building & testing the system is relatively automatic, with Travis CI automatically running our current unit testing scripts on each commit.

For manual building/testing, you can perform `./gradlew build` to build the system, and then `./gradlew test` to run unit tests.

Instrumentation tests can be performed by starting up an emulator and then running `./gradlew cAT`.

For all steps at once, one can start up an emulator and then run `./gradlew build cC`.

## How to Add New Tests
Adding new unit tests can be done by adding JUnit test Java files to /app/src/test/java. They will be automatically tested through `./gradlew test`.

Instrumentation tests can be added at /app/src/androidTest/java/ instead to be run automatically through `./gradlew cAT`.

## Currently Unautomated Tasks
The only thing that is currently unautomated is setting up an emulator (lots has been tried on Travis CI, to no avail). Android Studio can be used to set up an emulator instead in order for `./gradlew cAT` and `./gradlew build cC` to be run.

Builds currently run/take place on android API version 30. Be sure that scrolling and the keyboard automatically hiding after typing is complete is functional on non-emulator devices.