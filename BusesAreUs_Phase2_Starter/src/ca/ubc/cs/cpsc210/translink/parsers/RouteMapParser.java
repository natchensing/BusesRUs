package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     *
     * @param str
     */
    private void parseOnePattern(String str) {
        String[] splitstr = str.split(";");
        List<LatLon> elements = new ArrayList<LatLon>();
        String[] splitDash = splitstr[0].split("-");
        if (splitDash[0].charAt(0) == 'N') {
            String routeNumber = splitDash[0].substring(1);
            String patternName = splitstr[0].substring(splitstr[0].indexOf("-") + 1, splitstr[0].length());
            for (int i = 1; i < splitstr.length - 1; i += 2) {
                if (isDouble(splitstr[i]) && isDouble(splitstr[i + 1])) {
                    double lat = Double.parseDouble(splitstr[i]);
                    double lon = Double.parseDouble(splitstr[i + 1]);
                    LatLon element = new LatLon(lat, lon);
                    elements.add(element);
                }
            }
            storeRouteMap(routeNumber, patternName, elements);
        }

    }

    private static boolean isDouble(String x) {
        try {
            Double.parseDouble(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber the number of the route
     * @param patternName the name of the pattern
     * @param elements    the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}
