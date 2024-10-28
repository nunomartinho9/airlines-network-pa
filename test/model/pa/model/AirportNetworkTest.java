package model.pa.model;


import command.Command;
import command.CommandHistory;
import command.CopyCommand;
import model.pa.graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirportNetworkTest {

    AirportNetwork a;

    @BeforeEach
    void setUp() {
        a = new AirportNetwork();

        a.addAirport(new Airport("Lisboa", "LIS", 31, 25));
        a.addAirport(new Airport("Milao", "MIL", 11, 60));
        a.addAirport(new Airport("New York", "NY", 7, 1));
        a.addAirport(new Airport("Ankara", "ANK", 81, 5));
        a.addAirport(new Airport("nig2", "nig2", 67, 36));
        a.addAirport(new Airport("Porto", "POR", 67, 36));


        a.addRoute("Lisboa", "Porto", 400);
        a.addRoute("Lisboa", "Milao", 1500);
        a.addRoute("Lisboa", "Ankara", 3000);
        a.addRoute("New York", "Porto", 8000);
        a.addRoute("Milao", "New York", 10000);

    }

    @Test
    void AirportCount() {
        assertEquals(5, a.airportCount());
        a.addAirport(new Airport("Paris", "PAR", 55, 89));
        assertEquals(6, a.airportCount());
        a.addAirport(new Airport("Madrid", "MAD", 13, 45));
        a.addAirport(new Airport("Roma", "ROM", 77, 6));
        a.addAirport(new Airport("Denver", "DEN", 120, 89));
        assertEquals(9, a.airportCount());
        a.removeAirport("Paris");
        a.removeAirport("Lisboa");
        a.removeAirport("Madrid");
        a.removeAirport("Milao");
        a.removeAirport("Denver");
        assertEquals(4, a.airportCount());
    }

    @Test
    void routeCount() {
        assertEquals(5, a.routeCount());
        a.addRoute("Ankara", "Milao", 3000);
        assertEquals(6, a.routeCount());
        a.removeRoute("Lisboa", "Porto");
        a.removeRoute("Lisboa", "Ankara");
        a.removeRoute("Ankara", "Milao");
        a.removeRoute("Milao", "New York");
        assertEquals(2, a.routeCount());
        a.addRoute("Lisboa", "New York", 7800);
        a.addRoute("Milao", "Ankara", 1000);
        assertEquals(4, a.routeCount());
    }

    @Test
    void addAirport_removeAirport() {
        a.addAirport(new Airport("Nice", "NIC", 3, 2));
        assertEquals(6, a.airportCount());
        a.addAirport(new Airport("Berlim", "BER", 6, 9));
        a.addAirport(new Airport("Coimbra", "COI", 3, 2));
        assertEquals(8, a.airportCount());
        a.removeAirport("Lisboa");
        a.removeAirport("Milao");
        a.removeAirport("Nice");
        a.removeAirport("Berlim");
        assertEquals(4,  a.airportCount());
        assertThrows(AirportNetworkException.class, () -> a.removeAirport("Lisboa"));
    }

    @Test
    void addRoute_removeRoute() {
        a.addRoute("Milao", "Ankara", 1001);
        assertEquals(6, a.routeCount());
        assertThrows(AirportNetworkException.class, () -> a.addRoute("Milao", "Ankara", 1001));
        a.removeRoute("Milao", "Ankara");
        a.removeRoute("Lisboa", "Milao");
        a.removeRoute("Milao", "New York");
        assertEquals(3, a.routeCount());
        a.addRoute("Milao", "Ankara", 1001);
        a.addRoute("Milao", "New York", 6700);
        assertEquals(5, a.routeCount());
        a.addAirport(new Airport("Los Angeles","LA", 7, 9));
        a.addAirport(new Airport("San Diego","SDI", 7, 9));
        a.addRoute("Los Angeles", "San Diego",1005);
        assertThrows(AirportNetworkException.class, () ->  a.addRoute("Los Angeles", "San Diego",1005));
    }

    @Test
    void shortestPathTest(){
        LinkedList<Vertex<Airport>> list = new LinkedList<>();
        list.add(a.findAirport("Porto"));
        list.add(a.findAirport("Lisboa"));
        list.add(a.findAirport("Ankara"));

        assertEquals(list, a.shortestPath("Porto", "Ankara").getPath());
        assertEquals(3400, a.shortestPath("Porto", "Ankara").getCost());
    }

    @Test
    void farthestAirportsTest(){
        LinkedList<Vertex<Airport>> list = new LinkedList<>();
        list.add(a.findAirport("New York"));
        list.add(a.findAirport("Porto"));
        list.add(a.findAirport("Lisboa"));
        list.add(a.findAirport("Ankara"));

        assertEquals(list, a.farthestAirports().getPath());
        assertEquals(11400, a.farthestAirports().getCost());
    }

    @Test
    void calculatePercentageOfAirportsWithConnectionsBetween(){
        //System.out.println(a.calculatePercentageOfAirportsWithConnectionsBetween(3));
        assertEquals(20.0, a.calculatePercentageOfAirportsWithConnectionsBetween(3));
        //System.out.println(a.calculatePercentageOfAirportsWithConnectionsBetween(1,1));
        assertEquals(20.0, a.calculatePercentageOfAirportsWithConnectionsBetween(1,1));
        //System.out.println(a.calculatePercentageOfAirportsWithConnectionsBetween(2,3));
        assertEquals(80.0, a.calculatePercentageOfAirportsWithConnectionsBetween(2,3));
    }

    @Test
    void shortestRouteOnGraph(){
        a.addRoute("Milao", "Porto", 3);
        assertEquals(3, a.shortestRouteOnGraph().getDistance());
        a.removeRoute("Milao", "Porto");
        assertEquals(400, a.shortestRouteOnGraph().getDistance());
        a.addRoute("Milao", "Porto", 80);
        assertEquals(80, a.shortestRouteOnGraph().getDistance());
    }


    @Test
    void longestRouteOnGraph(){
        a.addRoute("Milao", "Porto", 15000);
        assertEquals(15000, a.longestRouteOnGraph().getDistance());
        a.removeRoute("Milao", "Porto");
        assertEquals(10000, a.longestRouteOnGraph().getDistance());
        a.addRoute("Milao", "Porto", 21000);
        assertEquals(21000, a.longestRouteOnGraph().getDistance());

    }

    @Test
    void averageOfDistanceOfRoutes(){
        assertEquals(4580.0, a.averageOfDistanceOfRoutes());
        a.addRoute("Milao", "Porto", 1400);
        assertEquals(4050.0, a.averageOfDistanceOfRoutes());
        a.removeRoute("Milao", "Porto");
        assertEquals(4580.0, a.averageOfDistanceOfRoutes());
        a.removeRoute("Milao", "New York");
        assertEquals(3225.0, a.averageOfDistanceOfRoutes());
    }

    @Test
    void ListOfAirportsWithNoRoutesOrderedAlphabetically(){
        List<Airport> lista = new LinkedList<>();
        a.addAirport(new Airport("Nice", "NIC", 3, 2));
        a.addAirport(new Airport("Berlim", "BER", 6, 9));
        a.addAirport(new Airport("Coimbra", "COI", 3, 2));
        a.addAirport(new Airport("Andorra", "AND", 15, 99));
        a.addAirport(new Airport("Veneza", "VEN", 823, 23));

        lista.add(a.findAirport("Andorra").element());
        lista.add(a.findAirport("Berlim").element());
        lista.add(a.findAirport("Coimbra").element());
        lista.add(a.findAirport("Nice").element());
        lista.add(a.findAirport("Veneza").element());

        System.out.println(a.ListOfAirportsWithNoRoutesOrderedAlphabetically());
        assertEquals(lista, a.ListOfAirportsWithNoRoutesOrderedAlphabetically());

    }

    @Test
    void reset(){
        a.reset();
        assertEquals(0,a.airportCount());
        assertEquals(0,a.routeCount());
    }

    @Test
    void Top10Central(){
        a.addAirport(new Airport("Nice", "NIC", 3, 2));
        a.addAirport(new Airport("Berlim", "BER", 6, 9));
        a.addAirport(new Airport("Coimbra", "COI", 3, 2));
        a.addAirport(new Airport("Andorra", "AND", 15, 99));
        a.addAirport(new Airport("Veneza", "VEN", 823, 23));
        a.addAirport(new Airport("Istambul", "IST", 83, 78));
        a.addAirport(new Airport("Barcelo", "BAR", 23, 23));
        a.addAirport(new Airport("Madrid", "MAD", 873, 21));
        a.addAirport(new Airport("Frankfurt", "FRA", 16, 50));


        a.addRoute("Nice", "Berlim", 3000);
        a.addRoute("Veneza", "Berlim", 3000);
        a.addRoute("Lisboa", "Berlim", 3000);
        a.addRoute("Andorra", "Coimbra", 3000);
        a.addRoute("Lisboa", "Coimbra", 3000);
        a.addRoute("Lisboa", "Andorra", 3000);
        a.addRoute("Frankfurt", "Berlim", 3000);
        a.addRoute("Milao", "Berlim", 3000);

        System.out.println(a.top10Central());
    }


}