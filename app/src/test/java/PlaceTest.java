import org.junit.Test;
import models.Place;

public class PlaceTest {
    @Test
    public void testAddNeighbor() {
        Place p1 = new Place(1, 1, true);
        Place p2 = new Place(2, 2, true);
        Place p3 = new Place(3, 3, false);
        assert p1.getNeighbors().isEmpty();
        assert p2.getNeighbors().isEmpty();
        assert p3.getNeighbors().isEmpty();

        p1.addNeighbor(p2, 4, true, true);
        assert p1.getNeighbors().size() == 1 && p2.getNeighbors().size() == 1;
        p2.addNeighbor(p1, 4, true, false);
        assert p1.getNeighbors().size() == 1 && p2.getNeighbors().size() == 1;
        p3.addNeighbor(p1, 5, true, true);
        assert p3.getNeighbors().size() == 1 && p1.getNeighbors().size() == 2;
    }
}
