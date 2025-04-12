package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    @Test
    public void testToString() {
        assertEquals("N", MapDirection.NORTH.toString());
        assertEquals("S", MapDirection.SOUTH.toString());
        assertEquals("W", MapDirection.WEST.toString());
        assertEquals("E", MapDirection.EAST.toString());
        assertEquals("NW", MapDirection.NORTHWEST.toString());
        assertEquals("NE", MapDirection.NORTHEAST.toString());
        assertEquals("SW", MapDirection.SOUTHWEST.toString());
        assertEquals("SE", MapDirection.SOUTHEAST.toString());
    }

    @Test
    public void testNext() {
        assertEquals(MapDirection.NORTHEAST, MapDirection.NORTH.next());
        assertEquals(MapDirection.EAST, MapDirection.NORTHEAST.next());
        assertEquals(MapDirection.SOUTHEAST, MapDirection.EAST.next());
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTHEAST.next());
        assertEquals(MapDirection.SOUTHWEST, MapDirection.SOUTH.next());
        assertEquals(MapDirection.WEST, MapDirection.SOUTHWEST.next());
        assertEquals(MapDirection.NORTHWEST, MapDirection.WEST.next());
        assertEquals(MapDirection.NORTH, MapDirection.NORTHWEST.next());
    }

    @Test
    public void testToUnitVector() {
        assertEquals(new Vector2d(0, 1), MapDirection.NORTH.toUnitVector());
        assertEquals(new Vector2d(1, 1), MapDirection.NORTHEAST.toUnitVector());
        assertEquals(new Vector2d(1, 0), MapDirection.EAST.toUnitVector());
        assertEquals(new Vector2d(1, -1), MapDirection.SOUTHEAST.toUnitVector());
        assertEquals(new Vector2d(0, -1), MapDirection.SOUTH.toUnitVector());
        assertEquals(new Vector2d(-1, -1), MapDirection.SOUTHWEST.toUnitVector());
        assertEquals(new Vector2d(-1, 0), MapDirection.WEST.toUnitVector());
        assertEquals(new Vector2d(-1, 1), MapDirection.NORTHWEST.toUnitVector());
    }

    @Test
    public void testGetFullName() {
        assertEquals("Północ", MapDirection.NORTH.getFullName());
        assertEquals("Południe", MapDirection.SOUTH.getFullName());
        assertEquals("Zachód", MapDirection.WEST.getFullName());
        assertEquals("Wschód", MapDirection.EAST.getFullName());
        assertEquals("Północny zachód", MapDirection.NORTHWEST.getFullName());
        assertEquals("Północny wschód", MapDirection.NORTHEAST.getFullName());
        assertEquals("Południowy zachód", MapDirection.SOUTHWEST.getFullName());
        assertEquals("Południowy wschód", MapDirection.SOUTHEAST.getFullName());
    }
}