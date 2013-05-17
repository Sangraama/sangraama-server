package org.sangraama.gameLogic;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;

public class GameEngine implements Runnable {
    //Debug
    private static final String TAG = "Game Engine :";
    private GameEngine gameEngine = null;
    private World world = null;
    private boolean execute = true;
    private boolean isNewPlayerAvai = false;
    private ArrayList<Player> playerList = null;
    private ArrayList<Player> newPlayerQueue = null;

    // this method only access via class
    public GameEngine() {
	//this.world = new World(new Vec2(0.0f, 0.0f), true);
	playerList = new ArrayList<Player>();
	this.newPlayerQueue = new ArrayList<Player>();
    }

    public GameEngine getInstance() {
	if (gameEngine == null) {
	    gameEngine = new GameEngine();
	    System.out.println(TAG + "Create a new Game Engine");
	}
	return gameEngine;
    }

    public void init() {

    }

    public void update() {
	// Add new player to the world
	if (isNewPlayerAvai) {
	    System.out.println(TAG + "Adding new players");
	    for (Player newPlayer : newPlayerQueue) {
		//newPlayer.setBody(world.createBody(newPlayer.getBodyDef()));
		//this.playerList.add(newPlayer);
	    }
	    this.isNewPlayerAvai = false;
	}
	for (Player player : playerList) {
	    System.out.println(TAG + "Adding players Updates");
	}
    }

    public void pushUpdate() {
	for (Player player : playerList) {
	    System.out.println(TAG + "Sending player updates");
	    //player.sendUpdate();
	}
    }

    @Override
    public void run() {
	init();
	while (execute) {
	    update();
	    //world.step(Constants.timeStep, Constants.velocityIterations, Constants.positionIterations);
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
