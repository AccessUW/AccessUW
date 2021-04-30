package models;

import java.util.List;
import java.util.Set;

public class RouteFinderModel {
    private Set<RouteEdge> edges;

    public RouteFinderModel(Set<RouteEdge> edges) {
        this.edges = edges;
    }

    public List<Place> shortestPath(Place start, Set<Place> ends, boolean wheelchair,
                                    boolean stairs) {
        return null;
    }
}
