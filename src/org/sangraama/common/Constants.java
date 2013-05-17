package org.sangraama.common;

import org.jbox2d.common.Vec2;

public final class Constants {
  //World constants
  public static final Vec2 gravity = new Vec2(0.0f, 0.0f);
  public static final boolean doSleep = true;
  public static final int fps = 30;
  public static final float timeStep = 1.0f / fps;
  public static final int velocityIterations = 6;
  public static final int positionIterations = 2;
}
