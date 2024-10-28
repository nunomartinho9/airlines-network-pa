package command;

import model.pa.graph.GraphAdjacencyMatrix;
import model.pa.model.Airport;
import model.pa.model.AirportNetwork;
import model.pa.model.Route;
import view.AirportNetworkView;

/**
 * Abstract class Command
 */
public abstract class Command  {
    public AirportNetwork model;
    private GraphAdjacencyMatrix<Airport, Route> backup;

    /**
     * Command constructor
     * @param model
     */
    public Command(AirportNetwork model) {
        this.model = model;
    }

    /**
     * Assign the value of the current GraphAdjacencyMatrix to the backup
     */
    void backup(){
       backup = new GraphAdjacencyMatrix<Airport, Route>((GraphAdjacencyMatrix<Airport, Route>) model.getNetwork());
    }

    /**
     * Restores the current model to the last backup saved
     */
    public void undo() {
        model.restore(backup);
    }

    /**
     * Abstract method to be used in instances of Command
     * @return boolean
     */
    public abstract boolean execute();

}