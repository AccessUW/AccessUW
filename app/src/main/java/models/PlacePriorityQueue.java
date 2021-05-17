package models;

import android.renderscript.ScriptGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines a PriorityQueue designed for Place objects to be used, where priority can be
 * updated after creation.
 */
public class PlacePriorityQueue {
    // Internal heap
    private List<Place> items;
    // Priorities of each item in the queue
    private Map<Place, Float> priorities;
    // Indices of each item in the queue
    private Map<Place, Integer> indices;

    /**
     * Creates a new PlacePriorityQueue object
     */
    public PlacePriorityQueue() {
        items = new ArrayList<>();
        priorities = new HashMap<>();
        indices = new HashMap<>();
    }

    /**
     * Returns the Place with the lowest priority
     * @return Place with the lowest priority
     * @throws IllegalStateException if the priorityqueue is empty
     */
    public Place getMin() {
        return items.get(0);
    }

    /**
     * Removes and returns the Place with the lowest priority
     * @return Place with the lowest priority
     * @throws IllegalStateException if the priorityqueue is empty
     */
    public Place popMin() {
        swap(0, getSize() - 1);
        Place min = items.remove(getSize() - 1);
        indices.remove(min);
        priorities.remove(min);

        sink(0);

        return min;
    }

    /**
     * Adds the given place to the priority queue with the given priority
     * @param place place you want to add
     * @param priority priority of the place
     */
    public void add(Place place, float priority) {
        if (!items.contains(place)) {
            items.add(place);
            priorities.put(place, priority);
            int idx = items.size() - 1;
            indices.put(place, idx);
            swim(idx);
        }
    }

    /**
     * Updates the given place's priority, only if it is lower than the place's current priority. If
     * the given place isn't already in the queue, it is added.
     * @param place place whose priority we want to change
     * @param priority priority we want to set
     * @throws IllegalArgumentException if place is null
     */
    public void updatePriority(Place place, float priority) {
        if (place == null) {
            throw new IllegalArgumentException("updatePriority - given place is null");
        } else if (!items.contains(place)) {
            this.add(place, priority);
        }

        Float oldPriority = priorities.get(place);
        if (oldPriority != null && oldPriority < priority) {
            return;
        }

        priorities.put(place, priority);
        Integer idx = indices.get(place);

        // If priority was increased, sink lower down the heap. Otherwise, swim to better position
        if (oldPriority != null && idx != null) {
            if (oldPriority < priority) {
                sink(idx);
            } else if (oldPriority > priority) {
                swim(idx);
            }
        }
    }

    /**
     * Returns whether the priority queue contains a given place
     * @param place place we want to check the existence of
     * @return True if place is in the priority queue, false otherwise
     */
    public boolean contains(Place place) {
        for (Place p : items) {
            if (p.equals(place)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the current size of the priority queue
     * @return number of elements currently in the priority queue
     */
    public int getSize() {
        return items.size();
    }

    /**
     * Checks whether the priority queue is empty or not
     * @return true if the prioirty queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return items.size() == 0;
    }

    /**
     * Returns true if the place at index i has a greater priority than the one at index j
     * @param i index of the first place you want to compare
     * @param j index of the second place you want to compare
     * @return True if p has a greater priority than q
     */
    private boolean hasGreaterPriority(int i, int j) {
        if (items.get(i) == null || items.get(j) == null) {
            throw new IllegalArgumentException("hasGreaterPriority - index has no associated Place");
        }
        return priorities.get(items.get(i)) > priorities.get(items.get(j));
    }

    /**
     * Swaps the objects at indices i and j of the items array
     * @param i first index of the element you want to swap
     * @param j second index of the element you want to swap
     */
    private void swap(int i, int j) {
        if (i >= getSize() || j >= getSize()) {
            throw new IllegalArgumentException("swap - indices are not valid");
        }
        Place pi = items.get(i);
        Place pj = items.get(j);
        items.set(i, pj);
        items.set(j, pi);
        indices.put(pi, j);
        indices.put(pj, i);
    }

    // Sink and swim helpers:
    // Root == 0
    // Parent       == items[(i - 1) / 2]
    // Left Child   == items[2 * i + 1]
    // Right Child  == items[2 * i + 2]

    /**
     * Swims the place at the given index "up" the heap
     * @param i index we want to swim up
     */
    private void swim(int i) {
        if (i == 0) {
            return;
        }

        int parent = (i - 1) / 2;
        if (hasGreaterPriority(parent, i)) {
            // i is better so swap it with parent
            swap(i, parent);
            swim(parent);
        }
    }

    /**
     * Sinks the place at the given index "down" the heap
     * @param i index we want to sink
     */
    private void sink(int i) {
        int leftChild = (2 * i) + 1;
        int rightChild = leftChild + 1;
        if (rightChild < getSize()) { // sink to smaller of leftchild and rightchild
            if (hasGreaterPriority(leftChild, rightChild)) { // swap with right
                if (hasGreaterPriority(i, rightChild)) {
                    swap(i, rightChild);
                    sink(rightChild);
                }
            } else if (hasGreaterPriority(i, leftChild)) { // swap with left
                swap(i, leftChild);
                sink(leftChild);
            }
        } else if (leftChild < getSize() && hasGreaterPriority(i, leftChild)) {
            // If i has one child, then we are at the bottom layer of the heap, so stop recursing
            swap(i, leftChild);
        }
    }
}
