package maps;

import simulation.WorldConfiguration;
import util.Vector2d;
import world_elements.animals.Animal;
import world_elements.animals.MutationMode;
import world_elements.plants.Plant;
import org.junit.jupiter.api.Test;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ForestedEquatorMapTest {
    @Test
    public void testWorldMapInitialization() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        ForestedEquatorMap map = new ForestedEquatorMap(configuration);

        assertEquals(10, map.mapHeight);
        assertEquals(10, map.mapWidth);
        assertNotNull(map.configuration);
        assertEquals(10, map.getAnimals().size());
        assertEquals(5, map.getPlants().size());
        assertEquals(50, map.getAnimals().getFirst().getEnergy());
    }

    @Test
    public void testMakeNewPlants() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 0, 10, 10, 50, 20, 5, 10, 5, 5, MutationMode.NORMAL, 15);
        ForestedEquatorMap map = new ForestedEquatorMap(configuration);

        long initialNumberOfPlants = map.elements.values().stream()
                .flatMap(List::stream)
                .filter(element -> element instanceof Plant)
                .count();

        assertEquals(0, initialNumberOfPlants);

        map.makeNewPlants(10);

        long totalPlantsAfterGeneration = map.elements.values().stream()
                .flatMap(List::stream)
                .filter(element -> element instanceof Plant)
                .count();

        assertEquals(10, totalPlantsAfterGeneration);
    }

    @Test
    public void testDeleteDeadAnimals() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);

        Vector2d location = new Vector2d(0, 0);
        Animal animal = new Animal(location, configuration);
        Plant negativeEnergyPlant = new Plant(location, -50);
        animal.eatPlant(negativeEnergyPlant);

        assertEquals(0, animal.getEnergy());
    }

    @Test
    public void testGetOrderedAnimals() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        ForestedEquatorMap map = new ForestedEquatorMap(configuration);

        map.getAnimals().sort(Comparator.comparingInt(Animal::getEnergy));
        assertTrue(map.getOrderedAnimals().get(0).getEnergy() <= map.getOrderedAnimals().get(1).getEnergy());
    }

    @Test
    public void testIsOccupiedByPlant() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        ForestedEquatorMap map = new ForestedEquatorMap(configuration);

        Vector2d position = new Vector2d(3, 3);
        assertFalse(map.isOccupiedByPlant(position));
    }
}
