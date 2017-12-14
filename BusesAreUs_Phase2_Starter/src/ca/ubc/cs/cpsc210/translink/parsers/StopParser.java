package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }

    /**
     * Parse stop data from the file and add all stops to stop manager.
     */
    public void parse() throws IOException, StopDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }

    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param jsonResponse string encoding JSON data to be parsed
     * @throws JSONException            when JSON data does not have expected format
     * @throws StopDataMissingException when
     *                                  <ul>
     *                                  <li> JSON data is not an array </li>
     *                                  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop
     *                                  </li>
     *                                  </ul>
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        JSONArray stops = new JSONArray(jsonResponse);

        for (int i = 0; i < stops.length(); i++) {
            JSONObject stop = stops.getJSONObject(i);
            parseStop(stop);
        }
    }

    private void parseStop(JSONObject stop) throws StopDataMissingException, JSONException {
        if (!stop.has("StopNo") ||
                !stop.has("Name") ||
                !stop.has("Latitude") ||
                !stop.has("Longitude") ||
                !stop.has("Routes")) {
            throw new StopDataMissingException("Stop Data Missing!");
        }
        StopManager sm = StopManager.getInstance();
        RouteManager rm = RouteManager.getInstance();
        int stopNo = stop.getInt("StopNo");
        String name = stop.getString("Name");
        double lat = stop.getDouble("Latitude");
        double lon = stop.getDouble("Longitude");
        LatLon latLon = new LatLon(lat, lon);
        String routeNames = stop.getString("Routes");
        String[] routesArray = routeNames.split(", ");

        Stop stopObj = sm.getStopWithId(stopNo, name, latLon);
        for (int i = 0; i < routesArray.length; i++) {
            Route r = rm.getRouteWithNumber(routesArray[i].trim());
            stopObj.addRoute(r);
            r.addStop(stopObj);
        }
    }

}
