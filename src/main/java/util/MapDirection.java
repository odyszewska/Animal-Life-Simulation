package util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum MapDirection {
    NORTH("Północ", "N"),
    SOUTH("Południe", "S"),
    WEST("Zachód", "W"),
    EAST("Wschód", "E"),
    NORTHWEST("Północny zachód", "NW"),
    NORTHEAST("Północny wschód", "NE"),
    SOUTHWEST("Południowy zachód", "SW"),
    SOUTHEAST("Południowy wschód", "SE");

    MapDirection(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    private final String fullName;
    private final String shortName;

    public String toString() {
        return this.shortName;
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> MapDirection.NORTHEAST;
            case NORTHEAST -> MapDirection.EAST;
            case EAST -> MapDirection.SOUTHEAST;
            case SOUTHEAST -> MapDirection.SOUTH;
            case SOUTH -> MapDirection.SOUTHWEST;
            case SOUTHWEST -> MapDirection.WEST;
            case WEST -> MapDirection.NORTHWEST;
            case NORTHWEST -> MapDirection.NORTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHWEST -> new Vector2d(-1, -1);
        };
    }

    public MapDirection opposite() {
        return switch (this) {
            case NORTH -> MapDirection.SOUTH;
            case SOUTH -> MapDirection.NORTH;
            case WEST -> MapDirection.EAST;
            case EAST -> MapDirection.WEST;
            case NORTHWEST -> MapDirection.SOUTHEAST;
            case NORTHEAST -> MapDirection.SOUTHWEST;
            case SOUTHWEST -> MapDirection.NORTHEAST;
            case SOUTHEAST -> MapDirection.NORTHWEST;
        };
    }

    public static MapDirection getRandomMapDirection() {
        List<MapDirection> directions = Arrays.asList(MapDirection.values());
        Random random = new Random();
        return directions.get(random.nextInt(directions.size()));
    }

    public String getFullName() {
        return fullName;
    }
}
