package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.util.*;
import java.util.List;


/**
 * Represents a bus stop with an number, name, location (lat/lon)
 * set of routes which stop at this stop and a list of arrivals.
 */
// TODO: Task 2: Complete all the methods of this class

public class Stop implements Iterable<Arrival> {
    private List<Arrival> arrivals = new ArrayList<>();

    private int number;
    private String name;
    private LatLon locn;
    private Set<Route> routes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stop stop = (Stop) o;

        return number == stop.number;

    }

    @Override
    public int hashCode() {
        return number;
    }

    /**
     * Constructs a stop with given number, name and location.
     * Set of routes and list of arrivals are empty.
     *
     * @param number the number of this stop
     * @param name   name of this stop
     * @param locn   location of this stop
     */
    public Stop(int number, String name, LatLon locn) {
        this.number = number;
        this.name = name;
        this.locn = locn;
        this.routes = new HashSet<Route>();
        this.arrivals = new ArrayList<Arrival>();
    }

    /**
     * getter for name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * getter for locn
     *
     * @return the location
     */
    public LatLon getLocn() {
        return locn;
    }

    /**
     * getter for number
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * getter for set of routes
     *
     * @return the set of routes using this stop
     */
    public Set<Route> getRoutes() {
        return Collections.unmodifiableSet(routes);         //changed to unmod set after sub 2
    }

    /**
     * Add route to set of routes with stops at this stop.
     *
     * @param route the route to add
     */
    public void addRoute(Route route) {
        if (!routes.contains(route)) {
            routes.add(route);
            route.addStop(this);
        }
    }

    /**
     * Remove route from set of routes with stops at this stop
     *
     * @param route the route to remove
     */
    public void removeRoute(Route route) {
        routes.remove(route);
        route.removeStop(this);
    }

    /**
     * Determine if this stop is on a given route
     *
     * @param route the route
     * @return true if this stop is on given route
     */
    public boolean onRoute(Route route) {
        if (route.hasStop(this))
            return true;
        return false;
    }

    /**
     * Add bus arrival travelling on a particular route at this stop.
     * Arrivals are to be sorted in order by arrival time
     *
     * @param arrival the bus arrival to add to stop
     */
    public void addArrival(Arrival arrival) {
        arrivals.add(arrival);
        Collections.sort(arrivals);

//        List<Arrival> arrivalList = new ArrayList<Arrival>();     //edited to be more efficient after sub 5
//        if (arrivals.size() > 0) {
//            for (Arrival a : arrivals) {
//                if (a.compareTo(arrival) >= 0) {
//                    arrivalList.add(a);
//                    arrivalList.add(arrival);
//                }
//                arrivalList.add(a);
    }

//        arrivals.add(arrival);             //changed to current after submission 2


    /**
     * Remove all arrivals from this stop
     */
    public void clearArrivals() {
        arrivals.clear();
    }

    @Override
    public Iterator<Arrival> iterator() {
        // Do not modify the implementation of this method!
        return arrivals.iterator();
    }

    /**
     * setter for name
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter for location
     *
     * @param locn the new location
     */
    public void setLocn(LatLon locn) {
        this.locn = locn;
    }
}
