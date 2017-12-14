package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when JSON response does not have expected format
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        int count = 0;
        for (Arrival a : stop) {
            count++;
        }
        JSONArray arrivals = new JSONArray(jsonResponse);
        for (int i = 0; i < arrivals.length(); i++) {
            JSONObject arrival = arrivals.getJSONObject(i);
            parseArrival(stop, arrival);
        }
        int count1 = 0;
        for (Arrival a : stop) {
            count1++;
        }
        if (count1 == count)
            throw new ArrivalsDataMissingException("No Arrivals");
    }

    private static void parseArrival(Stop stop, JSONObject arrival) throws JSONException, ArrivalsDataMissingException {
        try {

            JSONArray schedules = arrival.getJSONArray("Schedules");
            RouteManager rm = RouteManager.getInstance();
            for (int i = 0; i < schedules.length(); i++) {
                JSONObject object = schedules.getJSONObject(i);
                if (object.has("ExpectedCountdown") &&
                        object.has("Destination") &&
                        object.has("ScheduleStatus")) {
                    int expectedCountdown = object.getInt("ExpectedCountdown");
                    String destination = object.getString("Destination");
                    String scheduleStatus = object.getString("ScheduleStatus");
                    String routeNo = arrival.getString("RouteNo");
                    String routeName = arrival.getString("RouteName");

                    Route r = rm.getRouteWithNumber(routeNo, routeName);
                    Arrival a = new Arrival(expectedCountdown, destination, r);
                    a.setStatus(scheduleStatus);
                    stop.addArrival(a);
                    stop.addRoute(r);
                    r.addStop(stop);
                }
            }
        } catch (JSONException js) {
        }
    }
}
