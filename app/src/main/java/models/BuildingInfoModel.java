package models;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class defines a model for information of all buildings of the UW campus
 */
public class BuildingInfoModel {
    private Map<String, String> longToShortName;
    private Map<String, String> shortToLongName;
    private Map<String, Building> shortNameToBuilding;

    /**
     * Constructs a new BuildingInfoModel object
     * @param longToShortName maps building long names to short names
     * @param shortToLongName maps building short names to long names
     * @param shortNameToBuilding maps building short names to the building
     */
    public BuildingInfoModel(Map<String, String> longToShortName, Map<String, String> shortToLongName,
                             Map<String, Building> shortNameToBuilding) {
        this.longToShortName = longToShortName;
        this.shortToLongName = shortToLongName;
        this.shortNameToBuilding = shortNameToBuilding;
    }

    /**
     * Gets the short name identifier of a building on campus
     * @param longName long name of the building on UW campus
     * @return short name id corresponding to the given long name
     * @throws IllegalArgumentException if longName is not a building on the UW campus
     */
    public String getShortName(String longName) throws IllegalArgumentException {
        if (!longToShortName.containsKey(longName)) {
            throw new IllegalArgumentException("getShortName - given name isn't a building on the" +
                    " UW campus");
        }
        return longToShortName.get(longName);
    }

    /**
     * Gets the short name identifier of a building on campus
     * @param shortName short name identifier of the building on UW campus
     * @return long name corresponding to the given short name id
     * @throws IllegalArgumentException if shortName is not a building on the UW campus
     */
    public String getLongName(String shortName) throws IllegalArgumentException {
        if (!shortToLongName.containsKey(shortName)) {
            throw new IllegalArgumentException("getLongName - given name isn't a building on the" +
                    " UW campus");
        }
        return shortToLongName.get(shortName);
    }

    /**
     * Gets the building corresponding to the given short name identifier
     * @param shortName short name identifier of the building
     * @return building corresponding to the given short name
     * @throws IllegalArgumentException if the short name doesn't have a corresponding building
     */
    public Building getBuilding(String shortName) throws IllegalArgumentException {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("getBuilding - given name isn't a building on the" +
                    " UW campus");
        }
        return shortNameToBuilding.get(shortName);
    }

    /**
     * Gets the entrances of a given building, with the option of only getting accessible entrances
     * @param shortName short name id of the building you want entrances of
     * @param assisted true if you only want assited entrances, false otherwise
     * @return Places that represent entrances of the given building
     * @throws IllegalArgumentException if the given short name is invalid
     */
    public Set<Place> getEntrances(String shortName, boolean assisted) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("getEntrances -- given short name is invalid");
        }

        if (assisted) {
            return shortNameToBuilding.get(shortName).getAccessibleEntrances();
        }
        return shortNameToBuilding.get(shortName).getEntrances();
    }

    /**
     * Gets the closest entrance to the given x, y location
     * @param x x position you want the closest entrance to
     * @param y y position you want the closest entrance to
     * @param shortName short name identifier of the building you want the closest entrance of
     * @param assisted true if the entrance must be assited, false otherwise
     * @return closest entrance of the given building to the given x, y or null if no entrance exists
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public Place getClosestEntrance(float x, float y, String shortName, boolean assisted) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("getClosestEntrance -- given short name is invalid");
        }
        Set<Place> entrances = getEntrances(shortName, assisted);
        Place best = null;
        for (Place p : entrances) {
            if (best == null || getDistance(x, y, p) < getDistance(x, y, best)) {
                best = p;
            }
        }
        return best;
    }

    /**
     * Gets the distance between two places
     * @param x x coordinate we want distance between
     * @param y y coordinate we want distance between
     * @param p2 place we want to get distance between
     * @return euclidean distance between the two places
     */
    private float getDistance(float x, float y, Place p2) {
        return (float) Math.sqrt(Math.pow(x - p2.getX(), 2) + Math.pow(y - p2.getY(), 2));
    }

    /**
     * Get the address of the given building
     * @param shortName short name identifier of the building you want the address of
     * @return address of the given building
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public String getAddress(String shortName) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("getAddress -- given short name is invalid");
        }
        // TODO: implement getAddress
        return "";
    }

    /**
     * Get whether of not the given building has elevator access
     * @param shortName short name identifier of the building
     * @return true if the building has elevator access, otherwise false
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public boolean hasElevatorAccess(String shortName) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("getAddress -- given short name is invalid");
        }
        return shortNameToBuilding.get(shortName).hasElevator();
    }

    /**
     * Get whether or not the given building has a gender neutral restroom
     * @param shortName short name identifier of the building
     * @return true if the building has a gender neutral restroom, otherwise false
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public boolean hasGenderNeutralRestroom(String shortName) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("hasGenderNeutralRestroom -- given short name is invalid");
        }
        return shortNameToBuilding.get(shortName).hasGenderNeutralRestroom();
    }

    /**
     * Gets the floor numbers of all floors with a gender neutral restroom in this building
     * @param shortName short name identifier of the building
     * @return Space separated string of floors with gender neutral restroom, with the empty string
     * meaning there are no floors and 'All' meaning there are on all floors
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public String getGenderNeutralRestroomFloors(String shortName) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("getGenderNeutralRestroomFloors -- given short name is invalid");
        }
        return shortNameToBuilding.get(shortName).getGenderNeutralRestroomFloors();
    }

    /**
     * Get whether or not the given building has an accessible restroom
     * @param shortName short name identifier of the building
     * @return true if the building has an accessible restroom, otherwise false
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public boolean hasAccessibleRestroom(String shortName) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("hasAccessibleRestroom -- given short name is invalid");
        }
        return shortNameToBuilding.get(shortName).hasAccessibleRestroom();
    }

    /**
     * Gets the floor numbers of all floors with an accessible restroom in this building
     * @param shortName short name identifier of the building
     * @return Space separated string of floors with an accessible restroom, with the empty string
     * meaning there are no floors and 'All' meaning there are on all floors
     * @throws IllegalArgumentException if the given shortname is not valid
     */
    public String getAccessibleRestroomFloors(String shortName) {
        if (!shortNameToBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException("getAccessibleRestroomFloors -- given short name is invalid");
        }
        return shortNameToBuilding.get(shortName).getAccessibleRestroomFloors();
    }

    /**
     * Gets the long names of all buildings on the UW campus
     * @return all long names of UW campus buildings
     */
    public Set<String> getAllBuildingNames() {
        Set<String> allNames = new HashSet<>();
        for (Building b : shortNameToBuilding.values()) {
            allNames.add(getLongName(b.getShortName()));
        }
        return allNames;
    }
}
