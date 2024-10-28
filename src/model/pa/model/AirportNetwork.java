package model.pa.model;

import java.io.FileNotFoundException;
import java.util.*;


import model.pa.dataLoader.DatasetLoader;
import model.pa.graph.*;
import model.pa.dijkstra.DijkstraConcreteResult;
import model.pa.dijkstra.DijkstraResult;
import observer.*;

/**
 * Represents the network of airports and routes
 */
public class AirportNetwork extends Subject {
    //Graph<>
    private Graph<Airport, Route> network;
    private DijkstraResult<Airport> dijkstra;
    private String folder;

    /**
     * Constructor
     */
    public AirportNetwork () { network = new GraphAdjacencyMatrix<>(); this.folder = "";}

    /**
     *
     * @param folderpath the path of the file
     * @param route routs file name
     * @throws FileNotFoundException thrown if the file is not found
     */
    public void loadNetwork(String folderpath, String route) throws FileNotFoundException {

        try {
            DatasetLoader set = new DatasetLoader(folderpath, route);
            this.folder = folderpath;
            System.out.println("iamgem: "+ folderpath);
            set.createGraph(set.load(), this.network);
            notifyObservers(null);
        }catch (FileNotFoundException e) {
            throw new AirportNetworkException();
        }
    }

    /**
     *
     * @param folderpath the path of the folder to extract to
     * @throws FileNotFoundException thrown if the file is not found
     */
    public void exportNetwork(String folderpath) throws FileNotFoundException{
        try {
            if (folderpath.isEmpty()) throw new FileNotFoundException();
            DatasetLoader set = new DatasetLoader(folderpath, "routes_1.txt");
            set.exportRoutes(folderpath, this.network);
        }
        catch (FileNotFoundException e) {
            throw new AirportNetworkException();
        }
    }

    /**
     *
     * @return the full network
     */
    public Graph<Airport, Route> getNetwork() { return network; }

    /**
     *
     * @return the folder
     */
    public String getFolder() { return folder; }

    /**
     *
     * @return a list of all airports
     */
    public Collection<Airport> getAirports () {
        List<Airport> airports = new ArrayList<>();
        for (Vertex<Airport> vertex : network.vertices()) {
            airports.add(vertex.element());
        }

        return airports;
    }

    /**
     *
     * @return a list of all routes
     */
    public Collection<Route> getRoutes () {
        List<Route> routes = new ArrayList<>();
        for (Edge<Route, Airport> edge : network.edges()) {
            routes.add(edge.element());
        }
        return routes;
    }

    /**
     *
     * @return the number of airports in the network
     */
    public int airportCount(){ return network.numVertices(); }

    /**
     *
     * @return the number of routes in the network
     */
    public int routeCount(){ return network.numEdges(); }

    /**
     * searches for a vertex in the network
     * @param a the vertex to find
     * @return the vertex if it exists or null
     */
    public Vertex<Airport> findAirport(Vertex<Airport> a) {
        return findAirport(a.element().getName());
    }

    /**
     * searches for a vertex in the network with the name
     * @param name the element to search
     * @return the vertex if it exists or null
     */
    public Vertex<Airport> findAirport(String name) {
        for (Vertex<Airport> h : network.vertices()) {
            if (h.element().getName().equalsIgnoreCase(name)) {
                return h;
            }
        }
        return null;
    }

    /**
     * Searches for a route in the network
     * @param r the route to find
     * @return the route it it exists or null
     */
    public Route findRoute(Route r) {
        for (Edge<Route, Airport> e : network.edges()) {
            if (e.element().equals(r)) {
                return r;
            }
        }
        return null;
    }

    /**
     * adds a new airport to the network
     * @param a the airport to add
     * @return the airport added
     * @throws AirportNetworkException thrown if the airport already exists
     */
    public Vertex<Airport> addAirport(Airport a) throws AirportNetworkException {
        if (findAirport(a.getName()) != null) {
            throw new AirportNetworkException("This airport already exists.");
        }
        Vertex<Airport> airport;
        try {
            airport = network.insertVertex(a);
            notifyObservers(airport);
        }
         catch (InvalidEdgeException e) {
            throw new AirportNetworkException();
         }
        return airport;
    }

    /**
     * adds a new route to the network
     * @param a1 origin vertex
     * @param a2 destination vertex
     * @param distance size of the route
     * @return the route added
     * @throws AirportNetworkException thrown if one of the vertices doesn't exist or if the route already exists
     */
    public Route addRoute(Vertex<Airport> a1, Vertex<Airport> a2, int distance) throws AirportNetworkException{
        if (findAirport(a1) == null) throw new AirportNetworkException("Airport: " + a1 + "doesn't exist.");
        if (findAirport(a2) == null) throw new AirportNetworkException("Airport: " + a2 + "doesn't exist.");
        if (findRoute(new Route(a1.element(), a2.element(), distance)) != null) throw new AirportNetworkException("This route already exists.");

        Route r;
        try {
            r = new Route(a1.element(), a2.element(), distance);
            network.insertEdge(a1.element(), a2.element(), r);
            notifyObservers(r);
        }
        catch (InvalidEdgeException e) {
            throw new AirportNetworkException();
        }
        return r;
    }

    /**
     * adds a new route to the network
     * @param name1 origin vertex name
     * @param name2 destination vertex name
     * @param distance size of the route
     * @return the route added
     */
    public Route addRoute(String name1, String name2, int distance) {
        Vertex<Airport> a1 = findAirport(name1);
        Vertex<Airport> a2 = findAirport(name2);
        return addRoute(a1,a2, distance);
    }

    /**
     * returns the edge between two vertices
     * @param a1 origin vertex
     * @param a2 destination vertex
     * @return the edge between the two vertices
     * @throws InvalidVertexException thrown if the vertices are not adjacent
     */
    public Edge<Route, Airport> getEdgeBetweenTwoV(Vertex<Airport> a1, Vertex<Airport> a2) throws InvalidVertexException{
        if (!network.areAdjacent(a1, a2)) {
            throw new InvalidVertexException("Vertices are not adjacent");
        }

        for (Edge<Route, Airport> incidentEdge : network.incidentEdges(a1)) {
            if (network.opposite(a1, incidentEdge) == a2) return incidentEdge;
        }

        return null;
    }

    /**
     * removes an airport from the network
     * @param name the name of the airport to be removed
     * @throws AirportNetworkException thrown if the airport doesn't exist
     */
    public void removeAirport(String name) throws AirportNetworkException {
        Vertex<Airport> airport = findAirport(name);
        if (airport == null) throw new AirportNetworkException("This airport doesn't exist.");
        try {
            network.removeVertex(airport);
            notifyObservers(network);
        }
        catch (InvalidVertexException e) {
            throw new AirportNetworkException();
        }

    }

    /**
     * removes a route from the network
     * @param name1 name of origin vertex
     * @param name2 name of destination vertex
     * @throws AirportNetworkException thrown if the route doesn't exist
     */
    public void removeRoute(String name1, String name2) throws AirportNetworkException{
        Vertex<Airport> a1 = findAirport(name1);
        Vertex<Airport> a2 = findAirport(name2);

        for (Edge<Route, Airport> edge : network.incidentEdges(a1)) {
            if (network.opposite(a1, edge) == a2) {
                network.removeEdge(edge);
            }
        }
        notifyObservers(network);
    }

    /**
     * removes a route from the network
     * @param route the route to be removed
     * @throws AirportNetworkException thrown if the route doesn't exist
     */
    public void removeRoute(Edge<Route, Airport> route) throws AirportNetworkException {
        try {
            network.removeEdge(route);
            notifyObservers(null);
        }
        catch (AirportNetworkException e )
        {
            throw new AirportNetworkException();
        }
    }

    /**
     * Returns the shortest path between two airports
     * @param start origin airport
     * @param end destination airport
     * @return a dijkstraConcreteResult
     * @throws AirportNetworkException thrown if one of the vertices doesn't exist or if there's no possible path
     */
    public DijkstraConcreteResult<Airport> shortestPath(String start, String end) throws AirportNetworkException {
        try {
            dijkstra = new DijkstraResult<>(network);
            Vertex<Airport> h1 = findAirport(start);
            Vertex<Airport> h2 = findAirport(end);
            if (h1 == null) throw new AirportNetworkException("Airport: " + start + "doesn't exist.");
            if (h2 == null) throw new AirportNetworkException("Airport: " + end + "doesn't exist.");

            return dijkstra.dijkstra(h1, h2);
        }
        catch (InvalidEdgeException e) {
            throw new AirportNetworkException();
        }
        catch (InvalidVertexException e2) {
            throw new AirportNetworkException("No path available.");
        }
    }

    /**
     * returns the path between the two farthest hubs following the shortest routes
     * @return a dijkstraConcreteResult
     * @throws AirportNetworkException thrown if one of the vertices doesn't exist
     */
    public DijkstraConcreteResult<Airport> farthestAirports() throws AirportNetworkException {
        try {
            dijkstra = new DijkstraResult<>(network);
            DijkstraConcreteResult<Airport> farthest = new DijkstraConcreteResult<>(-Double.MAX_VALUE, null);
            System.out.println("Calculating...");
            for (Vertex<Airport> v1 : network.vertices()) {
                for (Vertex<Airport> v2 : network.vertices()) {
                    if (v2 == v1) continue;
                    if (network.incidentEdges(v1).isEmpty() || network.incidentEdges(v2).isEmpty() )continue;
                    DijkstraConcreteResult<Airport> temp;
                    temp = dijkstra.dijkstra(v1, v2);
                    if(temp.getPath()==null)continue;
                    if (temp.getCost() > farthest.getCost()) {
                        farthest = temp;
                    }
                }
            }
            return farthest;
        } catch (InvalidEdgeException e) {
            throw new AirportNetworkException();
        }
        catch (InvalidVertexException e2) {
            throw new AirportNetworkException("Failed.");
        }
    }

    /**
     * calculates the percentage of airports with determined routes
     * @param start minimum number of routes
     * @param end maximum number of routes
     * @return the percentage
     */
    public float calculatePercentageOfAirportsWithConnectionsBetween(int start, int end){
        int numOfAirportsWithCondition = 0;
        for (Vertex<Airport> vertex : network.vertices()) {
            int numOfConnections = ((GraphAdjacencyMatrix<Airport, Route>)network).adjacentTo(vertex).size();
            if(numOfConnections>=start&&numOfConnections<=end) numOfAirportsWithCondition++;
        }
        return (float)numOfAirportsWithCondition/network.vertices().size()*100;
    }

    /**
     * calculates the percentage of airports with at least minimum routes
     * @param start minimum routes
     * @return the percentage
     */
    public float calculatePercentageOfAirportsWithConnectionsBetween(int start){
        return calculatePercentageOfAirportsWithConnectionsBetween(start, Integer.MAX_VALUE);
    }

    /**
     * returns the longest route on the graph
     * @return a route
     */
    public Route longestRouteOnGraph() throws AirportNetworkException {
        Route r = new Route(null, null, 0);
        for (Edge<Route, Airport> edge : network.edges()) {
            if(r.compareTo(edge.element())>0){
                r=edge.element();
            }
        }
        return r;
    }

    /**
     * returns the shortest route on the graph
     * @return a route
     */
    public Route shortestRouteOnGraph() throws AirportNetworkException {
        Route r = new Route(null, null, Integer.MAX_VALUE);
        for (Edge<Route, Airport> edge : network.edges()) {
            if(r.compareTo(edge.element())<0){
                r=edge.element();
            }
        }
        return r;
    }

    /**
     * calculates the average distance of all routes
     * @return a float
     */
    public float averageOfDistanceOfRoutes(){
        int sumOfRoutesDistance = 0;
        for (Edge<Route, Airport> e : network.edges()) {
            sumOfRoutesDistance += e.element().getDistance();
        }
        return (float)sumOfRoutesDistance/network.numEdges();
    }

    /**
     * returns a map of the airport with the most connections
     * @return a hashmap
     */
    public Map<Airport, Integer> centralAirport(){
        int count = 0;
        Map<Airport, Integer> airports = new HashMap<>();
        LinkedHashMap<Airport, Integer> reverseOrderMap = new LinkedHashMap<>();

        for(Vertex<Airport> v1 : network.vertices()){
            for(Vertex<Airport> v2 : network.vertices()){
                if(network.areAdjacent(v1, v2) && v1 != v2) {
                    count++;
                }
            }
            airports.put(v1.element(), count);
            count = 0;
        }
        airports.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach(x -> reverseOrderMap.put(x.getKey(), x.getValue()));
        return reverseOrderMap;
    }

    /**
     * returna a hashmap of the 10 airports with moire connections
     * @return hashmap
     */
    public Map<Airport, Integer> top10Central(){
        Map<Airport, Integer> top10 = new LinkedHashMap<>();
        Iterator<Map.Entry<Airport, Integer>> iterator = centralAirport().entrySet().iterator();

        for(int i=0;i<10;i++) {
            if(iterator.hasNext()) {
                Map.Entry<Airport, Integer> next = iterator.next();
                top10.put(next.getKey(), next.getValue());
            }
        }

        return top10;
    }

    /**
     * return a list of airports with no routes ordered alphabetically
     * @return a list
     */
    public List<Airport> ListOfAirportsWithNoRoutesOrderedAlphabetically(){
        List<Airport> l = new LinkedList<>();
        for (Vertex<Airport> v : network.vertices()) {
            if(((GraphAdjacencyMatrix<Airport, Route>)network).isIsolated(v)) l.add(v.element());
        }
        Collections.sort(l);
        return l;
    }

    /**
     * returna a list of the incident routes of an airport
     * @param city name of the airport
     * @return collection
     */
    public Collection<Edge<Route, Airport>> getIncidentRoutes (String city) {
        return network.incidentEdges(findAirport(city));
    }

    /**
     * clears the network
     */
    public void reset(){
        ((GraphAdjacencyMatrix<Airport, Route>) network).clear();
    }

    /**
     * restores last command
     */
    public void restore(GraphAdjacencyMatrix<Airport, Route> g){
        network = new GraphAdjacencyMatrix<>(g);
        notifyObservers(network);
    }

    @Override
    public String toString() {
        return "LogisticsNetwork{" +
                "network=" + network +
                ", dijkstra=" + dijkstra +
                '}';
    }
}
