package models;

import java.util.HashSet;
import java.util.Set;

/**
 * This class defines a Place, which is a vertex in the graph representation of the UW Campus.
 */
public class Place {
    private float xPos;
    private float yPos;
    private boolean entrance;
    private Set<RouteEdge> neighbors;

    /**
     * Creates a new Place object
     * @param x x position of the place
     * @param y y position of the place
     * @param entrance true if the place is an entrance to a building, false otherwise
     */
    public Place(float x, float y, boolean entrance) {
        this.xPos = x;
        this.yPos = y;
        this.entrance = entrance;
        this.neighbors = new HashSet<>();
    }

    /**
     * Creates a new non-entrance place object
     * @param x x position of the place
     * @param y y position of the place
     */
    public Place(float x, float y) {
        this(x, y, false);
    }

    /**
     * Get the x value of this place
     * @return x value of this place
     */
    public float getX() {
        return xPos;
    }

    /**
     * Get the y value of this place
     * @return y value of this place
     */
    public float getY() {
        return yPos;
    }

    /**
     * Get the neighboring places connected to this one by an edge
     * @return Set of edges that are connected to this place
     */
    public Set<RouteEdge> getNeighbors() {
        return neighbors;
    }

    /**
     * Adds a neighbor to this place, ignoring if it is already added as a neighbor
     * @param destination neighboring place we want to connect to
     * @param distance distance between this place and destination
     * @param wheelChairAccess true if the path between the two places is wheelchair accessible
     * @param hasStairs true if the path between the two places has stairs
     * @throws IllegalArgumentException if destination is the same as this Place or if the distance
     * isn't positive
     */
    public void addNeighbor(Place destination, float distance, boolean wheelChairAccess,
                            boolean hasStairs) throws IllegalArgumentException {
        if (this == destination) {
            throw new IllegalArgumentException("addNeighbor - cannot add self as a neighbor");
        } else if (distance < 0) {
            throw new IllegalArgumentException("addNeighbor - distance must be positive");
        }
        RouteEdge edge = new RouteEdge(this, destination, distance, wheelChairAccess, hasStairs);
        this.neighbors.add(edge);
    }

    // TODO: add equals method

    // TODO: add hash method
}
