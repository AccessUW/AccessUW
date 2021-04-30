package com.example.accessUWMap;

/**
 * Representation of a search result for one of the auto-complete search bars.
 */
public class LocationSearchResult {
    private final String locationResultName; // Location's name

    public LocationSearchResult(String locationResultName) {
        this.locationResultName = locationResultName;
    }

    public String getLocationResultName() {
        return locationResultName;
    }
}
