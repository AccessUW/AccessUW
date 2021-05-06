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
     * @return The short name of the building closest to the given x, y
     */
    public String findClosestBuilding(float x, float y, boolean genderNeutralRestroom,
                                      boolean elevator) {
        BuildingTreeNode closestNode = (BuildingTreeNode) getClosestNode(x, y, buildingRoot, null, true);
        Building closest = closestNode.getBuilding();
        return closest.getShortName();
    }

    /**
     * Finds the place that is closest to the given x, y
     * @param x x value of the point we want the closest place to
     * @param y y value of the point we want the closest place to
     * @return the Place closest to the given x, y
     */
    public Place findClosestPlace(float x, float y) {
        PlaceTreeNode closestNode = (PlaceTreeNode) getClosestNode(x, y, placeRoot, null, true);
        return closestNode.getPlace();
    }

    /**
     * Finds the TreeNode closest to the given x and y starting from the given root
     * @param x x position we want to find closest to
     * @param y y position we want to find closest to
     * @param curr current node we are examining
     * @param best current best node we have encountered
     * @return TreeNode closest to the given x, y coordinates
     */
    private TreeNode getClosestNode(float x, float y, TreeNode curr, TreeNode best, boolean searchX) {
        if (curr == null) {
            return best;
        }

        if (best == null) {
            best = curr;
        }

        // if current node is better than out current best, update it
        if (distanceTo(curr, x, y) < distanceTo(best, x, y)) {
            best = curr;
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
        best = getClosestNode(x, y, goodSide, best, !searchX);

        // Check if the bad side has a potentially closer match
        if (searchX && distanceTo(best, x, y) > distanceToCoordinates(x, y, curr.getX(), y)) {
            best = getClosestNode(x, y, badSide, best, false);
        } else if (!searchX && distanceTo(best, x, y) > distanceToCoordinates(x, y, x, curr.getY())) {
            best = getClosestNode(x, y, badSide, best, true);
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
