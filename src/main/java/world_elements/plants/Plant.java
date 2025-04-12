package world_elements.plants;
import util.Vector2d;
import world_elements.WorldElement;

public class Plant implements WorldElement {
    private final Vector2d location;
    private final int energy;

    public Plant(Vector2d location, int energy) {
        this.location = location;
        this.energy = energy;
    }
    public Vector2d getLocation(){
        return location;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.location.equals(position);
    }

    @Override
    public String getImageName() {
        return "grass.png";
    }

    @Override
    public String getVisualLabel() {
        return String.format("E: %d", this.energy);
    }

    public String toString(){
        return "*";
    }
    public int getEnergy(){
        return energy;
    }
}
