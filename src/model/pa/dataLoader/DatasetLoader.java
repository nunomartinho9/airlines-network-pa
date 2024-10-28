package model.pa.dataLoader;

import jdk.management.resource.internal.inst.DatagramDispatcherRMHooks;
import model.pa.graph.Edge;
import model.pa.graph.Graph;
import model.pa.graph.GraphAdjacencyMatrix;
import model.pa.graph.Vertex;
import model.pa.model.Airport;
import model.pa.model.Route;
import observer.Subject;

import javax.xml.crypto.Data;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the data set loader
 */
public class DatasetLoader extends Subject {
    private String folderpath, route;

    /**
     *
     * @param folderpath path of the folder dataset
     * @param route routes file name
     */
    public DatasetLoader(String folderpath, String route) {
        this.folderpath = folderpath;
        this.route = route;
    }


    private interface loadHandler {
        void handlerAction(String s);
    }

    /**
     * Function for reading the files
     * @param folderpath path of the folder dataset
     * @param handler how the function reads the file
     * @throws FileNotFoundException sends this error if the file is not found
     */
    private void doReadFile(String folderpath, loadHandler handler) throws FileNotFoundException {
        try(BufferedReader br = new BufferedReader(new FileReader(folderpath))) {
            String line;

            while ((line = br.readLine()) != null) {

                if (line.charAt(0) != '#') handler.handlerAction(line);
            }
        } catch (IOException e) {
            throw new FileNotFoundException(String.format("The file %s does not seem to exist.", folderpath));
        }
    }

    /**
     * Loads the files
     * @return the Dataset
     * @throws FileNotFoundException sends this error if the file is not found
     */
    public Dataset load() throws FileNotFoundException {
        //read names
        List<String> city = new LinkedList<>();
        List<String> iata = new LinkedList<>();
        this.doReadFile(this.folderpath+"\\name.txt", s -> {
            //System.out.println("s: "+ s);
            String[] tokens = s.trim().split("\t");
            iata.add(tokens[0]);
            city.add(tokens[1]);
        });

        //read routes
        List<Integer> distance = new LinkedList<>();
        List<String> start = new LinkedList<>();
        List<String> destination = new LinkedList<>();
        this.doReadFile(this.folderpath+"\\"+this.route, s -> {
            String[] tokens = s.trim().split("\t");
            start.add(tokens[0]);
            destination.add(tokens[2]);
            distance.add(Integer.parseInt(tokens[1]));
        });

        //read xy
        List<Integer> x = new LinkedList<>();
        List<Integer> y = new LinkedList<>();
        this.doReadFile(this.folderpath+"\\xy.txt", s -> {
            String[] tokens = s.trim().split("\t");
            x.add(Integer.parseInt(tokens[0]));
            y.add(Integer.parseInt(tokens[1]));
        });


        //read weight
        List<Double> lat = new LinkedList<>();
        List<Double> longitude = new LinkedList<>();
        List<Integer> alt = new LinkedList<>();
        List<String> names = new LinkedList<>();

        this.doReadFile(this.folderpath+"\\weight.txt", s -> {
            NumberFormat nf = NumberFormat.getInstance();
            String[] tokens = s.trim().split("\t");
            names.add(tokens[0]);
            try {
                lat.add(nf.parse(tokens[1]).doubleValue());
                longitude.add(nf.parse(tokens[2]).doubleValue());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            alt.add(Integer.parseInt(tokens[3]));
        });

        return new Dataset(iata, city, start, destination, names, distance, x, y, alt, lat, longitude);
    }

    /**
     * Creates the graph with the provided dataset
     * @param dataset object with dataset information
     * @param network graph
     * @return a new graph
     */
    public Graph<Airport, Route> createGraph(Dataset dataset, Graph<Airport, Route> network) {

        for (Edge<Route, Airport> edge : network.edges()) {
            network.removeEdge(edge);
        }

        for (Vertex<Airport> vertex : network.vertices()) {
            network.removeVertex(vertex);
        }

        //insert airports
        for (int i = 0; i < dataset.getCity().size(); i++) {
           network.insertVertex(new Airport(dataset.getCity().get(i), dataset.getIata().get(i), dataset.getLat().get(i), dataset.getLongitude().get(i), dataset.getAlt().get(i), dataset.getX().get(i), dataset.getY().get(i)));
           notifyObservers(null);
        }

        //insert routes
        for (int i = 0; i < dataset.getStart().size(); i++) {
            Airport start = findAirport(network, dataset.getStart().get(i));
            Airport dest = findAirport(network, dataset.getDestination().get(i));
            try {
                network.insertEdge(start, dest, new Route(start, dest, dataset.getDistance().get(i)));
                notifyObservers(null);
            }catch (Exception e) {
                //e.printStackTrace();

            }
        }

        return network;
    }

    /**
     * Finds an airport in the graph
     * @param graph
     * @param s name of the airport
     * @return the airport
     */
    private Airport findAirport(Graph<Airport, Route> graph, String s) {
        for (Vertex<Airport> vertex : graph.vertices()) {
            if (vertex.element().compareTo(new Airport("", s, 0, 0, 0)) == 0) return vertex.element();
        }
        return null;
    }

    /**
     * Exports the routes
     * @param directory path where the file will be saved
     * @param graph
     */
    public void exportRoutes(String directory, Graph<Airport, Route> graph) {
        List<String> list = new LinkedList<>();
        for (Edge<Route, Airport> edge : graph.edges()) {
            String o = ""+edge.element().getOrigin();
            String d = ""+edge.element().getDestination();

            o = o.replace(" ", "");
            d = d.replace(" ", "");
            list.add("" + o +"\t" + edge.element().getDistance() + "\t" + d);
        }
        System.out.println("lista");
        System.out.println(list);

        File file = new File(directory); //initialize File object and passing path as argument
        boolean result;
        try
        {
            result = file.createNewFile();  //creates a new file
            if(result)  // test if successfully created a new file
            {
                System.out.println("file created "+file.getCanonicalPath()); //returns the path string
            }
            else
            {
                System.out.println("File already exist at location: "+file.getCanonicalPath());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();    //prints exception if any
        }

        StringBuilder text = new StringBuilder();
        for (String l : list) {
            text.append(l);
            text.append("\n");
        }

        try{
            FileWriter fileWriter = new FileWriter(directory);
            fileWriter.write(text.toString());
            System.out.println(text);
            fileWriter.close();
            System.out.println("File is created successfully with the content.");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

}
