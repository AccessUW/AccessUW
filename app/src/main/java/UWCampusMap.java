import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UWCampusMap represents an immutable undirected graph of the UW campus map. UWCampusMap has
 * a series of interconnected non-null nodes (of String type) labeling building entrances and
 * different spots on campus, with edges representing paths between 2 points on campus. This,
 * all together, forms a map.
 */
public class UWCampusMap {

    /**
     * Constructs the representation of the UW Campus
     */
    public void initUWCampusMap() {

    }

    /**
     * Obtain a list of potential search options that include the user's input in the search bar.
     *
     * @param currInput is the current text the user has typed into the search bar
     * @return a list of results that contain the user's input
     */
    public ArrayList<String> getSearchResults(String currInput) {




        return new ArrayList<>(Collections.singleton("Sample list of results for: " + currInput));
    }

    /**
     * Finds the shortest path between the given start and end buildings, and filters for
     * wheelchair accessibility and whether stairs are okay accordingly.
     *
     * @param startShortName is the short name for the building at the start of the route.
     * @param endShortName is the short name for the building at the end of the route.
     * @param wheelchairAccess toggles the need for wheelchair-accessible routes or not
     * @param noStairs toggles the need for no stairs on the route or not
     *
     * @return a representation of the shortest path between the given buildings
     */
    public List<String> findShortestPath(String startShortName, String endShortName,
                                         boolean wheelchairAccess, boolean noStairs) {
        return null;
    }

    /**
     * Obtains the short name of the nearest building to where the user touched on the map.
     *
     * @param x coord. (in pixels) of where the user touched on the map.
     * @param y coord. (in pixels) of where the user touched on the map.
     *
     * @return short name of the nearest building
     */
    public String getNearestBuilding(int x, int y) {
        return "";
    }

    /**
     * Returns the long name of a building given its short name.
     */
    public String getLongName(String shortName) {
        return "";
    }

    /**
     * Returns the short name of a building given its long name.
     */
    public String getShortName(String longName) {
        return "";
    }

    /**
     * Returns the description of a building (including bathrooms, accessibility info, etc.)
     * given its short name.
     */
    public String getDescOfBuilding(String shortName) {
        return "";
    }
}
