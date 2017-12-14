package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.RouteMapParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the parser for route pattern map information
 */

// TODO: Write more tests

public class TestRouteMapParser {
    @Before
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    private int countNumRoutePatterns() {
        int count = 0;
        for (Route r : RouteManager.getInstance()) {
            for (RoutePattern rp : r.getPatterns()) {
                count ++;
            }
        }
        return count;
    }

//    @Test
//    public void testSample() {
//        RouteMapParser p = new RouteMapParser("sample");
//        p.parse();
//        assertEquals(6, countNumRoutePatterns());
//    }

    @Test
    public void testRouteParserNormal() {
        RouteMapParser p = new RouteMapParser("allroutemaps.txt");
        p.parse();
        assertEquals(1232, countNumRoutePatterns());
    }

    @Test
    public void testRouteParser() {
        RouteMapParser p = new RouteMapParser("allroutemaps.txt");
        p.parse();
        RouteManager.getInstance().getRouteWithNumber("006").getPattern("EB3");
    }
}
