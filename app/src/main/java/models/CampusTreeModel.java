package models;

/**
 * This class represents another model component, namely the KD-Tree used to estimate the closest
 * building or place to a given x and y value
 */
public class CampusTreeModel {
    private BuildingTreeNode buildingRoot;
    private PlaceTreeNode placeRoot;

    /**
     * Creates a new CampusTreeModel object
     * @param buildingRoot root of the KD Tree of all buildings
     * @param placeRoot root of the KD Tree of all places
     */
    public CampusTreeModel(BuildingTreeNode buildingRoot, PlaceTreeNode placeRoot) {
        this.buildingRoot = buildingRoot;
        this.placeRoot = placeRoot;
    }

    /**
     * Finds the building that is closest to the given x, y
     * @param x x value of the point we want the closest building to
     * @param y y value of the point we want the closest building to
     * @param genderNeutralRestroom True if the building must have a genderNeutralRestroom
     * @param elevator True if the building must have an elevator
     * @return The long name of the building closest to the given x, y
     */
    public String findClosestBuilding(float x, float y, boolean genderNeutralRestroom,
                                      boolean elevator) {
        return "";
    }

    /**
     * Finds the place that is closest to the given x, y
     * @param x x value of the point we want the closest place to
     * @param y y value of the point we want the closest place to
     * @return the Place closest to the given x, y
     */
    public Place findClosestPlace(float x, float y) {
        return null;
    }
}
