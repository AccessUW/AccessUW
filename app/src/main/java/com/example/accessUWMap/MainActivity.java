package com.example.accessUWMap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;

import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<LocationSearchResult> searchableLocations;


    ////////////////////////////////////////////////////////////
    ///     Methods
    ////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get scroll views that control 2-D scrollability (i.e. user can move freely around map)
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);

        // Init full list of possible search results for start and end search bars
        initSearchResults();
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


        System.out.println("NEW START: " + newStart);
    }

    /**
     * Updater method that controls the current end location of the user's selected route
     * @param newEnd is the new end location for the user's route
     */
    public void updateEndLocation(String newEnd) {
        System.out.println("NEW END: " + newEnd);
    }

    /**
     * Search for the best route between the entered start and end locations. If either start or
     * end is not selected, user will be notified to choose a valid start/end location.
     */
    public void startRouteSearch() {
        //TODO: Add catch here in updateStartLocation to tell user to give a valid input in case it's wrong
    }

    /**
     * Initialize the search results to populate the AutoCompleteTextView start and end location
     * search bars.
     */
    private void initSearchResults() {
        searchableLocations = new ArrayList<>();

        // Acquire list of all buildings on campus
        Set<String> allLocations = new HashSet<>(); //CampusModel.getAllBuildingNames();

        allLocations.add("Terry Hall");
        allLocations.add("Suzallo Library");
        allLocations.add("Madrona Hall");
        allLocations.add("Hocus Pocus");
        allLocations.add("Paccar Hall");
        allLocations.add("Condon Hall");

        for (String currLocation : allLocations) {
            searchableLocations.add(new LocationSearchResult(currLocation));
        }
    }
}