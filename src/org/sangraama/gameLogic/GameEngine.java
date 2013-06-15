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
    private UpdateEngine updateEngine = null;
    private boolean execute = true;
    private boolean isNewPlayerAvai = false;
    private boolean isRemovePlayerAvai = false;
    private ArrayList<Player> playerList = null;
    private ArrayList<Player> newPlayerQueue = null;
    private ArrayList<Player> removePlayerQueue = null;

    // this method only access via class
    GameEngine() {
        System.out.println(TAG + "Init GameEngine...");
        this.world = new World(new Vec2(0.0f, 0.0f), true);
        this.playerList = new ArrayList<Player>();
        this.newPlayerQueue = new ArrayList<Player>();
        this.removePlayerQueue = new ArrayList<Player>();
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
        if (isRemovePlayerAvai) {
            System.out.println(TAG + "Removing players");
            for (Player rmPlayer : removePlayerQueue) {
                this.playerList.remove(rmPlayer);
                System.out.println(TAG + "Removed player :" + rmPlayer.getUserID());
            }
            this.removePlayerQueue.clear();
            this.isRemovePlayerAvai = false;
        }
        // Add new player to the world
        if (isNewPlayerAvai) {
            System.out.println(TAG + "Adding new players");
            for (Player newPlayer : newPlayerQueue) {
                newPlayer.setBody(world.createBody(newPlayer.getBodyDef()));
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

}