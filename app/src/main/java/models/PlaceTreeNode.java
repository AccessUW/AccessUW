package models;

public class PlaceTreeNode implements TreeNode {
    private Place location;

    public PlaceTreeNode(Place location) {
        this.location = location;
    }

    @Override
    public float getX() {
        return location.getX();
    }

    @Override
    public float getY() {
        return location.getY();
    }

    @Override
    public TreeNode getLeftUp() {
        return null;
    }

    @Override
    public TreeNode getRightDown() {
        return null;
    }

    public Place getPlace() {
        return location;
    }
}
