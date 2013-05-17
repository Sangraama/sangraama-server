package org.sangraama.common;

import org.jbox2d.common.Vec2;
import org.sangraama.controller.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Constants {
    // Debug and Log constants
    public static final boolean L = true;
    public static final boolean D = true;
    public static final Logger log = LoggerFactory.getLogger(EventHandler.class);

    // World constants
    public static final Vec2 gravity = new Vec2(0.0f, 0.0f);
    public static final boolean doSleep = true;
    public static final int fps = 1;
    public static final float timeStep = 1.0f / fps;
    public static final int velocityIterations = 6;
    public static final int positionIterations = 2;
}
