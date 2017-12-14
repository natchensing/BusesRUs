package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.RouteParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the RouteParser
 */
// TODO: Write more tests

public class TestRouteParser {
    @Before
    public void setup() {
        RouteManager.getInstance().clearRoutes();

    }

    @Test
    public void testRouteParserNormal() throws RouteDataMissingException, JSONException, IOException {
        RouteParser p = new RouteParser("allroutes.json");
        p.parse();
        assertEquals(229, RouteManager.getInstance().getNumRoutes());
    }

    @Test
    public void testPatterns() throws RouteDataMissingException, JSONException, IOException {
        RouteParser p = new RouteParser("sampleJSON routes");
        p.parse();
        Route r = new Route("002");
        RoutePattern rp1 = new RoutePattern("EB3", "BURRARD STN", "EAST", r);
        RoutePattern rp2 = new RoutePattern("EB1", "DOWNTOWN", "EAST", r);
        RoutePattern rp3 = new RoutePattern("EB2", "DOWNTOWN", "EAST", r);
        List<RoutePattern> testlist = new ArrayList<RoutePattern>();
        testlist.add(rp1);
        testlist.add(rp2);
        testlist.add(rp3);
        assertEquals(3, RouteManager.getInstance().getRouteWithNumber("002").getPatterns().size());
    }

    @Test
    public void testLastPatterns() throws RouteDataMissingException, JSONException, IOException {
        RouteParser p = new RouteParser("allroutes.json");
        p.parse();
        RouteManager rm = RouteManager.getInstance();
        assertEquals(5, rm.getRouteWithNumber("002").getPatterns().size());
        assertEquals(2, rm.getRouteWithNumber("N9").getPatterns().size());
    }

//    @Test (expected = RouteDataMissingException.class)
//    public void testMissing() throws RouteDataMissingException, JSONException, IOException {
//        RouteParser p = new RouteParser("sampleJSON routes");
//        p.parse();
//    }
}
