package model.pa.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.pa.graph.Vertex;

import static org.junit.jupiter.api.Assertions.*;


class GraphAdjacencyMatrixTest<V, E> {

    GraphAdjacencyMatrix<String, String> graph;



    @BeforeEach
    void setUp() {
        graph = new GraphAdjacencyMatrix<>();


        Vertex<String> vA = graph.insertVertex("A");
        Vertex<String> vB = graph.insertVertex("B");
        Vertex<String> vC = graph.insertVertex("C");
        Vertex<String> vD = graph.insertVertex("D");
        Vertex<String> vE = graph.insertVertex("E");


        graph.insertEdge(vA, vB, "e1");
        graph.insertEdge(vA, vC, "e2");
        graph.insertEdge(vC, vB, "e3");
        graph.insertEdge(vE, vD, "e4");
        graph.insertEdge(vE, vC, "e5");
    }

    @Test
    void numVertices_shouldReturnNumberOfVertexes() {
        assertEquals(5, graph.numVertices());
        graph.insertVertex("G");
        graph.insertVertex("H");
        graph.insertVertex("I");
        assertEquals(8, graph.numVertices());
        graph.removeVertex(graph.findVertex("A"));
        graph.removeVertex(graph.findVertex("B"));
        graph.removeVertex(graph.findVertex("C"));
        graph.removeVertex(graph.findVertex("D"));
        assertEquals(4, graph.numVertices());
    }


    @Test
    void numEdges_shouldReturnNumberOfEdges() {
        assertEquals(5, graph.numEdges());
        graph.insertEdge(graph.findVertex("A"), graph.findVertex("D"), "e6");
        assertEquals(6, graph.numEdges());
        graph.removeEdge(graph.findEdge("e1"));
        graph.removeEdge(graph.findEdge("e2"));
        graph.removeEdge(graph.findEdge("e3"));
        graph.removeEdge(graph.findEdge("e4"));
        assertEquals(2, graph.numEdges());
    }

    @Test
    void vertices_shouldReturnVertexes() {
        assertEquals(5, new ArrayList<>(graph.vertices()).size());
        graph.insertVertex("G");
        assertEquals(6, new ArrayList<>(graph.vertices()).size());
        graph.removeVertex(graph.findVertex("A"));
        graph.removeVertex(graph.findVertex("B"));
        graph.removeVertex(graph.findVertex("C"));
        assertEquals(3, new ArrayList<>(graph.vertices()).size());
        graph.insertVertex("Q");
        graph.insertVertex("Y");
        graph.insertVertex("T");
        assertEquals(6, new ArrayList<>(graph.vertices()).size());
    }

    @Test
    void edges_shouldReturnEdges() {
        assertEquals(5, new ArrayList(graph.edges()).size());
        graph.insertEdge("B", "D", "e6");
        graph.insertEdge("E", "A", "e7");
        graph.insertEdge("D", "C", "e8");
        graph.insertEdge("A", "D", "e9");
        assertEquals(9, new ArrayList<>(graph.edges()).size());
        graph.removeEdge(graph.findEdge("e1"));
        graph.removeEdge(graph.findEdge("e2"));
        graph.removeEdge(graph.findEdge("e3"));
        graph.removeEdge(graph.findEdge("e4"));
        graph.removeEdge(graph.findEdge("e5"));
        graph.removeEdge(graph.findEdge("e6"));
        graph.removeEdge(graph.findEdge("e7"));
        assertEquals(2, new ArrayList<>(graph.edges()).size());
    }

    @Test
    void opposite_shouldReturnTheVertexOpposite() {
        assertEquals(graph.findVertex("B"), graph.opposite(graph.findVertex("A"), graph.findEdge("e1")));
        graph.insertEdge("D", "C", "e6");
        assertEquals(graph.findVertex("D"), graph.opposite(graph.findVertex("C"), graph.findEdge("e6")));
        graph.insertEdge("B", "D", "e7");
        assertEquals(graph.findVertex("B"), graph.opposite(graph.findVertex("D"), graph.findEdge("e7")));
    }

    @Test
    void incidentEdges_shouldReturnIncidentEdges() {
        assertEquals(2, new ArrayList(graph.incidentEdges(graph.findVertex("A"))).size());
        graph.insertEdge("A", "D", "e6");
        graph.insertEdge("A", "E", "e7");
        assertEquals(4, new ArrayList(graph.incidentEdges(graph.findVertex("A"))).size());
        graph.removeEdge(graph.findEdge("e1"));
        graph.removeEdge(graph.findEdge("e2"));
        graph.removeEdge(graph.findEdge("e6"));
        graph.removeEdge(graph.findEdge("e7"));
        assertEquals(0, new ArrayList(graph.incidentEdges(graph.findVertex("A"))).size());
    }

    @Test
    void areAdjacent_shouldReturnTrue_ifAreAdjacent() {
        assertTrue(graph.areAdjacent(graph.findVertex("C"), graph.findVertex("B")));
        graph.insertEdge(graph.findVertex("B"), graph.findVertex("D"), "e6");
        assertTrue(graph.areAdjacent((graph.findVertex("B")), graph.findVertex("D")));
        graph.insertEdge(graph.findVertex("A"), graph.findVertex("B"), "e7");
        assertTrue(graph.areAdjacent(graph.findVertex("A"), graph.findVertex("B")));
        graph.removeEdge(graph.findEdge("e3"));
        assertFalse(graph.areAdjacent(graph.findVertex("C"), graph.findVertex("B")));
    }

    @Test
    void isIsolated_shouldReturnTrue_isIsolated() {
        graph.insertVertex("X");
        assertTrue(graph.isIsolated(graph.findVertex("X")));
        assertFalse(graph.isIsolated(graph.findVertex("A")));
    }

    @Test
    void replace_shouldReturnTrue_ifVertexIsReplaced() {
        graph.replace(graph.findVertex("A"), "Z");
        assertTrue(graph.existsVertexWith("Z"));
        graph.replace(graph.findVertex("B"), "Y");
        assertTrue(graph.existsVertexWith("Y"));
        graph.insertVertex("T");
        graph.replace(graph.findVertex("T"), "B");
        assertTrue(graph.existsVertexWith("B"));
        graph.removeVertex(graph.findVertex("Y"));
        assertFalse(graph.existsVertexWith("Y"));
    }

    @Test
    void replace_shouldReturnTrue_ifEdgesIsReplaced() {
        graph.replace(graph.findEdge("e1"), "e9");
        assertTrue(graph.existsEdgeWith("e9"));
        graph.replace(graph.findEdge("e3"), "e11");
        assertTrue(graph.existsEdgeWith("e11"));
        graph.insertEdge(graph.findVertex("B"), graph.findVertex("D"),"e7");
        assertTrue(graph.existsEdgeWith("e7"));
        graph.replace(graph.findEdge("e7"), "e10");
        assertTrue(graph.existsEdgeWith("e10"));
        graph.removeEdge(graph.findEdge("e11"));
        assertFalse(graph.existsEdgeWith("e11"));
    }

    @Test
    void top10Central_shouldReturnTop10Central() {

        LinkedList<String> vertexList = new LinkedList<>();

        Vertex<String> vA = graph.findVertex("A");
        Vertex<String> vB = graph.findVertex("B");
        Vertex<String> vC = graph.findVertex("C");
        Vertex<String> vD = graph.findVertex("D");
        Vertex<String> vE = graph.findVertex("E");

        Vertex<String> vF = graph.insertVertex("F");
        Vertex<String> vG = graph.insertVertex("G");
        Vertex<String> vH = graph.insertVertex("H");
        Vertex<String> vI = graph.insertVertex("I");
        Vertex<String> vJ = graph.insertVertex("J");
        Vertex<String> vK = graph.insertVertex("K");
        Vertex<String> vL = graph.insertVertex("L");

        vertexList.add(vA.element());
        vertexList.add(vE.element());
        vertexList.add(vB.element());
        vertexList.add(vC.element());
        vertexList.add(vK.element());
        vertexList.add(vL.element());
        vertexList.add(vD.element());
        vertexList.add(vI.element());
        vertexList.add(vF.element());
        vertexList.add(vG.element());

        graph.insertEdge("A","E", "e6");
        graph.insertEdge("A","D", "e7");
        graph.insertEdge("A","F", "e8");
        graph.insertEdge("A","I", "e9");
        graph.insertEdge("B","E", "e10");
        graph.insertEdge("B","L", "e11");
        graph.insertEdge("L","K", "e12");
        graph.insertEdge("G","I", "e13");
        graph.insertEdge("H","A", "e14");
        graph.insertEdge("L","E", "e15");
        graph.insertEdge("K","F", "e16");
        graph.insertEdge("K","C", "e17");


        System.out.println(graph.top10CentralElements());
        System.out.println(vertexList);


        assertEquals(vertexList, graph.top10CentralElements());

    }











    
}