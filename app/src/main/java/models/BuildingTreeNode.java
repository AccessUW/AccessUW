package models;

/**
 * This class defines a BuildingTreeNode, which represents a building within a K-D Tree
 */
public class BuildingTreeNode implements TreeNode {
    private Building building;
    public BuildingTreeNode leftDown;
    public BuildingTreeNode rightUp;

    /**
     * Creates a new BuildingTreeNode object
     * @param building building that this TreeNode represents
     */
    public BuildingTreeNode(Building building) {
        this.building = building;
    }

    /**
     * Get the x value of this TreeNode
     * @return x value of this TreeNode
     */
    @Override
    public float getX() {
        return building.getX();
    }

    /**
     * Get the y value of this TreeNode
     * @return y value of this TreeNode
     */
    @Override
    public float getY() {
        return building.getY();
    }

    /**
     * Get the node that results from traversing left, or up the KD Tree
     * @return node that is left or up the KD Tree
     */
    @Override
    public TreeNode getLeftDown() {
        return leftDown;
    }

    /**
     * Get the node that results from traversing right, or down the KD Tree
     * @return node that is right or down the KD Tree
     */
    @Override
    public TreeNode getRightUp() {
        return rightUp;
    }

    /**
     * Get the building that this TreeNode represents
     * @return Building this TreeNode is representing
     */
    public Building getBuilding() {
        return building;
    }
}
