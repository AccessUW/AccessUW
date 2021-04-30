package models;

import java.util.Map;
import java.util.Set;

public class BuildingInfoModel {
    private Map<String, String> longToShortName;
    private Map<String, String> shortToLongName;
    private Map<String, Building> shortNameToBuilding;
    private Map<String, String> shortNameToAddress;

    public String getShortName(String longName) {
        return "";
    }

    public String getBuildingDescription(String shortName) {
        return "";
    }

    public Set<Place> getEntrances(String shortName, boolean assisted) {
        return null;
    }

    public Place getClosestEntrance(float x, float y, String shortName, boolean assisted) {
        return null;
    }

    public String getAddress(String shortName) {
        return "";
    }

    public boolean hasElevatorAccess(String shortName) {
        return false;
    }

    public boolean hasGenderNeutralRestroom(String shortName) {
        return false;
    }
}
