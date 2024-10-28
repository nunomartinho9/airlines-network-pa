package model.pa.dataLoader;

import java.util.List;

/**
 * Data class with all the dataset information
 */
public class Dataset {

    private List<String> iata, city, start, destination, names;
    private List<Integer> distance, x, y, alt;
    private List<Double> lat, longitude;

    public Dataset(List<String> iata, List<String> city, List<String> start, List<String> destination, List<String> names, List<Integer> distance, List<Integer> x, List<Integer> y, List<Integer> alt, List<Double> lat, List<Double> longitude) {
        this.iata = iata;
        this.city = city;
        this.start = start;
        this.destination = destination;
        this.names = names;
        this.distance = distance;
        this.x = x;
        this.y = y;
        this.alt = alt;
        this.lat = lat;
        this.longitude = longitude;
    }

    public List<String> getIata() {
        return iata;
    }

    public List<String> getCity() {
        return city;
    }

    public List<String> getStart() {
        return start;
    }

    public List<String> getDestination() {
        return destination;
    }

    public List<String> getNames() {
        return names;
    }

    public List<Integer> getDistance() {
        return distance;
    }

    public List<Integer> getX() {
        return x;
    }

    public List<Integer> getY() {
        return y;
    }

    public List<Integer> getAlt() {
        return alt;
    }

    public List<Double> getLat() {
        return lat;
    }

    public List<Double> getLongitude() {
        return longitude;
    }
}
