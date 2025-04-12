package maps;

import org.junit.jupiter.api.Test;
import simulation.WorldConfiguration;
import util.Vector2d;
import world_elements.animals.Animal;
import world_elements.animals.MutationMode;
import world_elements.plants.Plant;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LifeGivingCorpsesMapTest {
    @Test
    public void testWorldMapInitialization() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        LifeGivingCorpsesMap map = new LifeGivingCorpsesMap(configuration);

        assertEquals(10, map.mapHeight);
        assertEquals(10, map.mapWidth);
        assertNotNull(map.configuration);
        assertEquals(10, map.getAnimals().size());
        assertEquals(50, map.getAnimals().getFirst().getEnergy());
    }

    @Test
    public void testMakeNewPlants() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 0, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        LifeGivingCorpsesMap map = new LifeGivingCorpsesMap(configuration);

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
    public void testUpdateMapAfterAnimalDeath() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        LifeGivingCorpsesMap map = new LifeGivingCorpsesMap(configuration);

        Vector2d position = new Vector2d(5, 5);
        map.updateMapAfterAnimalDeath(position);

        List<Vector2d> neighbors = List.of(new Vector2d(4, 4), new Vector2d(5, 4), new Vector2d(6, 4),
                new Vector2d(4, 5), new Vector2d(6, 5),
                new Vector2d(4, 6), new Vector2d(5, 6), new Vector2d(6, 6)
        );

        neighbors.forEach(neighbor -> assertTrue(map.getPreferredFields().contains(neighbor)));

    }

    @Test
    public void testDeleteDeadAnimals() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
//        Set<Vector2d> preferredFields  = new HashSet<>();
        Vector2d location = new Vector2d(0, 0);
        Animal animal = new Animal(location, configuration);
        Plant negativeEnergyPlant = new Plant(location, -50);
        animal.eatPlant(negativeEnergyPlant);

        assertEquals(0, animal.getEnergy());
//        assertNotNull(preferredFields);
    }

    @Test
    public void testGetOrderedAnimals() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        LifeGivingCorpsesMap map = new LifeGivingCorpsesMap(configuration);

        map.getAnimals().sort(Comparator.comparingInt(Animal::getEnergy));
        assertTrue(map.getOrderedAnimals().get(0).getEnergy() <= map.getOrderedAnimals().get(1).getEnergy());
    }

    @Test
    public void testIsOccupiedByPlant() {
        WorldConfiguration configuration = new WorldConfiguration(10, 10, 5, 10, 10, 50, 20, 5, 10, 5, 2, MutationMode.NORMAL, 15);
        LifeGivingCorpsesMap map = new LifeGivingCorpsesMap(configuration);

        Vector2d position = new Vector2d(3, 3);
        assertFalse(map.isOccupiedByPlant(position));
    }

}
