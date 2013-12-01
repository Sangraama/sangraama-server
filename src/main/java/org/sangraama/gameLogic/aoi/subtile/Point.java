package org.sangraama.gameLogic.aoi.subtile;

/**
 * Currently hardcoded to 2 dimensions, but could be extended..
 *
 * @author: Gihan Karunarathne
 * Date: 12/1/13
 * Time: 9:00 PM
 * @email: gckarunarathne@gmail.com
 */
public class Point {
    /**
     * The (x, y) coordinates of the point.
     */
    public float x, y;

    /**
     * Constructor.
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy from another point into this one
     */
    public void set(Point other) {
        x = other.x;
        y = other.y;
    }

    /**
     * Print as a string in format "(x, y)"
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * @return X coordinate rounded to an int
     */
    public float getX() {
        return x;
    }

    /**
     * @return Y coordinate rounded to an int
     */
    public float getY() {
        return y;
    }
}