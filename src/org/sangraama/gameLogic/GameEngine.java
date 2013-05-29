package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.SangraamaMap;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;
import org.sangraama.controller.clientprotocol.PlayerDelta;

public enum GameEngine implements Runnable {
    INSTANCE;
    // Debug
    private String TAG = "Game Engine :";

    private World world = null;
    private SangraamaMap sangraamaMap = null;
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
	this.sangraamaMap = SangraamaMap.INSTANCE;
	this.sangraamaMap.setMap(1000f, 0f, 1000f, 1000f);
    }

    @Override
    public void run() {
	System.out.println(TAG + "GameEngine Start running.. fps:"
		+ Constants.fps + " timesteps:" + Constants.timeStep);
	init();
	Timer timer = new Timer(100, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		update();
		world.step(Constants.timeStep, Constants.velocityIterations,
			Constants.positionIterations);
		pushUpdate();
	    }
	});

	while (execute) {
	    // update();
	    // world.step(Constants.timeStep, Constants.velocityIterations,
	    // Constants.positionIterations);
	    // pushUpdate();

	    timer.start();
	}
    }

    public void init() {

    }

    public void update() {
	// Add new player to the world
	if (isNewPlayerAvai) {
	    System.out.println(TAG + "Adding new players");
	    for (Player newPlayer : newPlayerQueue) {
		newPlayer.setBody(world.createBody(newPlayer.getBodyDef()));
		this.playerList.add(newPlayer);
	    }
	    this.newPlayerQueue.clear();
	    this.isNewPlayerAvai = false;
	}
	for (Player player : playerList) {
	    // System.out.println(TAG + player.getUserID()
	    // +" Adding players Updates");
	    player.applyUpdate();
	}
    }

    public void pushUpdate() {
	ArrayList<PlayerDelta> deltaList = new ArrayList<PlayerDelta>();
	// System.out.println(TAG + "delta list length :" + deltaList.size());
	for (Player player : playerList) {
	    // System.out.println(TAG + player.getUserID() +
	    // " Sending player updates");
	    deltaList.add(player.getPlayerDelta());
	}
	// System.out.println(TAG + "delta list length :" + deltaList.size());
	for (Player player : playerList) {
	    player.sendUpdate(deltaList);
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