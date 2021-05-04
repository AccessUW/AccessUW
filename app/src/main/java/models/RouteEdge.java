package models;

import java.util.HashSet;
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
        ends = new HashSet<>();
        ends.add(u);
        ends.add(v);
        this.distance = distance;
        this.wheelChairAccess = wheelChair;
        this.hasStairs = stairs;
    }

    /**
     * Checks the equality of this RouteEdge with another object
     * @param o object you want to check the equality of
     * @return true if this object is equal to o, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        // Can only equal if other is also a RouteEdge
        if (!(o instanceof RouteEdge)) {
            return false;
        }

        // Typecast so we can compare
        RouteEdge re = (RouteEdge) o;

        return re.ends.equals(this.ends);
    }

    /**
     * Get the hash code of this RouteEdge
     * @return hashcode of this RouteEdge
     */
    @Override
    public int hashCode() {
        int code = 0;
        for (Place end : this.ends) {
            code += end.hashCode();
        }
        return (int) (this.distance + code);
    }
}
