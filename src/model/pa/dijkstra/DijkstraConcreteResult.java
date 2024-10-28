package model.pa.dijkstra;

import java.util.List;
import model.pa.graph.Vertex;

public class DijkstraConcreteResult <V> {
    private double dist;
    private List<Vertex<V>> path;

    public DijkstraConcreteResult(double dist, List<Vertex<V>> path) {
        this.dist = dist;
        this.path = path;
    }

    public double getCost() {
        return dist;
    }

    public List<Vertex<V>> getPath() {
        return path;
    }

    @Override
    public String toString(){
        return "Dijkstra: { Distance: "+ dist + ", Path: " + path +"}";
    }
}
