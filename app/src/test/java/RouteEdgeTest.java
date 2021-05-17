import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import models.Place;
import models.RouteEdge;

public class RouteEdgeTest {
    @Test
    public void testConstructor() {
        Place p1 = new Place(1,1);
        Place p2 = new Place(2,2);

        RouteEdge re = new RouteEdge(p1, p2, 10, true, false);
        Set<Place> realEnds = new HashSet<>();
        realEnds.add(p1);
        realEnds.add(p2);

        assertEquals(10, re.distance, 0.01);
        assertEquals(realEnds, re.ends);
        assertTrue(re.wheelChairAccess);
        assertFalse(re.hasStairs);
    }

    @Test
    public void testEquals() {
        Place p1 = new Place(1,1);
        Place p2 = new Place(2, 2);
        Place p3 = new Place(3,3);

        RouteEdge re1 = new RouteEdge(p1, p2, 10, true, false);
        RouteEdge re2 = new RouteEdge(p1, p2, 200, false, true);

        // should be equals since only one route may exist between two places
        assertEquals(re1, re2);

        RouteEdge re3 = new RouteEdge(p1, p3, 10, false, true);
        assertNotEquals(re1, re3);
        assertNotEquals(re2, re3);

        assertEquals(re1.hashCode(), re2.hashCode());
        assertNotEquals(re1.hashCode(), re3.hashCode());
        assertNotEquals(re2.hashCode(), re3.hashCode());
    }
}
