import org.junit.Test;
import static org.junit.Assert.*;

import models.Building;

public class BuildingTest {
    @Test
    public void testConstructor() {
        String gn = "B 1 2 3";

        String acc = "1 2";

        String gnEmpty = "None";
        String accEmpty = "None";

        Building b = new Building("A", 1, 1, gn, acc, true);

        assertEquals("A", b.getShortName());
        assertEquals(1, b.getX(), 0.01);
        assertEquals(1, b.getY(), 0.01);
        assertTrue(b.hasGenderNeutralRestroom());
        assertTrue(b.hasAccessibleRestroom());
        assertTrue(b.hasElevator());

        Building b1 = new Building("B", 2, 2, gn, accEmpty, false);

        assertEquals("B", b1.getShortName());
        assertEquals(2, b1.getX(), 0.01);
        assertEquals(2, b1.getY(), 0.01);
        assertTrue(b1.hasGenderNeutralRestroom());
        assertFalse(b1.hasAccessibleRestroom());
        assertFalse(b1.hasElevator());

        Building b2 = new Building("C", 3, 3, gnEmpty, acc, true);

        assertEquals("C", b2.getShortName());
        assertEquals(3, b2.getX(), 0.01);
        assertEquals(3, b2.getY(), 0.01);
        assertFalse(b2.hasGenderNeutralRestroom());
        assertTrue(b2.hasAccessibleRestroom());
        assertTrue(b2.hasElevator());

        Building b3 = new Building("D", 4, 4, gnEmpty, accEmpty, false);

        assertEquals("D", b3.getShortName());
        assertEquals(4, b3.getX(), 0.01);
        assertEquals(4, b3.getY(), 0.01);
        assertFalse(b3.hasGenderNeutralRestroom());
        assertFalse(b3.hasAccessibleRestroom());
        assertFalse(b3.hasElevator());
    }

    @Test
    public void testEquals() {
        String gn = "B 1 2 3";

        String acc = "1 2";

        String accEmpty = "None";


        Building a = new Building("A", 1, 1, gn, acc, true);
        Building a2 = new Building("A", 1, 1, gn, acc, true);

        assertEquals(a, a2);

        Building b = new Building("B", 1, 2, gn, acc, true);

        assertNotEquals(a, b);
        assertNotEquals(a2, b);

        Building c = new Building("C", 1, 1, gn, accEmpty, true);

        assertNotEquals(a, c);
        assertNotEquals(a2, c);
        assertNotEquals(b, c);

        Building d = new Building("D", 1, 1, gn, acc, false);

        assertNotEquals(a, d);
        assertNotEquals(c, d);
    }
}
