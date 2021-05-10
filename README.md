# AccessUW
AccessUW is an Android mobile app that will support a map interface that provides information about 
accessible building entrances and gender neutral bathroom locations, as well as filtering and route 
finding, so that we can enable all individuals of all abilities to confidently navigate campus. This 
focus on equitable campus access is especially important as new students transition back to in-
person instruction this coming Autumn.

## How to install and run:
### Physical Android device:
(General steps listed below, follow [Javatpoint's instructions](https://www.javatpoint.com/how-to-install-apk-on-android#:~:text=Copy%20the%20downloaded%20APK%20file,tap%20on%20it%20to%20install) for more detailed steps and troubleshooting)
- Ensure your physical device is operating at Android 6.0 (Marshmallow) or newer
- Ensure you have a File Manager on your phone so you can easily find downloaded files
- On your Android device, use your UW net-ID to access [AccessUW's Google Drive](https://cse403group.slack.com/archives/D01SW89RP9A/p1620604483000200)
- Download the AccessUW app APK
- Find the APK in your downloads folder, click it, give it permissions, and install it
- The app should now be visible in your installed apps list!
### Android device emulator:
#### Set up emulator:
(General steps listed below, follow [Android Developer's guide](https://developer.android.com/studio/run/emulator) for more detailed steps and troubleshooting)
- Download Android Studios
- Download SDK Tools 26.1.1 or higher
- Download an Android Emulator of Android 6.0 (Marshmallow) or newer
#### Download APK of project:
- On your Android device, use your UW net-ID to access [AccessUW's Google Drive](https://cse403group.slack.com/archives/D01SW89RP9A/p1620604483000200)
- Download the AccessUW app APK
- Boot up your emulated Android device
- Drag and drop the APK into the emulator screen (the file be placed in the /sdcard/Download/ directory by default)
- Find the file using the Device File Explorer, Downloads app, or Files app
- Click on it and install the app
- The app should now be visible in yoru installed apps list!

## Major features:
- Finds shortest path between 2 locations on the UW campus and displays a route
- Filter for routes based on accessibility constraints (such as wheelchair-accessible routes)
- Option for destination to be nearest gender-neutral bathroom
- Ability to click on building to set that as start/end location for route without having to look up 
the name of it
- Any issues with path or start/end buildings are showed to the user and visualized on the route (in 
a different color to show contrast) such as doors that are not very accessible or a lack of ramps 
somewhere
- Ability to add multi-stop routes through various locations on the UW campus
- When clicking on a building, it can show you a brief description (i.e. name, accessibility 
features, etc.)

## Stretch goals:
- Option to choose between inputting your own starting destination or using phone’s location
- Phone’s location shows your progress on the route and shows your current position on the map
- Zoom feature on map
- Autocomplete when searching for buildings/on-campus locations

## General repository layout:
- Model-View-Presenter:
    - /app/src/main/java/models contains the Model of our MVP framework (with the back-end functionality)
    - /app/src/main/java/.../accessUWMap contains the View of our MVP framework (with the main app front-end functionality (e.g. viewing the
        map, setting destinations, etc.))
    - /app/src/main/java/CampusPresenter.java contains the Presenter of our MVP framework
- /app/src/main/res contains most of the app's resources (e.g. the map image and string names)
- /Reports contains our team's weekly progress reports