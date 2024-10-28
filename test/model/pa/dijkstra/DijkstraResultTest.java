package model.pa.dijkstra;

import model.pa.graph.GraphAdjacencyMatrix;
import model.pa.graph.Vertex;
import model.pa.model.Airport;
import model.pa.model.AirportNetwork;
import model.pa.model.Route;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.util.LinkedList;

class DijkstraResultTest {

    AirportNetwork an;
    DijkstraResult<Airport> dk;
    GraphAdjacencyMatrix<Airport, Route> graph;

    @BeforeEach
    void setUp() {
        an = new AirportNetwork();
        dk = new DijkstraResult<>(an.getNetwork());
        graph = new GraphAdjacencyMatrix<>();

        Vertex<Airport> a1 = an.addAirport(new Airport("Airport 1", "A1", 3, 2));
        Vertex<Airport> a2 = an.addAirport(new Airport("Airport 2", "A2", 0, 0));
        Vertex<Airport> a3 = an.addAirport(new Airport("Airport 3", "A3", 2, 2));
        Vertex<Airport> a4 = an.addAirport(new Airport("Airport 4", "A4", 1, 10));
        Vertex<Airport> a5 = an.addAirport(new Airport("Airport 5", "A5", 5, 0));
        Vertex<Airport> a6 = an.addAirport(new Airport("Airport 6", "A6", 5, 0));
        Vertex<Airport> a7 = an.addAirport(new Airport("Airport 7", "A7", 5, 0));

        an.addRoute(a1, a2, 1);
        an.addRoute(a3, a4, 1);
        an.addRoute(a1, a6, 5);
        an.addRoute(a6, a5, 2);
        an.addRoute(a7, a5, 2);
        an.addRoute(a2, a5, 3);
        an.addRoute(a3, a5, 3);
        an.addRoute(a4, a7, 5);
    }

    @Test
    void dijkstraTest() {
        Vertex<Airport> a1 = an.findAirport("Airport 1");
        Vertex<Airport> a2 = an.findAirport("Airport 2");
        Vertex<Airport> a3 = an.findAirport("Airport 3");
        Vertex<Airport> a4 = an.findAirport("Airport 4");
        Vertex<Airport> a5 = an.findAirport("Airport 5");
        Vertex<Airport> a6 = an.findAirport("Airport 6");
        Vertex<Airport> a7 = an.findAirport("Airport 7");

        LinkedList<Vertex<Airport>> list = new LinkedList<>();
        list.add(an.findAirport("Airport 1"));
        list.add(an.findAirport("Airport 2"));
        list.add(an.findAirport("Airport 5"));
        list.add(an.findAirport("Airport 3"));
        list.add(an.findAirport("Airport 4"));
        System.out.println(dk.dijkstra(a1, a4));
        assertEquals(list, dk.dijkstra(a1, a4).getPath());
        assertEquals(8, dk.dijkstra(a1, a4).getCost());
    }
}