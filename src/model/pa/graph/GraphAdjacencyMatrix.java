package model.pa.graph;
import model.pa.model.Airport;
import model.pa.model.Route;

import java.util.*;

/**
 * Class GraphAdjacencyMatrix structure of data to represent graph
 * @param <V>
 * @param <E>
 */
public class GraphAdjacencyMatrix<V, E> implements Graph<V, E> {

    private Map< Vertex<V>, Map<Vertex<V>, Edge<E,V>>> adjacencyMap;

    /**
     * Constructor of the class
     */
    public GraphAdjacencyMatrix() {
        this.adjacencyMap = new HashMap<>();
    }

    /**
     * Constructor of the class creating a copy of an existing GraphAdjancencyMatrix
     * @param g
     */
    public GraphAdjacencyMatrix(GraphAdjacencyMatrix<V,E> g){
        adjacencyMap = new HashMap<>(g.adjacencyMap);
    }

    /**
     * Finds a Vertex based on its elements
     * @param vElement
     * @return Vertex<V>
     */
    public Vertex<V> findVertex(V vElement){
        for (Vertex<V> v : vertices()){
            if (v.element().equals(vElement))
                return v;
        }
        return null;
    }


    /**
     * Finds an Edge based on its elements
     * @param eElement
     * @return Edge<E,V>
     */
    public Edge<E , V> findEdge(E eElement){
        for (Edge<E, V> e : edges()){
            if (e.element().equals(eElement))
                return e;
        }
        return null;
    }

    /**
     * Receives two Vertices and returns of they are Adjacent or not
     * @param u     a vertex
     * @param v     another vertex
     *
     * @return boolean
     * @throws InvalidVertexException
     */
    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        checkVertex(u);
        checkVertex(v);

        return adjacencyMap.get(u).containsKey(v);
    }


    /**
     * Receives a Vertex and returns the lists of its adjacent Vertices
     * @param v
     * @return List<Vertex<V>>
     */
    public List<Vertex<V>> adjacentTo(Vertex<V> v){
        return new ArrayList<>(adjacencyMap.get(v).keySet());
    }

    /**
     * Counts the number of Vertices on the Graph
     * @return int
     */
    @Override
    public int numVertices() { return adjacencyMap.size(); }

    /**
     * Counts the number of Edges on the Graph
     * @return int
     */
    @Override
    public int numEdges() { return edges().size(); }

    /**
     * Returns a collection of all the Vertices in the Graph
     * @return Collection<Vertex<V>>
     */
    @Override
    public Collection<Vertex<V>> vertices() {
        return new HashSet<>(adjacencyMap.keySet());
    }

    /**
     * Returns a collection of all the Edges in the Graph
     * @return Collection<Edge<E, V>>
     */
    @Override
    public Collection<Edge<E, V>> edges() {
        List<Edge<E, V>> edges = new LinkedList<>();
        for (Map<Vertex<V>, Edge<E, V>> map : adjacencyMap.values()) {
            for (Edge<E, V> edge : map.values()) {
                if(!edges.contains(edge)){
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    /**
     * Indicates the incident Edges of a Vertex
     * @param v     vertex for which to obtain the incident edges
     *
     * @return Collection<Edge<E, V>>
     * @throws InvalidVertexException
     */
    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);
        return new HashSet<>(adjacencyMap.get(v).values());
    }

    /**
     * Returns of a Vertex is isolated
     * @param v
     * @return boolean
     * @throws InvalidVertexException
     */
    public boolean isIsolated(Vertex<V> v) throws InvalidVertexException{
        return incidentEdges(v).isEmpty();
    }

    /**
     * Returns the opposite Vertex of the Edge given the other Vertex
     * @param v         vertex on one end of <code>e</code>
     * @param e         edge connected to <code>v</code>
     * @return Vertex<V>
     * @throws InvalidVertexException
     * @throws InvalidEdgeException
     */
    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        checkVertex(v);
        checkEdge(e);
        if(!adjacencyMap.get(v).containsValue(e)){
            throw new InvalidEdgeException("This edge is not connected to this vertex");
        }
        if(e.vertices()[0]==v){
            return e.vertices()[1];
        }
        return e.vertices()[0];
    }

    /**
     * Inserts a Vertex into the graph
     * @param vElement      the element to store at the vertex
     *
     * @return Vertex<V>
     * @throws InvalidVertexException
     */
    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if(findVertex(vElement)!=null) throw new InvalidVertexException("The vertex already exists");
        MyVertex v = new MyVertex(vElement);
        adjacencyMap.put(v, new LinkedHashMap<>());
        return v;
    }

    /**
     * Inserts an Edge into the graph
     * @param u             a vertex
     * @param v             another vertex
     * @param edgeElement   the element to store in the new edge
     *
     * @return Edge<E, V>
     * @throws InvalidEdgeException
     */
    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) throws InvalidEdgeException {
        checkVertex(u);
        checkVertex(v);
        if (findEdge(edgeElement)!=null) throw new InvalidEdgeException("The edge already exists");
        MyEdge edge = new MyEdge(edgeElement);
        adjacencyMap.get(u).put(v, edge);
        if(u==v) return edge;
        adjacencyMap.get(v).put(u, edge);
        return edge;
    }

    /**
     * Inserts an Edge into the graph
     * @param vElement1     a vertex's stored element
     * @param vElement2     another vertex's stored element
     * @param edgeElement   the element to store in the new edge
     *
     * @return Edge<E,V>
     * @throws InvalidVertexException
     * @throws InvalidEdgeException
     */
    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        return insertEdge(findVertex(vElement1), findVertex(vElement2), edgeElement);
    }

    /**
     * Removes a Vertex from the graph
     * @param v     vertex to remove
     *
     * @return V
     * @throws InvalidVertexException
     */
    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);
        for (Map<Vertex<V>, Edge<E, V>> map : adjacencyMap.values()) {
            map.remove(v);

        }
        adjacencyMap.remove(v);
        return v.element();
    }

    /**
     * Clears the graph
     */
    public void clear(){ adjacencyMap.clear(); }

    /**
     * Removes an Edge from the graph
     * @param e     edge to remove
     *
     * @return E
     * @throws InvalidEdgeException
     */
    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        MyEdge myEdge = checkEdge(e);
        Vertex<V> origin = myEdge.vertices()[0];
        Vertex<V> destination = myEdge.vertices()[1];

        adjacencyMap.get(origin).remove(destination, e);
        if (origin==destination) return e.element();
        adjacencyMap.get(destination).remove(origin, e);


        return e.element();
    }

    /**
     * Replaces the element of a Vertex with a new one
     * @param v             vertex to replace its element
     * @param newElement    new element to store in <code>v</code>
     *
     * @return V
     * @throws InvalidVertexException
     */
    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        MyVertex vertex = checkVertex(v);
        vertex.element = newElement;
        return vertex.element;
    }

    /**
     * Replaces the element of an Edge with a new one
     * @param e             edge to replace its element
     * @param newElement    new element to store in <code>e</code>
     *
     * @return E
     * @throws InvalidEdgeException
     */
    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        MyEdge edge = checkEdge(e);
        edge.element = newElement;
        return edge.element;
    }

    /**
     * Select the top 10 most central Vertices and returns its elements in a LinkedList
     * @return LinkedList<V>
     */
    public LinkedList<V> top10CentralElements(){
        LinkedList<Map.Entry<V, Integer>> list = new LinkedList<>();

        for (Map.Entry<Vertex<V>, Map<Vertex<V>, Edge<E, V>>> e : adjacencyMap.entrySet()) {
            list.add(new AbstractMap.SimpleEntry<>(e.getKey().element(), e.getValue().size()));
        }

        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        LinkedList<V> finalList = new LinkedList<>();
        Iterator<Map.Entry<V, Integer>> iter = list.listIterator();
        for ( int i=0; i<10; i++ ) {
            if(!iter.hasNext()) return finalList;
            finalList.add(iter.next().getKey());
        }

        return finalList;
    }

    /**
     * Travels the Graph in Depth Order
     * @param vertex
     * @return Collection<Vertex<V>>
     */
    public Collection<Vertex<V>> DFS(Vertex<V> vertex) {
        Stack<Vertex<V>> stack = new Stack<>();
        List<Vertex<V>> visited = new ArrayList<>();

        visited.add(vertex);
        stack.push(vertex);

        while(!stack.isEmpty()) {
            Vertex<V> v = stack.pop();

            for(Edge<E,V> e : incidentEdges(v)) {
                Vertex<V> w = opposite(v, e);

                if(!visited.contains(w)) {
                    visited.add(w);
                    stack.push(w);
                }
            }
        }
        return visited;
    }

    /**
     * Class MyVertex
     */
    private class MyVertex implements Vertex<V> {
        private V element;

        public MyVertex(V element) {
            this.element = element;
        }

        @Override
        public V element() {
            return element;
        }

        @Override
        public String toString() {
            return String.format("Vertex{%s}", element);
        }
    }

    /**
     * Class MyEdge
     */
    private class MyEdge implements Edge<E, V> {
        private E element;

        public MyEdge(E element) {
            this.element = element;
        }

        @Override
        public E element() {
            return element;
        }

        /**
         * Returns an array of all the Vertices in the Graph
         * @return Vertex<V>[]
         */
        @Override
        public Vertex<V>[] vertices() {
            //if the edge exists, then two existing vertices have the edge
            //in their incidentEdges lists
            for(Vertex<V> v : GraphAdjacencyMatrix.this.adjacencyMap.keySet()) {

                for (Map.Entry<Vertex<V>, Edge<E, V>> entry : adjacencyMap.get(v).entrySet()) {
                    if( entry.getValue().equals( this )) {
                        return new Vertex[]{v, entry.getKey()};
                    }
                }
            }

            return new Vertex[]{null, null}; //edge was removed from the graph
        }

        @Override
        public String toString() {
            return String.format("Edge{%s}", element);
        }
    }


    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if(v == null) throw new InvalidVertexException("Null vertex.");

        MyVertex vertex;
        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!adjacencyMap.containsKey(v)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if(e == null) throw new InvalidEdgeException("Null edge.");

        MyEdge edge;
        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an edge.");
        }

        if (!edges().contains(edge)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    /**
     * Checks of there is a Vertex with the elements received
     * @param vElement
     * @return boolean
     * @throws InvalidVertexException
     */
    public boolean existsVertexWith(V vElement) throws InvalidVertexException{
        MyVertex vertex = new MyVertex(vElement);
        for(Vertex<V> v : vertices()){
            if(v.element().equals(vertex.element)) return true;
        }
        return false;
    }

    /**
     * Checks of there is an Edge with the elements received
     * @param eElement
     * @return boolean
     */
    public boolean existsEdgeWith(E eElement) {
        MyEdge edge = new MyEdge(eElement);
        for(Edge<E, V> e : edges()){
            if(e.element().equals(edge.element)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph | Adjacency Matrix : \n");

        for(Vertex<V> v : adjacencyMap.keySet()) {

            sb.append( String.format("%10s | ", v) );

            for (Map.Entry<Vertex<V>, Edge<E, V>> entry : adjacencyMap.get(v).entrySet()) {
                sb.append( String.format("%10s ---> %-10s + ", entry.getValue(), entry.getKey()) );
            }

            sb.append("\n");
        }

        return sb.toString();
    }

}
