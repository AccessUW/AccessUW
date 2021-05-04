package models;

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
    private boolean genderNeutralRestroom;
    private boolean elevator;
    private Set<Place> entrances;
    private Set<Place> assistedEntrances;
    private String description;

    /**
     * Creates a new Building object
     * @param x x position of the building
     * @param y y position of the building
     * @param shortName short name identifier of the building
     * @param restroom true if the building has gender neutral restrooms
     * @param elevator true if the building has an elevator
     * @param entrances all entrances to this building
     * @param assisted assisted entrances to this building
     * @param description description of this building
     */
    public Building(float x, float y, String shortName, boolean restroom, boolean elevator,
                    Set<Place> entrances, Set<Place> assisted, String description) {

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
        return genderNeutralRestroom;
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
    public Set<Place> getAssistedEntrances() {
        return assistedEntrances;
    }

    /**
     * Get the description of this building
     * @return short description of this building
     */
    public String getDescription() {
        return description;
    }

    //TODO: add equals method

    //TODO: add hash method
}
