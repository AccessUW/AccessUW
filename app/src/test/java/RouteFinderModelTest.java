import org.junit.Test;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import models.Building;
import models.RouteFinderModel;
import models.Place;

public class RouteFinderModelTest {
    @Test
    public void testSimple() {
        //
        //  (1) -- 1 -- (4)
        //   I           I
        //   2           1
        //   I           I
        //  (2) -- 1 -- (3)
        //
        // (1) to (2) is wheelchair inaccessible
        Place p1 = new Place(0, 0);
        Place p2 = new Place(1, 1);
        Place p3 = new Place(2, 2);
        Place p4 = new Place(3, 3);

        p1.addNeighbor(p4, 1, true, false);
        p1.addNeighbor(p2, 2, false, true);
        p4.addNeighbor(p3, 1, true, false);
        p3.addNeighbor(p2, 1, true, false);

        Building start = new Building("A", 10, 10, "", "", true);
        Building end = new Building("B", 20, 20, "", "", true);

        start.addEntrance(p1, true);
        end.addEntrance(p2, true);

        RouteFinderModel rfm = new RouteFinderModel();

        List<Place> path = rfm.shortestPathBetweenBuildings(start, end, true,false);
        List<Place> expectedAccessible = new LinkedList<>();
        expectedAccessible.add(p1);
        expectedAccessible.add(p4);
        expectedAccessible.add(p3);
        expectedAccessible.add(p2);

        assertEquals(expectedAccessible, path);

        // test second method
        path = rfm.shortestPath(p1, end, true, false);
        assertEquals(expectedAccessible, path);

        path = rfm.shortestPathBetweenBuildings(start, end, false, true);
        List<Place> expectedAllPaths = new LinkedList<>();
        expectedAllPaths.add(p1);
        expectedAllPaths.add(p2);

        assertEquals(expectedAllPaths, path);

        // test second method
        path = rfm.shortestPath(p1, end, false, true);
        assertEquals(expectedAllPaths, path);
    }

    @Test
    public void testNoSolution() {
        Place p1 = new Place(0, 0);
        Place p2 = new Place(1, 1);
        Place p3 = new Place(2, 2);
        Place p4 = new Place(3, 3);

        p1.addNeighbor(p2, 1, true, false);
        p2.addNeighbor(p3, 2, true, false);
        p3.addNeighbor(p4, 3, false, true);

        Building start = new Building("A", 10, 10, "", "", true);
        Building end1 = new Building("B", 20, 20, "", "", true);

        start.addEntrance(p1, true);
        end1.addEntrance(p4, true);

        List<Place> expected = new LinkedList<>();

        RouteFinderModel rfm = new RouteFinderModel();
        List<Place> path = rfm.shortestPathBetweenBuildings(start, end1, true, false);

        assertEquals(expected, path);

        Building end2 = new Building("C", 20, 20, "", "", true);
        end2.addEntrance(p3, false);

        path = rfm.shortestPathBetweenBuildings(start, end2, true, false);

        assertEquals(expected, path);
    }

    @Test
    public void testMedium() {
        //
        // (1)            (5)
        //  \1           4/
        //   (3) - 3 - (4)      (7)
        //  /4           4\   /4
        // (2)            (6)
        //
        //
        Place p1 = new Place(0,6);
        Place p2 = new Place(0,0);
        Place p3 = new Place(3,5);
        Place p4 = new Place(6,5);
        Place p5 = new Place(9,10);
        Place p6 = new Place(9,0);
        Place p7 = new Place(12,5);

        p1.addNeighbor(p3, 1, true, false);
        p2.addNeighbor(p3, 4, true, false);
        p3.addNeighbor(p4, 3, true, false);
        p4.addNeighbor(p5, 4, false, true);
        p4.addNeighbor(p6, 4, true, false);
        p6.addNeighbor(p7, 4, true, false);

        Building start = new Building("A", 10, 10, "", "", true);
        Building end = new Building("B", 20, 20, "", "", true);

        start.addEntrance(p1, false);
        start.addEntrance(p2, true);
        end.addEntrance(p7, true);
        end.addEntrance(p5, true);

        List<Place> expectedAccessible = new LinkedList<>();
        expectedAccessible.add(p2);
        expectedAccessible.add(p3);
        expectedAccessible.add(p4);
        expectedAccessible.add(p6);
        expectedAccessible.add(p7);

        RouteFinderModel rfm = new RouteFinderModel();

        assertEquals(expectedAccessible, rfm.shortestPathBetweenBuildings(start, end, true, false));

        List<Place> expectedAll = new LinkedList<>();
        expectedAll.add(p1);
        expectedAll.add(p3);
        expectedAll.add(p4);
        expectedAll.add(p5);

        assertEquals(expectedAll, rfm.shortestPathBetweenBuildings(start, end, false, true));
    }
}
