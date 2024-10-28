package model.pa.model;

import java.util.Objects;

public class Route implements Comparable<Route> {
    private Airport origin;
    private Airport destination;
    private int distance;

    public Route(Airport _origin, Airport _destination,int _distance ){
        origin = _origin;
        destination = _destination;
        distance = _distance;
    }

    public int getDistance() { return distance; }

    public Airport getOrigin() { return origin; }

    public Airport getDestination() { return destination; }

    @Override
    public int compareTo(Route o) {
        return Integer.compare(o.getDistance(), distance);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return origin.equals(route.origin) && destination.equals(route.destination) || origin.equals(route.destination) && destination.equals(route.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination);
    }

    @Override
    public String toString() {
        return "" + distance + "Km - " + "["+origin + ", "+ destination +"]";
    }
}
