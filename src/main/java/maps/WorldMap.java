package maps;

import simulation.WorldConfiguration;
import util.Boundary;
import util.Vector2d;
import world_elements.WorldElement;
import world_elements.animals.Animal;
import world_elements.plants.Plant;

import java.util.*;

public abstract class WorldMap {
    protected final Map<Vector2d, List<WorldElement>> elements;
    protected final int mapHeight;
    protected final int mapWidth;
    protected final int lowerEquatorBound;
    protected int upperEquatorBound;
    protected final WorldConfiguration configuration;
    private final Boundary bounds;

    public WorldMap(WorldConfiguration configuration) {
        this.configuration = configuration;
        this.elements = new HashMap<>();
        this.mapHeight = configuration.mapHeight;
        this.mapWidth = configuration.mapWidth;
        for (int i = 0; i < configuration.startingNumberOfAnimals; i++) {
            Vector2d randomVector = Vector2d.getRandomVector(0, 0, mapWidth - 1, mapHeight - 1);
            this.addWorldElement(new Animal(randomVector, configuration));
        }
        this.lowerEquatorBound = mapHeight / 2 - mapHeight / 10;
        this.upperEquatorBound = mapHeight / 2 + mapHeight / 10;
        if (this.lowerEquatorBound == this.upperEquatorBound) {
            this.upperEquatorBound += 1;
        }
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));

        this.makeNewPlants(configuration.startingNumberOfPlants);
    }

    public boolean isOccupiedByPlant(Vector2d position) {
        return this.elements.containsKey(position) && this.elements.get(position).stream().anyMatch(worldElement -> worldElement instanceof Plant);
    }

    public boolean isOccupiedByAnimal(Vector2d position) {
        return this.elements.containsKey(position) && this.elements.get(position).stream().anyMatch(worldElement -> worldElement instanceof Animal);
    }

    public abstract void makeNewPlants(int numberOfPlants);

    public abstract void updateMapAfterAnimalDeath(Vector2d position);

    protected Vector2d generateVectorsUntilFreeOfPlants(int lowerYBound, int upperXBound, int upperYBound) {
        int i = 0;
        Vector2d randomVector = Vector2d.getRandomVector(0, lowerYBound, upperXBound, upperYBound);
        while (this.isOccupiedByPlant(randomVector) && i < (upperYBound - lowerYBound) * (upperXBound + 1)) {
            randomVector = Vector2d.getRandomVector(0, lowerYBound, upperXBound, upperYBound);
            i++;
        }
        return randomVector;
    }

    public void deleteDeadAnimals(int day) {
        for (Animal animal : this.getAnimals()) {
            if (animal.getEnergy() == 0) {
                this.removeAnimal(animal, animal.getLocation());
                this.updateMapAfterAnimalDeath(animal.getLocation());
                animal.kill(day);
            }
        }
    }

    public List<Animal> getAnimals() {
        List<Animal> animals = new ArrayList<>();
        for (List<WorldElement> elements : this.elements.values()) {
            for (WorldElement element : elements) {
                if (element instanceof Animal) {
                    animals.add((Animal) element);
                }
            }
        }
        return animals;
    }

    public List<Animal> getAnimalsOnASpecificField(Vector2d location) {
        return this.getAnimals().stream().filter(animal -> animal.getLocation().equals(location)).sorted().toList();
    }

    public List<Plant> getPlants() {
        List<Plant> plants = new ArrayList<>();
        for (List<WorldElement> elements : this.elements.values()) {
            for (WorldElement element : elements) {
                if (element instanceof Plant) {
                    plants.add((Plant) element);
                }
            }
        }
        return plants;
    }

    public Optional<Plant> getPlant(Vector2d location) {
        List<WorldElement> elements = this.elements.get(location);
        for (WorldElement element : elements) {
            if (element instanceof Plant) {
                return Optional.of((Plant) element);
            }
        }
        return Optional.empty();
    }

    public List<Animal> getOrderedAnimals() {
        List<Animal> animals = this.getAnimals();
        Collections.sort(animals);
        return animals;
    }

    public void removeAnimal(Animal animal, Vector2d location) {
        this.elements.get(location).remove(animal);
    }

    public void removePlant(Plant plant) {
        this.elements.get(plant.getLocation()).remove(plant);
    }

    public Boundary getBounds() {
        return this.bounds;
    }

    public void addWorldElement(WorldElement worldElement) {
        if (!this.elements.containsKey(worldElement.getLocation())) {
            this.elements.put(worldElement.getLocation(), new ArrayList<>());
        }
        this.elements.get(worldElement.getLocation()).add(worldElement);
    }

    public int getLowerEquatorBound() {
        return this.lowerEquatorBound;
    }

    public int getUpperEquatorBound() {
        return this.upperEquatorBound;
    }
}
