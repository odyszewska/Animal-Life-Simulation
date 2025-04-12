package statistics;

import simulation.Simulation;
import maps.WorldMap;
import world_elements.animals.Animal;
import util.Vector2d;
import world_elements.animals.GeneArray;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private final Simulation simulation;
    private double totalLifespan = 0;
    private int totalDeaths = 0;

    public Statistics(Simulation simulation) {
        this.simulation = simulation;
    }

    public StatisticsData collectStatistics() {
        WorldMap map = simulation.getMap();

        int totalAnimals = map.getAnimals().size();
        int totalPlants = map.getPlants().size();
        int freeFields = calculateFreeFields(map);

        double averageEnergy = map.getAnimals().stream()
                .mapToDouble(Animal::getEnergy)
                .average()
                .orElse(0);
        averageEnergy = Math.round(averageEnergy * 100.0) / 100.0;

        map.getAnimals().forEach(animal -> {
            if (animal.getEnergy() == 0) {
                updateLifespanData(animal);
            }
        });
        double averageLifespan = calculateAverageLifespan();
        averageLifespan = Math.round(averageLifespan * 100.0) / 100.0;

        double averageChildren = map.getAnimals().stream()
                .mapToInt(Animal::getNumberOfChildren)
                .average()
                .orElse(0);
        averageChildren = Math.round(averageChildren * 100.0) / 100.0;

        List<String> mostPopularGenes = calculateMostPopularGenotypes(map);

        return new StatisticsData(totalAnimals, totalPlants, freeFields, mostPopularGenes, averageEnergy, averageLifespan, averageChildren, this.simulation);
    }

    private List<String> calculateMostPopularGenotypes(WorldMap map) {
        Map<String, Integer> genotypeCounts = new HashMap<>();

        map.getAnimals().forEach(animal -> {
            GeneArray geneArray = animal.getGenes();
            String genotype = Arrays.toString(geneArray.getGenesArray());
            genotypeCounts.put(genotype, genotypeCounts.getOrDefault(genotype, 0) + 1);
        });

        return genotypeCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int calculateFreeFields(WorldMap map) {
        int totalFields = map.getBounds().getWidth() * map.getBounds().getHeight();
        int occupiedFields = 0;

        for (int x = 0; x < map.getBounds().getWidth(); x++) {
            for (int y = 0; y < map.getBounds().getHeight(); y++) {
                Vector2d position = new Vector2d(x, y);
                if (map.isOccupiedByPlant(position) || map.isOccupiedByAnimal(position)) {
                    occupiedFields++;
                }
            }
        }
        return totalFields - occupiedFields;
    }

    private double calculateAverageLifespan() {
        if (totalDeaths == 0) {return 0;}
        return totalLifespan / totalDeaths;
    }

    private void updateLifespanData(Animal animal) {
        totalLifespan += animal.getAge();
        totalDeaths++;
    }

    public static class StatisticsData {
        private final Simulation simulation;
        private final int totalAnimals;
        private final int totalPlants;
        private final int freeFields;
        private final List<String> mostPopularGens;
        private final double averageEnergy;
        private final double averageLifespan;
        private final double averageChildren;

        public StatisticsData(int totalAnimals, int totalPlants, int freeFields, List<String> mostPopularGenotypes,
                              double averageEnergy, double averageLifespan, double averageChildren, Simulation simulation) {
            this.totalAnimals = totalAnimals;
            this.totalPlants = totalPlants;
            this.freeFields = freeFields;
            this.mostPopularGens = mostPopularGenotypes;
            this.averageEnergy = averageEnergy;
            this.averageLifespan = averageLifespan;
            this.averageChildren = averageChildren;
            this.simulation = simulation;
        }

        public int getTotalAnimals() {
            return totalAnimals;
        }

        public int getTotalPlants() {
            return totalPlants;
        }

        public int getFreeFields() {
            return freeFields;
        }

        public List<String> getMostPopularGenotypes() {
            return mostPopularGens;
        }

        public double getAverageEnergy() {
            return averageEnergy;
        }

        public double getAverageLifespan() {
            return averageLifespan;
        }

        public double getAverageChildren() {
            return averageChildren;
        }

        @Override
        public String toString() {
            return String.format("""
                            Statystyki symulacji %s w dniu %d:
                            Liczba zwierząt: %d
                            Liczba roślin: %d
                            Liczba wolnych pól: %d
                            Najpopularniejszy genotyp: %s
                            Średni poziom energii: %f
                            Średnia długość życia: %f
                            Średnia liczba dzieci: %f
                            """, simulation.getId(), simulation.getDayCount(), this.getTotalAnimals(),
                    this.getTotalPlants(), this.getFreeFields(), this.getMostPopularGenotypes(), this.getAverageEnergy(),
                    this.getAverageLifespan(), this.getAverageChildren());
        }
    }
}

