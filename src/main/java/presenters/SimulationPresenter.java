package presenters;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import maps.ForestedEquatorMap;
import maps.LifeGivingCorpsesMap;
import maps.WorldMap;
import simulation.Simulation;
import statistics.Statistics;
import util.Vector2d;
import world_elements.WorldElement;
import util.Boundary;
import world_elements.animals.Animal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Label mostPopularGenes;
    @FXML
    public GridPane statisticsPane;
    @FXML
    public Label selectedGenome;
    @FXML
    public Label selectedGenomePart;
    @FXML
    private Label currentDay;
    @FXML
    private Label selectedEnergy;
    @FXML
    private Label selectedEatenPlants;
    @FXML
    private Label selectedChildren;
    @FXML
    private Label selectedAge;
    @FXML
    private Label selectedTimeOfDeath;
    private WorldMap map;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button startStopButton;
    @FXML
    public Label totalAnimals;
    @FXML
    public Label totalPlants;
    @FXML
    public Label freeFields;
    @FXML
    public Label averageEnergy;
    @FXML
    public Label averageLifespan;
    @FXML
    public Label averageChildren;
    private Simulation simulation;
    private Statistics statistics;
    private Animal trackedAnimal;
    private static final double GRID_SIZE = 500.0;

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.statistics = new Statistics(simulation);
    }

    @FXML
    public void initialize() {
        this.startStopButton.setOnAction(actionEvent -> onStartStopButtonClicked());
        gridPane.widthProperty().addListener((obs, oldVal, newVal) -> drawMap());
        gridPane.heightProperty().addListener((obs, oldVal, newVal) -> drawMap());
    }

    public void setWorldMap(WorldMap map) {
        this.map = map;
    }

    public void drawMap() {
        this.clearGrid();
        Boundary currentBounds = this.map.getBounds();
        Label label = new Label("y\\x");
        label.setAlignment(Pos.CENTER);
        this.gridPane.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);

        double width = GRID_SIZE / (currentBounds.upperRight().x() - currentBounds.lowerLeft().x());
        for (int i = currentBounds.lowerLeft().x(); i <= currentBounds.upperRight().x(); i++) {
            label = new Label(String.valueOf(i));
            this.gridPane.add(label, i - currentBounds.lowerLeft().x() + 1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(width));
        }

        double height = GRID_SIZE / (currentBounds.upperRight().y() - currentBounds.lowerLeft().y());
        for (int i = currentBounds.upperRight().y(); i >= currentBounds.lowerLeft().y(); i--) {
            label = new Label(String.valueOf(i));
            this.gridPane.add(label, 0, currentBounds.upperRight().y() - i + 1);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            this.gridPane.getRowConstraints().add(new RowConstraints(height));
        }

        HashSet<Vector2d> fieldsTaken = new HashSet<>();
        Stream<WorldElement> animals = this.map.getOrderedAnimals().stream().map(animal -> animal);
        Stream<WorldElement> plants = this.map.getPlants().stream().map(plant -> plant);
        List<WorldElement> elements = Stream.concat(animals, plants).toList();

        if (!this.simulation.isRunning()) {
            if (this.map instanceof ForestedEquatorMap) {
                int upperEquatorBound = map.getUpperEquatorBound();
                int lowerEquatorBound = map.getLowerEquatorBound();

                for (int i = lowerEquatorBound + 1; i < upperEquatorBound + 1; i++) {
                    for (int j = 1; j <= this.map.getBounds().getWidth(); j++) {
                        Rectangle rectangle = new Rectangle(width, height);
                        rectangle.setFill(Color.LIGHTGREEN);
                        this.gridPane.add(rectangle, j, i);
                    }
                }
            } else {
                LifeGivingCorpsesMap currentMap = (LifeGivingCorpsesMap)this.map;
                Set<Vector2d> preferredFields = currentMap.getPreferredFields();
                Rectangle rectangle = new Rectangle(width, height);
                rectangle.setFill(Color.LIGHTGREEN);
                for (Vector2d field : preferredFields) {
                    this.gridPane.add(rectangle, field.x(), field.y());
                }
            }
        }

        for (WorldElement element : elements) {
            Vector2d coords = element.getLocation();
            if (fieldsTaken.contains(coords)) {
                continue;
            }
            fieldsTaken.add(coords);
            WorldElementBox box = new WorldElementBox(element);

            GridPane.setHalignment(box.getLabel(), HPos.CENTER);
            GridPane.setValignment(box.getLabel(), VPos.CENTER);

            box.getStackPane().setOnMouseClicked(event -> {
                if (element instanceof Animal) {
                    this.trackedAnimal = (Animal) element;
                    this.refreshAnimalStatistics();
                }
            });

            StackPane stackPaneToAdd = new StackPane(box.getStackPane());
            if (element instanceof Animal animal && this.statistics != null && !this.simulation.isRunning()) {
                Statistics.StatisticsData data = this.statistics.collectStatistics();
                if (Arrays.toString(Arrays.stream(animal.getGenes().getGenesArray()).mapToObj(String::valueOf).toArray(String[]::new)).equals(data.getMostPopularGenotypes().toArray(String[]::new)[0])) {
                    Rectangle rectangle = new Rectangle(width, height);
                    rectangle.setFill(Color.LIGHTBLUE);
                    stackPaneToAdd = new StackPane(rectangle, box.getStackPane());
                }
            }
            this.gridPane.add(stackPaneToAdd, coords.x() - currentBounds.lowerLeft().x() + 1, currentBounds.upperRight().y() - coords.y() + 1);
        }

        this.gridPane.getColumnConstraints().add(new ColumnConstraints(width));
        this.gridPane.getRowConstraints().add(new RowConstraints(height));
    }

    private void refreshAnimalStatistics() {
        Animal animal = this.trackedAnimal;

        this.statisticsPane.setVisible(true);
        this.selectedGenome.setText(String.join(", ", Arrays.stream(animal.getGenes().getGenesArray()).mapToObj(String::valueOf).toArray(String[]::new)));
        this.selectedGenomePart.setText(String.valueOf(animal.getGenes().getCurrentGeneIndex()));
        this.selectedChildren.setText(String.valueOf(animal.getNumberOfChildren()));
        this.selectedEnergy.setText(String.valueOf(animal.getEnergy()));
        this.selectedEatenPlants.setText(String.valueOf(animal.getEatenPlants()));
        if (animal.isDead()) {
            this.selectedTimeOfDeath.setText(animal.getTimeOfDeath() + " dzień");
            this.selectedAge.setText("Zwierzę martwe");
        } else {
            this.selectedTimeOfDeath.setText("Zwierzę żywe");
            this.selectedAge.setText(String.valueOf(animal.getAge()));
        }
    }

    private void drawStatistics() {
        Statistics.StatisticsData data = this.statistics.collectStatistics();

        this.currentDay.setText(String.valueOf(this.simulation.getDayCount()));
        this.totalAnimals.setText(String.valueOf(data.getTotalAnimals()));
        this.totalPlants.setText(String.valueOf(data.getTotalPlants()));
        this.freeFields.setText(String.valueOf(data.getFreeFields()));
        this.averageEnergy.setText(String.valueOf(data.getAverageEnergy()));
        this.averageLifespan.setText(String.valueOf(data.getAverageLifespan()));
        this.averageChildren.setText(String.valueOf(data.getAverageChildren()));
        if (data.getMostPopularGenotypes().size() == 1) {

            this.mostPopularGenes.setText(data.getMostPopularGenotypes().getFirst().substring(1, data.getMostPopularGenotypes().getFirst().length() - 1));
        } else {
            this.mostPopularGenes.setText("Brak zwierząt na mapie");
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap) {
        Platform.runLater(() -> {
            this.drawStatistics();
            this.drawMap();
            if (this.trackedAnimal != null) {
                this.refreshAnimalStatistics();
            }
        });
    }

    private void clearGrid() {
        this.gridPane.getChildren().retainAll(this.gridPane.getChildren().getFirst());
        this.gridPane.getColumnConstraints().clear();
        this.gridPane.getRowConstraints().clear();
    }

    public void onStartStopButtonClicked() {
        if (this.simulation.isRunning()) {
            this.simulation.stop();
            this.startStopButton.setText("Start");
        } else {
            this.simulation.start();
            this.startStopButton.setText("Stop");
        }
    }
}
