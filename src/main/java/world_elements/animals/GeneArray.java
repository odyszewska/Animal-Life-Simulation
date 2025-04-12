package world_elements.animals;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeneArray {
    private final int[] genes;
    private final int numberOfGenes;
    private int currentGeneIndex;
    private final int minNumberOfMutations;
    private final int maxNumberOfMutations;

    GeneArray(int numberOfGenes, int minNumberOfMutations, int maxNumberOfMutations) {
        this.numberOfGenes = numberOfGenes;
        this.genes = new int[numberOfGenes];
        Random generator = new Random();
        this.currentGeneIndex = generator.nextInt(numberOfGenes);
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;
        this.randomizeArray();
    }

    void randomizeArray() {
        Random generator = new Random();
        int[] genesToMutate = this.getGenesToMutate();
        for (int i : genesToMutate) {
            this.genes[i] = generator.nextInt(0, 8);
        }
    }

    void slightRandomization() {
        Random generator = new Random();
        int[] genesToMutate = this.getGenesToMutate();

        for (int i : genesToMutate) {
            int modify = generator.nextInt(-1, 1);
            if (modify == 0) {
                modify++;
            }
            this.genes[i] += modify;
            this.genes[i] %= 8;
            if (this.genes[i] < 0) {
                this.genes[i] = 7;
            }
        }
    }

    public void mixGenePool(Animal parent1, Animal parent2) {
        Random random = new Random();
        int first_parent = random.nextInt(1, 3);
        int parent1_coefficient = parent1.getEnergy() / (parent1.getEnergy() + parent2.getEnergy());
        int parent2_coefficient = parent2.getEnergy() / (parent1.getEnergy() + parent2.getEnergy());
        if (first_parent == 1) {
            System.arraycopy(parent1.getGenes().genes, 0, this.genes, 0, this.numberOfGenes * parent1_coefficient);
            System.arraycopy(parent2.getGenes().genes, this.numberOfGenes * parent2_coefficient, this.genes, this.numberOfGenes * parent2_coefficient, this.numberOfGenes - this.numberOfGenes * parent2_coefficient);
        } else {
            System.arraycopy(parent2.getGenes().genes, 0, this.genes, 0, this.numberOfGenes * parent2_coefficient);
            System.arraycopy(parent1.getGenes().genes, this.numberOfGenes * parent1_coefficient, this.genes, this.numberOfGenes * parent1_coefficient, this.numberOfGenes - this.numberOfGenes * parent1_coefficient);
        }
    }

    int getCurrentGene() {
        return this.genes[currentGeneIndex];
    }

    void changeGene() {
        this.currentGeneIndex++;
        this.currentGeneIndex %= this.numberOfGenes;
    }

    private int[] getGenesToMutate() {
        Random random = new Random();
        int numberOfMutations = random.nextInt(this.minNumberOfMutations, this.maxNumberOfMutations + 1);
        Set<Integer> genesToMutate = new HashSet<>();
        while (genesToMutate.size() < numberOfMutations && genesToMutate.size() < this.numberOfGenes) {
            genesToMutate.add(random.nextInt(0, this.numberOfGenes));
        }
        return genesToMutate.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] getGenesArray() {
        return this.genes;
    }

    public int getCurrentGeneIndex() {
        return this.currentGeneIndex;
    }

}
