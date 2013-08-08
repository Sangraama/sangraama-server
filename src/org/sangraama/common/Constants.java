package org.sangraama.common;

import org.jbox2d.common.Vec2;


public final class Constants {
    // Debug and Log constants
    public static final boolean L = true;
    public static final boolean D = true;

    // World constants
    public static final float gravityHorizontal = 0.0f;
    public static final float gavityVertical = 0.0f;
    public static final Vec2 gravity = new Vec2(0f, 0f);
    public static final boolean doSleep = true;
    public static final int fps = 4; // Default greater than 30
    public static final float timeStep = 1.0f / fps;
    public static final int velocityIterations = 6; // Default 6
    public static final int positionIterations = 2; // Defaults 2
    public static final int simulatingDelay = 50; // delay of simulating
    
    //Game Client Canvas width & height
    public static final int canvasWidth=600;
    public static final int canvasHeight=600;
    public static float TO_RADIANS = (float) (Math.PI / 180);
    public static float FROM_RADIANS = (float) (180 / Math.PI);
    
}
