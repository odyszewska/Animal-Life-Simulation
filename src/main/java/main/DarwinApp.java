package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import presenters.MenuPresenter;

import java.io.IOException;
import java.net.URL;

public class DarwinApp extends Application {
    MenuPresenter presenter;

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        URL resource = getClass().getClassLoader().getResource("menu.fxml");
        if (resource == null) {
            throw new IllegalStateException("menu.fxml not found in resources folder");
        }
        loader.setLocation(resource);

        try {
            StackPane viewRoot = loader.load();
            this.presenter = loader.getController();
            this.presenter.setSimulationApp();

            Scene scene = new Scene(viewRoot);
            stage.setScene(scene);
            stage.setTitle("Darwin Project");
            stage.setWidth(1100);
            stage.setHeight(800);
            stage.show();
        } catch (IOException e) {
            System.out.println("Loader nie załadował pliku fxml poprawnie: " + e.getMessage());
        }
    }
}
