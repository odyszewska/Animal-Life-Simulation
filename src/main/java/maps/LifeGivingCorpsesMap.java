package maps;

import simulation.WorldConfiguration;
import util.Vector2d;
import world_elements.plants.Plant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;

public class LifeGivingCorpsesMap extends WorldMap{
    private final Set<Vector2d> preferredFields;

    public LifeGivingCorpsesMap(WorldConfiguration configuration) {
        super(configuration);
        this.preferredFields = new HashSet<>();
    }


    @Override
    public void makeNewPlants(int numberOfPlants) {
        Random random = new Random();
        for (int i = 0; i < numberOfPlants; i++) {
            Vector2d newPlantPosition;
            if (random.nextDouble() <= 0.8 && preferredFields != null) {
                List<Vector2d> preferredList = new ArrayList<>(preferredFields);
                if (!preferredList.isEmpty()) {
                    newPlantPosition = preferredList.get(random.nextInt(preferredList.size()));
                } else {
                    newPlantPosition = generateVectorsUntilFreeOfPlants(0, mapWidth, mapHeight);
                }
            } else {
                newPlantPosition = generateVectorsUntilFreeOfPlants(0, mapWidth, mapHeight);
            }
            if (newPlantPosition != null && !isOccupiedByPlant(newPlantPosition)) {
                super.addWorldElement(new Plant(newPlantPosition, super.configuration.energyFromOnePlant));

            }
        }
        if (preferredFields != null) {
            preferredFields.clear();
        }
    }

    @Override
    public void updateMapAfterAnimalDeath(Vector2d position) {
        preferredFields.addAll(getNeighbors(position));
    }

    private List<Vector2d> getNeighbors(Vector2d position) {
        int[][] directions = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
        List<Vector2d> neighbors = new ArrayList<>();
        for (int[] direction: directions) {
            Vector2d neighbor = new Vector2d(position.x() + direction[0], position.y() + direction[1]);
            if (isWithinBounds(neighbor)){
                neighbors.add(neighbor);}
        }
        return neighbors;
    }

    private boolean isWithinBounds(Vector2d position) {
        return position.x() >= 0 && position.x() < mapWidth && position.y() >= 0 && position.y() < mapHeight;
    }

    public Set<Vector2d> getPreferredFields() {
        return new HashSet<>(preferredFields);
    }

}
