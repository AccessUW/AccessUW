import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import models.Place;
import models.PlacePriorityQueue;

import static org.junit.Assert.*;

public class PlacePriorityQueueTest {
    @Test
    public void testSimple() {
        PlacePriorityQueue ppq = new PlacePriorityQueue();

        assertTrue(ppq.isEmpty());
        assertEquals(0, ppq.getSize());

        Place p = new Place(1, 1);
        Place q = new Place(2,2);

        ppq.add(p, 4);

        assertEquals(p, ppq.getMin());
        assertFalse(ppq.isEmpty());
        assertEquals(1, ppq.getSize());
        assertTrue(ppq.contains(p));
        assertFalse(ppq.contains(q));

        ppq.add(q, 1);

        assertEquals(q, ppq.getMin());
        assertFalse(ppq.isEmpty());
        assertEquals(2, ppq.getSize());
        assertTrue(ppq.contains(p));
        assertTrue(ppq.contains(q));

        Place q1 = ppq.popMin();

        assertEquals(q, q1);
        assertFalse(ppq.isEmpty());
        assertEquals(1, ppq.getSize());
        assertEquals(p, ppq.getMin());
        assertTrue(ppq.contains(p));
        assertFalse(ppq.contains(q));

        Place p1 = ppq.popMin();

        assertEquals(p, p1);
        assertTrue(ppq.isEmpty());
        assertEquals(0, ppq.getSize());
        assertFalse(ppq.contains(p));
        assertFalse(ppq.contains(q));
    }

    @Test
    public void testMedium() {
        PlacePriorityQueue ppq = new PlacePriorityQueue();

        for (int i = 1; i < 10; i++) {
            ppq.add(new Place(i, i), i);
        }

        for (int i = 1; i < 10; i++) {
            Place p = new Place(i, i);
            assertEquals(p, ppq.popMin());
        }

        assertTrue(ppq.isEmpty());
    }

    @Test
    public void testUpdate() {
        PlacePriorityQueue ppq = new PlacePriorityQueue();
        Place p = new Place(1, 1);
        Place q = new Place(2,2);

        ppq.add(p, 4);

        assertEquals(p, ppq.getMin());

        ppq.add(q, 3);

        assertEquals(q, ppq.getMin());

        ppq.updatePriority(q, 5);

        // should only be updated when new priority is lower
        assertEquals(q, ppq.getMin());

        ppq.updatePriority(p, 2);

        assertEquals(p, ppq.getMin());
    }

    @Test
    public void testAddRemove() {
        List<Place> expected = new LinkedList<>();
        Place p1 = new Place(1,1);
        Place p2 = new Place(2,2);
        Place p3 = new Place(3,3);
        Place p4 = new Place(4,4);
        Place p5 = new Place(5,5);
        Place p6 = new Place(6,6);
        Place p7 = new Place(7,7);

        PlacePriorityQueue ppq = new PlacePriorityQueue();
        ppq.add(p5, 5);
        assertEquals(p5, ppq.getMin());

        ppq.add(p4, 4);
        //remove
        assertEquals(p4, ppq.popMin());

        ppq.add(p2, 2);
        assertEquals(p2, ppq.getMin());

        ppq.add(p3, 3);
        // remove
        assertEquals(p2, ppq.popMin());

        ppq.add(p7, 7);
        assertEquals(p3, ppq.getMin());

        ppq.add(p6, 6);
        assertEquals(p3, ppq.getMin());

        ppq.add(p1, 1);
        //remove
        assertEquals(p1, ppq.popMin());

        //remove
        assertEquals(p3, ppq.popMin());

        // remove remaining elements
        assertEquals(p5, ppq.popMin());
        assertEquals(p6, ppq.popMin());
        assertEquals(p7, ppq.popMin());
        assertEquals(0, ppq.getSize());

    }
}
