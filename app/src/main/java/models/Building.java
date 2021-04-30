package models;

import java.util.Set;

public class Building {
    private float x;
    private float y;
    private String shortName;
    private boolean genderNeutralRestroom;
    private boolean elevator;
    private Set<Place> entrances;
    private Set<Place> assistedEntrances;
    private String description;

    public Building(float x, float y, String shortName, boolean restroom, boolean elevator,
                    Set<Place> entrances, Set<Place> assisted, String description) {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean hasGenderNeutralRestroom() {
        return genderNeutralRestroom;
    }

    public boolean hasElevator() {
        return elevator;
    }

    public Set<Place> getEntrances() {
        return entrances;
    }

    public Set<Place> getAssistedEntrances() {
        return assistedEntrances;
    }

    public String getDescription() {
        return description;
    }
}
