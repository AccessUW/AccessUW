package models;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * This class defines a model for finding a route between two Buildings, or from a Place to any
 * of a given Building's valid entrances.
 */
public class RouteFinderModel {

    /**
     * Creates a new RouteFinderModel object
     */
    public RouteFinderModel() {
    }

    /**
     * Returns the shortest path between start and any of the places in ends
     * @param start Place we start the path from
     * @param end Building we want to end the path at
     * @param wheelchair True if the path needs to be wheelchair accessible
     * @param stairs True if the path can have stairs
     * @return A list of places that we will visit on this path, or an empty list if a valid path
     * doesn't exist.
     */
    public List<Place> shortestPath(Place start, Building end, boolean wheelchair,
                                    boolean stairs) {
        Set<Place> starts = new HashSet<>();
        starts.add(start);

        Set<Place> ends;
        if (wheelchair) {
            ends = end.getAssistedEntrances();
        } else {
            ends = end.getEntrances();
        }

        return getShortestPath(starts, ends, end, wheelchair, stairs);
    }

    /**
     * Returns the shortest path between the start building and the end building
     * @param start building we start at
     * @param end building we end at
     * @param wheelchair true if the path needs to be wheelchair accessible
     * @param stairs true if the path can have stairs
     * @return A list of places that we will visit on this path, or an empty list if a valid path
     * doesn't exist.
     */
    public List<Place> shortestPathBetweenBuildings(Building start, Building end,
                                                    boolean wheelchair, boolean stairs) {
        Set<Place> starts;
        Set<Place> ends;

        // If the path requires wheelchair accessibility, only find accessible entrances to the
        // building
        if (wheelchair) {
            ends = end.getAssistedEntrances();
            starts = start.getAssistedEntrances();
        } else {
            ends = end.getEntrances();
            starts = start.getEntrances();
        }

        return getShortestPath(starts, ends, end, wheelchair, stairs);
    }

    /**
     * Internal helper function to find the shortest path from a set of start locations to a set of
     * end locations
     * @param starts locations we can start the path at
     * @param ends locations we can end the path at
     * @param end building we are ending the path at
     * @param wheelchair true if the path should be wheelchair accessible
     * @param stairs true if the path can have stairs
     * @return ordered list of the places we will visit on this path, or an empty path if no result
     * was found
     */
    private List<Place> getShortestPath(Set<Place> starts, Set<Place> ends, Building end,
                                        boolean wheelchair, boolean stairs) {

        List<Place> solution = new LinkedList<>();
        Set<Place> visited = new HashSet<>();
        Map<Place, Place> parents = new HashMap<>();
        Map<Place, Float> distTo = new HashMap<>();

        PlacePriorityQueue fringe = new PlacePriorityQueue();
        for (Place entrance : starts) {

            fringe.add(entrance, heuristic(entrance, ends));
            distTo.put(entrance, 0f);
        }

        boolean foundSolution = false;

        // A* Search Algorithm:
        while (!fringe.isEmpty()) {
            Place p = fringe.popMin();

            visited.add(p);

            // If we found a goal, stop searching
            if (isGoal(p, end, wheelchair)) {
                solution.add(p);
                foundSolution = true;
                break;
            }

            for (RouteEdge re : p.getNeighbors()) {
                if (wheelchair && !re.wheelChairAccess || !stairs && re.hasStairs) {
                    continue; // skip paths that go against our filters
                }

                // Get the neighboring Place
                Place q = null;
                for (Place place : re.ends) {
                    if (!p.equals(q)) {
                        q = place;
                    }
                }

                // Error -- edge doesn't have 2 ends
                if (q == null) {
                    continue; // skip over bad edges
                }

                if (!visited.contains(q)) {
                    if (!distTo.containsKey(q)) {
                        distTo.put(q, Float.MAX_VALUE);
                    }

                    Float currDistance = distTo.get(q);
                    Float thisDistance = distTo.get(p);
                    if (currDistance == null || thisDistance == null) {
                        throw new Error();
                    }
                    thisDistance +=  + re.distance;

                    // Update parent and distTo if the current edge give a shorter path to q
                    if (thisDistance < currDistance) {
                        parents.put(q, p);
                        distTo.put(q, thisDistance);

                        // Change or add q to the fringe
                        if (fringe.contains(q)) {
                            fringe.updatePriority(q, thisDistance + heuristic(q, ends));
                        } else {
                            fringe.add(q, thisDistance + heuristic(q, ends));
                        }
                    }
                }
            }
        }

        // If we found a solution return it, otherwise return an empty list
        if (foundSolution) {
            // follow parents from goal to one of the start states
            Place current = solution.get(0);
            while (distTo.get(current) != null && distTo.get(current) != 0f) {
                Place from = parents.get(current);
                solution.add(from);
                current = from;
                if (current == null) {
                    break;
                }
            }

            // Reverse the list to get the correct order of visits
            Collections.reverse(solution);
            return solution;
        } else {
            // Return empty list if no solution was found
            return new LinkedList<>();
        }

    }

    /**
     * Private helper to check if a place is an entrance at our destination
     * @param place place we want to check
     * @param b our goal building
     * @param wheelchair true if the goal should be an accessible entrance
     * @return true if the given place is a valid entrance of the given building, false otherwise
     */
    private boolean isGoal(Place place, Building b, boolean wheelchair) {
        if (wheelchair) {
            return b.getAssistedEntrances().contains(place);
        } else {
            return b.getEntrances().contains(place);
        }
    }

    /**
     * This private method defines the heuristic function for the A* search algorithm
     * @param currPlace the place we want to get the heuristic value of
     * @param ends entrances of the building we want to estimate the distance to
     * @return estimated distance between currPlace and the closest end
     */
    private float heuristic(Place currPlace, Set<Place> ends) {
        float minDist = Float.MAX_VALUE;
        for (Place end : ends) {
            minDist = Math.min(minDist, Math.abs(currPlace.getX() - end.getX()) +
                    Math.abs(currPlace.getY() - end.getY()));
        }
        return minDist;
    }
}
