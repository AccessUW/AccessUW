package com.example.accessUWMap;

import android.content.Context;
import android.graphics.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import models.CampusModel;
import models.Place;

/**
 * This class represents the presenter component of the Model-Presenter-View framework.
 * It mainly acts as the ground truth for current selected start/end locations by the user
 * and what boxes are toggled.
 */
public class CampusPresenter {
    ////////////////////////////////////////////////////////////////////////////
    ///     Constants
    ////////////////////////////////////////////////////////////////////////////
    private static final int CAMPUS_MAP_IMAGE_WIDTH_PX = 4330;
    private static final int CAMPUS_MAP_IMAGE_HEIGHT_PX = 2964;

    ////////////////////////////////////////////////////////////////////////////
    ///     Fields
    ////////////////////////////////////////////////////////////////////////////
    private static String currentStart; // Start location of route
    private static String currentEnd; // End location of route
    private static List<Place> currentRoute; // Route as list of places in order
    private static boolean wheelchair; // Toggle whether route needs to be wheelchair-accessible
    private static boolean noStairs; // Toggle whether route can have stairs
    private static boolean assistedEntrance; // Toggle whether an assisted entrance is required
    private static List<String> recentLocations; // Recently searched locations
    private static Set<String> buildingNames; // List of all building long names


    ////////////////////////////////////////////////////////////////////////////
    ///     Methods
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Initializes CampusPresenter.
     */
    public static void init(Context context) throws IOException {
        // Init model
        CampusModel.init(context);

        // Init Presenter variables
        buildingNames = CampusModel.getAllBuildingNames();
        recentLocations = new ArrayList<>();
        wheelchair = false;
        noStairs = false;
        assistedEntrance = false;
    }

    /**
     * Getter method for current start building
     */
    public static String getCurrentStart() {
        return currentStart;
    }

    /**
     * Getter method for current end building
     */
    public static String getCurrentEnd() {
        return currentEnd;
    }

    /**
     * Getter method for whether wheelchair filter is applied
     */
    public static boolean getWheelchair() {
        return wheelchair;
    }

    /**
     * Getter method for whether no-stairs filter is applied
     */
    public static boolean getNoStairs() {
        return noStairs;
    }

    /**
     * Getter method for whether assisted entrance filter is applied
     */
    public static boolean getAssistedEntrance() {
        return assistedEntrance;
    }

    /**
     * Finds the closest building to the given (x,y) coordinates and returns its long name.
     *
     * @param x coordinate (in pixels on map image)
     * @param y coordinate (in pixels on map image)
     * @requires x >= 0 && x <= width (in px) of map image
     * @requires y >= 0 && y <= height (in px) of map image
     * @throws IllegalArgumentException if passed invalid x and/or y coordinates
     *
     * @return long name of building closest to the given (x,y) coordinates
     */
    public static String getClosestBuilding(int x, int y) {
        if (x < 0 || x > CAMPUS_MAP_IMAGE_WIDTH_PX || y < 0 || y > CAMPUS_MAP_IMAGE_HEIGHT_PX) {
            throw new IllegalArgumentException();
        }

        // Get the closest building to the x,y coordinate
        return CampusModel.findClosestBuilding(x, y, false, false);
    }

    /**
     * Get coordinates (as a point) of roughly the center of the given building.
     *
     * @param longBuildingName is the long version of the desired building name
     * @throws IllegalArgumentException if longBuildingName is not a valid building name
     * @return point representing coordinates of roughly the center of the given building
     */
    public static Point getRoughCenterOfBuilding(String longBuildingName) {
        if (!buildingNames.contains(longBuildingName)) {
            System.out.println("AAA: " + longBuildingName);
            throw new IllegalArgumentException();
        }

        Set<Place> buildingEntrances = CampusModel.getEntrances(CampusModel.getShortName(longBuildingName), false);

        // Get top-left-most and bottom-right-most entrances
        Place topLeftPlace = null;
        Place botRightPlace = null;
        for (Place currPlace : buildingEntrances) {
            if (topLeftPlace == null || (currPlace.getX() + currPlace.getY()) < (topLeftPlace.getX() + topLeftPlace.getY())) {
                topLeftPlace = currPlace;
            }
            if (botRightPlace == null || (currPlace.getX() + currPlace.getY()) > (botRightPlace.getX() + botRightPlace.getY())) {
                botRightPlace = currPlace;
            }
        }

        // If only 1 entrance, return that entrance's coordinates. Otherwise, return average of topLeft and botRight entrance's
        // coordinates.
        if (topLeftPlace.equals(botRightPlace)) {
            return new Point((int) topLeftPlace.getX(), (int) topLeftPlace.getY());
        } else {
            int roughX = (int) (topLeftPlace.getX() + botRightPlace.getX())/2;
            int roughY = (int) (topLeftPlace.getY() + botRightPlace.getY())/2;
            return new Point(roughX, roughY);
        }
    }

    /**
     * Takes a long building name and updates currentStart.
     *
     * @param startBuildingName is long building name for start location
     * @requires startBuildingName is non-null, has length of >= 1, and is a valid building name
     * @throws IllegalArgumentException if startBuildingName requirements aren't met
     */
    public static void updateStart(String startBuildingName) {
        if (startBuildingName == null || startBuildingName.length() < 1 ||
            !buildingNames.contains(startBuildingName)) {
            throw new IllegalArgumentException();
        }
        currentStart = startBuildingName;
    }

    /**
     * Takes a long building name and updates currentEnd.
     *
     * @param endBuildingName is long building name for end location
     * @requires endBuildingName is non-null, has length of >= 1, and is a valid building name
     * @throws IllegalArgumentException if endBuildingName requirements aren't met
     */
    public static void updateEnd(String endBuildingName) {
        if (endBuildingName == null || endBuildingName.length() < 1 ||
                !buildingNames.contains(endBuildingName)) {
            throw new IllegalArgumentException();
        }
        currentEnd = endBuildingName;
    }

    /**
     * Returns route from currentStart to currentEnd positions. If either currentStart or
     * currentEnd is not set, this function returns null.
     *
     * @throws IllegalArgumentException if currentStart or currentEnd is null or empty
     * @return list of places in order representing route from start to finish
     *              (if route does not exist between the two places, returns empty list)
     */
    public static List<Place> getRoute() {
        if (currentStart == null || currentStart.length() == 0 ||
                currentEnd == null || currentEnd.length() == 0) {
            throw new IllegalArgumentException();
        }

        String shortStart = CampusModel.getShortName(currentStart);
        String shortEnd = CampusModel.getShortName(currentEnd);
        // Obtain the current route
        // Note: !noStairs is passed in since routes are checked on whether they have stairs or not
        currentRoute = CampusModel.shortestPathBetweenBuildings(shortStart, shortEnd, wheelchair, !noStairs);

        return currentRoute;
    }

    /**
     * Swaps the current start and end locations, and also reverses the current route.
     */
    public static void swapStartAndEnd() {
        String tempLocation = currentStart;
        currentStart = currentEnd;
        currentEnd = tempLocation;

        if (currentRoute != null && !currentRoute.isEmpty()) {
            Collections.reverse(currentRoute);
        }
    }

    /**
     * Updates the wheelchair field to dictate whether the route needs to be wheelchair-accessible
     * or not.
     *
     * @param toggledWC is whether the wheelchair-accessible route filter has been checked (e.g.
     *                'true' means the route MUST be wheelchair-accessible)
     */
    public static void updateWheelchair(boolean toggledWC) {
        wheelchair = toggledWC;
    }

    /**
     * Updates the noStairs field to dictate whether the route can have stairs.
     *
     * @param toggledNS is whether the route must not have stairs (e.g. 'true' means the route
     *                  MUST NOT have stairs on it)
     */
    public static void updateNoStairs(boolean toggledNS) {
        noStairs = toggledNS;
    }

    /**
     * Updates the assistedEntrance field to dictate whether the route must lead to an assisted
     * entrance.
     *
     * @param toggledAE is whether the route must end at an assisted entrance (e.g. 'true' means
     *                  the route MUST end at an assisted entrance)
     */
    public static void updateAssistedEntrance(boolean toggledAE) {
        assistedEntrance = toggledAE;
    }

    /**
     * Returns info of the given building, including elevator and bathroom information.
     *
     * @param buildingName is long building name for given location
     * @requires buildingName has length of >= 1 and is a valid building name
     * @throws IllegalArgumentException if buildingName requirements are not met
     *
     * @return description of building with the given long name as an array with the following
     * values:
     *    0 - short name of building
     *    1 - elevator info
     *    2 - accessible restroom info
     *    3 - gender-neutral restroom info
     */
    public static String[] getDesc(String buildingName) {
        if (buildingName == null || buildingName.length() == 0) {
            throw new IllegalArgumentException();
        }

        // Get building information
        String shortBuildingName = CampusModel.getShortName(buildingName);
        String elevatorInfo = CampusModel.hasElevatorAccess(shortBuildingName) ? "All floors" : "None";
        String accRestroomInfo = CampusModel.getAccessibleRestroomFloors(shortBuildingName);
        String genderNeutralRestroomInfo = CampusModel.getGenderNeutralRestroomFloors(shortBuildingName);

        // Restructure accessible restroom info
        String descAccRestroomInfo = "";
        if (accRestroomInfo.equals("All")) {
            descAccRestroomInfo += "All floors";
        } else if (accRestroomInfo.equals("None")) {
            descAccRestroomInfo += "None";
        } else {
            String[] accRestroomFloors = accRestroomInfo.split(" ");
            if (accRestroomFloors.length == 1) {
                descAccRestroomInfo = "Floor " + accRestroomFloors[0];
            } else if (accRestroomFloors.length == 2) {
                descAccRestroomInfo = "Floors " + accRestroomFloors[0] + " and " + accRestroomFloors[1];
            } else {
                descAccRestroomInfo = "Floors ";
                for (int i = 0; i < accRestroomFloors.length; i++) {
                    if (accRestroomFloors.length > 1 && i == accRestroomFloors.length - 1) {
                        descAccRestroomInfo += "and " + accRestroomFloors[i];
                    } else {
                        descAccRestroomInfo += accRestroomFloors[i] + ", ";
                    }
                }
            }
        }

        // Restructure gender-neutral restroom info
        String descGenderNeutralRestroomInfo = "";
        if (genderNeutralRestroomInfo.equals("All")) {
            descGenderNeutralRestroomInfo += "All floors";
        } else if (genderNeutralRestroomInfo.equals("None")) {
            descGenderNeutralRestroomInfo += "None";
        } else {
            String[] genderNeutralRestroomFloors = genderNeutralRestroomInfo.split(" ");
            if (genderNeutralRestroomFloors.length == 1) {
                descGenderNeutralRestroomInfo = "Floor " + genderNeutralRestroomFloors[0];
            } else if (genderNeutralRestroomFloors.length == 2) {
                descGenderNeutralRestroomInfo = "Floors " + genderNeutralRestroomFloors[0] + " and " + genderNeutralRestroomFloors[1];
            } else {
                descGenderNeutralRestroomInfo = "Floors ";
                for (int i = 0; i < genderNeutralRestroomFloors.length; i++) {
                    if (genderNeutralRestroomFloors.length > 1 && i == genderNeutralRestroomFloors.length - 1) {
                        descGenderNeutralRestroomInfo += "and " + genderNeutralRestroomFloors[i];
                    } else {
                        descGenderNeutralRestroomInfo += genderNeutralRestroomFloors[i] + ", ";
                    }
                }
            }
        }

        // Return description of given building
        return new String[]{shortBuildingName, elevatorInfo, descAccRestroomInfo, descGenderNeutralRestroomInfo};
    }

    /**
     * Finds the building with a gender-neutral bathroom closest to the given (x,y) coordinates
     * and returns its long name.
     *
     * @param x coordinate (in pixels on map image)
     * @param y coordinate (in pixels on map image)
     * @requires x >= 0 && x <= width (in pixels) of map image
     * @requires y >= 0 && y <= height (in pixels) of map image
     * @throws IllegalArgumentException if given invalid x,y coordinates
     *
     * @return long name of building with a gender-neutral bathroom closest to the given (x,y)
     * coordinates
     */
    public static String getClosestGNBathroom(int x, int y) {
        if (x < 0 || x > CAMPUS_MAP_IMAGE_WIDTH_PX || y < 0 || y > CAMPUS_MAP_IMAGE_HEIGHT_PX) {
            throw new IllegalArgumentException();
        }

        return CampusModel.findClosestBuilding(x, y, true, false);
    }

    /**
     * Gets the long names of all buildings on the UW campus
     * @return all long names of UW campus buildings
     */
    public static Set<String> getAllBuildingNames() {
        return CampusModel.getAllBuildingNames();
    }
}
