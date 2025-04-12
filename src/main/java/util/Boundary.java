package util;

public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {
    public int getWidth() {
        return upperRight.x() - lowerLeft.x() + 1;
    }

    public int getHeight() {
        return upperRight.y() - lowerLeft.y() + 1;
    }
}
