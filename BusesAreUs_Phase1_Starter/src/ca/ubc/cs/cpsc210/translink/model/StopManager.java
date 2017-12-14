package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import ca.ubc.cs.cpsc210.translink.util.SphericalGeometry;


import java.util.*;

/**
 * Manages all bus stops.
 * <p>
 * Singleton pattern applied to ensure only a single instance of this class that
 * is globally accessible throughout application.
 */
// TODO: Task 2: Complete all the methods of this class

public class StopManager implements Iterable<Stop> {
    public static final int RADIUS = 10000;
    private static StopManager instance;
    // Use this field to hold all of the stops.
    // Do not change this field or its type, as the iterator method depends on it
    private Map<Integer, Stop> stopMap;

    private Stop selected;
    private LatLon defaultLocn = new LatLon(49.2606127, -123.2482097);

    /**
     * Constructs stop manager with empty collection of stops and null as the selected stop
     */
    private StopManager() {
        this.stopMap = new HashMap<Integer, Stop>();
        this.selected = null;
    }

    /**
     * Gets one and only instance of this class
     *
     * @return instance of class
     */
    public static StopManager getInstance() {
        // Do not modify the implementation of this method!
        if (instance == null) {
            instance = new StopManager();
        }

        return instance;
    }

    public Stop getSelected() {
        return selected;
    }

    /**
     * Get stop with given id, creating it if necessary. If it is necessary to create a new stop,
     * then provide it with an empty string as its name, and a default location somewhere in the
     * lower mainland as its location.
     * <p>
     * In this case, the correct name and location of the stop will be provided later
     *
     * @param id the id of this stop
     * @return stop with given id
     */
    public Stop getStopWithId(int id) {
        if (stopMap.containsKey(id)) {
            return stopMap.get(id);
        }
        Stop s = new Stop(id, "", defaultLocn);
        stopMap.put(id, s);
        return s;
    }

    /**
     * Get stop with given id, creating it if necessary, using the given name and latlon
     *
     * @param id the id of this stop
     * @return stop with given id
     */
    public Stop getStopWithId(int id, String name, LatLon locn) {
        if (stopMap.containsKey(id)) {
            return stopMap.get(id);
        }

        Stop s = new Stop(id, name, locn);
        stopMap.put(id, s);
        return s;
    }

    /**
     * Set the stop selected by user
     *
     * @param selected stop selected by user
     * @throws StopException when stop manager doesn't contain selected stop
     */
    public void setSelected(Stop selected) throws StopException {
        if (!stopMap.containsValue(selected)) {                     //changed containsKey to containsValue
            throw new StopException("No such stop: " + selected.getNumber() + " " + selected.getName());
        }
        this.selected = selected;
    }

    /**
     * Clear selected stop (selected stop is null)
     */

    public void clearSelectedStop() {
        this.selected = null;
    }

    /**
     * Get number of stops managed
     *
     * @return number of stops added to manager
     */
    public int getNumStops() {
        return stopMap.size();
    }

    /**
     * Remove all stops from stop manager
     */
    public void clearStops() {
        stopMap.clear();
    }

    /**
     * Find nearest stop to given point.  Returns null if no stop is closer than RADIUS metres.
     *
     * @param pt point to which nearest stop is sought
     * @return stop closest to pt but less than 10,000m away; null if no stop is within RADIUS metres of pt
     */
    public Stop findNearestTo(LatLon pt) {
        Collection<Stop> stops = stopMap.values();
        Stop closestStop = null;
        double radius = RADIUS;
        for (Stop s : stops) {

            if (s.getLocn() == pt) {
                closestStop = s;
            }
            LatLon latlon = s.getLocn();
            double dst = SphericalGeometry.distanceBetween(pt, latlon);
            if (dst <= radius) {
                radius = SphericalGeometry.distanceBetween(pt, latlon);
                closestStop = s;

            }
        }
        return closestStop;
    }

    @Override
    public Iterator<Stop> iterator() {
        // Do not modify the implementation of this method!
        return stopMap.values().iterator();
    }
}
