package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
import org.sangraama.common.Constants;
import org.sangraama.controller.clientprotocol.AbsDelta;
import org.sangraama.controller.clientprotocol.BulletDelta;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.controller.clientprotocol.SangraamaTile;
import org.sangraama.controller.clientprotocol.SendProtocol;

public enum UpdateEngine implements Runnable {
    INSTANCE;
    // Debug
    private String TAG = "Update Engine :";

    private volatile boolean isRun = true;
    private volatile boolean isUpdate = false;

    private List<Player> playerList; // don't modify;read only
    private List<Bullet> bulletList;
    /* Should be atomic operation. */
    private volatile List<Player> waitingPlayerList;
    private Map<Long, PlayerDelta> playerDelta;

    UpdateEngine() {
        System.out.println(TAG + "Init Update Engine ...");
        this.playerList = new ArrayList<>();
        this.bulletList = new ArrayList<>();
        this.waitingPlayerList = new ArrayList<>();
        // this.locations = new float[100][3];
    }

    public synchronized boolean setStop() {
        this.isRun = false;
        return this.isRun;
    }

    public void setWaitingPlayerList(List<Player> playerList) {
        /*
         * if previous updates unable to send to players, ignore current updates until previous
         * update sent.
         */
        this.waitingPlayerList = playerList;
        this.isUpdate = true;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.bulletList = bulletList;
    }

    @Override
    public void run() {
        Timer timer = new Timer(Constants.simulatingDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (isUpdate) {
                    pushUpdate();
                }
            }
        });
        timer.start();
    }

    public void pushUpdate() {
        playerDelta = new HashMap<Long, PlayerDelta>();
        // Make a clone of Updates which need to send
        this.playerList = this.waitingPlayerList;
        for (Player player : playerList) {

            playerDelta.put(player.getUserID(), player.getPlayerDelta());
        }

        try {
            for (Player player : playerList) {
                player.sendUpdate(getAreaOfInterest(player));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * When sub-tiles moving around the servers, size of map get change. Send notifications to
     * clients
     * 
     * @param tiles
     *            Details of sub-tiles
     */
    public void pushTileSizeInfo(ArrayList<SangraamaTile> tiles) {
        List<Player> playerLists = this.waitingPlayerList;
        for (Player player : playerLists) {
            player.sendTileSizeInfo(tiles);
        }
    }

    /**
     * This method can replace with region query in 4.14 box2D manual
     * 
     * @param player
     * @return ArrayList<PlayerDelta>
     */
    private List<AbsDelta> getAreaOfInterest(Player p) {
        List<AbsDelta> delta = new ArrayList<>();

        // inefficient when it becomes 1000 of players
        // Need a efficient algo and a data structure : #gihan

        // iterate through other players as well
        delta.add(p.getPlayerDelta());
        for (Player player : playerList) {
            if (p.getXVirtualPoint() - p.getAOIWidth() <= player.getX()
                    && player.getX() <= p.getXVirtualPoint() + p.getAOIWidth()
                    && p.getYVirtualPoint() - p.getAOIHeight() <= player.getY()
                    && player.getY() <= p.getYVirtualPoint() + p.getAOIHeight()) {
                if (player.getUserID() != p.getUserID()) {
                    delta.add(this.playerDelta.get(player.getUserID()));
                }
            }
        }
        for (Bullet bullet : bulletList) {
            delta.add(bullet.getBulletDelta());
        }
        // inefficient

        /*
         * for (Player player : playerList) { if (p.getX() - p.getAOIWidth() <= player.getX() &&
         * player.getX() <= p.getX() + p.getAOIWidth() && p.getY() - p.getAOIHeight() <=
         * player.getY() && player.getY() <= p.getY() + p.getAOIHeight()) {
         * delta.add(this.playerDelta.get(player.getUserID())); } }
         */
        /*
         * delta.add(this.playerDelta.get(p.getUserID())); for (Player player : playerList) { if
         * (player.getUserID() != p.getUserID()) { if (p.getMidX() - p.getAOIWidth() <=
         * player.getX() && player.getX() <= p.getMidX() + p.getAOIWidth() && p.getMidY() -
         * p.getAOIHeight() <= player.getY() && player.getY() <= p.getMidY() + p.getAOIHeight()) {
         * delta.add(this.playerDelta.get(player.getUserID())); } } } for (Bullet bullet :
         * bulletList) { BulletDelta bulletDelta = bullet.getBulletDelta(); if (p.getMidX() -
         * p.getAOIWidth() <= bulletDelta.getDx() && bulletDelta.getDx() <= p.getMidX() +
         * p.getAOIWidth() && p.getMidY() - p.getAOIHeight() <= bulletDelta.getDy() &&
         * bulletDelta.getDy() <= p.getMidY() + p.getAOIHeight()) { delta.add(bulletDelta); } }
         */
        return delta;
    }

}
