package world_elements.animals;

import static org.junit.jupiter.api.Assertions.*;

import maps.ForestedEquatorMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.Simulation;
import simulation.WorldConfiguration;
import util.Vector2d;
import world_elements.plants.Plant;
import util.MapDirection;
import java.util.Arrays;


class AnimalTest {
    WorldConfiguration configuration;
    Simulation simulation;
    @BeforeEach
    void setUp() {
        WorldConfiguration configuration = new WorldConfiguration(
                10, 10, // mapHeight, mapWidth
                10, // startingNumberOfPlants
                5, // energyFromOnePlant
                5, // startingNumberOfAnimals
                50, // startingEnergyOfAnimals
                30, // energyNecessaryToReproduce
                2, // plantsPerDay
                10, // energyUsedToReproduce
                3, // maxMutationNumber
                1, // minMutationNumber
                MutationMode.NORMAL, // mutationMode
                8 // genomeLength
        );
        this.simulation = new Simulation(configuration, new ForestedEquatorMap(configuration));
        this.configuration = configuration;
    }

    @Test
    void testAnimal() {
        Vector2d location = new Vector2d(0, 0);
        Animal animal = new Animal(location, configuration);

        assertNotNull(animal);
        assertEquals(location, animal.getLocation());
        assertEquals(50, animal.getEnergy());
        assertEquals(0, animal.getNumberOfChildren());
        assertNotNull(animal.getGenes());
        assertNotNull(animal.getDirection());
        assertEquals(0, animal.getAge());
    }

    @Test
    void testReproduce() {

        Vector2d location = new Vector2d(0, 0);
        Animal parent1 = new Animal(location, configuration);
        Animal parent2 = new Animal(location, configuration);

        Animal child = parent1.reproduce(parent2);

        assertNotNull(child);
        assertEquals(location, child.getLocation());
        assertEquals(configuration.energyUsedToReproduce * 2, child.getEnergy());
        assertEquals(40, parent1.getEnergy());
        assertEquals(40, parent2.getEnergy());
        assertEquals(1, parent1.getNumberOfChildren());
        assertEquals(1, parent2.getNumberOfChildren());
    }

    @Test
    void testMakeAMove() {
        Vector2d location = new Vector2d(0, 0);
        Animal animal = new Animal(location, configuration);

        animal.makeAMove();

        assertNotEquals(location, animal.getLocation());
        assertEquals(1, animal.getAge());
    }

    @Test
    void testEatPlant() {
        Vector2d location = new Vector2d(0, 0);
        Animal animal = new Animal(location, configuration);
        Plant plant = new Plant(location, configuration.energyFromOnePlant);

        int initialEnergy = animal.getEnergy();
        animal.eatPlant(plant);

        assertEquals(initialEnergy + configuration.energyFromOnePlant, animal.getEnergy());
    }

    @Test
    void testCompareTo() {
        Vector2d location = new Vector2d(0, 0);
        Animal animal1 = new Animal(location, configuration);
        Animal animal2 = new Animal(location, configuration);

        animal1.makeAMove();
        animal2.eatPlant(new Plant(location, configuration.energyFromOnePlant));

        assertTrue(animal1.compareTo(animal2) != 0);
    }

    @Test
    void testMove() {
        Vector2d location = new Vector2d(5, 5);
        Animal animal = new Animal(location, configuration);
        animal.setDirection(MapDirection.NORTHEAST);
        animal.move();

        assertEquals(new Vector2d(6, 6), animal.getLocation());
    }

    @Test
    void testGetOlder() {
        Vector2d location = new Vector2d(0, 0);
        Animal animal = new Animal(location, configuration);

        animal.getOlder();
        assertEquals(1, animal.getAge());
    }

    @Test
    void testMutateNormal() {
        Vector2d location = new Vector2d(0, 0);
        Animal animal = new Animal(location, configuration);
        int[] initialGenes = animal.getGenes().getGenesArray().clone();

        animal.mutate();

        assertFalse(Arrays.equals(initialGenes, animal.getGenes().getGenesArray()));
    }

    @Test
    void testMutateSlight() {
        Vector2d location = new Vector2d(0, 0);
        WorldConfiguration configuration2 = new WorldConfiguration(
                10, 10, // mapHeight, mapWidth
                10, // startingNumberOfPlants
                5, // energyFromOnePlant
                5, // startingNumberOfAnimals
                50, // startingEnergyOfAnimals
                30, // energyNecessaryToReproduce
                2, // plantsPerDay
                10, // energyUsedToReproduce
                3, // maxMutationNumber
                1, // minMutationNumber
                MutationMode.SLIGHT, // mutationMode
                8 // genomeLength
        );
        Animal animal = new Animal(location, configuration2);
        int[] initialGenes = animal.getGenes().getGenesArray().clone();
        animal.mutate();

        assertFalse(Arrays.equals(initialGenes, animal.getGenes().getGenesArray()));
    }

    @Test
    void testMoveWrapLeftToRight() {
        Vector2d location = new Vector2d(0, 5);
        Animal animal = new Animal(location, configuration);
        animal.setDirection(MapDirection.WEST);
        animal.move();

        assertEquals(new Vector2d(configuration.mapWidth - 1, 5), animal.getLocation());
    }

    @Test
    void testMoveWrapRightToLeft() {
        Vector2d location = new Vector2d(configuration.mapHeight - 1, 5);
        Animal animal = new Animal(location, configuration);
        animal.setDirection(MapDirection.EAST);
        animal.move();

        assertEquals(new Vector2d(0, 5), animal.getLocation());
    }

    @Test
    void testMoveBlockedAtNorthPole() {
        Vector2d location = new Vector2d(5, configuration.mapHeight - 1);
        Animal animal = new Animal(location, configuration);
        animal.setDirection(MapDirection.NORTH);
        animal.move();

        assertEquals(location, animal.getLocation());
        assertEquals(MapDirection.SOUTH, animal.getDirection());
    }

    @Test
    void testMoveBlockedAtSouthPole() {
        Vector2d location = new Vector2d(5, 0);
        Animal animal = new Animal(location, configuration);
        animal.setDirection(MapDirection.SOUTH);
        animal.move();

        assertEquals(location, animal.getLocation());
        assertEquals(MapDirection.NORTH, animal.getDirection());
    }
}