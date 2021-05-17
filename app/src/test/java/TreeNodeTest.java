import org.junit.Test;

import models.Building;
import models.BuildingTreeNode;
import models.Place;
import models.PlaceTreeNode;

import static org.junit.Assert.*;


public class TreeNodeTest {
    @Test
    public void testPlaceNode() {
        Place p = new Place(1,1);
        PlaceTreeNode ptn = new PlaceTreeNode(p);

        assertNull(ptn.getLeftDown());
        assertNull(ptn.getRightUp());
        assertEquals(p, ptn.getPlace());
        assertEquals(p.getX(), ptn.getX(), 0.001);
        assertEquals(p.getY(), ptn.getY(), 0.001);

        Place p2 = new Place(2,2);
        PlaceTreeNode ptn2 = new PlaceTreeNode(p2);
        Place p3 = new Place(3,3);
        PlaceTreeNode ptn3 = new PlaceTreeNode(p3);

        ptn.leftDown = ptn2;
        ptn.rightUp = ptn3;

        assertEquals(ptn2, ptn.getLeftDown());
        assertEquals(ptn3, ptn.getRightUp());
    }

    @Test
    public void testBuildingNode() {
        Building b = new Building("A", 1, 1, "", "", true);
        BuildingTreeNode btn = new BuildingTreeNode(b);

        assertNull(btn.getLeftDown());
        assertNull(btn.getRightUp());
        assertEquals(b, btn.getBuilding());
        assertEquals(b.getX(), btn.getX(), 0.001);
        assertEquals(b.getY(), btn.getY(), 0.001);

        Building b2 = new Building("B", 2, 2, "", "", true);
        BuildingTreeNode btn2 = new BuildingTreeNode(b2);
        Building b3 = new Building("C", 3, 3, "", "", true);
        BuildingTreeNode btn3 = new BuildingTreeNode(b3);

        btn.leftDown = btn2;
        btn.rightUp = btn3;

        assertEquals(btn2, btn.getLeftDown());
        assertEquals(btn3, btn.getRightUp());
    }
}
