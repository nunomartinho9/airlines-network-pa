package model.pa.dijkstra;

import java.util.*;
import model.pa.graph.GraphAdjacencyMatrix;
import model.pa.graph.Graph;
import model.pa.graph.Vertex;
import model.pa.graph.Edge;
import model.pa.model.Airport;
import model.pa.model.Route;

public class DijkstraResult<V> {
    private Graph<V, Route> network;

    public Map<Vertex<V>, Double> minDist;
    public Map<Vertex<V>, Vertex<V>> predecessors;

    public DijkstraResult(Graph<V, Route> network) {
        this.network = network;
        this.minDist = new HashMap<>();
        this.predecessors = new HashMap<>();
    }

    //Algoritmo Dijkstra
    public DijkstraConcreteResult<V> dijkstra (Vertex<V> start, Vertex<V> end){
        DijkstraResult<V> dijkstraResult = new DijkstraResult<>(network);
        List<Vertex<V>> unvisited = new ArrayList<>();

        for(Vertex<V> h : network.vertices()){
            unvisited.add(h);
            dijkstraResult.predecessors.put(h, null);
            dijkstraResult.minDist.put(h, Double.MAX_VALUE);
        }

        dijkstraResult.minDist.put(start, 0.0);
        GraphAdjacencyMatrix<V, Route> network = (GraphAdjacencyMatrix<V, Route>) this.network;

        unvisited.removeIf(v -> network.incidentEdges(v).isEmpty());
        //unvisited.removeIf(v -> !network.DFS(start).contains(v));

        while(!unvisited.isEmpty()){
            Vertex<V> currentH = findMinDistVertex(dijkstraResult.minDist, unvisited);
            for (Edge<Route, V> e : this.network.incidentEdges(currentH)) {
                Vertex<V> oppositeH = this.network.opposite(currentH, e);

                if (unvisited.contains(oppositeH)) {
                    double currentDist = dijkstraResult.minDist.get(currentH);
                    double edgeDist = e.element().getDistance();
                    double totalDist = currentDist + edgeDist;

                    if (totalDist < dijkstraResult.minDist.get(oppositeH)) {
                        dijkstraResult.minDist.put(oppositeH, totalDist);
                        dijkstraResult.predecessors.put(oppositeH, currentH);
                    }
                }
            }
            unvisited.remove(currentH);
        }
        Double dist = dijkstraResult.minDist.get(end);

        if(dist == Double.MAX_VALUE){
            return new DijkstraConcreteResult<>(dist, null);
        }
        List<Vertex<V>> path = new ArrayList<>();
        Vertex<V> current = end;

        while(current != start){
            path.add(current);
            current = dijkstraResult.predecessors.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return new DijkstraConcreteResult<>(dist, path);
    }

    public Vertex<V> findMinDistVertex(Map<Vertex<V>, Double> distances, List<Vertex<V>> unvisited){
        Vertex<V> minDistVertex = null;
        double minDistValue = Double.MAX_VALUE;

        for(Vertex<V> v : unvisited){
            Double distV = distances.get(v);
            if(distV < minDistValue){
                minDistValue = distV;;
                minDistVertex = v;
            }
        }
        return minDistVertex;
    }

    @Override
    public String toString() {
        return "DijkstraResult{" +
                "network=" + network +
                ", minDist=" + minDist +
                ", predecessors=" + predecessors +
                '}';
    }
}
