package simulation;

import maps.WorldMap;
import presenters.MapChangeListener;
import util.Vector2d;
import world_elements.animals.Animal;
import world_elements.plants.Plant;

import java.util.*;

public class Simulation implements Runnable {
    public Simulation(WorldConfiguration configuration, WorldMap map) {
        configuration.validateData();
        this.configuration = configuration;
        this.running = false;
        this.map = map;
        this.dayCount = 1;
        this.id = UUID.randomUUID();
        this.observers = new ArrayList<>();
    }

    private void takeATurn() {
        this.map.deleteDeadAnimals(this.dayCount);
        List<Animal> animals = this.map.getOrderedAnimals();
        for (Animal animal : animals) {
            Vector2d previousLocation = animal.getLocation();
            animal.makeAMove();
            this.map.removeAnimal(animal, previousLocation);
            this.map.addWorldElement(animal);
        }
        for (Animal animal : animals) {
            Optional<Plant> current_plant = this.map.getPlant(animal.getLocation());
            current_plant.ifPresent(animal::eatPlant);
            current_plant.ifPresent(this.map::removePlant);
        }
        HashSet<Vector2d> fieldsCheckedForReproduction = new HashSet<>();
        for (Animal animal : animals) {
            if (fieldsCheckedForReproduction.contains(animal.getLocation())) {
                continue;
            }
            fieldsCheckedForReproduction.add(animal.getLocation());

            List<Animal> animalsToReproduce = this.map.getAnimalsOnASpecificField(animal.getLocation());
            for (int j = 0; j < animalsToReproduce.size() - 1; j += 2) {
                if (animalsToReproduce.get(j).getEnergy() > this.configuration.energyNecessaryToReproduce &&
                        animalsToReproduce.get(j + 1).getEnergy() > this.configuration.energyNecessaryToReproduce) {
                    Animal newAnimal = animalsToReproduce.get(j).reproduce(animalsToReproduce.get(j + 1));
                    this.map.addWorldElement(newAnimal);
                }
            }
        }
        this.map.makeNewPlants(this.configuration.plantsPerDay);
        this.dayCount++;
        this.mapChanged();
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (this.running) {
            this.takeATurn();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        this.running = false;
        this.mapChanged();
    }

    public void start() {
        if (!this.running) {
            this.running = true;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public int getDayCount() {
        return this.dayCount;
    }

    public boolean isRunning() {
        return this.running;
    }

    public UUID getId() {
        return this.id;
    }

    private void mapChanged() {
        for (MapChangeListener observer : this.observers) {
            observer.mapChanged(this.map);
        }
    }

    public void addObserver(MapChangeListener listener) {
        this.observers.add(listener);
    }

    private final WorldConfiguration configuration;
    private boolean running;
    private final WorldMap map;
    private int dayCount;
    private final UUID id;
    List<MapChangeListener> observers;

    public WorldMap getMap() {
        return this.map;
    }
}
