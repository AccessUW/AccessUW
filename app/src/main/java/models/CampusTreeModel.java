package models;

public class CampusTreeModel {
    private BuildingTreeNode buildingRoot;
    private PlaceTreeNode placeRoot;

    public CampusTreeModel(BuildingTreeNode buildingRoot, PlaceTreeNode placeRoot) {
        this.buildingRoot = buildingRoot;
        this.placeRoot = placeRoot;
    }

    public String findClosestBuilding(float x, float y, boolean genderNeutralRestroom,
                                      boolean elevator) {
        return "";
    }

    public Place findClosestPlace(float x, float y) {
        return null;
    }
}
