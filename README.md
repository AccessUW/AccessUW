# AccessUW
AccessUW is an Android mobile app that will support a map interface that provides information about 
accessible building entrances and gender neutral bathroom locations, as well as filtering and route 
finding, so that we can enable all individuals of all abilities to confidently navigate campus. This 
focus on equitable campus access is especially important as new students transition back to in-
person instruction this coming Autumn.

## How to install and run:
### Physical Android device:
(General steps listed below, follow [Javatpoint's instructions](https://www.javatpoint.com/how-to-install-apk-on-android#:~:text=Copy%20the%20downloaded%20APK%20file,tap%20on%20it%20to%20install) for more detailed steps and troubleshooting)
- Ensure your physical device is operating at Android 9.0 or newer
- Ensure you have a File Manager on your phone so you can easily find downloaded files
- On your Android device, use your UW net-ID to access [AccessUW's Google Drive](https://drive.google.com/drive/folders/15NJg1pMImN9zvDMFNJpA-DUkWg-hoXbQ?usp=sharing)
- Download the AccessUW app APK named access_uw_app.apk
- Find the APK in your downloads folder, click it, give it permissions, and install it
- The app should now be visible in your installed apps list!
### Android device emulator:
#### Set up emulator:
(General steps listed below, follow [Android Developer's guide](https://developer.android.com/studio/run/emulator) for more detailed steps and troubleshooting)
- Download Android Studios
- Download SDK Tools 26.1.1 or higher
- Download an Android Emulator of Android 9.0 or newer
#### Download APK of project:
- On your Android device, use your UW net-ID to access [AccessUW's Google Drive](https://drive.google.com/drive/folders/15NJg1pMImN9zvDMFNJpA-DUkWg-hoXbQ?usp=sharing)
- Download the AccessUW app APK named access_uw_app.apk
- Boot up your emulated Android device
- Drag and drop the APK into the emulator screen (the file be placed in the /sdcard/Download/ directory by default)
- Find the file using the Device File Explorer, Downloads app, or Files app
- Click on it and install the app
- The app should now be visible in your installed apps list!

## Operational use cases:
- Use case 1: User finds shortest route between two buildings on campus is mostly operational (some
    buildings don't work but the app can generate shortest routes between two working buildings)
- Use case 2: When user searches the specific building, the map moves to that location and shows that location
- Use case 3: The user wants to search for a building on the UW Campus by name is partially operational
    (nearly all UW campus buildings are present in the autocomplete feature and all building descriptions
    are present in the data, however the app does not currently query the description of the given building)

## Major features:
- Finds shortest path between 2 locations on the UW campus and displays a route
- Filter for routes based on accessibility constraints (such as wheelchair-accessible routes)
- Option for destination to be nearest gender-neutral bathroom
- Ability to click on building to set that as start/end location for route without having to look up 
the name of it
- Any issues with path or start/end buildings are showed to the user and visualized on the route (in 
a different color to show contrast) such as doors that are not very accessible or a lack of ramps 
somewhere
- When clicking on a building, it can show you a brief description (i.e. name, accessibility 
features, etc.)
- Autocomplete when searching for buildings/on-campus locations

## Stretch goals:
- Option to choose between inputting your own starting destination or using phone’s location
- Phone’s location shows your progress on the route and shows your current position on the map
- Zoom feature on map
- Ability to add multi-stop routes through various locations on the UW campus

## How to obtain the source code
All code that is required to run the project is found in this repository and in Android Studio.

## General repository layout:
- Model-View-Presenter:
    - /app/src/main/java/models contains the Model of our MVP framework (with the back-end functionality)
    - /app/src/main/java/.../accessUWMap contains the View of our MVP framework (with the main app front-end functionality (e.g. viewing the
        map, setting destinations, etc.))
    - /app/src/main/java/CampusPresenter.java contains the Presenter of our MVP framework
- /app/src/main/res contains most of the app's resources and data (e.g. the map image and string names)
- /app/src/test/java contains JUnit tests, with all class names ending in Test (e.g. PlaceTest) and all methods starting with test (e.g. testSimple())
- /app/src/androidTest/java contains instrumented tests interacting with an emulator
- /Reports contains our team's weekly progress reports
- Documentation is found in the comments of java src files

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

## Bug reporting for the users
Users are able to report the bugs in the spreadsheet, followed by the link under.
https://docs.google.com/spreadsheets/d/1Afv3kSqC3Bg_IEs7vqW1ajjeVEligyM9pQl7dC6dgJ8/edit?usp=sharing

## Known Bugs
all the known bugs and limitation will be reported in AccessUW Github issues.
https://github.com/AccessUW/AccessUW/issues
