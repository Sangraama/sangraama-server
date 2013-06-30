package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Bullet;
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
    private UpdateEngine updateEngine = null;
    private boolean execute = true;
    private boolean isNewPlayerAvai = false;
    private boolean isRemovePlayerAvai = false;
    private boolean newBulletAvai = false;
    private ArrayList<Player> playerList = null;
    private ArrayList<Player> newPlayerQueue = null;
    private ArrayList<Player> removePlayerQueue = null;
    private ArrayList<Bullet> newBulletList = null;
    private ArrayList<Bullet> bulletList = null;

    // this method only access via class
    GameEngine() {
        System.out.println(TAG + "Init GameEngine...");
        this.world = new World(new Vec2(0.0f, 0.0f), true);
        this.playerList = new ArrayList<Player>();
        this.newPlayerQueue = new ArrayList<Player>();
        this.removePlayerQueue = new ArrayList<Player>();
        this.bulletList = new ArrayList<Bullet>();
        this.updateEngine = UpdateEngine.INSTANCE;
        this.sangraamaMap = SangraamaMap.INSTANCE;
    }

    @Override
    public void run() {
        System.out.println(TAG + "GameEngine Start running.. fps:" + Constants.fps + " timesteps:"
                + Constants.timeStep);
        init();
        Timer timer = new Timer(40, new ActionListener() {
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
        // Load static map asserts into JBox2D
    }

    public void update() {
        if (isRemovePlayerAvai && !this.updateEngine.isUpdateVal()) {
            System.out.println(TAG + "Removing players");
            for (Player rmPlayer : removePlayerQueue) {
                if (this.playerList.indexOf(rmPlayer) != -1) {
                    this.playerList.remove(rmPlayer);
                    System.out.println(TAG + "Removed player :" + rmPlayer.getUserID());
                }
            }
            this.removePlayerQueue.clear();
            this.isRemovePlayerAvai = false;
        }
        // Add new player to the world
        if (isNewPlayerAvai) {
            System.out.println(TAG + "Adding new players");
            for (Player newPlayer : newPlayerQueue) {
                Body newPlayerBody = world.createBody(newPlayer.getBodyDef());
                newPlayerBody.createFixture(newPlayer.getFixtureDef());
                newPlayer.setBody(newPlayerBody);
                // this.world.createBody(newPlayer.getBodyDef()).createFixture(newPlayer.getFixtureDef());
                this.playerList.add(newPlayer);
                System.out.println(TAG + "Added new player :" + newPlayer.getUserID());
            }
            this.newPlayerQueue.clear();
            this.isNewPlayerAvai = false;
        }
        for (Player player : playerList) {
            // System.out.println(TAG + player.getUserID()
            // +" Adding players Updates");
            player.applyUpdate();
        }
        if (newBulletAvai) {
            System.out.println(TAG + "Adding new bullets");
            for (Bullet bullet : newBulletList) {
                Body newBulletBody = world.createBody(bullet.createBodyDef());
                newBulletBody.createFixture(bullet.createFixDef());
                this.bulletList.add(bullet);
                bullet.setBulletBody(newBulletBody);
            }
            this.newBulletList.clear();
            this.newBulletAvai = false;
        }
    }

    public void pushUpdate() {
        this.updateEngine.setPlayerList(playerList);
    }

    public void stopGameWorld() {
        this.execute = false;
    }

    public void addToPlayerQueue(Player player) {
        this.newPlayerQueue.add(player);
        this.isNewPlayerAvai = true;
    }

    public void addToRemovePlayerQueue(Player player) {
        this.removePlayerQueue.add(player);
        this.isRemovePlayerAvai = true;
    }

    public void addBulletToNewBulletList(Bullet bullet) {
        if (newBulletList == null) {
            newBulletList = new ArrayList<Bullet>();
            newBulletList.add(bullet);
        } else {
            newBulletList.add(bullet);
        }
        this.newBulletAvai = true;
    }

}