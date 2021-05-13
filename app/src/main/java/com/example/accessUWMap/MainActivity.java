package com.example.accessUWMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.MotionEvent;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

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


    ////////////////////////////////////////////////////////////
    ///     Fields
    ////////////////////////////////////////////////////////////

    // Screen dimensions
    private int screenWidth;
    private int screenHeight;

    // Current state app is in
    private AppStates mState;

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

    // Views for displaying building description
    private LinearLayout buildDescLayout;

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
    private Set<String> allBuildingNames; // Names of buildings
    private List<LocationSearchResult> searchableLocations; // Set of search result objects

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
        // and initialize mx,my starting coordinates and userLastMapTouch coordinates
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);
        mx = 0;
        my = 0;

        // Set up search bar layout and listeners
        startSearchBarLayout = findViewById(R.id.startSearchBarLayout);
        endSearchBarAndSwapLayout = findViewById(R.id.endSearchBarAndSwapLayout);
        routeFilterLayout = findViewById(R.id.routeFilterButtonsLayout);

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
        Bitmap routeBitmap = Bitmap.createBitmap(CAMPUS_MAP_IMAGE_WIDTH_PX, CAMPUS_MAP_IMAGE_HEIGHT_PX,
                Bitmap.Config.ARGB_8888);
        routeView.setImageBitmap(routeBitmap);
        // Initialize canvas from bitmap
        routeCanvas = new Canvas(routeBitmap);
    }

    /**
     * Initialize the search results to populate the AutoCompleteTextView start and end location
     * search bars.
     */
    private void initSearchResults() {
        searchableLocations = new ArrayList<>();

        // Acquire list of all buildings on campus
        allBuildingNames = CampusPresenter.getAllBuildingNames();

        for (String currLocation : allBuildingNames) {
            searchableLocations.add(new LocationSearchResult(currLocation));
        }
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
     * Select closest building to where the user touched as the start location (if in SEARCH or
     * FOUND_START states) or the end location (if in BUILD_ROUTE state)
     *
     * @param x is the x coordinate (in pixels) on the map of where the user pressed
     * @param y is the y coordinate (in pixels) on the map of where the user pressed
     */
    private void clickChooseBuilding(int x, int y) {
        // Adjusts invalid input
        if (x < 0) {
            x = 0;
        } else if (x > CAMPUS_MAP_IMAGE_WIDTH_PX) {
            x = CAMPUS_MAP_IMAGE_WIDTH_PX;
        }
        if (y < 0) {
            y = 0;
        } else if (y > CAMPUS_MAP_IMAGE_HEIGHT_PX) {
            y = CAMPUS_MAP_IMAGE_HEIGHT_PX;
        }

        // Selects nearest start or end building (depending on current app state) based on where the user clicked
        if (mState == AppStates.SEARCH || mState == AppStates.FOUND_START) {
            String closestStartBuilding = CampusPresenter.getClosestBuilding(x, y);
            System.out.println("User click coords: " + x + ", " + y);
            System.out.println("CLOSE: " + closestStartBuilding);
        } else if (mState == AppStates.BUILD_ROUTE) {
            String closestEndBuilding = CampusPresenter.getClosestBuilding(x, y);
        }
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
        moveMapToBuilding(CampusPresenter.getCurrentStart());
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
        moveMapToBuilding(CampusPresenter.getCurrentEnd());
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
                    buildBuildingDesc();
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

    /**
     * Moves map view to see the start location.
     *
     * @param longBuildingName is the long version of the desired building name to center the map on.
     */
    private void moveMapToBuilding(String longBuildingName) {
        // Get coordinate of roughly the center of the start building
        Point roughCenter = CampusPresenter.getRoughCenterOfBuilding(longBuildingName);
        // Convert roughCenter coordinates from px to dp
        float centerX = pxToDP(roughCenter.x);
        float centerY = pxToDP(roughCenter.y);
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
        int scrollX = (int) (centerX - horizontalOffset);
        int scrollY = (int) (centerY - verticalOffset);
        // Scroll map views
        hScroll.scrollTo(scrollX, scrollY);
        vScroll.scrollTo(scrollX, scrollY);
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
            path.moveTo(p.getX(), p.getY());
        }
        // Set rest of path
        while (it.hasNext()) {
            Place p = it.next();
            path.lineTo(p.getX(), p.getY());
            path.moveTo(p.getX(), p.getY());
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