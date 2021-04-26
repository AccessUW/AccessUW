package com.example.accessUWMap;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Coords for scrolling in map view
    private float mx, my;
    private float curX, curY;
    // Scroll views for moving on the map
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    // Search bar variables
    SearchView searchStartView;
    String startLocation;
    String endLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Define params for scrollable map view
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);

        // Set click listener for lower-right button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        searchStartView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("THIS TRIGGERS WHEN YOU OPEN SEARCH BAR (maybe this could toggle visibility of suggested results)");
            }
        });
        searchStartView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                System.out.println("SUBMIT: " + s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String updatedText) {
                System.out.println("NEW TEXT: " + updatedText);

                // FROM HERE, reference SearchListSuggestions' getTopResults
                // Make list underneath in Scrollable/LinearView of those suggestions as they type
                // Also add Hint text to search bar
                // SetSearchableInfo above????
                // Look into SearchManager and searchManager.triggerSearch()

                // These following lines may be useful above outside of the QueryTextListener:
                // - Anything with searchStartMenuItem (searchStartMenuItem = menu.findItem(R.id.searchStart);)
                //    - searchStartView = (SearchView) searchStartMenuItem.getActionView();
//                MenuInflater inflater = getMenuInflater();
//                inflater.inflate(R.menu.search_menu, menu);

                // Do you need to check state? Like have State.VIEWING or State.MAPPING or something?
                // Check menu or maybe color picker assignments?

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
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }

        return true;
    }
}