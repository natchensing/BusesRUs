package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the RouteManager
 */
public class TestRouteManager {

    @Before
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    public void testConstructor() {
        Route r43 = new Route("43");
        RouteManager rm = RouteManager.getInstance();
        Route r = RouteManager.getInstance().getRouteWithNumber("43");
        assertEquals(r43, r);
    }

    @Test
    public void testRoutePattern() {
        Route r43 = new Route("43");
        RoutePattern routePattern = new RoutePattern("North", "UBC", "North", null);
        r43.addPattern(routePattern);
        assertEquals(routePattern, r43.getPattern("North"));
        RoutePattern expected = new RoutePattern("North", "Downtown", "West", null);
        assertEquals(expected, r43.getPattern("North", "Downtown", "West"));
    }

}
