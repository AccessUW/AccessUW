package models;

import java.util.Set;

public class RouteEdge {
    public Set<Place> ends;
    public float distance;
    public boolean wheelChairAccess;
    public boolean hasStairs;

    public RouteEdge(Place u, Place v, float distance, boolean wheelChair, boolean stairs) {

    }
}
