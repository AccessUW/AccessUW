package models;

import java.util.Map;
import java.util.Set;

/**
 * This class defines a model for information of all buildings of the UW campus
 */
public class BuildingInfoModel {
    private Map<String, String> longToShortName;
    private Map<String, Building> shortNameToBuilding;
    private Map<String, String> shortNameToAddress;

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
        return "";
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
        return null;
    }

    /**
     * Gets the description of the given building
     * @param shortName short name id of the building you want the description of
     * @return description of the given building
     */
    public String getBuildingDescription(String shortName) {
        return "";
    }

    /**
     * Gets the entrances of a given building, with the option of only getting accessible entrances
     * @param shortName short name id of the building you want entrances of
     * @param assisted true if you only want assited entrances, false otherwise
     * @return Places that represent entrances of the given building
     */
    public Set<Place> getEntrances(String shortName, boolean assisted) {
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
    public Place getClosestEntrance(float x, float y, String shortName, boolean assisted) {
        return null;
    }

    /**
     * Get the address of the given building
     * @param shortName short name identifier of the building you want the address of
     * @return address of the given building
     */
    public String getAddress(String shortName) {
        return "";
    }

    /**
     * Get whether of not the given building has elevator access
     * @param shortName short name identifier of the building
     * @return true if the building has elevator access, otherwise false
     */
    public boolean hasElevatorAccess(String shortName) {
        return false;
    }

    /**
     * Get whether or not the given building has a gender neutral restroom
     * @param shortName short name identifier of the building
     * @return true if the building has a gender neutral restroom, otherwise false
     */
    public boolean hasGenderNeutralRestroom(String shortName) {
        return false;
    }

    /**
     * Gets the long names of all buildings on the UW campus
     * @return all long names of UW campus buildings
     */
    public Set<String> getAllBuildingNames() {
        return null;
    }
}
