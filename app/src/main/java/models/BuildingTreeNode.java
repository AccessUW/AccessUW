package models;

public class BuildingTreeNode implements TreeNode {
    private Building building;
    private BuildingTreeNode leftUp;
    private BuildingTreeNode downRight;

    public BuildingTreeNode(Building building) {
        this.building = building;
    }

    @Override
    public float getX() {
        return building.getX();
    }

    @Override
    public float getY() {
        return building.getY();
    }

    @Override
    public TreeNode getLeftUp() {
        return leftUp;
    }

    @Override
    public TreeNode getRightDown() {
        return downRight;
    }


}
