import controller.AirportNetworkController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.pa.dataLoader.DatasetLoader;
import model.pa.graph.Graph;
import model.pa.graph.GraphAdjacencyMatrix;
import model.pa.model.Airport;
import model.pa.model.AirportNetwork;
import model.pa.model.Route;
import view.AirportNetworkView;

import java.io.FileNotFoundException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private static final String DATAFILE = "dataset\\iberian";
    private static final String ROUTE = "routes_1.txt";


    @Override
    public void start(Stage primaryStage) {


        AirportNetwork model = new AirportNetwork();
        try {
            model.loadNetwork(DATAFILE, ROUTE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AirportNetworkView view = new AirportNetworkView(model, primaryStage);
        AirportNetworkController controller = new AirportNetworkController(view, model);

        Scene scene = new Scene(view, 1500, 860);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Airport Network");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        view.initGraphDisplay();


    }


}
