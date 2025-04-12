package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import maps.WorldMap;
import presenters.SimulationPresenter;
import simulation.Simulation;

public class SimulationApp extends Application {

    private static Simulation currentSimulation;
    private static WorldMap currentMap;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));

            BorderPane root = loader.load();

            SimulationPresenter simulationPresenter = loader.getController();

            simulationPresenter.setWorldMap(currentMap);
            simulationPresenter.setSimulation(currentSimulation);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(String.format("Symulacja %s", currentSimulation.getId()));
            primaryStage.setWidth(1000);
            primaryStage.setHeight(1000);
            primaryStage.show();

            simulationPresenter.drawMap();

            currentSimulation.addObserver(simulationPresenter);
            currentSimulation.start();

            primaryStage.setOnCloseRequest(event -> {
                currentSimulation.stop();
            });
        } catch (Exception e) {
            System.err.println("Błąd w ładowaniu symulacji: " + e.getMessage());
        }
    }

    public static void launchSimulation(Simulation simulation, WorldMap map) {
        currentSimulation = simulation;
        currentMap = map;

        Platform.runLater(() -> {
            try {
                Application.launch(SimulationApp.class);
            } catch (IllegalStateException e) {
                new SimulationApp().start(new Stage());
            }
        });
    }
}
