package world_elements.animals;

import simulation.WorldConfiguration;
import util.MapDirection;
import util.Vector2d;
import world_elements.WorldElement;
import world_elements.plants.Plant;

import java.util.Comparator;
import java.util.Random;

public class Animal implements WorldElement, Comparable<Animal> {
    private Vector2d location;
    private int numberOfChildren;
    private int energy;
    private final GeneArray genes;
    private MapDirection direction;
    private final MutationMode mutationMode;
    private int age;
    private final WorldConfiguration configuration;
    private final int tieBreaker;
    private int eatenPlants;
    private boolean isDead;
    private int timeOfDeath;

    public Animal(Vector2d parentsLocation, WorldConfiguration configuration) {
        this.location = parentsLocation;
        this.numberOfChildren = 0;
        this.energy = configuration.startingEnergyOfAnimals;
        this.genes = new GeneArray(configuration.genomeLength, configuration.minMutationNumber, configuration.maxMutationNumber);
        this.direction = MapDirection.getRandomMapDirection();
        this.age = 1;
        this.mutationMode = configuration.mutationMode;
        this.configuration = configuration;
        this.tieBreaker = new Random().nextInt();
        this.eatenPlants = 0;
    }

    public Animal reproduce(Animal other) {
        Animal child = new Animal(this.location, this.configuration);
        child.genes.mixGenePool(this, other);
        child.mutate();
        child.energy = this.configuration.energyUsedToReproduce * 2;
        this.energy -= this.configuration.energyUsedToReproduce;
        other.energy -= this.configuration.energyUsedToReproduce;
        this.numberOfChildren += 1;
        other.numberOfChildren += 1;
        return child;
    }

    @Override
    public Vector2d getLocation() {
        return this.location;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.location.equals(position);
    }

    @Override
    public String getImageName() {
        return switch (this.direction) {
            case NORTH -> "up.png";
            case NORTHEAST -> "up_right.png";
            case EAST -> "right.png";
            case SOUTHEAST -> "down_right.png";
            case SOUTH -> "down.png";
            case SOUTHWEST -> "down_left.png";
            case WEST -> "left.png";
            case NORTHWEST -> "up_left.png";
        };
    }

    @Override
    public String getVisualLabel() {
        return String.format("E: %d", this.energy);
    }


    public void makeAMove() {
        this.move();
        this.turnAndChangeGene();
        this.getOlder();
        this.energy -= 1;
    }

    public void getOlder() {
        this.age++;
    }

    public void mutate() {
        switch (this.mutationMode) {
            case NORMAL:
                this.genes.randomizeArray();
            case SLIGHT:
                this.genes.slightRandomization();
        }
    }

    public void eatPlant(Plant plant) {
        this.energy += plant.getEnergy();
        this.eatenPlants += 1;
    }

    @Override
    public int compareTo(Animal other) {
        return Comparator.comparingInt((Animal a) -> a.energy).thenComparingInt(a -> a.age).thenComparingInt(a -> a.numberOfChildren)
                .thenComparingInt(a -> a.tieBreaker).compare(this, other);
    }

    public void move() {
        Vector2d newLocation = this.location.add(this.direction.toUnitVector());

        if (newLocation.x() < 0) {
            newLocation = new Vector2d(this.configuration.mapWidth - 1, newLocation.y());
        } else if (newLocation.x() > this.configuration.mapWidth - 1) {
            newLocation = new Vector2d(0, newLocation.y());
        }

        if (newLocation.y() < 0) {
            newLocation = new Vector2d(newLocation.x(), 0);
            this.direction = this.direction.opposite();
        } else if (newLocation.y() > this.configuration.mapHeight - 1) {
            newLocation = new Vector2d(newLocation.x(), this.configuration.mapHeight - 1);
            this.direction = this.direction.opposite();
        }

        this.location = newLocation;
    }


    public int getEnergy() {
        return this.energy;
    }

    public GeneArray getGenes() {
        return this.genes;
    }

    public void turnAndChangeGene() {
        int currentGene = this.genes.getCurrentGene();
        for (int i = 0; i < currentGene; i++) {
            this.direction = this.direction.next();
        }
        this.genes.changeGene();
    }

    public int getNumberOfChildren() {
        return this.numberOfChildren;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public int getAge() {
        return this.age;
    }

    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    public double getEnergyPercentage() {
        return (double) this.energy / this.configuration.startingEnergyOfAnimals;
    }

    public int getEatenPlants() {
        return this.eatenPlants;
    }

    public void kill(int day) {
        this.isDead = true;
        this.timeOfDeath = day;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public int getTimeOfDeath() {
        return this.timeOfDeath;
    }
}
