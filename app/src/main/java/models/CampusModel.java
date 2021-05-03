package models;

import java.util.List;
import java.util.Set;

/**
 * This class defines the Model component of the Model-View-Presenter architecture. You can query
 * this model to find the closest path between two places, retrieve building information, or get
 * the closest building or place to a given x, y position.
 */
public class CampusModel {

    /**
     * This method initializes a CampusModel program
     * @param filepath filepath to the UW Campus data
     * @throws IllegalArgumentException if the filepath does not exist
     */
    public static void init(String filepath) throws IllegalArgumentException {

    }

    /**
     * Gets the short name identifier of a building on campus
     * @param longName long name of the building on UW campus
     * @return short name id corresponding to the given long name
     */
    public static String getShortName(String longName) {
        return "";
    }

    /**
     * Gets the short name identifier of a building on campus
     * @param shortName short name identifier of the building on UW campus
     * @return long name corresponding to the given short name id
     * @throws IllegalArgumentException if shortName is not a building on the UW campus
     */
    public static String getLongName(String shortName) throws IllegalArgumentException {
        return "";
    }

    /**
     * Gets the building corresponding to the given short name identifier
     * @param shortName short name identifier of the building
     * @return building corresponding to the given short name
     * @throws IllegalArgumentException if the short name doesn't have a corresponding building
     */
    public static Building getBuilding(String shortName) {
        return null;
    }

    /**
     * Gets the description of the given building
     * @param shortName short name id of the building you want the description of
     * @return description of the given building
     */
    public static String getBuildingDescription(String shortName) {
        return "";
    }

    /**
     * Gets the entrances of a given building, with the option of only getting accessible entrances
     * @param shortName short name id of the building you want entrances of
     * @param assisted true if you only want assited entrances, false otherwise
     * @return Places that represent entrances of the given building
     */
    public static Set<Place> getEntrances(String shortName, boolean assisted) {
        return null;
    }

    /**
     * Gets the closest entrance to the given x, y location
     * @param x x position you want the closest entrance to
     * @param y y position you want the closest entrance to
     * @param shortName short name identifier of the building you want the closest entrance of
     * @param assisted true if the entrance must be assited, false otherwise
     * @return closest entrance of the given building to the given x, y
     */
    public static Place getClosestEntrance(float x, float y, String shortName, boolean assisted) {
        return null;
    }

    /**
     * Get the address of the given building
     * @param shortName short name identifier of the building you want the address of
     * @return address of the given building
     */
    public static String getAddress(String shortName) {
        return "";
    }

    /**
     * Get whether of not the given building has elevator access
     * @param shortName short name identifier of the building
     * @return true if the building has elevator access, otherwise false
     */
    public static boolean hasElevatorAccess(String shortName) {
        return false;
    }

    /**
     * Get whether or not the given building has a gender neutral restroom
     * @param shortName short name identifier of the building
     * @return true if the building has a gender neutral restroom, otherwise false
     */
    public static boolean hasGenderNeutralRestroom(String shortName) {
        return false;
    }

    /**
     * Gets the long names of all buildings on the UW campus
     * @return all long names of UW campus buildings
     */
    public static Set<String> getAllBuildingNames() {
        return null;
    }

    /**
     * Finds the building that is closest to the given x, y
     * @param x x value of the point we want the closest building to
     * @param y y value of the point we want the closest building to
     * @param genderNeutralRestroom True if the building must have a genderNeutralRestroom
     * @param elevator True if the building must have an elevator
     * @return The long name of the building closest to the given x, y
     */
    public static String findClosestBuilding(float x, float y, boolean genderNeutralRestroom,
                                      boolean elevator) {
        return "";
    }

    /**
     * Finds the place that is closest to the given x, y
     * @param x x value of the point we want the closest place to
     * @param y y value of the point we want the closest place to
     * @return the Place closest to the given x, y
     */
    public static Place findClosestPlace(float x, float y) {
        return null;
    }

    /**
     * Returns the shortest path between start and any of the places in ends
     * @param start Place we start the path from
     * @param end Building we want to end the path at
     * @param wheelchair True if the path needs to be wheelchair accessible
     * @param stairs True if the path can have stairs
     * @return A list of places that we will visit on this path, or an empty list if a valid path
     * doesn't exist
     */
    public static List<Place> getShortestPath(Place start, Building end, boolean wheelchair,
                                       boolean stairs) {
        return null;
    }

    /**
     * Returns the shortest path between the start building and the end building
     * @param start building we start at
     * @param end building we end at
     * @param wheelchair true if the path needs to be wheelchair accessible
     * @param stairs true if the path can have stairs
     * @return A list of places that we will visit on this path, or an empty list if a valid path
     * doesn't exist.
     */
    public static List<Place> shortestPathBetweenBuildings(Building start, Building end,
                                                    boolean wheelchair, boolean stairs) {
        return null;
    }
}
