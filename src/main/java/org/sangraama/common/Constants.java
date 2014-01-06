package org.sangraama.common;

import org.jbox2d.common.Vec2;

public final class Constants {
    // Debug and Log constants
    public static final boolean L = true;
    public static final boolean D = true;

    /**
     * JBox2D World configurations Warning !!!. Don't change if you don't know exactly their
     * meanings More details about JBox2D physics engine refer to : http://box2d.org/manual.pdf
     */
    public static final float gravityHorizontal = 0.0f;
    public static final float gavityVertical = 0.0f;
    public static final boolean doSleep = true;
    public static final int velocityIterations = 8; // Default 6
    public static final int positionIterations = 3; // Defaults 2
    public static final int fps = 20; // Default 20 ( 20 < fps < 30 ) refer : section 2.4 in manual

    /**
     * Auto calculated
     */
    public static final Vec2 gravity = new Vec2(gravityHorizontal, gavityVertical);
    public static final float timeStep = 1.0f / fps;
    public static final int simulatingDelay = 1000 / fps; // delay of simulating

    public static final float TO_RADIANS = (float) (Math.PI / 180);
    public static final float FROM_RADIANS = (float) (180 / Math.PI);

    // Scaling JBox2D units and client side pixels
    public static final float scale = 32.0f;

    public static final int playersLimit = 500;
    /* Ex: 32 means 1 unit in JBox2D is showing as 32 pixels in client side */

    public static final int loadBalancingDelay = 5000;

}
