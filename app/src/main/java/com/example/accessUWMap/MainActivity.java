package com.example.accessUWMap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Place;

public class MainActivity extends AppCompatActivity {

    public enum AppStates {SEARCH, FOUND_START, BUILD_ROUTE, NAV};

    ////////////////////////////////////////////////////////////
    ///     Constants
    ////////////////////////////////////////////////////////////
    private static final int AUTO_COMPLETE_FILTER_THRESHOLD = 1;

    ////////////////////////////////////////////////////////////
    ///     Fields
    ////////////////////////////////////////////////////////////

    // Current state app is in
    private AppStates mState;

    // Coords for scrolling in map view
    private float mx, my;
    // Scroll views for moving on the map
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    // Views for displaying search bars and route filters
    private LinearLayout startSearchBarLayout;
    private LinearLayout endSearchBarAndFiltersLayout;
    private AutoCompleteTextView startSearchBar;
    private AutoCompleteTextView endSearchBar;

    // Views for displaying building description
    private LinearLayout buildDescLayout;

    // Views for building the route
    private LinearLayout buildRouteLayout;

    // Views for navigation
    private LinearLayout navLayout;

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

        // Initialize state
        mState = AppStates.SEARCH;

        // Initialize the Presenter component of the MVP framework
        CampusPresenter.init();

        // Init full list of possible search results for start and end search bars
        initSearchResults();

        // Get scroll views that control 2-D scrollability (i.e. user can move freely around map)
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);

        // Set up search bar layout and listeners
        startSearchBarLayout = findViewById(R.id.startSearchBarLayout);
        endSearchBarAndFiltersLayout = findViewById(R.id.endSearchBarAndFiltersLayout);

        // Set up search bars' auto-complete functionality for start and end locations
        AutoCompleteSearchAdapter adapter = new AutoCompleteSearchAdapter(
                this, android.R.layout.select_dialog_item, searchableLocations);
        startSearchBar = findViewById(R.id.searchStartView);
        startSearchBar.setAdapter(adapter);
        startSearchBar.setThreshold(AUTO_COMPLETE_FILTER_THRESHOLD);
        endSearchBar = findViewById(R.id.searchEndView);
        endSearchBar.setAdapter(adapter);
        endSearchBar.setThreshold(AUTO_COMPLETE_FILTER_THRESHOLD);

        // Set up start and end search bar listeners for when user selects an option
        startSearchBar.setOnItemClickListener((adapterView, view, i, l) ->
                updateStartLocation(((LocationSearchResult) adapterView.getItemAtPosition(i))
                        .getLocationResultName()));
        endSearchBar.setOnItemClickListener((adapterView, view, i, l) ->
                updateEndLocation(((LocationSearchResult) adapterView.getItemAtPosition(i))
                        .getLocationResultName()));

        // Set up back-arrow button listener
        findViewById(R.id.backArrowButton).setOnClickListener(view -> goBack());

        // Set up toggle button filter listeners for when user filters their route for accessibility
        ((ToggleButton) findViewById(R.id.filterWheelchair)).setOnCheckedChangeListener(
                (toggleButtonView, isChecked) -> CampusPresenter.updateWheelchair(isChecked));
        ((ToggleButton) findViewById(R.id.filterNoStairs)).setOnCheckedChangeListener(
                (toggleButtonView, isChecked) -> CampusPresenter.updateNoStairs(isChecked));

        // Set up building description layout and listeners
        buildDescLayout = findViewById(R.id.building_description_layout);
        findViewById(R.id.findRouteButton).setOnClickListener(view -> updateState(AppStates.BUILD_ROUTE));

        // Set up route-making layout
        buildRouteLayout = findViewById(R.id.build_route_layout);
        findViewById(R.id.startRouteButton).setOnClickListener(view -> updateState(AppStates.NAV));
        findViewById(R.id.swapLocationButton).setOnClickListener(view -> swapStartAndEnd());

        // Set up nav layout
        navLayout = findViewById(R.id.nav_layout);
        findViewById(R.id.cancelRouteButton).setOnClickListener(view -> goBack());
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

        // Update state to FOUND_START if currently in START state
        if (mState == AppStates.SEARCH) {
            updateState(AppStates.FOUND_START);
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
     * Swaps start and end locations in the build-route state of the app.
     */
    public void swapStartAndEnd() {
        String start = CampusPresenter.getCurrentStart();
        String end = CampusPresenter.getCurrentEnd();
        CampusPresenter.swapStartAndEnd();
        startSearchBar.setText(end);
        endSearchBar.setText(start);
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

    /**
     * Updates the state of the app based on user activity so that the appropriate components
     * are (in)visible.
     *
     * @param newState is the new state the user is moving to
     */
    private void updateState(AppStates newState) {
        AppStates lastState = mState;
        mState = newState;

        System.out.println("WAS " + lastState);
        System.out.println("NOW " + newState);

        switch(lastState) {
            case SEARCH:
                // Going forward through route-building steps
                if (newState == AppStates.FOUND_START) {
                    buildBuildingDesc();
                    buildDescLayout.setVisibility(View.VISIBLE);
                }

            case FOUND_START:
                // Going forward through route-building steps
                if (newState == AppStates.BUILD_ROUTE) {
                    buildDescLayout.setVisibility(View.INVISIBLE);
                    endSearchBarAndFiltersLayout.setVisibility(View.VISIBLE);
                    buildRouteLayout.setVisibility(View.VISIBLE);
                }
                // Going backward through route-building steps (i.e. hit back arrow)
                if (newState == AppStates.SEARCH) {
                    buildDescLayout.setVisibility(View.INVISIBLE);
                }

            case BUILD_ROUTE:
                // Going forward through route-building steps
                if (newState == AppStates.NAV) {
                    startSearchBarLayout.setVisibility(View.INVISIBLE);
                    endSearchBarAndFiltersLayout.setVisibility(View.INVISIBLE);
                    buildRouteLayout.setVisibility(View.INVISIBLE);
                    navLayout.setVisibility(View.VISIBLE);
                }
                // Going backward through route-building steps (i.e. hit back arrow)
                if (newState == AppStates.FOUND_START) {
                    endSearchBarAndFiltersLayout.setVisibility(View.INVISIBLE);
                    buildRouteLayout.setVisibility(View.INVISIBLE);
                    buildDescLayout.setVisibility(View.VISIBLE);
                }

            case NAV:
                // Going backward through route-building steps (i.e. hit back arrow)
                if (newState == AppStates.BUILD_ROUTE) {
                    navLayout.setVisibility(View.INVISIBLE);
                    startSearchBarLayout.setVisibility(View.VISIBLE);
                    endSearchBarAndFiltersLayout.setVisibility(View.VISIBLE);
                    buildRouteLayout.setVisibility(View.VISIBLE);
                }
        }
    }

    /**
     * Method triggered by back-arrow button that calls updateState with the appropriate state
     */
    private void goBack() {
        if (mState == AppStates.FOUND_START) {
            updateState(AppStates.SEARCH);
        } else if (mState == AppStates.BUILD_ROUTE) {
            updateState(AppStates.FOUND_START);
        } else if (mState == AppStates.NAV) {
            updateState(AppStates.BUILD_ROUTE);
        }
    }

    /**
     * Updates the building description layout based on the current building the user is looking
     * at.
     */
    private void buildBuildingDesc() {
        String building = CampusPresenter.getCurrentStart();
    }
}