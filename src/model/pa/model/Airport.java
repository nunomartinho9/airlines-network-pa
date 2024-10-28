package model.pa.model;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;

/**
 * This class represents the Airport
 */
public class Airport implements Comparable<Airport>{
    private String name;
    private String abreviation;
    private Weight weight;
    private int x, y;

    /**
     *
     * @param _name of the Airport
     * @param _abreviation of the Airport
     * @param _latitude of the Airport
     * @param _longitude of the Airport
     * @param _altitude of the Airport
     */
    public Airport(String _name, String _abreviation, double _latitude, double _longitude, int _altitude){
        name = _name;
        abreviation = _abreviation;
        weight = new Weight(_latitude,  _longitude,  _altitude);
        x = 0;
        y = 0;
    }

    /**
     *
     * @param _name of the Airport
     * @param _abreviation of the Airport
     * @param _latitude of the Airport
     * @param _longitude of the Airport
     * @param _altitude of the Airport
     * @param x of the Airport
     * @param y of the Airport
     */
    public Airport(String _name, String _abreviation, double _latitude, double _longitude, int _altitude, int x, int y){
        name = _name;
        abreviation = _abreviation;
        weight = new Weight(_latitude,  _longitude,  _altitude);
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param _name of the Airport
     * @param _abreviation of the Airport
     * @param x of the Airport
     * @param y of the Airport
     */
    public Airport(String _name, String _abreviation, int x, int y){
        name = _name;
        abreviation = _abreviation;
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return the value of x
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the value of y
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param o
     * @return
     * the value 0 if the abreviation is equal ;
     * a value less than 0 if the abreviation less ;
     * and a value greater than 0 if the argument is greater.
     */
    @Override
    public int compareTo(Airport o) {
        return abreviation.compareTo(o.abreviation);
    }

    /**
     * Represents the weight of the airport
     */
    private class Weight{
        private double latitude;
        private double longitude;
        private int altitude;

        /**
         *
         * @param _latitude of the weight
         * @param _longitude of the weight
         * @param _altitude of the weight
         */
        private Weight(double _latitude, double _longitude, int _altitude){
            latitude = _latitude;
            longitude = _longitude;
            altitude = _altitude;
        }
    }

    /**
     *
     * @return the abreviation of the airport
     */
    public String getAbreviation() {
        return abreviation;
    }

    /**
     *
     * @return the weight of the airport
     */
    public Weight getWeight() {
        return weight;
    }

    /**
     *
     * @return the name of the airport
     */
    @SmartLabelSource
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return " " + abreviation;
    }
}
