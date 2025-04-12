package presenters;

import maps.WorldMap;
import simulation.Simulation;
import statistics.Statistics;

import java.io.FileWriter;
import java.util.UUID;

public class CSVWriter implements MapChangeListener {
    private final Statistics statistics;
    private final UUID simulationID;

    public CSVWriter(Simulation simulation) {
        this.statistics = new Statistics(simulation);
        this.simulationID = simulation.getId();
    }
    @Override
    public void mapChanged(WorldMap worldMap) {
        Statistics.StatisticsData data = this.statistics.collectStatistics();

        try (FileWriter writer = new FileWriter(String.format("sim_%s.csv", this.simulationID), true)) {
            writer.append(data.toString()).append("\n");
        } catch (java.io.IOException e) {
            System.out.println("Nastąpił błąd przy próbie zapisu statytyk do pliku");
        }
    }
}
