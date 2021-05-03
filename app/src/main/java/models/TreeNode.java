package models;

/**
 * This interface defines a TreeNode used in a KD-Tree
 */
public interface TreeNode {
    /**
     * Get the node that results from traversing left, or up the KD Tree
     * @return node that is left or up the KD Tree
     */
    TreeNode getLeftUp();
    /**
     * Get the node that results from traversing right, or down the KD Tree
     * @return node that is right or down the KD Tree
     */
    TreeNode getRightDown();

    /**
     * Get the x value of this TreeNode
     * @return x value of this TreeNode
     */
    float getX();

    /**
     * Get the y value of this TreeNode
     * @return y value of this TreeNode
     */
    float getY();
}
