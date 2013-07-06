package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;

public enum GameEngine implements Runnable {

	INSTANCE;
	// Debug
	private String TAG = "Game Engine :";

	private World world;
	private UpdateEngine updateEngine;
	private ArrayList<Player> playerList;
	private ArrayList<Player> newPlayerQueue;
	private ArrayList<Player> removePlayerQueue;

	// this method only access via class
	GameEngine() {
		System.out.println(TAG + "Init GameEngine...");
		this.world = new World(new Vec2(0.0f, 0.0f), true);
		this.playerList = new ArrayList<Player>();
		this.newPlayerQueue = new ArrayList<Player>();
		this.removePlayerQueue = new ArrayList<Player>();
		this.updateEngine = UpdateEngine.INSTANCE;
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

		timer.start();
	}

	public void init() {
		// Load static map asserts into JBox2D
	}

	public void update() {

		for (Player rmPlayer : removePlayerQueue) {
			System.out.println(TAG + "Removing players");

			this.playerList.remove(rmPlayer);
			System.out.println(TAG + "Removed player :" + rmPlayer.getUserID());

			
		}
		this.removePlayerQueue.clear();

		// Add new player to the world

		for (Player newPlayer : newPlayerQueue) {
			System.out.println(TAG + "Adding new players");
			 Body newPlayerBody = world.createBody(newPlayer.getBodyDef());
             newPlayerBody.createFixture(newPlayer.getFixtureDef());
             newPlayer.setBody(newPlayerBody);
			this.playerList.add(newPlayer);
			System.out.println(TAG + "Added new player :"
					+ newPlayer.getUserID());
		}
		this.newPlayerQueue.clear();

		for (Player player : playerList) {
			// System.out.println(TAG + player.getUserID()
			// +" Adding players Updates");
			player.applyUpdate();
		}
	}

	public void pushUpdate() {
		this.updateEngine.setWaitingPlayerList(playerList);
	}

	public void stopGameWorld() {

	}

	public void addToPlayerQueue(Player player) {
		this.newPlayerQueue.add(player);

	}

	public void addToRemovePlayerQueue(Player player) {
		this.removePlayerQueue.add(player);

	}
   

}