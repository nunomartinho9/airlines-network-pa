package view;

import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import controller.AirportNetworkController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.pa.dijkstra.DijkstraConcreteResult;
import model.pa.graph.Edge;
import model.pa.graph.Vertex;
import model.pa.model.Airport;
import model.pa.model.AirportNetwork;
import model.pa.model.Route;
import observer.Observable;
import observer.Observer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the view in the MVC pattern
 */
public class AirportNetworkView extends BorderPane implements AirportNetworkUI, Observer {

    private SmartGraphPanel<Airport, Route> graphPanel;
    private AirportNetwork model;

    private Button btnAddAirport;
    private Button btnRemoveAirport;
    private Button btnAddRoute;
    private Button btnRemoveRoute;
    private Button btnOpenPieChart;
    private Button btnShortestRoute;
    private Button btnFarthestAirport;

    private Label lblError;

    private ComboBox<String> cbAirports1;
    private ComboBox<String> cbAirports2;

    private TextField txtAirportName;
    private TextField iata;
    private TextField txtX;
    private TextField txtY;
    private TextField txtDistance;
    private TextField txtAirportsCount;
    private TextField txtRoutesCount;
    private TextArea txtTop10;
    private TextField txtShortRoute;
    private TextField txtLongRoute;
    private TextField txtAvgRoutes;
    private TextArea txtAirportsIsolated;
    private TextField txtPathResult;

    private Menu fileMenu = new Menu("File");
    private MenuItem importItem = new MenuItem("Import");
    private MenuItem exportItem = new MenuItem("Export Routes");
    private MenuItem undoItem = new MenuItem("Undo");

    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private FileChooser fileChooser = new FileChooser();
    private Stage primaryStage;

    private Group root;
    private ImageView imageView;

    /**
     *
     * @param model of the mvc pattern
     * @param primaryStage of the main stage
     */
    public AirportNetworkView (AirportNetwork model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
        createLayout();
    }


    /**
     * Updates the Interface
     * @param subject
     * @param arg
     */
    @Override
    public void update(Observable subject, Object arg) {

        if(subject == model) {
            /* update graph panel */
            graphPanel.updateAndWait();
            System.out.println("update...");
            clearError();


            /* Insersão das coordenadas no graph */

            for (Vertex<Airport> v : model.getNetwork().vertices()) {
                graphPanel.setVertexPosition(v, v.element().getX(), v.element().getY());
            }

            /* image */
            /*
            try {
                image = new Image(new FileInputStream( model.getFolder() + "\\img\\back.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageView = new ImageView(image);
            */

            /* update ID comboboxes */
            cbAirports1.getItems().clear();
            cbAirports2.getItems().clear();
            List<Airport> airports = new ArrayList<>( model.getAirports() );
            for (Airport airport : airports) {
                cbAirports1.getItems().add(String.valueOf(airport.getName()) );
                cbAirports2.getItems().add(String.valueOf(airport.getName()) );
            }

            /* Statistics */
            txtAirportsCount.setText(String.valueOf(model.airportCount()));
            txtRoutesCount.setText(String.valueOf(model.routeCount()));
            txtShortRoute.setText(model.shortestRouteOnGraph()+ "");
            txtLongRoute.setText(model.longestRouteOnGraph() + "");
            txtAvgRoutes.setText(model.averageOfDistanceOfRoutes() + "Km");

            String lIsolated = "";
            for (Airport airport : model.ListOfAirportsWithNoRoutesOrderedAlphabetically()) {
                lIsolated += "" + airport.getName() + " - " + airport.getAbreviation() + "\n";
            }
            txtAirportsIsolated.setText(lIsolated);

            String ltop10 = "";
            for (Airport airport : model.top10Central().keySet()) {
                ltop10 += "" + airport.getName() + " - " + airport.getAbreviation() + "\n";
            }
            txtTop10.setText(ltop10);

        }
    }

    /**
     * Sets the triggers for the buttons
     * @param controller
     */

    public void setTriggers(AirportNetworkController controller) {

        btnAddAirport.setOnAction(event -> controller.doAddAirport());

        btnRemoveAirport.setOnAction(event -> controller.doRemoveAirport());

        btnAddRoute.setOnAction(event -> controller.doAddRoute() );

        btnRemoveRoute.setOnAction(event -> controller.doRemoveRoute());

        btnShortestRoute.setOnAction(event -> {
            controller.doShortestPath();
        });

        btnFarthestAirport.setOnAction(event -> {
            controller.doFarthestPath();
        });

        undoItem.setOnAction(event ->{
            controller.doUndo();

        });

        btnOpenPieChart.setOnAction(event -> {
            PieChartSample pieChart = new PieChartSample();
            pieChart.start();
        });

        importItem.setOnAction(event -> {
            System.out.println("Importing...");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            String s = selectedFile.getPath().replace("\\"+selectedFile.getName(), "");
            controller.doLoadMap(s, selectedFile.getName());
            updateImage();
        });

        exportItem.setOnAction(event -> {
            System.out.println("EXPORTING....");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            controller.doExport(selectedDirectory.getAbsolutePath()+"\\routes_1.txt");

        });

    }


    /**
     * Creates the app main layout
     */
    private void createLayout() {

        /* Criação do graph view */
        String customProps = "vertex.radius = 4 " + "\n" + "edge.label = false" + "\n" + "edge.arrow = false";
        //SmartGraphProperties properties = new SmartGraphProperties(customProps);


        /* TOP PANEL */
        setTop(createTopMenu());

        try {
            Image image = new Image(new FileInputStream( model.getFolder() + "\\img\\back.png"));
            imageView = new ImageView(image);

            /* CENTER PANEL */
            graphPanel = new SmartGraphPanel<>(model.getNetwork(), new SmartCircularSortedPlacementStrategy());
            //graphPanel.setAutomaticLayout(true);
            root = new Group(imageView, graphPanel);
            setCenter(root);
            //setCenter(graphPanel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* RIGHT PANEL */
        setRight(createSidePanel());

        /* BOTTOM */
        lblError = new Label("Ready");
        HBox bottom = new HBox(lblError);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        setBottom(bottom);

        /* bind double click on vertex */
        graphPanel.setVertexDoubleClickAction((SmartGraphVertex<Airport> graphVertex) -> {

            txtAirportName.setText( String.valueOf( graphVertex.getUnderlyingVertex().element().getName()) );
            iata.setText(String.valueOf(graphVertex.getUnderlyingVertex().element().getAbreviation()));
        });
    }


    /**
     * Creates the top menu
     * @return a VBOX
     */
    private VBox createTopMenu() {

        fileMenu.getItems().add(importItem);
        fileMenu.getItems().add(exportItem);
        fileMenu.getItems().add(undoItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        VBox vBox = new VBox(menuBar);
        return vBox;
    }

    /**
     * Creates the side panel
     * @return a VBOX
     */
    private VBox createSidePanel() {

        /* ADD PERSON CONTROLS */
        GridPane pane = new GridPane();
        //pane.setAlignment(Pos.CENTER);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(10,10,10,10)); // set top, right, bottom, left

        txtAirportName = new TextField("");
        iata = new TextField("");
        txtAirportName.setPrefColumnCount(10);
        iata.setPrefColumnCount(10);
        txtX = new TextField("");
        txtX.setPrefColumnCount(10);
        txtY = new TextField("");
        txtY.setPrefColumnCount(10);
        Label labelName = new Label("Name: ");
        Label labelIATA = new Label("IATA: ");
        Label labelX = new Label("X: ");
        Label labelY = new Label("Y: ");

        pane.add(labelName, 0, 1);
        pane.add(txtAirportName, 1, 1);
        pane.add(labelIATA, 2, 1);
        pane.add(iata, 3, 1);
        pane.add(labelX, 0, 2);
        pane.add(txtX, 1, 2);
        pane.add(labelY, 2, 2);
        pane.add(txtY, 3, 2);

        btnAddAirport = new Button("Add");
        pane.add(btnAddAirport, 1, 4);

        btnRemoveAirport = new Button("Remove");
        btnRemoveAirport.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        pane.add(btnRemoveAirport, 2, 4);

        /* Relationship Controls */
        GridPane routenPane = new GridPane();
        //routenPane.setAlignment(Pos.CENTER);
        routenPane.setHgap(5);
        routenPane.setVgap(5);
        routenPane.setPadding(new Insets(10,10,10,10)); // set top, right, bottom, left

        txtPathResult = new TextField("");
        txtDistance = new TextField("");
        cbAirports1 = new ComboBox<>();
        cbAirports1.setMaxWidth(Double.MAX_VALUE); //hack to hgrow
        cbAirports2 = new ComboBox<>();
        cbAirports2.setMaxWidth(Double.MAX_VALUE); //hack to hgrow

        Label labelId1 = new Label("Id 1: ");
        Label labelId2 = new Label("Id 2: ");
        Label labelDesc = new Label("Distance: ");
        Label labelResult = new Label("Result: ");

        routenPane.add(labelId1, 0, 1);
        routenPane.add(cbAirports1, 1, 1);
        routenPane.add(labelId2, 0, 2);
        routenPane.add(cbAirports2, 1, 2);
        routenPane.add(labelDesc, 0, 3);
        routenPane.add(txtDistance, 1, 3);

        btnAddRoute = new Button("Add Route");
        btnRemoveRoute = new Button("Remove Route");
        btnShortestRoute = new Button("Calc Shortest Path");
        btnShortestRoute.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        btnRemoveRoute.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnFarthestAirport = new Button("Calc Farthest Path");
        btnFarthestAirport.setStyle("-fx-background-color: blue; -fx-text-fill: white;");

        routenPane.add(btnAddRoute, 0, 4);
        routenPane.add(btnRemoveRoute, 1, 4);
        routenPane.add(btnShortestRoute, 0, 5);
        routenPane.add(labelResult, 0, 6);
        routenPane.add(txtPathResult, 1, 6);
        routenPane.add(btnFarthestAirport, 1, 5);
        //stats
        GridPane statsPane = new GridPane();
        Label labelAirportsCount = new Label("Airports count: ");
        Label labelRoutesCount = new Label("Routes count: ");
        Label labelTop10 = new Label("Top 10: ");
        Label labelShortRoute = new Label("Shortest Route: ");
        Label labelLongRoute = new Label("Longest Route: ");
        Label labelIsolated = new Label("Isolated Airports: ");
        Label labelAvgRoutes = new Label("Average routes: ");

        btnOpenPieChart = new Button("Open PieChart");

        txtAirportsCount = new TextField("");
        txtAirportsCount.setEditable(false);
        txtAirportsCount.setPrefColumnCount(10);
        txtRoutesCount = new TextField("");
        txtRoutesCount.setEditable(false);
        txtRoutesCount.setPrefColumnCount(10);
        txtTop10 = new TextArea("");
        txtTop10.setEditable(false);
        txtTop10.setPrefColumnCount(12);
        txtShortRoute = new TextField("");
        txtShortRoute.setEditable(false);
        txtShortRoute.setPrefColumnCount(10);
        txtLongRoute = new TextField("");
        txtLongRoute.setPrefColumnCount(10);
        txtLongRoute.setEditable(false);
        txtAvgRoutes = new TextField("");
        txtAvgRoutes.setEditable(false);
        txtAvgRoutes.setPrefColumnCount(10);
        txtAirportsIsolated = new TextArea("");
        txtAirportsIsolated.setEditable(false);
        txtAirportsIsolated.setPrefColumnCount(12);

        statsPane.add(labelAirportsCount, 0, 1);
        statsPane.add(txtAirportsCount, 1, 1);
        statsPane.add(labelRoutesCount, 0, 2);
        statsPane.add(txtRoutesCount, 1, 2);
        statsPane.add(labelTop10,0, 3);
        statsPane.add(txtTop10, 1, 3);
        statsPane.add(labelShortRoute, 0, 4);
        statsPane.add(txtShortRoute, 1, 4);
        statsPane.add(labelLongRoute, 0, 5);
        statsPane.add(txtLongRoute, 1, 5);
        statsPane.add(labelAvgRoutes, 0, 6);
        statsPane.add(txtAvgRoutes, 1, 6);
        statsPane.add(labelIsolated, 0, 7);
        statsPane.add(txtAirportsIsolated, 1, 7);
        statsPane.add(btnOpenPieChart, 0, 8);

        /* COMPOSE */

        Label pHelp = new Label("You can double click on a airport to select its ID.");
        pHelp.setStyle("-fx-font-size: 10px;");
        pHelp.setWrapText(true);
        pHelp.setMaxWidth(200);


        VBox panel = new VBox(new Label("Manage Airports"),
                pane,
                pHelp,
                new Separator(),
                new Label("Manage Routes"),
                routenPane,
                new Separator(),
                new Label("Statistics"),
                statsPane);
        panel.setPadding(new Insets(10, 10, 10, 10));
        panel.setSpacing(10);

        return panel;
    }

    /**
     * Class of the pie chart
     */
    public class PieChartSample {
       public void start() {
           Scene scene = new Scene(new Group());
           Stage stage = new Stage(StageStyle.DECORATED);
           stage.setTitle("PieChart");
           stage.setWidth(500);
           stage.setHeight(500);

            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("10 or more", model.calculatePercentageOfAirportsWithConnectionsBetween(10)),
                            new PieChart.Data("Between 5-9", model.calculatePercentageOfAirportsWithConnectionsBetween(5,9)),
                            new PieChart.Data("Less then 5", model.calculatePercentageOfAirportsWithConnectionsBetween(0,4)),
                            new PieChart.Data("No connections", model.calculatePercentageOfAirportsWithConnectionsBetween(0,0)));
            final PieChart chart = new PieChart(pieChartData);
            chart.setTitle("PieChart - Airport");

            ((Group) scene.getRoot()).getChildren().add(chart);
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * Function for updating the new background image when imported new dataset
     */
    public void updateImage() {
        try {
            Image img = new Image(new FileInputStream( model.getFolder() + "\\img\\back.png"));
            ImageView imgview = new ImageView(img);
            root = new Group(imgview, graphPanel);
            setCenter(root);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Updates the text path text field
     * @param result
     */
    @Override
    public void setTxtPathResult(String result) {
        txtPathResult.setText(result);
    }

    public SmartGraphPanel<Airport, Route> getGraphPanel() {
        return graphPanel;
    }

    /**
     * Changes the colors of the shortest and farthest paths
     * @param result Dijktra result
     * @param color
     */
    public void colorPaths(DijkstraConcreteResult<Airport> result, String color) {
        clearPaths();
        for (int i = 0; i < result.getPath().size(); i++) {
            graphPanel.getStylableVertex(result.getPath().get(i)).setStyle("-fx-stroke: "+color+"; -fx-stroke-width: 4;");
            if (result.getPath().size() != i+1)
                graphPanel.getStylableEdge(model.getEdgeBetweenTwoV(result.getPath().get(i), result.getPath().get(i+1))).setStyle("-fx-stroke: "+color+"; -fx-stroke-width: 4;");
        }
    }

    /**
     * Resets the paths colors
     */
    public void clearPaths(){
        // hubs
        for (Vertex<Airport> v : model.getNetwork().vertices())
            graphPanel.getStylableVertex(v.element()).setStyleClass("vertex");

        // routes
        for (Edge<Route, Airport> e : model.getNetwork().edges())
            graphPanel.getStylableEdge(e).setStyleClass("edge");
    }

    /**
     * Gets the name of the airport from the text field
     * @return String
     */
    @Override
    public String getNameAirportAdd() {
        return txtAirportName.getText();
    }

    /**
     * Gets the IATA of the airport from the text field
     * @return String
     */
    @Override
    public String getIATAAirportAdd() {
        return iata.getText();
    }

    /**
     * Gets the coordinate X of the airport from the text field
     * @return String
     */
    @Override
    public String getXadd() {
        return txtX.getText();
    }

    /**
     * Gets the coordiante Y of the airport from the text field
     * @return String
     */
    @Override
    public String getYadd() {
        return txtY.getText();
    }

    /**
     * Gets the name of the selected item from the combo box
     * @return String
     */
    @Override
    public String getCombo1() {
        return cbAirports1.getSelectionModel().getSelectedItem();
    }

    /**
     * Gets the name of the selected item from the combo box
     * @return String
     */
    @Override
    public String getCombo2() {
        return cbAirports2.getSelectionModel().getSelectedItem();
    }

    /**
     * Gets the distance of the route from the text field
     * @return String
     */
    @Override
    public String getDistanceAdd() {
        return txtDistance.getText();
    }

    /**
     * Displays the provided error
     * @param msg string of the error provided
     */
    @Override
    public void displayError(String msg) {
        lblError.setStyle("-fx-text-fill: red;");
        lblError.setText(msg);
    }

    /**
     * clear the errors
     */
    @Override
    public void clearError() {
        lblError.setText("Ready");
        lblError.setStyle("-fx-text-fill: black;");
    }
    /**
     * clear the controls
     */
    @Override
    public void clearControls() {
        txtAirportName.clear();
        iata.clear();
        txtDistance.clear();
    }

    /**
     * Initiates the graphpanel
     */
    public void initGraphDisplay() {
        graphPanel.init();
        update(model, null);
    }

}
