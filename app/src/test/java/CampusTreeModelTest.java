import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;
import models.Building;
import models.Place;
import models.CampusTreeModel;

public class CampusTreeModelTest {
    @Test
    public void testSimple() {
        Building b1 = new Building("A", 1,1,"","", true);
        Building b2 = new Building("B", 5,5,"","", true);
        Building b3 = new Building("C", 10,10,"","", true);

        Place p1 = new Place(1, 1);
        Place p2 = new Place(5, 5);
        Place p3 = new Place(10, 10);

        Set<Building> buildingSet = new HashSet<>();
        buildingSet.add(b1);
        buildingSet.add(b2);
        buildingSet.add(b3);

        Set<Place> placeSet = new HashSet<>();
        placeSet.add(p1);
        placeSet.add(p2);
        placeSet.add(p3);

        CampusTreeModel ctm = new CampusTreeModel(buildingSet, placeSet);

        assertEquals("A", ctm.findClosestBuilding(2, 3, true, true));
        assertEquals("B", ctm.findClosestBuilding(7, 7, true, false));
        assertEquals("C", ctm.findClosestBuilding(20, 5, true, true));

        assertEquals(p1, ctm.findClosestPlace(3, 3));
        assertEquals(p2, ctm.findClosestPlace(6, 7));
        assertEquals(p3, ctm.findClosestPlace(9, 12));
    }

    @Test
    public void testLarge() {
        Set<Building> buildingSet = new HashSet<>();
        Set<Place> placeSet = new HashSet<>();
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            float randX = r.nextFloat();
            float randY = r.nextFloat();
            placeSet.add(new Place(randX, randY));
            buildingSet.add(new Building("" + i, randX, randY, "", "", true));
        }

        CampusTreeModel ctm = new CampusTreeModel(buildingSet, placeSet);

        for (int i = 0; i < 100; i++) {
            float randX = r.nextFloat();
            float randY = r.nextFloat();

            String closestBuilding = "";
            float bestDist = Float.MAX_VALUE;
            for (Building b : buildingSet) {
                float dist = (float)(Math.pow(b.getX() - randX, 2) + Math.pow(b.getY() - randY, 2));
                if (dist < bestDist) {
                    bestDist = dist;
                    closestBuilding = b.getShortName();
                }
            }

            assertEquals(closestBuilding, ctm.findClosestBuilding(randX, randY, true, true));

            bestDist = Float.MAX_VALUE;
            Place closestPlace = null;
            for (Place p : placeSet) {
                float dist = (float)(Math.pow(p.getX() - randX, 2) + Math.pow(p.getY() - randY, 2));
                if (dist < bestDist) {
                    bestDist = dist;
                    closestPlace = p;
                }
            }

            assertEquals(closestPlace, ctm.findClosestPlace(randX, randY));
        }
    }
}
