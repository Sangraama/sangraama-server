package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
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
import org.sangraama.assets.Wall;
import org.sangraama.common.Constants;
import org.sangraama.util.BoundaryCreator;

import java.awt.event.ActionListener;
import javax.swing.Timer;

public enum GameEngine implements Runnable {

    INSTANCE;
    // Debug
    private String TAG = "Game Engine :";

    private volatile boolean isRun = true;

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
    private CollisionDetector sangraamaCollisionDet;
    private List<Wall> wallList;

    // this method only access via class
    GameEngine() {
        System.out.println(TAG + "Init GameEngine...");
        this.world = new World(new Vec2(0.0f, 0.0f));
        this.playerList = new ArrayList<>();
        this.dummyList = new ArrayList<>();
        this.newPlayerQueue = new ArrayList<>();
        this.newDummyQueue = new ArrayList<>();
        this.removePlayerQueue = new ArrayList<>();
        this.removeDummyQueue = new ArrayList<>();
        this.wallList = new ArrayList<>();
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
        // Remove existing players from the game world
        for (Player rmPlayer : removePlayerQueue) {
            System.out.println(TAG + "Removing players");
            this.playerList.remove(rmPlayer);
            this.world.destroyBody(rmPlayer.getBody());
            System.out.println(TAG + "Removed player :" + rmPlayer.getUserID());
            rmPlayer = null; // free the memory
        }
        this.removePlayerQueue.clear();

        // Add new player to the world

        for (Player newPlayer : newPlayerQueue) {
            System.out.println(TAG + "Adding new players");
            Body newPlayerBody = world.createBody(newPlayer.getBodyDef());
            newPlayerBody.createFixture(newPlayer.getFixtureDef());
            newPlayer.setBody(newPlayerBody);
            //PhysicsAPI physicsAPI=new PhysicsAPI(); 
            //physicsAPI.applyPhysics(newPlayer, world);
            this.playerList.add(newPlayer);
            System.out.println(TAG + "Added new player :" + newPlayer.getUserID());
            // Send size of the tile
            newPlayer.sendTileSizeInfo();
        }
        this.newPlayerQueue.clear();

        for (Player ship : playerList) {
            ship.applyUpdate();
            peformBulletUpdates(ship);
            removeBullet(ship);
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

    public void removeBullet(Player player) {
        float w = player.getScreenWidth();
        float h = player.getScreenHeight();
        float x = player.getBody().getPosition().x;
        float y = player.getBody().getPosition().y;
        float minX = x - x % w;
        float maxX = minX + w;
        float minY = y - y % h;
        float maxY = minY + h;
        List<Bullet> rmvList = new ArrayList<>();
        for (Bullet bullet : player.getBulletList()) {
            float bulletX = bullet.getBody().getPosition().x;
            float bulletY = bullet.getBody().getPosition().y;
            if (bulletX < minX || bulletX > maxX || bulletY < minY || bulletY > maxY) {
                world.destroyBody(bullet.getBody());
                rmvList.add(bullet);
                System.out.println(TAG + "Bullet removed..");
            }
        }
        player.getBulletList().removeAll(rmvList);
        player.setRemovedBulletList(rmvList);
    }
    
    public void removeBullet(Bullet bullet){
        List<Bullet> rmvList = new ArrayList<>();
        world.destroyBody(bullet.getBody());
        rmvList.add(bullet);
        for(Player player : playerList){
            if(player.getUserID()==bullet.getPlayerId()){
                player.getBulletList().removeAll(rmvList);
                player.setRemovedBulletList(rmvList);
            }
        }
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

    public List<Player> getPlayerList() {
        return playerList;
    }
    
    private void addWalls(){
        BoundaryCreator wallGen = new BoundaryCreator();
        wallList = wallGen.calculateWallBoundary();
        for(Wall wall : wallList){
            this.world.createBody(wall.getBodyDef()).createFixture(wall.getFixtureDef());
        }
    }
}
