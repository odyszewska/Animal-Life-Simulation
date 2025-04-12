package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Vector2dTest {
    @Test
    public void testToString() {
        Vector2d v = new Vector2d(2, 3);
        assertEquals("(2, 3)", v.toString());

        v = new Vector2d(-1, 5);
        assertEquals("(-1, 5)", v.toString());
    }

    @Test
    public void testPrecedes() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(3, 4);
        assertTrue(v1.precedes(v2));
        assertFalse(v2.precedes(v1));

        v1 = new Vector2d(3, 4);
        v2 = new Vector2d(3, 4);
        assertTrue(v1.precedes(v2));
    }

    @Test
    public void testFollows() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(3, 4);
        assertFalse(v1.follows(v2));
        assertTrue(v2.follows(v1));

        v1 = new Vector2d(3, 4);
        v2 = new Vector2d(3, 4);
        assertTrue(v1.follows(v2));
    }

    @Test
    public void testAdd() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(1, 4);
        Vector2d result = v1.add(v2);
        assertEquals(new Vector2d(3, 7), result);
    }

    @Test
    public void testSubtract() {
        Vector2d v1 = new Vector2d(5, 7);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d result = v1.subtract(v2);
        assertEquals(new Vector2d(3, 4), result);
    }

    @Test
    public void testUpperRight() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(5, 1);
        Vector2d result = v1.upperRight(v2);
        assertEquals(new Vector2d(5, 3), result);
    }

    @Test
    public void testLowerLeft() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(5, 1);
        Vector2d result = v1.lowerLeft(v2);
        assertEquals(new Vector2d(2, 1), result);
    }

    @Test
    public void testOpposite() {
        Vector2d v = new Vector2d(2, 3);
        Vector2d result = v.opposite();
        assertEquals(new Vector2d(-2, -3), result);
    }

    @Test
    public void testEquals() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(3, 4);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(v2, v3);
        assertNotEquals(null, v1);
    }
}