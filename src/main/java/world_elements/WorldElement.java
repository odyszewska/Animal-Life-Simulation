package world_elements;

import util.Vector2d;

public interface WorldElement {
    Vector2d getLocation();

    boolean isAt(Vector2d position);

    String getImageName();
    String getVisualLabel();
}
