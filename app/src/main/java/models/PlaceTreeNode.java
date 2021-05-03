package models;

/**
 * This class defines a PlaceTreeNode, which represents a Place within a KD-Tree
 */
public class PlaceTreeNode implements TreeNode {
    private Place location;

    /**
     * Creates a new PlaceTreeNode object
     * @param location the Place that this node represents
     */
    public PlaceTreeNode(Place location) {
        this.location = location;
    }

    /**
     * Get the x value of this TreeNode
     * @return x value of this TreeNode
     */
    @Override
    public float getX() {
        return location.getX();
    }

    /**
     * Get the y value of this TreeNode
     * @return y value of this TreeNode
     */
    @Override
    public float getY() {
        return location.getY();
    }

    /**
     * Get the node that results from traversing left, or up the KD Tree
     * @return node that is left or up the KD Tree
     */
    @Override
    public TreeNode getLeftUp() {
        return null;
    }

    /**
     * Get the node that results from traversing right, or down the KD Tree
     * @return node that is right or down the KD Tree
     */
    @Override
    public TreeNode getRightDown() {
        return null;
    }

    /**
     * Get the place that this TreeNode represents
     * @return Place that this TreeNode represents
     */
    public Place getPlace() {
        return location;
    }
}
