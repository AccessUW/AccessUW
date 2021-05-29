# AccessUW User Guide

## Table of Contents
- [Downloading the App](#installing/running-the-app)
    - Using a Physical Android Device
    - Using an Emulated Android Device
- [How to Use](#how-to-use)
- [Bugs & Issues](#bugs-&-issues)
    - Bug reporting for the users
    - Known Bugs

## Downloading the App

### Using a Physical Android Device:
(General steps listed below, follow [Javatpoint's instructions](https://www.javatpoint.com/how-to-install-apk-on-android#:~:text=Copy%20the%20downloaded%20APK%20file,tap%20on%20it%20to%20install) for more detailed steps and troubleshooting)
- Ensure your physical device is operating at Android 9.0 or newer.
- Ensure you have a File Manager on your phone so you can easily find downloaded files.
- On your Android device, use your UW net-ID to access [AccessUW's Google Drive](https://drive.google.com/drive/folders/15NJg1pMImN9zvDMFNJpA-DUkWg-hoXbQ?usp=sharing).
- Download the AccessUW app APK named access_uw_app.apk.
- Find the APK in your downloads folder, click it, give it permissions, and install it.
- The app should now be visible in your installed apps list!

### Using an Emulated Android Device:

#### Set up emulator:
(General steps listed below, follow [Android Developer's guide](https://developer.android.com/studio/run/emulator) for more detailed steps and troubleshooting)
- Download Android Studio (version 4.1 or newer).
- Download SDK Tools 26.1.1 or higher.
- Download an Android Emulator of Android 9.0 or newer.
#### Download APK of project:
- On your Android device, use your UW net-ID to access [AccessUW's Google Drive](https://drive.google.com/drive/folders/15NJg1pMImN9zvDMFNJpA-DUkWg-hoXbQ?usp=sharing).
- Download the AccessUW app APK named access_uw_app.apk.
- Boot up your emulated Android device.
- Drag and drop the APK into the emulator screen (the file be placed in the /sdcard/Download/ directory by default).
- Find the file using the Device File Explorer, Downloads app, or Files app.
- Click on it and install the app.
- The app should now be visible in your installed apps list!

## How to Use

### Start
- Upon opening the app, you'll see a search bar at the top and a map filling the rest of the screen.
    - You can click on the search bar to bring up the keyboard and enter a location's name.
    - You can use your finger to drag across the map to move your view.
    - You can click on a building on the map to select it as well.

### Get Accessibility Description of a Building
- Either type in the building's name in the search bar at the top OR click on the building on the map itself.
### Navigate from Building to Building
- Select a start location by doing one of the following:
    - Typing in the name of the location in the search bar.
    - Find it on the map and click on it.
- Click "Find Route".
- Select an end location by doing one of the following:
    - Typing in the name of the location in the search bar.
    - Find it on the map and click on it.
- Select desired route filters:
    - WHEELCHAIR: filters for wheelchair-accessible routes.
    - NO STAIRS: filters for routes with no stairs.
- Click "Start Route".

### Route to the Nearest Gender-Neutral Restroom
- Select a start location (of which to find the nearest gender-neutral restroom) by doing one of the following:
    - Typing in the name of the location in the search bar.
    - Find it on the map and click on it.
- Click "Find Route".
- Start typing "Nearest gender-neutral restroom" into the end location search bar until that result appears.
- Select that option.
- Click "Start Route".
- App will output one of the following:
    - If the start building contains a gender-neutral bathroom, there will just be a dot displaying the route of one of that building's entrances to itself.
    - If the start building does not contain a gender-neutral bathroom, it will route to the nearest building that does (displaying which building that is at the bottom of the screen).

## Bugs & Issues

### Bug reporting for the users:
Users are able to report the bugs in [this spreadsheet](https://docs.google.com/spreadsheets/d/1Afv3kSqC3Bg_IEs7vqW1ajjeVEligyM9pQl7dC6dgJ8/edit?usp=sharing).

### Known Bugs:
All the known bugs and limitation are reported in [AccessUW Github Issues](https://github.com/AccessUW/AccessUW/issues).
