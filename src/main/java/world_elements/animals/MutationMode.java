package world_elements.animals;

public enum MutationMode {
    NORMAL(1),
    SLIGHT(2);

    public final int modeNumber;

    MutationMode(int modeNumber) {
        this.modeNumber = modeNumber;
    }
}
