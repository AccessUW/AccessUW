package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class defines the Model component of the Model-View-Presenter architecture. You can query
 * this model to find the closest path between two places, retrieve building information, or get
 * the closest building or place to a given x, y position.
 */
public class CampusModel {
    private static CampusTreeModel campusTreeModel;
    private static RouteFinderModel routeFinderModel;
    private static BuildingInfoModel buildingInfoModel;

    /**
     * This method initializes a CampusModel program
     * @param filepath filepath to the map_data
     * @throws IllegalArgumentException if the filepath does not exist or does not contain the data
     * @throws IOException if there was an error when reading from the csv data
     */
    public static void init(String filepath) throws IllegalArgumentException, IOException {
        File entranceFile = new File(filepath + "campus_entrance_data.csv");
        File pathFile = new File(filepath + "campus_path_data.csv");

        FileReader entranceFileReader;
        FileReader pathFileReader;
        try {
            entranceFileReader = new FileReader(entranceFile);
            pathFileReader = new FileReader(pathFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("CampusModel init -- given filepath does not " +
                    "contain necessary entrance or path csv files");
        }

        BufferedReader entranceReader = new BufferedReader(entranceFileReader);

        // Data to fill
        Set<Place> allPlaces = new HashSet<>();
        Map<String, String> shortToLongName = new HashMap<>();
        Map<String, String> longToShortName = new HashMap<>();
        Map<String, Building> shortToBuilding = new HashMap<>();

        while (true) {
            String row = entranceReader.readLine();
            if (row == null) {
                break;
            }

            String[] data = row.split(",");
            // 0 : short name
            // 1 : long name
            // 2 : x
            // 3 : y
            // 4 : type of entrance (M = manual, A = assisted, U = unaccessible)

            String shortName = data[0];
            if (shortName.equals("")) { // Skip over incomplete data for now
                continue;
            }
            // Remove the parentheses from short names
            int parenIdx = shortName.indexOf('(');
            if (parenIdx != -1) {
                shortName = shortName.substring(0, parenIdx - 1);
            }

            if (!shortToBuilding.containsKey(shortName)) {
                // TODO: add correct restroom, elevator, and description when building data is done
                shortToBuilding.put(shortName, new Building(shortName, true, true, ""));
            }

            Building building = shortToBuilding.get(shortName);

            // Get the long name of the building
            String longName = data[1];
            parenIdx = longName.indexOf('(');
            if (parenIdx != -1) {
                longName = longName.substring(0, parenIdx - 1);
            }
            shortToLongName.put(shortName, longName);
            longToShortName.put(longName, shortName);

            // Add the entrance for this building
            if (data[2].equals("") || data[3].equals("")) {
                continue; // continue if there is no entrance coordinates
            }
            float x = Float.parseFloat(data[2]);
            float y = Float.parseFloat(data[3]);
            Place entrance = new Place(x, y, true);
            allPlaces.add(entrance);
            building.addEntrance(entrance, !data[4].equals("U"));
        }
        entranceReader.close();

    }

    /**
     * Gets the short name identifier of a building on campus
     * @param longName long name of the building on UW campus
     * @return short name id corresponding to the given long name
     */
    public static String getShortName(String longName) {
        return buildingInfoModel.getShortName(longName);
    }

    /**
     * Gets the short name identifier of a building on campus
     * @param shortName short name identifier of the building on UW campus
     * @return long name corresponding to the given short name id
     * @throws IllegalArgumentException if shortName is not a building on the UW campus
     */
    public static String getLongName(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.getLongName(shortName);
    }

    /**
     * Gets the building corresponding to the given short name identifier
     * @param shortName short name identifier of the building
     * @return building corresponding to the given short name
     * @throws IllegalArgumentException if the short name doesn't have a corresponding building
     */
    public static Building getBuilding(String shortName) {
        return buildingInfoModel.getBuilding(shortName);
    }

    /**
     * Gets the description of the given building
     * @param shortName short name id of the building you want the description of
     * @return description of the given building
     */
    public static String getBuildingDescription(String shortName) {
        return buildingInfoModel.getBuildingDescription(shortName);
    }

    /**
     * Gets the entrances of a given building, with the option of only getting accessible entrances
     * @param shortName short name id of the building you want entrances of
     * @param assisted true if you only want assited entrances, false otherwise
     * @return Places that represent entrances of the given building
     */
    public static Set<Place> getEntrances(String shortName, boolean assisted) {
        return buildingInfoModel.getEntrances(shortName, assisted);
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
        return buildingInfoModel.getClosestEntrance(x, y, shortName, assisted);
    }

    /**
     * Get the address of the given building
     * @param shortName short name identifier of the building you want the address of
     * @return address of the given building
     */
    public static String getAddress(String shortName) {
        return buildingInfoModel.getAddress(shortName);
    }

    /**
     * Get whether of not the given building has elevator access
     * @param shortName short name identifier of the building
     * @return true if the building has elevator access, otherwise false
     */
    public static boolean hasElevatorAccess(String shortName) {
        return buildingInfoModel.hasElevatorAccess(shortName);
    }

    /**
     * Get whether or not the given building has a gender neutral restroom
     * @param shortName short name identifier of the building
     * @return true if the building has a gender neutral restroom, otherwise false
     */
    public static boolean hasGenderNeutralRestroom(String shortName) {
        return buildingInfoModel.hasGenderNeutralRestroom(shortName);
    }

    /**
     * Gets the long names of all buildings on the UW campus
     * @return all long names of UW campus buildings
     */
    public static Set<String> getAllBuildingNames() {
        return buildingInfoModel.getAllBuildingNames();
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
        return campusTreeModel.findClosestBuilding(x, y, genderNeutralRestroom, elevator);
    }

    /**
     * Finds the place that is closest to the given x, y
     * @param x x value of the point we want the closest place to
     * @param y y value of the point we want the closest place to
     * @return the Place closest to the given x, y
     */
    public static Place findClosestPlace(float x, float y) {
        return campusTreeModel.findClosestPlace(x, y);
    }

    /**
     * Returns the shortest path between start and any of the places in ends
     * @param startx x location the route starts from
     * @param starty y location the route starts from
     * @param end shortName identifier of the building we want to end the path at
     * @param wheelchair True if the path needs to be wheelchair accessible
     * @param stairs True if the path can have stairs
     * @return A list of places that we will visit on this path, or an empty list if a valid path
     * doesn't exist
     */
    public static List<Place> getShortestPath(float startx, float starty, String end,
                                              boolean wheelchair, boolean stairs) {
        Place start = campusTreeModel.findClosestPlace(startx, starty);
        Building endBuilding = buildingInfoModel.getBuilding(end);
        return routeFinderModel.shortestPath(start, endBuilding, wheelchair, stairs);
    }

    /**
     * Returns the shortest path between the start building and the end building
     * @param start shortName identifer of the building we start at
     * @param end shortName identifier of the building we end at
     * @param wheelchair true if the path needs to be wheelchair accessible
     * @param stairs true if the path can have stairs
     * @return A list of places that we will visit on this path, or an empty list if a valid path
     * doesn't exist.
     */
    public static List<Place> shortestPathBetweenBuildings(String start, String end,
                                                           boolean wheelchair, boolean stairs) {
        Building startBuilding = buildingInfoModel.getBuilding(start);
        Building endBuilding = buildingInfoModel.getBuilding(end);
        return routeFinderModel.shortestPathBetweenBuildings(startBuilding, endBuilding, wheelchair,
                stairs);
    }
}
