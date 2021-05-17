import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;
import models.Place;
import models.RouteEdge;


public class PlaceTest {

    @Test
    public void testConstructor() {
        Place p = new Place(1,1,true);
        assertEquals(1, p.getX(), 0.01);
        assertEquals(1, p.getY(), 0.01);
        assertEquals(new HashSet<RouteEdge>(), p.getNeighbors());

        Place q = new Place(2,2);
        assertEquals(2, q.getX(), 0.01);
        assertEquals(2, q.getY(), 0.01);
        assertEquals(new HashSet<RouteEdge>(), q.getNeighbors());
    }

    @Test
    public void testAddNeighbor() {
        Place p1 = new Place(1, 1, true);
        Place p2 = new Place(2, 2, true);
        Place p3 = new Place(3, 3, false);
        assertTrue(p1.getNeighbors().isEmpty());
        assertTrue(p2.getNeighbors().isEmpty());
        assertTrue(p3.getNeighbors().isEmpty());

        p1.addNeighbor(p2, 4, true, true);
        assertEquals(1, p1.getNeighbors().size());
        assertEquals(1, p2.getNeighbors().size());

        p2.addNeighbor(p1, 4, true, false);
        assertEquals(1, p1.getNeighbors().size());
        assertEquals(1, p2.getNeighbors().size());

        p3.addNeighbor(p1, 5, true, true);
        assertEquals(2, p1.getNeighbors().size());
        assertEquals(1, p3.getNeighbors().size());
    }
}
