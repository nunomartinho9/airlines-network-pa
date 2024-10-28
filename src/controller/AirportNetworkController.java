package controller;

import command.Command;
import command.CommandHistory;
import command.CopyCommand;
import model.pa.dijkstra.DijkstraConcreteResult;
import model.pa.graph.Edge;
import model.pa.model.Airport;
import model.pa.model.AirportNetwork;
import model.pa.model.AirportNetworkException;
import model.pa.model.Route;
import view.AirportNetworkView;

import java.io.FileNotFoundException;

/**
 * This class represents the controller in the MVC pattern
 */
public class AirportNetworkController {

    private AirportNetworkView view;
    private AirportNetwork model;
    private CommandHistory history;

    /**
     *
     * @param view of the MVC
     * @param model of the MVC
     */
    public AirportNetworkController(AirportNetworkView view, AirportNetwork model) {
        this.view = view;
        this.model = model;

        /* binds actions to user interface*/
        this.view.setTriggers(this);

        /* bind observer */
        this.model.addObserver(this.view);
        history = new CommandHistory();

        backup(new CopyCommand(model));

    }

    public void backup(Command command){
        command.execute();
        history.push(command);
    }

    /**
     * This function adds the described airport
     */
    public void doAddAirport() {
        try {
            String name = view.getNameAirportAdd();
            String iata = view.getIATAAirportAdd();
            String x = view.getXadd();
            String y = view.getYadd();
            if (name.trim().isEmpty() || iata.trim().isEmpty() || x.trim().isEmpty() || y.trim().isEmpty()) {
                view.displayError("Name, Iata, X and Y cannot be null.");
            }
            backup(new CopyCommand(model));
            model.addAirport(new Airport(name, iata, Integer.parseInt(x), Integer.parseInt(y)));
            //System.out.println(model.airportCount());
            view.clearError();
            view.clearControls();
        }catch (AirportNetworkException e) {
            view.displayError(e.getMessage() );
        }catch (NumberFormatException e2) {
            view.displayError("The X and Y must be an integer number.");
        }

    }

    /**
     * This function removes the provided airport
     */
    public void doRemoveAirport() {
        try {
            if (view.getNameAirportAdd().trim().isEmpty()) {
                view.displayError("Name and iata cannot be null.");
                return;
            }
            backup(new CopyCommand(model));
            for (Edge<Route, Airport> r : model.getIncidentRoutes(view.getNameAirportAdd())) {
                model.removeRoute(r);
            }
            model.removeAirport(view.getNameAirportAdd());
            view.clearError();
            view.clearControls();
        }catch (AirportNetworkException e) {
            view.displayError(e.getMessage());
        }

    }

    /**
     * This function add the route with the provided airports
     */
    public void doAddRoute() {
        try {
            if (view.getCombo1() == null || view.getCombo2() == null) {
                view.displayError("You must select the airports.");
                return;
            }

            if (view.getDistanceAdd().trim().isEmpty()) {
                view.displayError("You must provide a distance.");
                return;
            }
            backup(new CopyCommand(model));
            model.addRoute(view.getCombo1(), view.getCombo2(), Integer.parseInt( view.getDistanceAdd()));
        } catch (AirportNetworkException e) {
            view.displayError(e.getMessage());
        }catch (NumberFormatException e2) {
            view.displayError("The Distance must be an integer number.");
        }

    }

    /**
     * This function removes the selected route
     */
    public void doRemoveRoute() {
       try {
           if (view.getCombo1()== null || view.getCombo2() == null) {
               view.displayError("You must select the airports.");
               return;
           }
           backup(new CopyCommand(model));
           model.removeRoute(view.getCombo1(), view.getCombo2());


           view.clearError();
           view.clearControls();

       } catch (AirportNetworkException e) {
            view.displayError(e.getMessage());
       }

    }

    /**
     * This function restores the previous state
     */
    public void doUndo() {
        if (history.isEmpty()){
            return;
        }

        Command command = history.pop();
        if (command != null) {
            command.undo();
        }
    }

    public void doExport(String folderpath) {
        try {
            model.exportNetwork(folderpath);
            view.clearError();
        } catch (AirportNetworkException | FileNotFoundException e) {
            view.displayError( e.getMessage() );
        }

    }

    /**
     * This function loads the imported map
     *
     * @param folderpath path of the folder of the dataset
     * @param routes file name
     */
    public void doLoadMap (String folderpath, String routes) {
        try {
            history.push(new CopyCommand(this.model));
            model.loadNetwork(folderpath, routes);
        }catch (FileNotFoundException e) {
            view.displayError("FIle not found.");
        }
        catch (AirportNetworkException e2) {
            view.displayError(e2.getMessage());
        }

    }

    /**
     * This function shows the shortest path between a pair of airports
     */
    public void doShortestPath() {
        try {
            String start = view.getCombo1();
            String end = view.getCombo2();
            if (start == null || end == null) {
                view.displayError("You must select the cities.");
                return;
            }
            DijkstraConcreteResult<Airport> result = model.shortestPath(start, end);

            view.colorPaths(result, "green");
            view.setTxtPathResult(result+"");
            view.clearError();
        } catch (AirportNetworkException e) {
            view.displayError(e.getMessage());
        }
    }

    /**
     * This function shows the shortest path between the farthest airports
     */
    public void doFarthestPath() {

        try {
            DijkstraConcreteResult<Airport> result = model.farthestAirports();
            view.colorPaths(result, "blue");
            view.setTxtPathResult(result+"");
            view.clearError();
        }catch (AirportNetworkException e) {
            view.displayError(e.getMessage());
        }

    }

}
