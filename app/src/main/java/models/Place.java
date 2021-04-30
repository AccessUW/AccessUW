package models;

import java.util.Set;

/**
 * This class defines a Place, which is a vertex in the graph representation of the UW Campus.
 *
 * @author Jack Skinner
 * @version 1.0
 * @since 04-29-2021
 */
public class Place {
    private float xPos;
    private float yPos;
    private boolean entrance;
    private Set<RouteEdge> neighbors;

    public Place(float x, float y, boolean entrance) {

    }

    public Place(float x, float y) {
        this(x, y, false);
    }

    public float getX() {
        return xPos;
    }

    public float getY() {
        return yPos;
    }

    public Set<RouteEdge> getNeighbors() {
        return null;
    }

    public void addNeighbor(Place desitination, float distance, boolean wheelChairAccess,
                            boolean hasStairs) {

    }
}
