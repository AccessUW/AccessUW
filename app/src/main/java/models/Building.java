package models;

import android.os.Build;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class defines a Building, which has an x and y position, short name identifier, a
 * description, as well as information about the building's accessibility accommodations such as
 * assisted entrances and gender neutral restrooms
 */
public class Building {
    private float x;
    private float y;
    private String shortName;
    private String genderNeutralRestrooms;
    private String accessibleRestrooms;
    private boolean elevator;
    private Set<Place> entrances;
    private Set<Place> accessibleEntrances;

    /**
     * Creates a new Building object
     * @param shortName short name identifier of the building
     * @param x x coordinate of the building
     * @param y y coordinate of the building
     * @param gNRestroomFloors space separated string of floors with gender neutral restrooms
     * @param accessibleRestroomFloors space separated string of floors with accessible restrooms
     * @param elevator true if the building has an elevator
     */
    public Building(String shortName, float x, float y, String gNRestroomFloors,
                    String accessibleRestroomFloors, boolean elevator) {
        this.shortName = shortName;
        this.genderNeutralRestrooms = gNRestroomFloors;
        this.accessibleRestrooms = accessibleRestroomFloors;
        this.elevator = elevator;
        this.entrances = new HashSet<>();
        this.accessibleEntrances = new HashSet<>();
    }

    /**
     * Get the x position of this building
     * @return x position of the building
     */
    public float getX() {
        return x;
    }

    /**
     * Get the y position of this building
     * @return y position of the building
     */
    public float getY() {
        return y;
    }

    /**
     * Get the short name for this building
     * @return short name identifier of the building
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Returns whether the building has a gender neutral restroom
     * @return true if the building has a gender neutral restroom, false otherwise
     */
    public boolean hasGenderNeutralRestroom() {
        return !this.genderNeutralRestrooms.isEmpty();
    }

    /**
     * Gets the floor numbers of all floors with a gender neutral restroom in this building
     * @return Space separated string of floors with gender neutral restroom, with the empty string
     * meaning there are no floors and 'All' meaning there are on all floors
     */
    public String getGenderNeutralRestroomFloors() {
        return this.genderNeutralRestrooms;
    }

    /**
     * Returns whether the building has an accessible restroom or not
     * @return true if the building has an accessible restroom, false otherwise
     */
    public boolean hasAccessibleRestroom() {
        return !this.accessibleRestrooms.isEmpty();
    }

    /**
     * Gets the floor numbers of all floors with an accessible restroom in this building
     * @return Space separated string of floors with an accessible restroom, with the empty string
     * meaning there are no floors and 'All' meaning there are on all floors
     */
    public String getAccessibleRestroomFloors() {
        return this.accessibleRestrooms;
    }

    /**
     * Returns whether the building has an elevator
     * @return true if the building has an elevator, false otherwise
     */
    public boolean hasElevator() {
        return elevator;
    }

    /**
     * Get all of the entrances to this building
     * @return Places for all entrances to this building
     */
    public Set<Place> getEntrances() {
        return entrances;
    }

    /**
     * Get all assisted entrances to this building
     * @return Places for all assisted entrances to this building
     */
    public Set<Place> getAccessibleEntrances() {
        return accessibleEntrances;
    }

    /**
     * Adds an entrance to this building, updating this building's x, y position
     * @param entrance Place of the entrance we want to add to this building
     * @param assisted true if the entrance is assisted, false otherwise
     */
    public void addEntrance(Place entrance, boolean assisted) {
        if (!entrances.contains(entrance)) {
            entrances.add(entrance);
            if (assisted) {
                accessibleEntrances.add(entrance);
            }

            // Recalculate Building's x, y by averaging entrances
            float xSum = (x * entrances.size() - 1) + entrance.getX();
            float ySum = (y * entrances.size() - 1) + entrance.getY();
            x = xSum / entrances.size();
            y = ySum / entrances.size();
        }
    }

    /**
     * Returns whether this Building is equal to another object
     * @param o object we want to check the equality of
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Building)) {
            return false;
        }

        Building other = (Building) o;

        return this.shortName.equals(other.shortName) &&
                this.genderNeutralRestrooms.equals(other.genderNeutralRestrooms) &&
                this.accessibleRestrooms.equals(other.accessibleRestrooms) &&
                this.entrances.equals(other.entrances) &&
                this.elevator == other.elevator;
    }

    /**
     * Get the hash code of this Building
     * @return int hash code for this building
     */
    @Override
    public int hashCode() {
        return this.shortName.length() + this.genderNeutralRestrooms.length() +
                this.accessibleRestrooms.length() + this.entrances.size() + (elevator ? 3 : 0);
    }
}
