import org.junit.Test;


import models.Building;

public class BuildingTest {
    @Test
    public void testConstructor() {
        String gn = "B 1 2 3";

        String acc = "1 2";

        String gnEmpty = "None";
        String accEmpty = "None";

        Building b = new Building("A", 1, 1, gn, acc, true);

        assert b.getShortName().equals("A");
        assert b.getX() == 1 && b.getY() == 1;
        assert b.hasGenderNeutralRestroom();
        assert b.hasAccessibleRestroom();
        assert b.hasElevator();

        Building b1 = new Building("B", 2, 2, gn, accEmpty, false);

        assert b1.getShortName().equals("B");
        assert b1.getX() == 2 && b1.getY() == 2;
        assert b1.hasGenderNeutralRestroom();
        assert !b1.hasAccessibleRestroom();
        assert !b1.hasElevator();

        Building b2 = new Building("C", 3, 3, gnEmpty, acc, true);

        assert b2.getShortName().equals("C");
        assert b2.getX() == 3 && b2.getY() == 3;
        assert !b2.hasGenderNeutralRestroom();
        assert b2.hasAccessibleRestroom();
        assert b2.hasElevator();

        Building b3 = new Building("D", 4, 4, gnEmpty, accEmpty, false);

        assert b3.getShortName().equals("D");
        assert b3.getX() == 4 && b3.getY() == 4;
        assert !b3.hasGenderNeutralRestroom();
        assert !b3.hasAccessibleRestroom();
        assert !b3.hasElevator();
    }

    @Test
    public void testEquals() {
        String gn = "B 1 2 3";

        String acc = "1 2";

        String accEmpty = "None";


        Building a = new Building("A", 1, 1, gn, acc, true);
        Building a2 = new Building("A", 1, 1, gn, acc, true);

        assert a.equals(a2);

        Building b = new Building("B", 1, 2, gn, acc, true);

        assert !a.equals(b);
        assert !a2.equals(b);

        Building c = new Building("C", 1, 1, gn, accEmpty, true);

        assert !a.equals(c);
        assert !c.equals(a2);
        assert !b.equals(c);

        Building d = new Building("D", 1, 1, gn, acc, false);
        assert !a.equals(d);
        assert !c.equals(d);
    }
}
