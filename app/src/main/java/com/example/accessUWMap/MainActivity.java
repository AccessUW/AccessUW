package com.example.accessUWMap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;

import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Place;

public class MainActivity extends AppCompatActivity {
    ////////////////////////////////////////////////////////////
    ///     Constants
    ////////////////////////////////////////////////////////////
    private static final int AUTO_COMPLETE_FILTER_THRESHOLD = 1;

    ////////////////////////////////////////////////////////////
    ///     Fields
    ////////////////////////////////////////////////////////////

    // Coords for scrolling in map view
    private float mx, my;
    // Scroll views for moving on the map
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    // List of buildings on campus
    private Set<String> allBuildingNames; // Names of buildings
    private List<LocationSearchResult> searchableLocations; // Set of search result objects


    ////////////////////////////////////////////////////////////
    ///     Methods
    ////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Presenter component of the MVP framework
        CampusPresenter.init();

        // Init full list of possible search results for start and end search bars
        initSearchResults();

        // Get scroll views that control 2-D scrollability (i.e. user can move freely around map)
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);

        // Set up search bars for start and end locations
        AutoCompleteSearchAdapter adapter = new AutoCompleteSearchAdapter(
                this, android.R.layout.select_dialog_item, searchableLocations);
        AutoCompleteTextView startSearchBar = findViewById(R.id.searchStartView);
        startSearchBar.setAdapter(adapter);
        startSearchBar.setThreshold(AUTO_COMPLETE_FILTER_THRESHOLD);
        AutoCompleteTextView endSearchBar = findViewById(R.id.searchEndView);
        endSearchBar.setAdapter(adapter);
        endSearchBar.setThreshold(AUTO_COMPLETE_FILTER_THRESHOLD);

        // Set up start and end search bar listeners for when user selects an option
        startSearchBar.setOnItemClickListener((adapterView, view, i, l) ->
                updateStartLocation(((LocationSearchResult) adapterView.getItemAtPosition(i))
                        .getLocationResultName()));
        endSearchBar.setOnItemClickListener((adapterView, view, i, l) ->
                updateEndLocation(((LocationSearchResult) adapterView.getItemAtPosition(i))
                        .getLocationResultName()));

        // Set up toggle button filter listeners for when user filters their route for accessibility
        ((ToggleButton) findViewById(R.id.filterWheelchair)).setOnCheckedChangeListener(
                (toggleButtonView, isChecked) -> CampusPresenter.updateWheelchair(isChecked));
        ((ToggleButton) findViewById(R.id.filterNoStairs)).setOnCheckedChangeListener(
                (toggleButtonView, isChecked) -> CampusPresenter.updateNoStairs(isChecked));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Current x,y position on map that the user is looking at
        float curX, curY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // Get current x,y on screen of where user clicks
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Scroll on the map based on user's finger movement
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                // Stop moving the map once the user releases the screen
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }
        return true;
    }

    /**
     * Updater method that controls the current start location of the user's selected route
     * @param newStart is the new start location for the user's route
     */
    public void updateStartLocation(String newStart) {
        // Ensure valid start input to send to presenter
        if (allBuildingNames.contains(newStart)) {
            CampusPresenter.updateStart(newStart);
        }
    }

    /**
     * Updater method that controls the current end location of the user's selected route
     * @param newEnd is the new end location for the user's route
     */
    public void updateEndLocation(String newEnd) {
        // Ensure valid end input to send to presenter
        if (allBuildingNames.contains(newEnd)) {
            CampusPresenter.updateEnd(newEnd);
        }
    }

    /**
     * Search for the best route between the entered start and end locations. If either start or
     * end is not selected, user will be notified to choose a valid start/end location.
     */
    public void startRouteSearch() {
        // Get the route between inputted start and end locations
        try {
            List<Place> route = CampusPresenter.getRoute();

            if (route.isEmpty()) {
                Toast.makeText(this,
                        "Sorry, no route exists between those 2 places with the given filters.",
                        Toast.LENGTH_LONG).show();
            } else {
                // Process successful route built between inputted start and end locations
                System.out.println(route.toString());
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Please enter valid start/end locations.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize the search results to populate the AutoCompleteTextView start and end location
     * search bars.
     */
    private void initSearchResults() {
        allBuildingNames = new HashSet<>();
        searchableLocations = new ArrayList<>();

        // Acquire list of all buildings on campus
        allBuildingNames = new HashSet<>(); //CampusPresenter.getAllBuildingNames();

        allBuildingNames.add("Terry Hall");
        allBuildingNames.add("Odegaard Library");
        allBuildingNames.add("The HUB");
        allBuildingNames.add("Condon Hall");
        allBuildingNames.add("The District Market");

        for (String currLocation : allBuildingNames) {
            searchableLocations.add(new LocationSearchResult(currLocation));
        }
    }
}