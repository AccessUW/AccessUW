import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the presenter component of the Model-Presenter-View framework.
 * It mainly acts as the ground truth for current selected start/end locations by the user
 * and what boxes are toggled.
 */
public class CampusPresenter {

    ////////////////////////////////////////////////////////////////////////////
    ///     Fields                                                           ///
    ////////////////////////////////////////////////////////////////////////////
    private static Place currentStart; // Start location of route
    private static Place currentEnd; // End location of route
    private static List<Place> currentRoute; // Route as list of places in order
    private static boolean wheelchair; // Toggle whether route needs to be wheelchair-accessible
    private static boolean noStairs; // Toggle whether route can have stairs
    private static boolean assistedEntrance; // Toggle whether an assisted entrance is required
    private static List<String> recentLocations; // Recently searched locations
    private static List<String> buildingNames;

    /**
     * Takes a short building name and updates currentStart.
     *
     * @param startBuildingName is short building name for start location
     * @requires startBuildingName has length of >= 1 and is a valid building name
     */
    public static void updateStart(String startBuildingName) {

    }

    /**
     * Takes a short building name and updates currentEnd.
     *
     * @param endBuildingName is short building name for end location
     * @requires endBuildingName has length of >= 1 and is a valid building name
     */
    public static void updateEnd(String endBuildingName) {

    }

    /**
     * Takes a short building name and returns (x,y) coordinate of that place as a Point.
     * The (x,y) coordinates are in terms of pixels on the map image.
     *
     * @param buildingName is short building name for given location
     * @requires buildingName has length of >= 1 and is a valid building name
     *
     * @return point representing (x,y) coordinates of the building in terms of pixels on the map.
     */
    public static Point getPlace(String buildingName) {
        return null;
    }

    /**
     * Returns route from currentStart to currentEnd positions. If either currentStart or
     * currentEnd is not set, this function returns null.
     *
     * @return list of places in order representing route from start to finish
     */
    public static List<Place> getRoute() {
        if (currentStart == null || currentEnd == null) {
            return null;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Swaps the current start and end locations, and also reverses the current route.
     */
    public static void swapStartAndEnd() {

    }

    /**
     * Updates the wheelchair field to dictate whether the route needs to be wheelchair-accessible
     * or not.
     *
     * @param toggledWC is whether the wheelchair-accessible route filter has been checked (e.g.
     *                'true' means the route MUST be wheelchair-accessible)
     */
    public static void updateWheelchair(boolean toggledWC) {

    }

    /**
     * Updates the noStairs field to dictate whether the route can have stairs.
     *
     * @param toggledNS is whether the route must not have stairs (e.g. 'true' means the route
     *                  MUST NOT have stairs on it)
     */
    public static void updateNoStairs(boolean toggledNS) {

    }

    /**
     * Updates the assistedEntrance field to dictate whether the route must lead to an assisted
     * entrance.
     *
     * @param toggledAE is whether the route must end at an assisted entrance (e.g. 'true' means
     *                  the route MUST end at an assisted entrance)
     */
    public static void updateAssistedEntrance(boolean toggledAE) {

    }

    /**
     * Finds the closest building to the given (x,y) coordinates and returns its description.
     *
     * @param x coordinate (in pixels on map image)
     * @param y coordinate (in pixels on map image)
     * @requires x >= 0 && x <= width (in pixels) of map image
     * @requires y >= 0 && y <= height (in pixels) of map image
     *
     * @return description of building closest to the given (x,y) coordinates
     */
    public static String getClosest(int x, int y) {
        return "";
    }

    /**
     * Returns info of the given building, including address, description, and accessibility
     * information.
     *
     * @param buildingName is short building name for given location
     * @requires buildingName has length of >= 1 and is a valid building name
     *
     * @return description of building with the given short name
     */
    public static String getInfo(String buildingName) {
        return "";
    }

    /**
     * Finds the building with a gender-neutral bathroom closest to the given (x,y) coordinates
     * and returns its description.
     *
     * @param x coordinate (in pixels on map image)
     * @param y coordinate (in pixels on map image)
     * @requires x >= 0 && x <= width (in pixels) of map image
     * @requires y >= 0 && y <= height (in pixels) of map image
     *
     * @return description of building with a gender-neutral bathroom closest to the given (x,y)
     * coordinates
     */
    public static String getClosestGNBathroom(int x, int y) {
        return "";
    }

    private class Place {
        public Place() {

        }
    }
}
