package com.example.accessUWMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.MotionEvent;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import models.Place;

public class MainActivity extends AppCompatActivity {

    public enum AppStates {SEARCH, FOUND_START, BUILD_ROUTE, NAV};

    ////////////////////////////////////////////////////////////
    ///     Constants
    ////////////////////////////////////////////////////////////
    private static final int CAMPUS_MAP_IMAGE_WIDTH_PX = 4330;
    private static final int CAMPUS_MAP_IMAGE_HEIGHT_PX = 2964;
    private static final int AUTO_COMPLETE_FILTER_THRESHOLD = 1;
    private static final int CLICK_NEAREST_MOVEMENT_THRESHOLD = 20;
    private static final int VERTICAL_OFFSET_CLICK_ON_MAP = 30;
    private static final int VERTICAL_OFFSET_ZOOM_LEVEL_TWO = 30;
    private static final int VERTICAL_OFFSET_ZOOM_LEVEL_ONE = 36;
    private static final int VERTICAL_OFFSET_ZOOM_LEVEL_ZERO = 37;
    private static final int MOST_ZOOMED_IN_LEVEL = 2;
    private static final int SCALE_ZOOM_LEVEL_TWO = 1;
    private static final int SCALE_ZOOM_LEVEL_ONE = 2;
    private static final int SCALE_ZOOM_LEVEL_ZERO = 4;
    private static final int ZOOM_LEVEL_TWO_CENTER_MAP_SHIFT_X = 800;
    private static final int ZOOM_LEVEL_TWO_CENTER_MAP_SHIFT_Y = 1200;
    private static final int ZOOM_IN_LEVEL_ONE_CENTER_MAP_SHIFT_X = 600;
    private static final int ZOOM_IN_LEVEL_ONE_CENTER_MAP_SHIFT_Y = 400;
    private static final int ZOOM_OUT_LEVEL_ONE_CENTER_MAP_SHIFT_X = -300;
    private static final int ZOOM_OUT_LEVEL_ONE_CENTER_MAP_SHIFT_Y = -500;
    private static final int ZOOM_LEVEL_ZERO_CENTER_MAP_SHIFT_X = -300;
    private static final int ZOOM_LEVEL_ZERO_CENTER_MAP_SHIFT_Y = -500;

    ////////////////////////////////////////////////////////////
    ///     Fields
    ////////////////////////////////////////////////////////////

    // Screen dimensions
    private int screenWidth;
    private int screenHeight;

    // Current state app is in
    private AppStates mState;

    // Map view
    private ImageView mapViewImage;

    // Map zoom variables
    private int zoomLevel;
    private boolean initPrimaryZoom;
    private boolean initZoom;
    private static float map_image_height_to_width_ratio;
    private static int scale_zoom_level_2_width;
    private static int scale_zoom_level_1_width;
    private static int scale_zoom_level_0_width;

    // Coords for scrolling in map view
    private float mx, my;
    // Scroll views for moving on the map
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;
    // Track where and for how long user has been pressing to see if they're doing a long press
    private int pressDownX;
    private int pressDownY;

    // Views for displaying search bars and route filters
    private LinearLayout startSearchBarLayout;
    private LinearLayout endSearchBarAndSwapLayout;
    private LinearLayout routeFilterLayout;
    private AutoCompleteTextView startSearchBar;
    private AutoCompleteTextView endSearchBar;
    private ToggleButton wheelchairToggleButton;
    private ToggleButton noStairsToggleButton;

    // Views for displaying building description
    private LinearLayout buildDescLayout;
    private TextView descBuildingName;
    private TextView descShortBuildingName;
    private TextView descElevatorInfo;
    private TextView descAccessibleRestroomInfo;
    private TextView descGenderNeutralRestroomInfo;

    // Button for building the route
    private Button startNavRouteButton;

    // Views for navigation
    private LinearLayout navLayout;
    private TextView destTextView;

    // View for drawing the route
    private ImageView routeView;

    // Canvas for drawing the route
    private Canvas routeCanvas;

    // List of buildings on campus
    private Map<String, String> longToShortAndLong; // Long names of buildings mapped to their short and long names (e.g. Savery Hall -> SAV: Savery Hall)
    private Map<String, String> shortAndLongToLong; // Short and long building names mapped to their long names
    private List<LocationSearchResult> searchableStartLocations; // Set of start location search result objects
    private List<LocationSearchResult> searchableEndLocations; // Set of end location search result objects

    ////////////////////////////////////////////////////////////
    ///     Methods
    ////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // Initialize state
        mState = AppStates.SEARCH;

        // Initialize the Presenter component of the MVP framework
        try {
            CampusPresenter.init(this);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        // Init full list of possible search results for start and end search bars
        initSearchResults();

        // Get scroll views that control 2-D scrollability (i.e. user can move freely around map)
        // and initialize mx,my starting coordinates
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);
        mx = 0;
        my = 0;

        // Set up zoom functionality
        initZoom = false;
        initPrimaryZoom = false;
        zoomLevel = MOST_ZOOMED_IN_LEVEL;
        findViewById(R.id.zoom_in).setOnClickListener(view -> zoom(zoomLevel + 1));
        findViewById(R.id.zoom_out).setOnClickListener(view -> zoom(zoomLevel - 1));

        // Set up search bar layout and listeners
        startSearchBarLayout = findViewById(R.id.startSearchBarLayout);
        endSearchBarAndSwapLayout = findViewById(R.id.endSearchBarAndSwapLayout);
        routeFilterLayout = findViewById(R.id.routeFilterButtonsLayout);

        // Set up search bars' auto-complete functionality for start and end locations
        AutoCompleteSearchAdapter startSearchAdapter = new AutoCompleteSearchAdapter(
                this, android.R.layout.select_dialog_item, searchableStartLocations);
        startSearchBar = findViewById(R.id.searchStartView);
        startSearchBar.setAdapter(startSearchAdapter);
        startSearchBar.setThreshold(AUTO_COMPLETE_FILTER_THRESHOLD);
        AutoCompleteSearchAdapter endSearchAdapter = new AutoCompleteSearchAdapter(
                this, android.R.layout.select_dialog_item, searchableEndLocations);
        endSearchBar = findViewById(R.id.searchEndView);
        endSearchBar.setAdapter(endSearchAdapter);
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

        // Set up toggle button filter listeners and colors for when user filters their route for accessibility
        wheelchairToggleButton = findViewById(R.id.filterWheelchair);
        wheelchairToggleButton.setOnCheckedChangeListener(
                (toggleButtonView, isChecked) -> updateWheelchairFilter(isChecked));
        wheelchairToggleButton.setBackgroundColor(getColor(R.color.filter_button_background_off));
        wheelchairToggleButton.setTextColor(getColor(R.color.filter_button_text_off));
        noStairsToggleButton = findViewById(R.id.filterNoStairs);
        noStairsToggleButton.setOnCheckedChangeListener(
                (toggleButtonView, isChecked) -> updateNoStairsFilter(isChecked));
        noStairsToggleButton.setBackgroundColor(getColor(R.color.filter_button_background_off));
        noStairsToggleButton.setTextColor(getColor(R.color.filter_button_text_off));



        // Set up building description layout and listeners
        buildDescLayout = findViewById(R.id.building_description_layout);
        findViewById(R.id.findRouteButton).setOnClickListener(view -> updateState(AppStates.BUILD_ROUTE));
        descBuildingName = findViewById(R.id.buildingNameTextView);
        descShortBuildingName = findViewById(R.id.buildingShortName);
        descElevatorInfo = findViewById(R.id.elevatorInfoTextView);
        descAccessibleRestroomInfo = findViewById(R.id.accRRTextView);
        descGenderNeutralRestroomInfo = findViewById(R.id.gendNeuRRTextView);

        // Set up route-making layout
        startNavRouteButton = findViewById(R.id.startRouteButton);
        startNavRouteButton.setOnClickListener(view -> startRouteSearch());
        findViewById(R.id.swapLocationButton).setOnClickListener(view -> swapStartAndEnd());

        // Set up nav layout
        navLayout = findViewById(R.id.nav_layout);
        findViewById(R.id.cancelRouteButton).setOnClickListener(view -> goBack());
        destTextView = findViewById(R.id.destinationTextView);

        // Set up canvas and paint for drawing route
        // Get routeView
        routeView = (ImageView) findViewById(R.id.routeView);
        // Initialize bitmap
        Bitmap routeBitmap = Bitmap.createBitmap(CAMPUS_MAP_IMAGE_WIDTH_PX/SCALE_ZOOM_LEVEL_TWO, CAMPUS_MAP_IMAGE_HEIGHT_PX/SCALE_ZOOM_LEVEL_TWO,
                Bitmap.Config.ARGB_8888);
        routeView.setImageBitmap(routeBitmap);
        // Initialize canvas from bitmap
        routeCanvas = new Canvas(routeBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Set up map to the starting zoom level once the user is able to navigate the map
        if (!initPrimaryZoom) {
            zoom(2);
            initPrimaryZoom = true;
        }

        // Current x,y position on map that the user is looking at
        float curX, curY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // Get current x,y on screen of where user clicks
                mx = event.getX();
                my = event.getY();
                // Track where user first clicks down
                pressDownX = (int) mx;
                pressDownY = (int) my;
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
                // If minimal x,y movement, trigger click choose building on map
                if ((Math.abs((event.getX() - pressDownX)) <= CLICK_NEAREST_MOVEMENT_THRESHOLD) &&
                        (Math.abs((event.getY() - pressDownY)) <= CLICK_NEAREST_MOVEMENT_THRESHOLD)) {
                    // Get coordinates of where the user pressed in terms of pixels then pass it to select
                    // the nearest building
                    int mapClickX = (int) (dpToPX(hScroll.getScrollX() + mx));
                    int mapClickY = (int) (dpToPX(vScroll.getScrollY() + my) - VERTICAL_OFFSET_CLICK_ON_MAP);
                    clickChooseBuilding(mapClickX, mapClickY);
                }
                break;
        }
        return true;
    }

    /**
     * Initialize the search results to populate the AutoCompleteTextView start and end location
     * search bars.
     */
    private void initSearchResults() {
        searchableStartLocations = new ArrayList<>();
        searchableEndLocations = new ArrayList<>();

        // Acquire list of all buildings on campus
        Map<String, String> shortToLongBuildingNames = CampusPresenter.getAllBuildingNames();
        longToShortAndLong = new HashMap<>();
        shortAndLongToLong = new HashMap<>();

        for (String currShortName : shortToLongBuildingNames.keySet()) {
            String currLongName = shortToLongBuildingNames.get(currShortName);
            String shortAndLongName = currShortName + ": " + currLongName;
            longToShortAndLong.put(currLongName, shortAndLongName);
            shortAndLongToLong.put(shortAndLongName, currLongName);
            searchableStartLocations.add(new LocationSearchResult(shortAndLongName));
            searchableEndLocations.add(new LocationSearchResult(shortAndLongName));
        }

        // Add gender-neutral restroom as one of the end locations
        searchableEndLocations.add(new LocationSearchResult(getString(R.string.gender_neutral_restroom_search_result)));
    }

    /**
     * Zoom the map to the given zoom level.
     * @param levelToZoom is the level of zoom
     */
    private void zoom(int levelToZoom) {
        // Init zoom numbers (now that all views are set up)
        if (!initZoom) {
            // Set up zoom sizes
            mapViewImage = findViewById(R.id.mapView);
            int mapWidth = mapViewImage.getMeasuredWidth();
            int mapHeight = mapViewImage.getMeasuredHeight();
            map_image_height_to_width_ratio = (float) (mapHeight * 1.0) / mapWidth;
            scale_zoom_level_2_width = mapWidth / SCALE_ZOOM_LEVEL_TWO;
            scale_zoom_level_1_width = mapWidth / SCALE_ZOOM_LEVEL_ONE;
            scale_zoom_level_0_width = mapWidth / SCALE_ZOOM_LEVEL_ZERO;

            initZoom = true;
        }

        // If it is possible to zoom in the given direction (in or out), then scale map to do so
        if (levelToZoom >= 0 && levelToZoom <= MOST_ZOOMED_IN_LEVEL) {
            int prevLevel = zoomLevel;
            zoomLevel = levelToZoom;
            if (levelToZoom == 2) {
                mapViewImage.getLayoutParams().width = scale_zoom_level_2_width;
                mapViewImage.getLayoutParams().height = (int) (scale_zoom_level_2_width * map_image_height_to_width_ratio);
                mapViewImage.requestLayout();
                adjustRouteViewSize();
                moveMapToPoint( hScroll.getScrollX()*2 + ZOOM_LEVEL_TWO_CENTER_MAP_SHIFT_X,  vScroll.getScrollY()*2 + ZOOM_LEVEL_TWO_CENTER_MAP_SHIFT_Y);
            } else if (levelToZoom == 1) {
                mapViewImage.getLayoutParams().width = scale_zoom_level_1_width;
                mapViewImage.getLayoutParams().height = (int) (scale_zoom_level_1_width * map_image_height_to_width_ratio);
                mapViewImage.requestLayout();
                adjustRouteViewSize();
                if (prevLevel == 2) { // If zooming out
                    moveMapToPoint( hScroll.getScrollX()/2 + ZOOM_OUT_LEVEL_ONE_CENTER_MAP_SHIFT_X, vScroll.getScrollY()/2 + ZOOM_OUT_LEVEL_ONE_CENTER_MAP_SHIFT_Y);
                } else { // If zooming in
                    moveMapToPoint( hScroll.getScrollX()*2 + ZOOM_IN_LEVEL_ONE_CENTER_MAP_SHIFT_X,  vScroll.getScrollY()*2 + ZOOM_IN_LEVEL_ONE_CENTER_MAP_SHIFT_Y);
                }
            } else if (levelToZoom == 0) {
                mapViewImage.getLayoutParams().width = scale_zoom_level_0_width;
                mapViewImage.getLayoutParams().height = (int) (scale_zoom_level_0_width * map_image_height_to_width_ratio);
                mapViewImage.requestLayout();
                adjustRouteViewSize();
                moveMapToPoint( hScroll.getScrollX()/2 + ZOOM_LEVEL_ZERO_CENTER_MAP_SHIFT_X,  vScroll.getScrollY()/2 + ZOOM_LEVEL_ZERO_CENTER_MAP_SHIFT_Y);
            }

            // Redraw the route if in the NAV state
            if (mState == AppStates.NAV) {
                drawRoute(CampusPresenter.getRoute());
            }
        }
    }

    /**
     * Adjusts the size of the route view to only limit the user to scrolling around the map image
     * (and not farther into the grey space that surrounds). It includes a buffer for menus and views
     * that could cover up large parts of the map so that the user can still see all of the map.
     */
    private void adjustRouteViewSize() {
        // Calculate amount needed to buffer route view so that description view doesn't cover up the map
        int heightBuffer = 0;
        if (mState == AppStates.FOUND_START) {
            heightBuffer = findViewById(R.id.building_description_layout).getMeasuredHeight();
        } else if (mState == AppStates.NAV) {
            heightBuffer = findViewById(R.id.nav_layout).getMeasuredHeight();
        }

        // Adjust size of route view
        Bitmap routeBitmap;
        if (zoomLevel == 2) {
            routeView.getLayoutParams().width = scale_zoom_level_2_width;
            routeView.getLayoutParams().height = heightBuffer + ((int) (scale_zoom_level_2_width * map_image_height_to_width_ratio));
            routeBitmap = Bitmap.createBitmap(CAMPUS_MAP_IMAGE_WIDTH_PX/SCALE_ZOOM_LEVEL_TWO, CAMPUS_MAP_IMAGE_HEIGHT_PX/SCALE_ZOOM_LEVEL_TWO,
                    Bitmap.Config.ARGB_8888);
        } else if (zoomLevel == 1) {
            routeView.getLayoutParams().width = scale_zoom_level_1_width;
            routeView.getLayoutParams().height = heightBuffer + ((int) (scale_zoom_level_1_width * map_image_height_to_width_ratio));
            routeBitmap = Bitmap.createBitmap(CAMPUS_MAP_IMAGE_WIDTH_PX/SCALE_ZOOM_LEVEL_ONE, CAMPUS_MAP_IMAGE_HEIGHT_PX/SCALE_ZOOM_LEVEL_ONE,
                    Bitmap.Config.ARGB_8888);
        } else {
            routeView.getLayoutParams().width = scale_zoom_level_0_width;
            routeView.getLayoutParams().height = heightBuffer + ((int) (scale_zoom_level_0_width * map_image_height_to_width_ratio));
            routeBitmap = Bitmap.createBitmap(CAMPUS_MAP_IMAGE_WIDTH_PX/SCALE_ZOOM_LEVEL_ZERO, CAMPUS_MAP_IMAGE_HEIGHT_PX/SCALE_ZOOM_LEVEL_ZERO,
                    Bitmap.Config.ARGB_8888);
        }
        // Update bitmap and canvas for route view
        routeView.requestLayout();
        routeView.setImageBitmap(routeBitmap);
        routeCanvas = new Canvas(routeBitmap);
    }

    /**
     * Select closest building to where the user touched as the start location (if in SEARCH or
     * FOUND_START states) or the end location (if in BUILD_ROUTE state).
     *
     * @param x is the x coordinate (in pixels) on the map of where the user pressed
     * @param y is the y coordinate (in pixels) on the map of where the user pressed
     */
    private void clickChooseBuilding(int x, int y) {
        // Adjusts invalid input
        if (zoomLevel == 2) {
            if (x < 0) {
                x = 0;
            } else if (x > CAMPUS_MAP_IMAGE_WIDTH_PX / SCALE_ZOOM_LEVEL_TWO) {
                x = CAMPUS_MAP_IMAGE_WIDTH_PX / SCALE_ZOOM_LEVEL_TWO;
            }
            if (y < 0) {
                y = 0;
            } else if (y > CAMPUS_MAP_IMAGE_HEIGHT_PX / SCALE_ZOOM_LEVEL_TWO) {
                y = CAMPUS_MAP_IMAGE_HEIGHT_PX / SCALE_ZOOM_LEVEL_TWO;
            }
            x *= SCALE_ZOOM_LEVEL_TWO;
            y *= SCALE_ZOOM_LEVEL_TWO;
        } else if (zoomLevel == 1) {
            if (x < 0) {
                x = 0;
            } else if (x > CAMPUS_MAP_IMAGE_WIDTH_PX / SCALE_ZOOM_LEVEL_ONE) {
                x = CAMPUS_MAP_IMAGE_WIDTH_PX / SCALE_ZOOM_LEVEL_ONE;
            }
            if (y < 0) {
                y = 0;
            } else if (y > CAMPUS_MAP_IMAGE_HEIGHT_PX / SCALE_ZOOM_LEVEL_ONE) {
                y = CAMPUS_MAP_IMAGE_HEIGHT_PX / SCALE_ZOOM_LEVEL_ONE;
            }
            x *= SCALE_ZOOM_LEVEL_ONE;
            y *= SCALE_ZOOM_LEVEL_ONE;
        } else if (zoomLevel == 0) {
            if (x < 0) {
                x = 0;
            } else if (x > CAMPUS_MAP_IMAGE_WIDTH_PX / SCALE_ZOOM_LEVEL_ZERO) {
                x = CAMPUS_MAP_IMAGE_WIDTH_PX / SCALE_ZOOM_LEVEL_ZERO;
            }
            if (y < 0) {
                y = 0;
            } else if (y > CAMPUS_MAP_IMAGE_HEIGHT_PX / SCALE_ZOOM_LEVEL_ZERO) {
                y = CAMPUS_MAP_IMAGE_HEIGHT_PX / SCALE_ZOOM_LEVEL_ZERO;
            }
            x *= SCALE_ZOOM_LEVEL_ZERO;
            y *= SCALE_ZOOM_LEVEL_ZERO;
        }

        // Selects nearest start or end building (depending on current app state) based on where the user clicked
        String selectedBuilding = CampusPresenter.getClosestBuilding(x, y);
        String selectedBuildingShortAndLongName = longToShortAndLong.get(selectedBuilding);
        if (mState == AppStates.SEARCH || mState == AppStates.FOUND_START) {
            updateStartLocation(selectedBuildingShortAndLongName);
            startSearchBar.setText(selectedBuildingShortAndLongName);
        } else if (mState == AppStates.BUILD_ROUTE) {
            updateEndLocation(selectedBuildingShortAndLongName);
            endSearchBar.setText(selectedBuildingShortAndLongName);
        }
    }

    /**
     * Updates the current start location of the user's selected route.
     * @param newStartShortAndLong is the new start location for the user's route
     */
    public void updateStartLocation(String newStartShortAndLong) {
        // Ensure valid start input to send to presenter
        if (shortAndLongToLong.containsKey(newStartShortAndLong)) {
            CampusPresenter.updateStart(shortAndLongToLong.get(newStartShortAndLong));
        }

        // Update state to FOUND_START if currently in START state
        if (mState == AppStates.SEARCH) {
            updateState(AppStates.FOUND_START);
        }
        buildBuildingDesc();
        moveMapToBuilding(CampusPresenter.getCurrentStart());
    }

    /**
     * Updates the current end location of the user's selected route.
     * @param newEndShortAndLong is the new end location for the user's route
     */
    public void updateEndLocation(String newEndShortAndLong) {
        // Check if the result is for the nearest gender-neutral restroom or specific location
        if (newEndShortAndLong.equals(getString(R.string.gender_neutral_restroom_search_result))) { // Nearest gender-neutral bathroom
            String startLocation = CampusPresenter.getCurrentStart();
            Point startPoint = CampusPresenter.getBuildingCoordinates(startLocation);
            String gnrrDestination = CampusPresenter.getClosestGNBathroom(startPoint.x, startPoint.y);
            CampusPresenter.updateEnd(gnrrDestination);
            moveMapToBuilding(gnrrDestination);
        } else { // Specific location
            // Ensure valid end input to send to presenter
            if (shortAndLongToLong.containsKey(newEndShortAndLong)) {
                String longNameDestination = shortAndLongToLong.get(newEndShortAndLong);
                CampusPresenter.updateEnd(longNameDestination);
                moveMapToBuilding(longNameDestination);
            }
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
     * Updates the current state of the wheelchair-accessible route filter button.
     * @param isChecked is whether the wheelchair-accessible filter is toggled or not
     */
    public void updateWheelchairFilter(boolean isChecked) {
        // Update the presenter in MVP
        CampusPresenter.updateWheelchair(isChecked);

        // Update button display to visually show whether it is toggled or not
        if (isChecked) {
            wheelchairToggleButton.setBackgroundColor(getColor(R.color.filter_button_background_on));
            wheelchairToggleButton.setTextColor(getColor(R.color.filter_button_text_on));
        } else {
            wheelchairToggleButton.setBackgroundColor(getColor(R.color.filter_button_background_off));
            wheelchairToggleButton.setTextColor(getColor(R.color.filter_button_text_off));
        }
    }

    /**
     * Updates the current state of the no-stairs route filter button.
     * @param isChecked is whether the noStairs filter is toggled or not
     */
    public void updateNoStairsFilter(boolean isChecked) {
        // Update the presenter in MVP
        CampusPresenter.updateNoStairs(isChecked);

        // Update button display to visually show whether it is toggled or not
        if (isChecked) {
            noStairsToggleButton.setBackgroundColor(getColor(R.color.filter_button_background_on));
            noStairsToggleButton.setTextColor(getColor(R.color.filter_button_text_on));
        } else {
            noStairsToggleButton.setBackgroundColor(getColor(R.color.filter_button_background_off));
            noStairsToggleButton.setTextColor(getColor(R.color.filter_button_text_off));
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
                // Update state
                updateState(AppStates.NAV);
                // Process successful route built between inputted start and end locations
                drawRoute(route);
                // Move map back to starting location
                moveMapToBuilding(CampusPresenter.getCurrentStart());
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Please enter valid start/end locations.",
                    Toast.LENGTH_SHORT).show();
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

        switch(lastState) {
            case SEARCH:
                // Going forward through route-building steps
                if (newState == AppStates.FOUND_START) {
                    buildDescLayout.setVisibility(View.VISIBLE);
                }

            case FOUND_START:
                // Going forward through route-building steps
                if (newState == AppStates.BUILD_ROUTE) {
                    buildDescLayout.setVisibility(View.INVISIBLE);
                    endSearchBarAndSwapLayout.setVisibility(View.VISIBLE);
                    routeFilterLayout.setVisibility(View.VISIBLE);
                    startNavRouteButton.setVisibility(View.VISIBLE);
                }
                // Going backward through route-building steps (i.e. hit back arrow)
                if (newState == AppStates.SEARCH) {
                    buildDescLayout.setVisibility(View.INVISIBLE);
                }

            case BUILD_ROUTE:
                // Going forward through route-building steps
                if (newState == AppStates.NAV) {
                    // Undo BUILD_ROUTE
                    startSearchBarLayout.setVisibility(View.INVISIBLE);
                    endSearchBarAndSwapLayout.setVisibility(View.INVISIBLE);
                    routeFilterLayout.setVisibility(View.INVISIBLE);
                    startNavRouteButton.setVisibility(View.INVISIBLE);
                    // Set up NAV
                    String newDestination = "To: " + CampusPresenter.getCurrentEnd();
                    destTextView.setText(newDestination);
                    navLayout.setVisibility(View.VISIBLE);
                }
                // Going backward through route-building steps (i.e. hit back arrow)
                if (newState == AppStates.FOUND_START) {
                    endSearchBarAndSwapLayout.setVisibility(View.INVISIBLE);
                    routeFilterLayout.setVisibility(View.INVISIBLE);
                    startNavRouteButton.setVisibility(View.INVISIBLE);
                    // Make building description layout visible
                    buildDescLayout.setVisibility(View.VISIBLE);
                }

            case NAV:
                // Going backward through route-building steps (i.e. hit back arrow)
                if (newState == AppStates.BUILD_ROUTE) {
                    navLayout.setVisibility(View.INVISIBLE);
                    // Clear canvas
                    routeCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
                    // Make BUILD_ROUTE-related layouts visible
                    startSearchBarLayout.setVisibility(View.VISIBLE);
                    endSearchBarAndSwapLayout.setVisibility(View.VISIBLE);
                    routeFilterLayout.setVisibility(View.VISIBLE);
                    startNavRouteButton.setVisibility(View.VISIBLE);
                }
        }
        adjustRouteViewSize();
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
        // Get start building
        String building = CampusPresenter.getCurrentStart();
        // Get description of start building
        String[] buildDesc = CampusPresenter.getDesc(building);

        // Build strings for description
        String descShortBuildingNameText = "Building code: " + buildDesc[0];
        String descElevatorText = "Elevator access: " + buildDesc[1];
        String descAccRestroomText = "Accessible restroom: " + buildDesc[2];
        String descGenderNeutralRestroomText = "Gender-neutral restroom: " + buildDesc[3];

        // Set text views
        descBuildingName.setText(building);
        descShortBuildingName.setText(descShortBuildingNameText);
        descElevatorInfo.setText(descElevatorText);
        descAccessibleRestroomInfo.setText(descAccRestroomText);
        descGenderNeutralRestroomInfo.setText(descGenderNeutralRestroomText);
    }

    /**
     * Moves map view to see the start location.
     *
     * @param longBuildingName is the long version of the desired building name to center the map on.
     */
    private void moveMapToBuilding(String longBuildingName) {
        // Get coordinates of the given building
        Point buildingCoords = CampusPresenter.getBuildingCoordinates(longBuildingName);

        // Calculate offsets of screen width/height to center start building in view
        //      - If in the BUILD_ROUTE state, center the building a bit lower since the top menu where
        //        user sets the end location takes up more space at the top
        //      - Otherwise, center the building a bit higher since there's more room above the lower menu
        float horizontalOffset = screenWidth / 2f;
        float verticalOffset = screenHeight / 3f;
        if (mState == AppStates.BUILD_ROUTE) {
            verticalOffset = screenHeight / 2f;
        }

        // Calculate coordinates for the map view to scroll to
        int scrollX = 0;
        int scrollY = 0;
        if (zoomLevel == 2) {
            // Convert center of building coordinates from px to dp
            float centerX = pxToDP(buildingCoords.x) / SCALE_ZOOM_LEVEL_TWO;
            float centerY = pxToDP(buildingCoords.y) / SCALE_ZOOM_LEVEL_TWO;

            scrollX = (int) (centerX - horizontalOffset);
            scrollY = (int) (centerY - verticalOffset);
        } else if (zoomLevel == 1) {
            // Convert center of building coordinates from px to dp
            float centerX = pxToDP(buildingCoords.x) / SCALE_ZOOM_LEVEL_ONE;
            float centerY = pxToDP(buildingCoords.y) / SCALE_ZOOM_LEVEL_ONE;

            scrollX = (int) (centerX - horizontalOffset);
            scrollY = (int) (centerY - verticalOffset);
        } else if (zoomLevel == 0) {
            // Convert center of building coordinates from px to dp
            float centerX = pxToDP(buildingCoords.x)/SCALE_ZOOM_LEVEL_ZERO;
            float centerY = pxToDP(buildingCoords.y)/SCALE_ZOOM_LEVEL_ZERO;

            scrollX = (int) (centerX - horizontalOffset);
            scrollY = (int) (centerY - verticalOffset);
        }

        // Scroll map views
        hScroll.scrollTo(scrollX, scrollY);
        vScroll.scrollTo(scrollX, scrollY);
    }

    /**
     * Moves view of map to center on the given coordinates.
     * @param x coordinate (in pixels on map image)
     * @param y coordinate (in pixels on map image)
     */
    private void moveMapToPoint(int x, int y) {
        // Ensure valid x value to scroll to
        if (x < 0) {
            x = 0;
        } else {
            if (zoomLevel == 2 && x > scale_zoom_level_2_width) {
                x = scale_zoom_level_2_width;
            } else if (zoomLevel == 1 && x > scale_zoom_level_1_width) {
                x = scale_zoom_level_1_width;
            } else if (zoomLevel == 0 && x > scale_zoom_level_0_width) {
                x = scale_zoom_level_0_width;
            }
        }

        // Ensure valid y value to scroll to
        if (y < 0) {
            y = 0;
        } else {
            if (zoomLevel == 2 && y > scale_zoom_level_2_width * map_image_height_to_width_ratio) {
                y = (int) (scale_zoom_level_2_width * map_image_height_to_width_ratio);
            } else if (zoomLevel == 1 && y > scale_zoom_level_1_width * map_image_height_to_width_ratio) {
                y = (int) (scale_zoom_level_1_width * map_image_height_to_width_ratio);
            } else if (zoomLevel == 0 && y > scale_zoom_level_0_width * map_image_height_to_width_ratio) {
                y = (int) (scale_zoom_level_0_width * map_image_height_to_width_ratio);
            }
        }
        System.out.println("REGULAR X,Y: " + hScroll.getScrollX() + ", " + vScroll.getScrollY());
        // Move the map
        hScroll.scrollTo(x, y);
        vScroll.scrollTo(x, y);
        System.out.println("SCALED X,Y: " + x + ", " + y);
    }

    /**
     * Draw the route passed on the map.
     * @param route is the route to be drawn on the map
     */
    private void drawRoute(List<Place> route) {
        // Clear canvas
        routeCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
        // Initialize paint and set paint settings
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getColor(R.color.dodger_blue));
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        // Initialize path
        Path path = new Path();
        // Get iterator over route
        ListIterator<Place> it = route.listIterator();

        if (it.hasNext()) {
            Place p = it.next();
            float scaledX = p.getX();
            float scaledY = p.getY();
            if (zoomLevel == 2) {
                scaledX /= SCALE_ZOOM_LEVEL_TWO;
                scaledY = (scaledY / SCALE_ZOOM_LEVEL_TWO) - VERTICAL_OFFSET_ZOOM_LEVEL_TWO;
                paint.setStrokeWidth(10);
            } else if (zoomLevel == 1) {
                scaledX /= SCALE_ZOOM_LEVEL_ONE;
                scaledY = (scaledY / SCALE_ZOOM_LEVEL_ONE) - VERTICAL_OFFSET_ZOOM_LEVEL_ONE;
                paint.setStrokeWidth(5);
            } else if (zoomLevel == 0) {
                scaledX /= SCALE_ZOOM_LEVEL_ZERO;
                scaledY = (scaledY / SCALE_ZOOM_LEVEL_ZERO) - VERTICAL_OFFSET_ZOOM_LEVEL_ZERO;
                paint.setStrokeWidth(2);
            }
            path.moveTo(scaledX, scaledY);
        }
        // Set rest of path
        while (it.hasNext()) {
            Place p = it.next();

            // Scale point and path based on zoom level
            float scaledX = p.getX();
            float scaledY = p.getY();
            if (zoomLevel == 2) {
                scaledX /= SCALE_ZOOM_LEVEL_TWO;
                scaledY = (scaledY / SCALE_ZOOM_LEVEL_TWO) - VERTICAL_OFFSET_ZOOM_LEVEL_TWO;
            } else if (zoomLevel == 1) {
                scaledX /= SCALE_ZOOM_LEVEL_ONE;
                scaledY = (scaledY / SCALE_ZOOM_LEVEL_ONE) - VERTICAL_OFFSET_ZOOM_LEVEL_ONE;
            } else if (zoomLevel == 0) {
                scaledX /= SCALE_ZOOM_LEVEL_ZERO;
                scaledY = (scaledY / SCALE_ZOOM_LEVEL_ZERO) - VERTICAL_OFFSET_ZOOM_LEVEL_ZERO;
            }

            path.lineTo(scaledX, scaledY);
            path.moveTo(scaledX, scaledY);
        }
        // Close path
        path.close();

        // Draw path
        routeCanvas.drawPath(path, paint);
        // Invalidate view so that next draw clears view
        routeView.invalidate();
    }

    /**
     * Convert the given number in pixels to dp.
     *
     * @param px value to be converted
     * @return converted px value in terms of dp
     */
    private float pxToDP(float px) {
        return px * ((float) getApplicationContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Convert the given number in dp to pixels.
     *
     * @param dp value to be converted
     * @return converted dp value in terms of px
     */
    private float dpToPX(float dp) {
        return dp / ((float) getApplicationContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}