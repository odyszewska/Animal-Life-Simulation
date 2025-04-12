package maps;

import simulation.WorldConfiguration;
import util.Vector2d;
import world_elements.plants.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForestedEquatorMap extends WorldMap {

    public ForestedEquatorMap(WorldConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void makeNewPlants(int numberOfPlants) {
        List<Vector2d> equatorFreePositions = getFreePositions(super.lowerEquatorBound, super.upperEquatorBound);
        List<Vector2d> outsideEquatorFreePositions = new ArrayList<>();
        outsideEquatorFreePositions.addAll(getFreePositions(0, super.lowerEquatorBound));
        outsideEquatorFreePositions.addAll(getFreePositions(super.upperEquatorBound, super.mapHeight));

        Random random = new Random();

        for (int i = 0; i < numberOfPlants; i++) {
            if (equatorFreePositions.isEmpty() && outsideEquatorFreePositions.isEmpty()) {
                return;
            }

            boolean growOnEquator = random.nextInt(10) < 8;

            Vector2d position = null;

            if (growOnEquator && !equatorFreePositions.isEmpty()) {
                position = removeRandomPosition(equatorFreePositions);
            } else if (!outsideEquatorFreePositions.isEmpty()) {
                position = removeRandomPosition(outsideEquatorFreePositions);
            } else if (!equatorFreePositions.isEmpty()) {
                position = removeRandomPosition(equatorFreePositions);
            }

            if (position != null) {
                super.addWorldElement(new Plant(position, super.configuration.energyFromOnePlant));
            }
        }
    }

    private List<Vector2d> getFreePositions(int lowerYBound, int upperYBound) {
        List<Vector2d> freePositions = new ArrayList<>();
        for (int x = 0; x < super.mapWidth; x++) {
            for (int y = lowerYBound; y < upperYBound; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!isOccupiedByPlant(position)) {
                    freePositions.add(position);
                }
            }
        }
        return freePositions;
    }

    private Vector2d removeRandomPosition(List<Vector2d> positions) {
        Random random = new Random();
        int randomIndex = random.nextInt(positions.size());
        return positions.remove(randomIndex);
    }

    @Override
    public void updateMapAfterAnimalDeath(Vector2d position) {
        // Ta metoda celowo nic nie robi — chcieliśmy, aby wszystkie możliwe mapy ją miały, ale nie wszystkie muszą jej używać
    }
}
