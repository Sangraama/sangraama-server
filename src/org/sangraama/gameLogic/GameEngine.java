package org.sangraama.gameLogic;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.map.GameMap;
import org.sangraama.asserts.map.PhysicsAPI;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.common.Constants;

public enum GameEngine implements Runnable {

    INSTANCE;
    // Debug
    private String TAG = "Game Engine :";

    private World world;
    private UpdateEngine updateEngine;
    // list for send updates
    private List<Player> playerList;
    private List<DummyPlayer> dummyList;
    // list of newly adding players
    private List<Player> newPlayerQueue;
    private List<DummyPlayer> newDummyQueue;
    // list of removing players
    private List<Player> removePlayerQueue;
    private List<DummyPlayer> removeDummyQueue;

    // this method only access via class
    GameEngine() {
        System.out.println(TAG + "Init GameEngine...");
        this.world = new World(new Vec2(0.0f, 0.0f), true);
        this.playerList = new ArrayList<Player>();
        this.dummyList = new ArrayList<DummyPlayer>();
        this.newPlayerQueue = new ArrayList<Player>();
        this.newDummyQueue = new ArrayList<DummyPlayer>();
        this.removePlayerQueue = new ArrayList<Player>();
        this.removeDummyQueue = new ArrayList<DummyPlayer>();
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

    public void init() {
        // Load static map asserts into JBox2D
        GameMap g = GameMap.getMap();
        g.generate(); // generate the static objects into game map, using any tile editor module.
        PhysicsAPI physicsAPI = new PhysicsAPI();
        for (int i = 0; i < g.getStaticObjects().size(); i++) {
            /*
             * for(int k=0;k<g.getStaticObjects().get(i).getCoordinates().size();k++){
             * if(g.getStaticObjects
             * ().get(i).getCoordinates().get(k).getX()<SangraamaMap.INSTANCE.getSubTileWidth()){
             * System.out.println("Test");
             * System.out.println(g.getStaticObjects().get(i).getCoordinates().get(k).getX());
             * System.out.println(g.getStaticObjects().get(i).getCoordinates().get(k).getY()); } }
             */
            physicsAPI.applyPhysics(g.getStaticObjects().get(i));
            if (physicsAPI.getBodyDef() != null) {
                Body newStaticObjectBody = world.createBody(physicsAPI.getBodyDef());
                newStaticObjectBody.createFixture(physicsAPI.getFixtureDef());
                // System.out.println("polygon");
            }

        }
        System.out.println("finish");
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

        for (Player ship : playerList) {
            ship.applyUpdate();
            peformBulletUpdates(ship);
        }

    }

    /**
     * update game world with new bullets
     * 
     * @param ship
     *            player who belongs the bullets
     * 
     */
    private void peformBulletUpdates(Player ship) {
        for (Bullet newBullet : ship.getNewBulletList()) {
            System.out.println(TAG + "Adding new bullet");
            Body newBulletBody = world.createBody(newBullet.getBodyDef());
            newBulletBody.setTransform(newBulletBody.getPosition(), ship.getAngle());
            newBulletBody.createFixture(newBullet.getFixtureDef());
            newBullet.setBody(newBulletBody);
            Vec2 velocity = new Vec2(newBulletBody.getPosition().x - ship.getX(),
                    newBulletBody.getPosition().y - ship.getY());
            newBulletBody.setLinearVelocity(velocity);
            ship.getBulletList().add(newBullet);

        }
        ship.getNewBulletList().clear();
    }

    public void pushUpdate() {
        this.updateEngine.setWaitingPlayerList(playerList);
    }

    public synchronized void addToPlayerQueue(Player ship) {
        this.newPlayerQueue.add(ship);
    }

    public synchronized void addToDummyQueue(DummyPlayer player) {
        this.newDummyQueue.add(player);
    }

    public synchronized void addToRemovePlayerQueue(Player ship) {
        this.removePlayerQueue.add(ship);
    }

    public synchronized void addToRemoveDummyQueue(DummyPlayer player) {
        this.removeDummyQueue.add(player);
    }

}
