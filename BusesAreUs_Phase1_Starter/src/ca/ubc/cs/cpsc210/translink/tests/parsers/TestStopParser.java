package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.ArrivalsParser;
import ca.ubc.cs.cpsc210.translink.parsers.StopParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the StopParser
 */

// TODO: Write more tests

public class TestStopParser {
    @Before
    public void setup() {
        StopManager.getInstance().clearStops();
        RouteManager.getInstance().clearRoutes();

    }

    @Test
    public void testStopParserNormal() throws StopDataMissingException, JSONException, IOException {
        StopParser p = new StopParser("stops.json");
        p.parse();
        assertEquals(9232, StopManager.getInstance().getNumStops());
    }

    @Test
    public void testRouteContainsStops() throws StopDataMissingException, JSONException, IOException {
        StopParser p = new StopParser("sampleStops");
        p.parse();
        assertEquals(4, StopManager.getInstance().getNumStops());
        LatLon latLon = new LatLon(49.27755, -123.12698);
        assertEquals(2, StopManager.getInstance().getStopWithId(50011, "EB DAVIE ST FS HOWE ST", latLon).getRoutes().size());
    }

    @Test
    public void testArrivals() throws JSONException, ArrivalsDataMissingException, IOException {
        LatLon latLon = new LatLon(49.27755, -123.12698);
        Stop s = StopManager.getInstance().getStopWithId(025, "BRENTWOOD STN/UBC             ", latLon);
        DataProvider dataProvider = new FileDataProvider("arrivals.json");
        String json = dataProvider.dataSourceToString();
        ArrivalsParser.parseArrivals(s, json);
        assertEquals(1, StopManager.getInstance().getNumStops());
        assertEquals(7, RouteManager.getInstance().getNumRoutes());
        assertEquals(7, s.getRoutes().size());
    }
}
