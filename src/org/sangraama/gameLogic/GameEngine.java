package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.Timer;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.map.GameMap;
import org.sangraama.asserts.map.PhysicsAPI;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.assets.Wall;
import org.sangraama.common.Constants;
import org.sangraama.gameLogic.queue.BulletQueue;
import org.sangraama.gameLogic.queue.DummyQueue;
import org.sangraama.gameLogic.queue.PlayerQueue;
import org.sangraama.util.BoundaryCreator;

public enum GameEngine implements Runnable {

    INSTANCE;
    // Debug
    private String TAG = "Game Engine :";

    private volatile boolean isRun = true;

    private World world;
    private UpdateEngine updateEngine;
    // list of players details
    private List<Player> playerList;
    private ConcurrentLinkedQueue<Player> newPlayerQueue;
    private ConcurrentLinkedQueue<Player> removePlayerQueue;
    // list of dummy players details
    private List<DummyPlayer> dummyList;
    private ConcurrentLinkedQueue<DummyPlayer> newDummyQueue;
    private ConcurrentLinkedQueue<DummyPlayer> removeDummyQueue;
    // list of bullet details
    private List<Bullet> bulletList;
    private ConcurrentLinkedQueue<Bullet> newBulletQueue;
    private ConcurrentLinkedQueue<Bullet> removeBulletQueue;

    private List<Player> defeatMsgList;
    private CollisionDetector sangraamaCollisionDet;
    private List<Wall> wallList;

    /**
     * Testing values
     */
    private int maxPlayers = 0;

    // this method only access via class
    GameEngine() {
        System.out.println(TAG + "Init GameEngine...");
        this.world = new World(new Vec2(0.0f, 0.0f));
        /**
         * Player Details
         */
        this.playerList = new ArrayList<>();
        this.newPlayerQueue = new ConcurrentLinkedQueue<Player>();
        this.removePlayerQueue = new ConcurrentLinkedQueue<Player>();
        PlayerQueue.INSTANCE.init(this.newPlayerQueue, this.removePlayerQueue);
        /**
         * Dummy Player Details
         */
        this.dummyList = new ArrayList<>();
        this.newDummyQueue = new ConcurrentLinkedQueue<DummyPlayer>();
        this.removeDummyQueue = new ConcurrentLinkedQueue<DummyPlayer>();
        DummyQueue.INSTANCE.init(this.newDummyQueue, this.removeDummyQueue);
        /**
         * Bullet details
         */
        this.bulletList = new ArrayList<>();
        this.newBulletQueue = new ConcurrentLinkedQueue<Bullet>();
        this.removeBulletQueue = new ConcurrentLinkedQueue<Bullet>();
        BulletQueue.INSTANCE.init(this.newBulletQueue, this.removeBulletQueue);

        this.wallList = new ArrayList<>();
        this.defeatMsgList = new ArrayList<>();
        this.updateEngine = UpdateEngine.INSTANCE;
    }

    public synchronized boolean setStop() {
        this.isRun = false;
        return this.isRun;
    }

    @Override
    public void run() {
        System.out.println(TAG + "GameEngine Start running.. fps:" + Constants.fps + " timesteps:"
                + Constants.timeStep);
        init();
        Timer timer = new Timer(Constants.simulatingDelay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateGameWorld();

                world.step(Constants.timeStep, Constants.velocityIterations,
                        Constants.positionIterations);
                pushUpdate();
            }
        });
        timer.start();

        /*
         * while (this.isRun) { try { Thread.sleep(Constants.simulatingDelay); updateGameWorld();
         * world.step(Constants.timeStep, Constants.velocityIterations,
         * Constants.positionIterations); pushUpdate();
         * 
         * } catch (InterruptedException e) { e.printStackTrace(); } }
         */
    }

    /* Load static map objects into game engine and apply object physics using JBox2D */
    public void init() {
        GameMap g = GameMap.getMap();
        g.generate(); // generate the static objects into game engine, using any tile editor module.
        PhysicsAPI physicsAPI = new PhysicsAPI();
        physicsAPI.applyPhysics(g.getStaticObjects(), world);// apply physics to the static objects,
                                                             // and add them to the game world.
        System.out.println("Static Game Objects added to the game world!!");
        this.sangraamaCollisionDet = new CollisionDetector();
        world.setContactListener(sangraamaCollisionDet);
        addWalls();

    }

    public void updateGameWorld() {
        performPlayerUpdates();
        performBulletUpdates();

    }

    private void performPlayerUpdates() {

        // Remove existing players from the game world
        Player rmPlayer;
        while ((rmPlayer = this.removePlayerQueue.poll()) != null) {
            // System.out.println(TAG + "Removing players");
            if (this.playerList.remove(rmPlayer)) { // True if player contains
                this.world.destroyBody(rmPlayer.getBody());
            }
            if (this.playerList.size() > maxPlayers)
                maxPlayers = this.playerList.size();
            System.out.print(TAG + " Removed player :" + rmPlayer.getUserID());
            System.out.println("=>> number of remained players : " + this.playerList.size() + "/"
                    + maxPlayers + " #################");
            rmPlayer = null; // free the memory @need to add to garbage collector
        }

        // Add new player to the world
        Player newPlayer;
        while ((newPlayer = this.newPlayerQueue.poll()) != null) {
            Body newPlayerBody = world.createBody(newPlayer.getBodyDef());
            newPlayerBody.createFixture(newPlayer.getFixtureDef());
            newPlayer.setBody(newPlayerBody);
            this.playerList.add(newPlayer);
            System.out.print(TAG + "Added new player :" + newPlayer.getUserID());
            System.out.println("=>> number of remained players : " + this.playerList.size() + "/"
                    + maxPlayers + " #################");
            // Send size of the tile
            newPlayer.sendTileSizeInfo();
        }

        for (Player player : playerList) {
            player.applyUpdate();
        }

    }

    private void performBulletUpdates() {
        Bullet rmvBullet;
        while ((rmvBullet = this.removeBulletQueue.poll()) != null) {
            if (this.bulletList.remove(rmvBullet)) {
                this.world.destroyBody(rmvBullet.getBody());
            }
            System.out.println(TAG + "Removed bullet :" + rmvBullet.getId());
            rmvBullet = null;
        }

        // Add new bullet to the world
        Bullet newBullet;
        while ((newBullet = this.newBulletQueue.poll()) != null) {
            // System.out.println(TAG + "Adding new bullets");
            Body newBulletBody = world.createBody(newBullet.getBodyDef());
            newBulletBody.createFixture(newBullet.getFixtureDef());
            newBullet.setBody(newBulletBody);
            newBulletBody.setLinearVelocity(newBullet.getVelocity());
            this.bulletList.add(newBullet);
            /*
             * System.out.println(TAG + "Added new bullet :" + newBullet.getId() + "x : " +
             * newBulletBody.getPosition().x + "y : " + newBulletBody.getPosition().y);
             */
        }

        for (Bullet bullet : bulletList) {
            removeBulletByPosition(bullet);
        }

    }

    public void removeBulletByPosition(Bullet bullet) {
        float x = bullet.getOriginX();
        float y = bullet.getOriginY();
        float bulletX = bullet.getBody().getPosition().x;
        float bulletY = bullet.getBody().getPosition().y;
        float bulletRange = 15;
        float currentRange = (float) Math.sqrt(Math.pow((bulletX - x), 2)
                + Math.pow((bulletY - y), 2));
        if (currentRange >= bulletRange) {
            world.destroyBody(bullet.getBody());
            removeBulletQueue.add(bullet);
        }
    }

    public void pushUpdate() {
        this.updateEngine.setUpdatedPlayerList(playerList);
        this.updateEngine.setBulletList(bulletList);
        this.updateEngine.setDefeatList(defeatMsgList);
        defeatMsgList.clear();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    private void addWalls() {
        BoundaryCreator wallGen = new BoundaryCreator();
        wallList = wallGen.calculateWallBoundary();
        for (Wall wall : wallList) {
            // System.out.println("Adding wall " + wall.getFixtureDef().userData);
            this.world.createBody(wall.getBodyDef()).createFixture(wall.getFixtureDef());
        }
    }

    public void addToDefaetList(Player player) {
        this.defeatMsgList.add(player);
    }

}
