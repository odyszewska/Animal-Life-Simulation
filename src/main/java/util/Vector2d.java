package util;

import java.util.Random;

public record Vector2d(int x, int y) {

    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public static Vector2d getRandomVector(int lowerXBound, int lowerYBound, int upperXBound, int upperYBound) {
        Random random = new Random();
        int x = random.nextInt(lowerXBound, upperXBound);
        int y = random.nextInt(lowerYBound, upperYBound);
        return new Vector2d(x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector2d that)) {
            return false;
        }
        return this.x == that.x && this.y == that.y;
    }

}
