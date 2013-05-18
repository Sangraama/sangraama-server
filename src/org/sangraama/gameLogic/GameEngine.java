package org.sangraama.gameLogic;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;

public enum GameEngine implements Runnable {
    INSTANCE;
    // Debug
    private String TAG = "Game Engine :";
    
    private World world = null;
    private float gameWorldWidth;
    private float gameWorldHeight;
    private boolean execute = true;
    private boolean isNewPlayerAvai = false;
    private ArrayList<Player> playerList = null;
    private ArrayList<Player> newPlayerQueue = null;

    // this method only access via class
    GameEngine() {
	System.out.println(TAG + "Init GameEngine...");
	this.world = new World(new Vec2(0.0f, 0.0f), true);
	this.playerList = new ArrayList<Player>();
	this.newPlayerQueue = new ArrayList<Player>();
    }
    
    @Override
    public void run() {
	System.out.println(TAG + "GameEngine Start running..");
	init();
	while (execute) {
	    update();
	    world.step(1, Constants.velocityIterations,
		    Constants.positionIterations);
	    pushUpdate();
	}
    }

    public void init() {
    	gameWorldWidth=1000f;
    	gameWorldHeight=1000f;
    }

    public void update() {
	// Add new player to the world
	if (isNewPlayerAvai) {
	    System.out.println(TAG + "Adding new players");
	    for (Player newPlayer : newPlayerQueue) {
		newPlayer.setBody(world.createBody(newPlayer.getBodyDef()));
		this.playerList.add(newPlayer);
	    }
	    this.isNewPlayerAvai = false;
	}
	for (Player player : playerList) {
	    //System.out.println(TAG + player.getUserID() +" Adding players Updates");
	    player.applyUpdate();
	}
    }

    public void pushUpdate() {
	for (Player player : playerList) {
	    //System.out.println(TAG + player.getUserID() + " Sending player updates");
	    player.sendUpdate();
	}
    }

    public void stopGameWorld() {
	this.execute = false;
    }

    public void addToPlayerQueue(Player player) {
	this.newPlayerQueue.add(player);
	this.isNewPlayerAvai = true;
    }
    
    public float getGameWorldWidth(){
    	return gameWorldWidth;
    }
    
    public float gameWorldHeight(){
    	return gameWorldHeight;
    }
}