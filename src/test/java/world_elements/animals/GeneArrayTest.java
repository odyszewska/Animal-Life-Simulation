package world_elements.animals;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.WorldConfiguration;

import java.util.Arrays;

class GeneArrayTest {
    private WorldConfiguration configuration;
    private GeneArray geneArray;

    @BeforeEach
    void setUp() {
        this.configuration = new WorldConfiguration(
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
        this.geneArray = new GeneArray(8, 1, 3);
    }

    @Test
    void testGeneArrayInitialization() {
        assertNotNull(geneArray);
        assertEquals(8, geneArray.getGenesArray().length);
        assertTrue(Arrays.stream(geneArray.getGenesArray()).allMatch(gene -> gene >= 0 && gene < 8));
    }

    @Test
    void testSlightRandomization() {
        int[] initialGenes = geneArray.getGenesArray().clone();
        geneArray.slightRandomization();
        int[] mutatedGenes = geneArray.getGenesArray();

        for (int i = 0; i < initialGenes.length; i++) {
            if (initialGenes[i] != mutatedGenes[i]) {
                assertTrue(Math.abs(initialGenes[i] - mutatedGenes[i]) == 1 || Math.abs(initialGenes[i] - mutatedGenes[i]) == 7);
            }
        }
    }
}
