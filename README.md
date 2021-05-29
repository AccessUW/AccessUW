# AccessUW

## Table of Contents

- [Project Overview](#project-overview)
- [Goals](#goals)
    - [Mission](#mission)
    - [Main Features](#main-features)
    - [Stretch Goals](#stretch-goals)
- [Getting Set Up](#getting-set-up)
    - [Repository Folder Structure](#repository-folder-structure)
    - [Guides](#guides)
    - [Operational Use Cases](#operational-use-cases)


## Project Overview
AccessUW is an Android mobile app that helps users navigate the University of Washington Seattle campus through a map interface that provides information about accessible building entrances, wheelchair-accessible routes, and gender-neutral restrooms.

## Goals
### Mission
Our mission is to create a simple map-based navigation app that enables all individuals of all abilities to efficiently move throughout the UW campus. We believe that a focus on equitable campus access is especially important now as students transition back to in-person instruction this coming Autumn.
### Main Features
- Finds shortest path between 2 locations on the UW campus.
    - Locations selected either by typing the name or clicking on the map.
    - Displays this shortest route on the map.
    - Issues with the selected building(s) (such as if a building has no accessible entrances) are shown to the user.
- Filters for routes based on accessibility constraints (such as wheelchair-accessible routes).
- Can search for the nearest gender-neutral restroom.
- Shows accessibility information about selected start building.
- Autocomplete functionality when searching for start/end locations.
- Zoom feature on map.
### Stretch goals
- Option to choose between inputting your own starting destination or using phone’s location.
- Phone’s location shows your progress on the route and shows your current position on the map.
- Ability to add multi-stop routes through various locations on the UW campus.

## Getting Set Up
### Repository Folder Structure
    .
    ├── .idea                                   # config files
    ├── Reports                                 # weekly progress reports on team's development.
    ├── app/src                                 # full front- and back-end of app.
        ├── main                                # front- and back-end of app.
            ├── java                            # all java classes.
                ├── com/example/accessUWMap     # view and presenter (in MVP-framework) for the app.
                ├── models                      # model (in MVP-framework) for the app.
                ├── views                       # custom views to allow for map-scrolling
            ├── res                             # all resources used (images, .csv data, etc.).
        ├── test                                # unit tests for back-end map representation
    ├── gradle/wrapper                          # gradle build files

### Guides
- [User Guide](USER_GUIDE.md)
- [Developer Guide](DEV_GUIDE.md)

### Operational Use Cases
- User finds shortest route between two buildings on campus.
- User wants to look up just 1 location so that the map will move to show them where it is on campus.
- User wants to search for a building on the UW Campus by name.
- User wants to find accessibility information about a building.
- User wants to find the nearest gender-neutral restroom.