package models;

import java.util.Set;

/**
 * This class defines an undirected route edge, connecting two places with a certain distance
 * between them.
 */
public class RouteEdge {
    /**
     * Places that this RouteEdge connects
     */
    public Set<Place> ends;
    /**
     * Distance between the two ends
     */
    public float distance;
    /**
     * True if the RouteEdge represents a path that is wheelchair accessible
     */
    public boolean wheelChairAccess;
    /**
     * True if the RouteEdge represents a path with stairs
     */
    public boolean hasStairs;

    /**
     * Creates a new RouteEdge object
     * @param u one end of this route
     * @param v the other end of this route
     * @param distance the distance between the two ends
     * @param wheelChair true if the path is wheelchair accessible
     * @param stairs true if the path has stairs
     */
    public RouteEdge(Place u, Place v, float distance, boolean wheelChair, boolean stairs) {

    }
}
