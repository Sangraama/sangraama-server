package org.sangraama.gameLogic;

import java.util.ArrayList;

import org.jbox2d.dynamics.World;
import org.sangraama.common.Constants;
import org.sangraama.controller.PlayerServlet;

public class GameWorld implements Runnable {
    private static GameWorld gameWorld = null;
    public int i = 0;
    public World world;
    private boolean execute = true;
    private ArrayList<JBoxPlayer> players = null;

    public GameWorld() {
	world = new World(Constants.gravity, Constants.doSleep);
	players = new ArrayList<JBoxPlayer>();
    }

    public static GameWorld getInstance() {
	if (gameWorld == null) {
	    gameWorld = new GameWorld();
	}
	return gameWorld;

    }

    public void init() {

    }

    public void seti() {
	i++;
    }

    public void update() {

    }

    public void pushUpdate() {
    }

    @Override
    public void run() {
	init();
	while (execute) {
	    update();
	    world.step(Constants.timeStep, Constants.velocityIterations,
		    Constants.positionIterations);
	    pushUpdate();
	}
    }

    public void stopSimulating() {
	execute = false;
    }

    public void addPlayerQueue() {

    }
}
