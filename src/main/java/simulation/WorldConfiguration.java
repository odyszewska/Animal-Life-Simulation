package simulation;

import world_elements.animals.MutationMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class WorldConfiguration {
    public UUID id;
    public final int mapWidth;
    public final int mapHeight;
    public final int startingNumberOfAnimals;
    public final int startingNumberOfPlants;
    public final int energyFromOnePlant;
    public final int startingEnergyOfAnimals;
    public final int energyUsedToReproduce;
    public final int genomeLength;
    public final int minMutationNumber;
    public final int maxMutationNumber;
    public final int plantsPerDay;
    public final MutationMode mutationMode;
    public final int energyNecessaryToReproduce;

    public WorldConfiguration(int mapHeight, int mapWidth, int startingNumberOfPlants, int energyFromOnePlant,
                      int startingNumberOfAnimals, int startingEnergyOfAnimals, int energyNecessaryToReproduce,
                      int plantsPerDay, int energyUsedToReproduce, int maxMutationNumber, int minMutationNumber,
                      MutationMode mutationMode, int genomeLength) {
        this.id = UUID.randomUUID();
        this.energyFromOnePlant = energyFromOnePlant;
        this.startingEnergyOfAnimals = startingEnergyOfAnimals;
        this.energyNecessaryToReproduce = energyNecessaryToReproduce;
        this.plantsPerDay = plantsPerDay;
        this.energyUsedToReproduce = energyUsedToReproduce;
        this.maxMutationNumber = maxMutationNumber;
        this.minMutationNumber = minMutationNumber;
        this.mutationMode = mutationMode;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.startingNumberOfAnimals = startingNumberOfAnimals;
        this.startingNumberOfPlants = startingNumberOfPlants;
        this.genomeLength = genomeLength;
    }
    public void saveToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("mapHeight,mapWidth,startingNumberOfPlants,energyFromOnePlant,startingNumberOfAnimals,")
                    .append("startingEnergyOfAnimals,energyNecessaryToReproduce,plantsPerDay,energyUsedToReproduce,")
                    .append("maxMutationNumber,minMutationNumber,mutationMode,genomeLength\n");

            writer.append(String.valueOf(mapHeight)).append(",")
                    .append(String.valueOf(mapWidth)).append(",")
                    .append(String.valueOf(startingNumberOfPlants)).append(",")
                    .append(String.valueOf(energyFromOnePlant)).append(",")
                    .append(String.valueOf(startingNumberOfAnimals)).append(",")
                    .append(String.valueOf(startingEnergyOfAnimals)).append(",")
                    .append(String.valueOf(energyNecessaryToReproduce)).append(",")
                    .append(String.valueOf(plantsPerDay)).append(",")
                    .append(String.valueOf(energyUsedToReproduce)).append(",")
                    .append(String.valueOf(maxMutationNumber)).append(",")
                    .append(String.valueOf(minMutationNumber)).append(",")
                    .append(mutationMode.toString()).append(",")
                    .append(String.valueOf(genomeLength)).append("\n");
        } catch (IOException e) {
            System.err.println("Błąd w czasie zapisu do CSV: " + e.getMessage());
        }
    }

    public static WorldConfiguration loadFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();
            if (header == null || header.isEmpty()) {
                throw new IllegalArgumentException("CSV jest pusty lub nie ma headera.");
            }

            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                throw new IllegalArgumentException("CSV nie ma danych.");
            }

            String[] values = line.split(",");
            if (values.length != 13) {
                throw new IllegalArgumentException("CSV ma nieprawidłową liczbę kolumn.");
            }

            int mapHeight = Integer.parseInt(values[0]);
            int mapWidth = Integer.parseInt(values[1]);
            int startingNumberOfPlants = Integer.parseInt(values[2]);
            int energyFromOnePlant = Integer.parseInt(values[3]);
            int startingNumberOfAnimals = Integer.parseInt(values[4]);
            int startingEnergyOfAnimals = Integer.parseInt(values[5]);
            int energyNecessaryToReproduce = Integer.parseInt(values[6]);
            int plantsPerDay = Integer.parseInt(values[7]);
            int energyUsedToReproduce = Integer.parseInt(values[8]);
            int maxMutationNumber = Integer.parseInt(values[9]);
            int minMutationNumber = Integer.parseInt(values[10]);
            MutationMode mutationMode = MutationMode.valueOf(values[11]);
            int genomeLength = Integer.parseInt(values[12]);

            return new WorldConfiguration(
                    mapHeight, mapWidth, startingNumberOfPlants, energyFromOnePlant, startingNumberOfAnimals,
                    startingEnergyOfAnimals, energyNecessaryToReproduce, plantsPerDay, energyUsedToReproduce,
                    maxMutationNumber, minMutationNumber, mutationMode, genomeLength
            );
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Błąd podczas ładowania WorldConfiguration z CSV: " + e.getMessage());
            return null;
        }
    }

    public void validateData(){
        if (mapWidth <= 0 || mapHeight <= 0) {
            throw new IllegalArgumentException("Wysokość i szerokość mapy muszą być większe od zera.");
        }
        if (startingNumberOfPlants < 0 || startingNumberOfAnimals < 0) {
            throw new IllegalArgumentException("Początkowa ilość zwierząt i roślin nie może być ujemna.");
        }
        if (energyFromOnePlant <= 0 || startingEnergyOfAnimals <= 0 || energyNecessaryToReproduce <= 0) {
            throw new IllegalArgumentException("Wszystkie wartości energii muszą być większe od zera.");
        }
        if (plantsPerDay < 0) {
            throw new IllegalArgumentException("Ilość nowych roślin wyrastających każdego dnia nie może być ujemna.");
        }
        if (energyUsedToReproduce < 0 || energyNecessaryToReproduce < energyUsedToReproduce) {
            throw new IllegalArgumentException("Energia używana podczas rozmnażania nie może być ujemna i musi być mniejsza od energii potrzebnej do reprodukcji.");
        }
        if (minMutationNumber < 0 || maxMutationNumber < 0 || maxMutationNumber < minMutationNumber) {
            throw new IllegalArgumentException("Minimalna i maksymalna liczba mutacji nie może być ujemna, a maksymalna liczba mutacji musi być większa lub równa od minimalnej.");
        }
        if (genomeLength <= 0) {
            throw new IllegalArgumentException("Długość genomu musi być większa od zera.");
        }
    }
}
