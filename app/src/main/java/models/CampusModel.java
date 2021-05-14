package models;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;

import com.example.accessUWMap.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
     * This method initializes a CampusModel program if it hasn't been initialized already
     * @param context for access to resources including raw folder of campus map data
     * @throws IllegalArgumentException if the filepath does not exist or does not contain the data
     * @throws IOException if there was an error when reading from the csv data
     */
    public static void init(Context context) throws IllegalArgumentException, IOException {
        if (campusTreeModel != null && routeFinderModel != null && buildingInfoModel != null) {
            return; // do nothing if we've already initialized
        } else {
            campusTreeModel = null;
            routeFinderModel = null;
            buildingInfoModel = null;
        }

        InputStream entranceInputStream;
        InputStream pathInputStream;
        InputStream buildingInputStream;
        try {
            entranceInputStream = context.getResources().openRawResource(R.raw.campus_entrance_data);
            pathInputStream = context.getResources().openRawResource(R.raw.campus_path_data);
            buildingInputStream = context.getResources().openRawResource(R.raw.campus_descriptions_data);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("CampusModel init -- given resource context does not " +
                    "contain necessary entrance or path csv files");
        }

        // Data to fill
        Set<Place> allPlaces = new HashSet<>();
        Map<String, String> shortToLongName = new HashMap<>();
        Map<String, String> longToShortName = new HashMap<>();
        Map<String, String> longToGNFloors = new HashMap<>();
        Map<String, String> longToAccFloors = new HashMap<>();
        Map<String, Float> longToX = new HashMap<>();
        Map<String, Float> longToY = new HashMap<>();

        Map<String, Building> shortToBuilding = new HashMap<>();

        // Add the building data
        BufferedReader buildingReader = new BufferedReader(new InputStreamReader(buildingInputStream));
        buildingReader.readLine(); // Skip column titles
        while (true) {
            String row = buildingReader.readLine();
            if (row == null) {
                break;
            }

            String[] data = row.split(",");
            // 0 : long name
            // 1 : x
            // 2 : y
            // 3 : gn restroom floors (separated by ' ')
            // 4 : accessible restroom floors (separated by ' ')
            // 5 : elevator access to all floors? (All if yes)
            if (data.length == 6) {
                boolean incomplete = false;
                for (int i = 0; i < 6; i++) {
                    if (data[i].equals("")) {
                        incomplete = true;
                        break;
                    }
                }

                if (incomplete) {
                    continue;
                }

                String longName = data[0];
                float x;
                try {
                    x = Float.parseFloat(data[1]);
                } catch (NumberFormatException e) {
                    x = 0;
                }

                float y;
                try {
                    y = Float.parseFloat(data[2]);
                } catch (NumberFormatException e) {
                    y = 0;
                }

                String gnFloors = data[3];
                String accFloors = data[4];

                // no need to check the last data point for elevator access, because all are true
                /*
                boolean elevator = true;
                if (!data[5].equals("All")) {
                    elevator = false;
                }*/

                longToAccFloors.put(longName, accFloors);
                longToGNFloors.put(longName, gnFloors);
                longToX.put(longName, x);
                longToY.put(longName, y);
            }

        }
        buildingReader.close();
        buildingInputStream.close();

        // Add the entrance data
        BufferedReader entranceReader = new BufferedReader(new InputStreamReader(entranceInputStream));
        entranceReader.readLine(); // Skip column title line
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
            if (data.length == 5) {
                boolean incomplete = false;
                for (int i = 0; i < 5; i++) {
                    if (data[i].equals("")) {
                        incomplete = true;
                        break;
                    }
                }

                if (incomplete) {
                    continue;
                }

                // Get the short name of the building
                String shortName = data[0];
                int parenIdx = shortName.indexOf('(');
                if (parenIdx != -1) {
                    shortName = shortName.substring(0, parenIdx - 1);
                }

                // Get the long name of the building
                String longName = data[1];
                parenIdx = longName.indexOf('(');
                if (parenIdx != -1) {
                    longName = longName.substring(0, parenIdx - 1);
                }

                if (!shortToBuilding.containsKey(shortName)) {
                    // Add the building if we have all info on it, otherwise add a simple version of it
                    if (longToX.containsKey(longName) && longToY.containsKey(longName) &&
                            longToGNFloors.containsKey(longName) && longToAccFloors.containsKey(longName)) {
                        shortToBuilding.put(shortName, new Building(shortName, longToX.get(longName),
                                longToY.get(longName), longToGNFloors.get(longName),
                                longToAccFloors.get(longName), true));
                    } else {
                        System.out.println("Adding placeholder building for: " + shortName);
                        shortToBuilding.put(shortName, new Building(shortName, 0, 0, "", "", true));
                    }
                }

                Building building = shortToBuilding.get(shortName);
                if (building == null) {
                    continue;
                }

                shortToLongName.put(shortName, longName);
                longToShortName.put(longName, shortName);

                // Add the entrance for this building
                if (data[2].equals("0") || data[3].equals("0")) {
                    continue; // continue if there is no entrance coordinates
                }
                float x = Float.parseFloat(data[2]);
                float y = Float.parseFloat(data[3]);
                Place entrance = new Place(x, y, true);
                allPlaces.add(entrance);
                building.addEntrance(entrance, !data[4].equals("U"));
            }
        }
        entranceReader.close();
        entranceInputStream.close();

        // Add all buildings found to set
        Set<Building> allBuildings = new HashSet<>(shortToBuilding.values());

        // Add the path data
        BufferedReader pathReader = new BufferedReader(new InputStreamReader(pathInputStream));
        pathReader.readLine(); // Skip column title line
        while (true) {
            String row = pathReader.readLine();
            if (row == null) {
                break;
            }

            String[] data = row.split(",");
            // 0 : startx
            // 1 : starty
            // 2 : endx
            // 3 : endy
            // 4 : wheelchair
            // 5 : no stairs?

            // Skip if we don't have a complete row
            boolean incomplete = false;
            for (String val : data) {
                if (val.equals("")) {
                    incomplete = true;
                    break;
                }
            }

            if (incomplete) {
                continue;
            }

            float x1 = Float.parseFloat(data[0]);
            float y1 = Float.parseFloat(data[1]);
            float x2 = Float.parseFloat(data[2]);
            float y2 = Float.parseFloat(data[3]);
            boolean wheelchairAccessible = data[4].equals("T");
            boolean hasStairs = data[5].equals("F");

            // TODO: optimize so we don't have O(n^2) time to add an edge each time
            // Check to see if there are places for either end
            Place p1 = null;
            Place p2 = null;
            for (Place p : allPlaces) {
                if (p.getX() == x1 && p.getY() == y1) {
                    p1 = p;
                } else if (p.getX() == x2 && p.getY() == y2) {
                    p2 = p;
                }

                if (p1 != null && p2 != null) {
                    break;
                }
            }

            if (p1 == null) {
                p1 = new Place(x1, y1, false);
                allPlaces.add(p1);
            }
            if (p2 == null) {
                p2 = new Place(x2, y2, false);
                allPlaces.add(p2);
            }

            // Add edge between the places
            p1.addNeighbor(p2, getDistance(p1, p2), wheelchairAccessible, hasStairs);
        }
        pathReader.close();
        pathInputStream.close();

        // Initialize the Models
        campusTreeModel = new CampusTreeModel(allBuildings, allPlaces);
        routeFinderModel = new RouteFinderModel();
        buildingInfoModel = new BuildingInfoModel(longToShortName, shortToLongName, shortToBuilding);
    }

    /**
     * Gets the distance between two places
     * @param p1 place we want to get distance between
     * @param p2 place we want to get distance between
     * @return euclidean distance between the two places
     */
    private static float getDistance(Place p1, Place p2) {
        return (float) Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    /**
     * Gets the short name identifier of a building on campus
     * @param longName long name of the building on UW campus
     * @return short name id corresponding to the given long name
     * @throws IllegalArgumentException if longName is not a building on the UW campus
     */
    public static String getShortName(String longName) throws IllegalArgumentException {
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
    public static Building getBuilding(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.getBuilding(shortName);
    }

    /**
     * Gets the description of the given building
     * @param shortName short name id of the building you want the description of
     * @return description of the given building
     * @throws IllegalArgumentException if the given short name is invalid
     */
    public static String getBuildingDescription(String shortName) throws IllegalArgumentException {
        return "";
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
     * @throws IllegalArgumentException if the given short name is invalid
     */
    public static Place getClosestEntrance(float x, float y, String shortName, boolean assisted)
            throws IllegalArgumentException{
        return buildingInfoModel.getClosestEntrance(x, y, shortName, assisted);
    }

    /**
     * Get the address of the given building
     * @param shortName short name identifier of the building you want the address of
     * @return address of the given building
     * @throws IllegalArgumentException if the given short name is invalid
     */
    public static String getAddress(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.getAddress(shortName);
    }

    /**
     * Get whether of not the given building has elevator access
     * @param shortName short name identifier of the building
     * @return true if the building has elevator access, otherwise false
     * @throws IllegalArgumentException if the given short name is invalid
     */
    public static boolean hasElevatorAccess(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.hasElevatorAccess(shortName);
    }

    /**
     * Get whether or not the given building has a gender neutral restroom
     * @param shortName short name identifier of the building
     * @return true if the building has a gender neutral restroom, otherwise false
     * @throws IllegalArgumentException if the given short name is invalid
     */
    public static boolean hasGenderNeutralRestroom(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.hasGenderNeutralRestroom(shortName);
    }

    /**
     * Gets the floor numbers of all floors with a gender neutral restroom in this building
     * @param shortName short name identifier of the building
     * @return Space separated string of floors with gender neutral restroom, with the empty string
     * meaning there are no floors and 'All' meaning there are on all floors
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public static String getGenderNeutralRestroomFloors(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.getGenderNeutralRestroomFloors(shortName);
    }

    /**
     * Get whether or not the given building has an accessible restroom
     * @param shortName short name identifier of the building
     * @return true if the building has an accessible restroom, otherwise false
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public static boolean hasAccessibleRestroom(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.hasAccessibleRestroom(shortName);
    }

    /**
     * Gets the floor numbers of all floors with an accessible restroom in this building
     * @param shortName short name identifier of the building
     * @return Space separated string of floors with an accessible restroom, with the empty string
     * meaning there are no floors and 'All' meaning there are on all floors
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public static String getAccessibleRestroomFloors(String shortName) throws IllegalArgumentException {
        return buildingInfoModel.getAccessibleRestroomFloors(shortName);
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
     * @throws IllegalStateException if there are no buildings or initialization had failed
     */
    public static String findClosestBuilding(float x, float y, boolean genderNeutralRestroom,
                                      boolean elevator) throws IllegalStateException {
        String shortName = campusTreeModel.findClosestBuilding(x, y, genderNeutralRestroom, elevator);
        return getLongName(shortName);
    }

    /**
     * Finds the place that is closest to the given x, y
     * @param x x value of the point we want the closest place to
     * @param y y value of the point we want the closest place to
     * @return the Place closest to the given x, y
     * @throws IllegalStateException if there are no buildings or initialization had failed
     */
    public static Place findClosestPlace(float x, float y) throws IllegalStateException {
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
                                              boolean wheelchair, boolean stairs) throws
            IllegalArgumentException {
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
                                                           boolean wheelchair, boolean stairs)
            throws IllegalArgumentException {
        Building startBuilding = buildingInfoModel.getBuilding(start);
        Building endBuilding = buildingInfoModel.getBuilding(end);
        return routeFinderModel.shortestPathBetweenBuildings(startBuilding, endBuilding, wheelchair,
                stairs);
    }
}
