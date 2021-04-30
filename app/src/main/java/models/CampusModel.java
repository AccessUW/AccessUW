package models;

import java.util.List;
import java.util.Set;

public class CampusModel {
    private BuildingInfoModel buildingInfo;
    private CampusTreeModel campusTree;
    private RouteFinderModel routeFinder;

    public String getShortName(String longName) {
        return buildingInfo.getShortName(longName);
    }

    public String getBuildingDescription(String shortName) {
        return buildingInfo.getBuildingDescription(shortName);
    }

    public Set<Place> getEntrances(String shortName, boolean assisted) {
        return null;
    }

    public Place getClosestEntrance(float x, float y, String shortName, boolean assisted) {
        return null;
    }

    public boolean hasElevatorAccess(String shortName) {
        return false;
    }

    public boolean hasGenderNeutralRestroom(String shortName) {
        return false;
    }

    public String findClosestBuilding(float x, float y, boolean restroom) {
        return "";
    }

    public Place findCLosestPlace(float x, float y) {
        return null;
    }

    public List<Place> getShortestPath(Place start, Set<Place> ends, boolean wheelchair,
                                       boolean noStairs) {
        return null;
    }
}
