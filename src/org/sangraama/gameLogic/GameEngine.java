package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;


import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import org.sangraama.asserts.map.GameMap;
import org.sangraama.asserts.map.PhysicsAPI;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.common.Constants;

public enum GameEngine implements Runnable {

    INSTANCE;
    // Debug
    private String TAG = "Game Engine :";

    private World world;
    private UpdateEngine updateEngine;
    private List<Player> playerList;
    private List<Player> newPlayerQueue;
    private List<Player> removePlayerQueue;

    // this method only access via class
    GameEngine() {
        System.out.println(TAG + "Init GameEngine...");
        this.world = new World(new Vec2(0.0f, 0.0f));
        this.playerList = new ArrayList<Player>();
        this.newPlayerQueue = new ArrayList<Player>();
        this.removePlayerQueue = new ArrayList<Player>();
        this.updateEngine = UpdateEngine.INSTANCE;
    }

    @Override
    public void run() {
        System.out.println(TAG + "GameEngine Start running.. fps:" + Constants.fps + " timesteps:"
                + Constants.timeStep);
      init();
        // Timer timer = new Timer(Constants.simulatingDelay, new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent arg0) {
        // updateGameWorld();
        // world.step(Constants.timeStep, Constants.velocityIterations,
        // Constants.positionIterations);
        // pushUpdate();
        // }
        // });
        // timer.start();

        while (true) {
            try {
                Thread.sleep(Constants.simulatingDelay);
                updateGameWorld();
                world.step(Constants.timeStep, Constants.velocityIterations,
                        Constants.positionIterations);
                pushUpdate();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*Load static map objects into game engine and apply object physics using JBox2D*/
    public void init() {
        
    	GameMap g=GameMap.getMap();
		g.generate();	//generate the static objects into game engine, using any tile editor module.
		PhysicsAPI physicsAPI=new PhysicsAPI(); 
		physicsAPI.applyPhysics(g.getStaticObjects(), world);// apply physics to the static objects, and add them to the game world.
		System.out.println("Static Game Objects added to the game world!!");
    }

    public void updateGameWorld() {
        // Remove existing players from the game world
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
            System.out.println(TAG + "Added new player :" + newPlayer.getUserID());
            // Send size of the tile
            newPlayer.sendTileSizeInfo();
        }
        this.newPlayerQueue.clear();

        for (Player player : playerList) {
            player.applyUpdate();
            peformBulletUpdates(player);
        }

    }

    /**
     * update game world with new bullets
     * 
     * @param player
     *            player who belongs the bullets
     * 
     */
    private void peformBulletUpdates(Player player) {
        for (Bullet newBullet : player.getNewBulletList()) {
            System.out.println(TAG + "Adding new bullet");
            Body newBulletBody = world.createBody(newBullet.getBodyDef());
            newBulletBody.setTransform(newBulletBody.getPosition(), player.getAngle());
            newBulletBody.createFixture(newBullet.getFixtureDef());
            newBullet.setBody(newBulletBody);
            Vec2 velocity = new Vec2(newBulletBody.getPosition().x - player.getX(),
                    newBulletBody.getPosition().y - player.getY());
            newBulletBody.setLinearVelocity(velocity);
            player.getBulletList().add(newBullet);

        }
        player.getNewBulletList().clear();
    }

    public void pushUpdate() {
        this.updateEngine.setWaitingPlayerList(playerList);
    }

    public void addToPlayerQueue(Player player) {
        this.newPlayerQueue.add(player);

    }

    public void addToRemovePlayerQueue(Player player) {
        this.removePlayerQueue.add(player);

    }

}
