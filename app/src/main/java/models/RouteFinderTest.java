package models;

import org.junit.Test;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class RouteFinderTest {

    @Test
    public void testSimple() {
        // Create some dummy vertices and edges
        Place a = new Place(1, 1, true);
        Place b = new Place(2, 3, false);
        Place c = new Place(4, 1, false);
        a.addNeighbor(b, 4, true, false);
        System.out.println(a.getNeighbors().size() == 1);
        System.out.println(b.getNeighbors().size() == 1);
        b.addNeighbor(c, 4, true,  false);
        System.out.println(b.getNeighbors().size() == 2);
        System.out.println(c.getNeighbors().size() == 1);
        c.addNeighbor(a, 3, false, true);

        Set<Place> b1Entrances = new HashSet<>();
        b1Entrances.add(a);
        Set<Place> b2Etrances = new HashSet<>();
        b2Etrances.add(c);

        Building b1 = new Building(0, 0, "", true, true,
                b1Entrances, b2Etrances, "");
        Building b2 = new Building(0, 0, "", true, true,
                b2Etrances, b2Etrances, "");

        Set<Place> vertices = new HashSet<>();
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);

        RouteFinderModel rfm = new RouteFinderModel(vertices);

        List<Place> expected1 = new LinkedList<>();
        expected1.add(a);
        expected1.add(b);
        expected1.add(c);

        List<Place> expected2 = new LinkedList<>();
        expected2.add(a);
        expected2.add(c);

        System.out.println(rfm.shortestPathBetweenBuildings(b1, b2, true, false).equals(expected1));
        System.out.println(rfm.shortestPathBetweenBuildings(b1, b2, false, true).equals(expected2));
    }
}
