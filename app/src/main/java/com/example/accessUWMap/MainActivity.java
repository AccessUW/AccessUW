package com.example.accessUWMap;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ////////////////////////////////////////////////////////////
    ///     Constants
    ////////////////////////////////////////////////////////////
    private static final int SEARCH_RESULT_TEXT_MARGIN = 25;

    ////////////////////////////////////////////////////////////
    ///     Fields
    ////////////////////////////////////////////////////////////

    // Coords for scrolling in map view
    private float mx, my;
    // Scroll views for moving on the map
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;
    // Search bar view and layout variables
    SearchView searchStartView;
    LinearLayout searchResultsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get scroll views that control 2-D scrollability (i.e. user can move freely around map)
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);

        // Get search results vertical linear layout
        searchResultsLayout = findViewById(R.id.searchResultsLayout);
        searchResultsLayout.setBackgroundColor(Color.WHITE);

        // Set click listener for lower-right button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Set up search start menu for determining start location of route
        searchStartView = findViewById(R.id.searchStart);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchStartView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchStartView.setSubmitButtonEnabled(true);

        // Set up search query listeners
        searchStartView.setOnSearchClickListener(view ->
                System.out.println("THIS TRIGGERS WHEN YOU OPEN SEARCH BAR (maybe this could toggle visibility of suggested results)"));
        searchStartView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Updates whenever the user submits their typed text
            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println("SUBMIT: " + s);
                return true;
            }

            // Updates whenever the user changes what's in the search text box
            @Override
            public boolean onQueryTextChange(String updatedText) {
                updateSearchResults(updatedText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        System.out.println("ID SELECTED: " + id);

        return super.onOptionsItemSelected(item);
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
     * Given the new text the user has entered, add the most relevant search results to
     * searchResultsLayout and display them to the user.
     *
     * @param newText is the text the user has typed into the search bar
     */
    private void updateSearchResults(String newText) {
        // Clear old results
        searchResultsLayout.removeAllViews();

        // Only display results if user has typed something
        if (newText.length() >= 1) {
            //TODO: Get top search results here instead of default list here
            System.out.println("NEW TEXT: " + newText);
            String[] sampleSearchResults = {"Terry Hall", "Kane Hall", "Odegaard Library", "The HUB", "Yahtzee!!", "Hocus pocus"};

            // Add search results to searchResultsLayout
            for (String currResult : sampleSearchResults) {
                // Create new SearchResultTextView
                TextView result = new TextView(this);
                result.setText(currResult);
                result.setTextColor(getResources().getColor(R.color.purple_700, null));
                result.setTextSize(18);

                // Create layout params for result text view
                LinearLayout.LayoutParams resultLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                resultLayoutParams.setMargins(SEARCH_RESULT_TEXT_MARGIN,SEARCH_RESULT_TEXT_MARGIN,SEARCH_RESULT_TEXT_MARGIN,SEARCH_RESULT_TEXT_MARGIN);
                result.setLayoutParams(resultLayoutParams);

                // Add result text view to linear layout search results
                searchResultsLayout.addView(result);
            }
        }
        // Make list underneath in Scrollable/LinearView of those suggestions as they type
        // Also add Hint text to search bar
        // SetSearchableInfo above????
        // Look into SearchManager and searchManager.triggerSearch()

        // These following lines may be useful above outside of the QueryTextListener:
        // - Anything with searchStartMenuItem (searchStartMenuItem = menu.findItem(R.id.searchStart);)
        //    - searchStartView = (SearchView) searchStartMenuItem.getActionView();
        //                MenuInflater inflater = getMenuInflater();
        //                inflater.inflate(R.menu.search_menu, menu);

    }
}