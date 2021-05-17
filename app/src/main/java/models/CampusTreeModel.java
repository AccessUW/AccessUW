package models;

import java.util.Set;

/**
 * This class represents another model component, namely the KD-Tree used to estimate the closest
 * building or place to a given x and y value
 */
public class CampusTreeModel {
    private BuildingTreeNode buildingRoot;
    private PlaceTreeNode placeRoot;

    /**
     * Creates a new CampusTreeModel object
     * @param buildings set of all buildings on the UW campus
     * @param places set of all places on the UW campus
     * @throws IllegalArgumentException if buildings or places is null
     */
    public CampusTreeModel(Set<Building> buildings, Set<Place> places) {
        if (buildings == null || places == null) {
            throw new IllegalArgumentException("CampusTreeModel -- parameters cannot be null");
        }

        // Initialize the building tree
        for (Building b : buildings) {
            buildingRoot = buildBuildingTree(buildingRoot, true, b);
        }

        // Initialize the place tree
        for (Place p : places) {
            placeRoot = buildPlaceTree(placeRoot, true, p);
        }
    }

    /**
     * Recursively builds the Building K-D Tree
     * @param n current node we are examining
     * @param leftRight true if we are examining x coordinates, false otherwise
     * @param building Building we are attempting to add to the tree
     * @return our current node after adding the given place
     */
    private BuildingTreeNode buildBuildingTree(BuildingTreeNode n, boolean leftRight, Building building) {
        if (n == null) {
            return new BuildingTreeNode(building);
        }

        // If the building already exists in the tree, don't add anything
        if (n.getBuilding().equals(building)) {
            return n;
        }

        if (leftRight) { // compare x coords
            if (building.getX() < n.getX()) {
                n.leftDown = buildBuildingTree(n.leftDown, false, building);
            } else {
                n.rightUp = buildBuildingTree(n.rightUp, false, building);
            }
        } else { // compare y coords
            if (building.getY() < n.getY()) {
                n.leftDown = buildBuildingTree(n.leftDown, true, building);
            } else {
                n.rightUp = buildBuildingTree(n.rightUp, true, building);
            }
        }

        return n;
    }

    /**
     * Recursively builds the Place K-D Tree
     * @param n current node we are examining
     * @param leftRight true if we are examining x coordinates, false otherwise
     * @param place Place we are attempting to add to the tree
     * @return our current node after adding the given place
     */
    private PlaceTreeNode buildPlaceTree(PlaceTreeNode n, boolean leftRight, Place place) {
        if (n == null) {
            return new PlaceTreeNode(place);
        }

        // If the building already exists in the tree, don't add anything
        if (n.getPlace().equals(place)) {
            return n;
        }

        if (leftRight) { // compare x coords
            if (place.getX() < n.getX()) {
                n.leftDown = buildPlaceTree(n.leftDown, false, place);
            } else {
                n.rightUp = buildPlaceTree(n.rightUp, false, place);
            }
        } else { // compare y coords
            if (place.getY() < n.getY()) {
                n.leftDown = buildPlaceTree(n.leftDown, true, place);
            } else {
                n.rightUp = buildPlaceTree(n.rightUp, true, place);
            }
        }

        return n;
    }

    /**
     * Finds the building that is closest to the given x, y
     * @param x x value of the point we want the closest building to
     * @param y y value of the point we want the closest building to
     * @param genderNeutralRestroom True if the building must have a genderNeutralRestroom
     * @param elevator True if the building must have an elevator
     * @return The short name of the building closest to the given x, y
     * @throws IllegalStateException if the tree is empty
     */
    public String findClosestBuilding(float x, float y, boolean genderNeutralRestroom,
                                      boolean elevator) {
        if (buildingRoot == null) {
            throw new IllegalStateException("findClosestBuilding -- building tree is empty");
        }
        BuildingTreeNode closestNode = (BuildingTreeNode) getClosestNode(x, y, buildingRoot, genderNeutralRestroom, null, true);
        Building closest = closestNode.getBuilding();
        return closest.getShortName();
    }

    /**
     * Finds the place that is closest to the given x, y
     * @param x x value of the point we want the closest place to
     * @param y y value of the point we want the closest place to
     * @return the Place closest to the given x, y or
     * @throws IllegalStateException if the tree is empty
     */
    public Place findClosestPlace(float x, float y) {
        if (placeRoot == null) {
            throw new IllegalStateException("findClosestPlace -- place tree is empty");
        }
        PlaceTreeNode closestNode = (PlaceTreeNode) getClosestNode(x, y, placeRoot, false, null, true);
        return closestNode.getPlace();
    }

    /**
     * Finds the TreeNode closest to the given x and y starting from the given root
     * @param x x position we want to find closest to
     * @param y y position we want to find closest to
     * @param curr current node we are examining
     * @param genderNeutralRestroom whether we are looking for a building with a genderNeutralRestroom or not
     * @param best current best node we have encountered
     * @return TreeNode closest to the given x, y coordinates
     */
    private TreeNode getClosestNode(float x, float y, TreeNode curr, boolean genderNeutralRestroom, TreeNode best, boolean searchX) {
        if (curr == null) {
            return best;
        }

        if (best == null) {
            if (genderNeutralRestroom) { // Only update best if curr has genderNeutralRestroom
                if (((BuildingTreeNode) curr).getBuilding().hasGenderNeutralRestroom()) {
                    best = curr;
                }
            } else { // If not looking for genderNeutralRestroom, just update best with curr
                best = curr;
            }
        }

        // if current node is better than out current best, update it
        if (distanceTo(curr, x, y) < distanceTo(best, x, y)) {
            if (genderNeutralRestroom) { // Only update best if curr has genderNeutralRestroom
                if (((BuildingTreeNode) curr).getBuilding().hasGenderNeutralRestroom()) {
                    best = curr;
                }
            } else { // If not looking for genderNeutralRestroom, just update best with curr
                best = curr;
            }
        }
        // good side is defined as the side we would place (x, y) if adding it to the tree
        TreeNode goodSide;
        TreeNode badSide;

        if (searchX && x < curr.getX()) {
            goodSide = curr.getLeftDown();
            badSide = curr.getRightUp();
        } else if (searchX) {
            goodSide = curr.getRightUp();
            badSide = curr.getLeftDown();
        } else if (y < curr.getY()) {
            goodSide = curr.getLeftDown();
            badSide = curr.getRightUp();
        } else {
            goodSide = curr.getRightUp();
            badSide = curr.getLeftDown();
        }

        // Update our best by searching through the good side of the tree
        best = getClosestNode(x, y, goodSide, genderNeutralRestroom, best, !searchX);

        // Check if the bad side has a potentially closer match
        if (searchX && distanceTo(best, x, y) > distanceToCoordinates(x, y, curr.getX(), y)) {
            best = getClosestNode(x, y, badSide, genderNeutralRestroom, best, false);
        } else if (!searchX && distanceTo(best, x, y) > distanceToCoordinates(x, y, x, curr.getY())) {
            best = getClosestNode(x, y, badSide, genderNeutralRestroom, best, true);
        }

        return best;
    }

    /**
     * Returns the squared euclidean distance from a given node to a given x, y coordinate
     * @param t1 Node we want to find distance between
     * @param x x coordinate we want to find distance to
     * @param y y coordinate we want to find distance to
     * @return squared euclidean distance from the node to the coordinates x,y
     */
    private float distanceTo(TreeNode t1, float x, float y) {
        return distanceToCoordinates(x, y, t1.getX(), t1.getY());
    }

    /**
     * Returns the squared euclidean distance between two coordinates
     * @param x1 first x coordinate
     * @param y1 first y coordinate
     * @param x2 second x coordinate
     * @param y2 second y coordinate
     * @return squared euclidean distance between the two coordinates
     */
    private float distanceToCoordinates(float x1, float y1, float x2, float y2) {
        return (float)(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
