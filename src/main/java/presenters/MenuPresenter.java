package presenters;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import main.SimulationApp;
import maps.ForestedEquatorMap;
import maps.LifeGivingCorpsesMap;
import maps.WorldMap;
import simulation.Simulation;
import simulation.WorldConfiguration;
import world_elements.animals.MutationMode;

public class MenuPresenter {
    @FXML
    public CheckBox saveToFileCheckbox;
    @FXML
    private TextField mapHeightField;

    @FXML
    private TextField mapWidthField;

    @FXML
    private TextField startPlantsField;

    @FXML
    private TextField dailyPlantsField;

    @FXML
    private TextField plantEnergyField;

    @FXML
    private TextField startAnimalsField;

    @FXML
    private TextField startAnimalEnergyField;

    @FXML
    private TextField reproductionEnergyField;

    @FXML
    private TextField energyLossReproductionField;

    @FXML
    private TextField minMutationsField;

    @FXML
    private TextField maxMutationsField;

    @FXML
    private TextField genotypeLengthField;

    @FXML
    private CheckBox lifeGivingVariantCheckbox;

    @FXML
    private CheckBox minorCorrectionVariantCheckbox;

    @FXML
    private Button createSimulationButton;

    @FXML
    private Button exportConfigButton;

    @FXML
    private Button importConfigButton;

    @FXML
    private TextField fileToImportField;

    public void setSimulationApp() {
        initializeListeners();
    }

    private void initializeListeners() {
        createSimulationButton.setOnAction(event -> createSimulation());
        exportConfigButton.setOnAction(event -> exportConfiguration());
        importConfigButton.setOnAction(event -> importConfiguration());
    }

    private void createSimulation() {
        WorldConfiguration configuration = this.parseConfiguration();
        WorldMap map;
        if (this.lifeGivingVariantCheckbox.isSelected()) {
            map = new LifeGivingCorpsesMap(configuration);
        } else {
            map = new ForestedEquatorMap(configuration);
        }
        Simulation simulation = new Simulation(configuration, map);
        if (this.saveToFileCheckbox.isSelected()) {
            simulation.addObserver(new CSVWriter(simulation));
        }
        SimulationApp.launchSimulation(simulation, map);
    }

    private WorldConfiguration parseConfiguration() {
        int mapHeight = Integer.parseInt(mapHeightField.getText());
        int mapWidth = Integer.parseInt(mapWidthField.getText());
        int startingNumberOfPlants = Integer.parseInt(startPlantsField.getText());
        int energyFromOnePlant = Integer.parseInt(plantEnergyField.getText());
        int startingNumberOfAnimals = Integer.parseInt(startAnimalsField.getText());
        int startingEnergyOfAnimals = Integer.parseInt(startAnimalEnergyField.getText());
        int energyNecessaryToReproduce = Integer.parseInt(reproductionEnergyField.getText());
        int plantsPerDay = Integer.parseInt(dailyPlantsField.getText());
        int energyUsedToReproduce = Integer.parseInt(energyLossReproductionField.getText());
        int maxMutationNumber = Integer.parseInt(maxMutationsField.getText());
        int minMutationNumber = Integer.parseInt(minMutationsField.getText());
        int genomeLength = Integer.parseInt(genotypeLengthField.getText());

        MutationMode mutationMode;
        if (this.minorCorrectionVariantCheckbox.isSelected()) {
            mutationMode = MutationMode.SLIGHT;
        } else {
            mutationMode = MutationMode.NORMAL;
        }

        return new WorldConfiguration(mapHeight, mapWidth,
                startingNumberOfPlants, energyFromOnePlant, startingNumberOfAnimals, startingEnergyOfAnimals, energyNecessaryToReproduce,
                plantsPerDay, energyUsedToReproduce, maxMutationNumber, minMutationNumber, mutationMode, genomeLength);
    }

    private void exportConfiguration() {
        WorldConfiguration configuration = this.parseConfiguration();
        configuration.saveToCSV(String.format("config_%s.csv", configuration.id));
    }

    private void importConfiguration() {
        String filePath = this.fileToImportField.getText();
        WorldConfiguration configuration = WorldConfiguration.loadFromCSV(filePath);
        if (configuration == null) {
            return;
        }
        this.mapHeightField.setText(String.valueOf(configuration.mapHeight));
        this.mapWidthField.setText(String.valueOf(configuration.mapWidth));
        this.startPlantsField.setText(String.valueOf(configuration.startingNumberOfPlants));
        this.plantEnergyField.setText(String.valueOf(configuration.energyFromOnePlant));
        this.startAnimalsField.setText(String.valueOf(configuration.startingNumberOfAnimals));
        this.startAnimalEnergyField.setText(String.valueOf(configuration.startingEnergyOfAnimals));
        this.reproductionEnergyField.setText(String.valueOf(configuration.energyNecessaryToReproduce));
        this.dailyPlantsField.setText(String.valueOf(configuration.plantsPerDay));
        this.energyLossReproductionField.setText(String.valueOf(configuration.energyUsedToReproduce));
        this.maxMutationsField.setText(String.valueOf(configuration.maxMutationNumber));
        this.minMutationsField.setText(String.valueOf(configuration.minMutationNumber));
        this.genotypeLengthField.setText(String.valueOf(configuration.genomeLength));
        this.minorCorrectionVariantCheckbox.setSelected(configuration.mutationMode == MutationMode.SLIGHT);
        this.lifeGivingVariantCheckbox.setSelected(false);
    }
}

