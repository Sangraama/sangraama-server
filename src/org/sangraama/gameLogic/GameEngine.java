package org.sangraama.gameLogic;

import java.util.ArrayList;

import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;

public class GameEngine implements Runnable {
    private static GameEngine gameEngine = null;
    private World world=null;
    private boolean execute = true;
    private boolean isNewPlayerAvai = false;
    private ArrayList<Player> players = null;
    private ArrayList<Player> newPlayerQueue = null;

    // this method only access via class
    private GameEngine() {
	world = new World(Constants.gravity, Constants.doSleep);
	players = new ArrayList<Player>();
	this.newPlayerQueue = new ArrayList<Player>();
    }

    public static GameEngine getInstance() {
	if (gameEngine == null) {
	    gameEngine = new GameEngine();
	}
	return gameEngine;
    }

    public void init() {

    }

    public void update() {
	// Add new player to the world
	if(isNewPlayerAvai){
	    for(Player player : newPlayerQueue){
		player.setBody( world.createBody(player.getBodyDef()) );
		
	    }
	    this.isNewPlayerAvai = false;
	}
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

    public void stopGameWorld() {
	this.execute = false;
    }

    public void addToPlayerQueue(Player player) {
	this.newPlayerQueue.add(player);
	this.isNewPlayerAvai = true;
    }
}
