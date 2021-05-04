package com.example.accessUWMap;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter for auto-complete functionality for start and end location search bars.
 */
public class AutoCompleteSearchAdapter extends ArrayAdapter<LocationSearchResult> {
    private List<LocationSearchResult> searchResultsFull; // Full list of unfiltered potential location search results

    public AutoCompleteSearchAdapter(@NonNull Context context, int resource, @NonNull List<LocationSearchResult> searchResults) {
        super(context, resource, searchResults);
        searchResultsFull = new ArrayList<>(searchResults);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return searchResultsFilter;
    }

    // Create and return converted view to represent single search result row
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.search_result_row, parent, false
            );
        }

        // Create TextView for given result
        TextView searchResultText = convertView.findViewById(R.id.search_result_text);
        LocationSearchResult searchResult = getItem(position);

        // As long as the result is non-null, set up search result row with that location's name
        if (searchResult != null) {
            searchResultText.setText(searchResult.getLocationResultName());
        }

        return convertView;
    }

    // Filter that provides autocomplete functionality for search bar
    private Filter searchResultsFilter = new Filter() {
        // Filter all possible locations to get those that match what the user has typed
        @Override
        protected FilterResults performFiltering(CharSequence userInput) {
            FilterResults results = new FilterResults();
            List<LocationSearchResult> suggestions = new ArrayList<>();

            if (userInput == null || userInput.length() == 0) {
                suggestions.addAll(searchResultsFull);
            } else {
                String filterPattern = userInput.toString().toLowerCase().trim();

                for (LocationSearchResult currLocation : searchResultsFull) {
                    if (currLocation.getLocationResultName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(currLocation);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        // Output filtered search results
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        // Get result's location name
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((LocationSearchResult) resultValue).getLocationResultName();
        }
    };
}
