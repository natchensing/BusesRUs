package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {

    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }

    /**
     * Parse route data from the file and add all route to the route manager.
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }

    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param jsonResponse string encoding JSON data to be parsed
     * @throws JSONException             when JSON data does not have expected format
     * @throws RouteDataMissingException when
     *                                   <ul>
     *                                   <li> JSON data is not an array </li>
     *                                   <li> JSON data is missing RouteNo, Name, or Patterns elements for any route</li>
     *                                   <li> The value of the Patterns element is not an array for any route</li>
     *                                   <li> JSON data is missing PatternNo, Destination, or Direction elements for any route pattern</li>
     *                                   <p>
     *                                   </ul>
     */

    public void parseRoutes(String jsonResponse) throws JSONException, RouteDataMissingException {
        JSONArray routes = new JSONArray(jsonResponse);

        for (int i = 0; i < routes.length(); i++) {
            JSONObject route = routes.getJSONObject(i);
            parseRoute(route);
        }
    }


    private void parseRoute(JSONObject route) throws RouteDataMissingException {
        try {
            if (!route.has("RouteNo") || !route.has("Name") || !route.has("Patterns")) {
                throw new RouteDataMissingException("Missing Route Data");
            }
            String name = route.getString("Name");
            JSONArray patterns = route.getJSONArray("Patterns");
            String routeNo = route.getString("RouteNo");
            Route r = RouteManager.getInstance().getRouteWithNumber(routeNo, name);
            for (int i = 0; i < patterns.length(); i++) {
                JSONObject rp = patterns.getJSONObject(i);
                if (!rp.has("Destination") ||
                        !rp.has("Direction") ||
                        !rp.has("PatternNo")) {
                    throw new RouteDataMissingException("No");
                }

                String destination = rp.getString("Destination");
                String direction = rp.getString("Direction");
                String patternNo = rp.getString("PatternNo");

                r.getPattern(patternNo, destination, direction);
            }
        } catch (JSONException je) {
            throw new RouteDataMissingException("Route Data Missing!");
        }
    }
}

