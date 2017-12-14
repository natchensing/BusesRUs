package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the StopManager
 */
public class TestStopManager {

    @Before
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testBoring() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithId(9999);
        assertEquals(s9999, r);
    }

    @Test
    public void testGetNearest() {
        LatLon latLon = new LatLon(-49.2, 123.2);
        Stop s9999 = new Stop(9999, "My house", latLon);
        StopManager sm = StopManager.getInstance();
        sm.getStopWithId(9999, "Myhouse", latLon);
        assertEquals(s9999, sm.findNearestTo(latLon));
    }

}
