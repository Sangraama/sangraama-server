package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.sangraama.asserts.Player;
import org.sangraama.asserts.SangraamaMap;
import org.sangraama.controller.clientprotocol.PlayerDelta;

public enum UpdateEngine implements Runnable {
    INSTANCE;
    // Debug
    private String TAG = "Update Engine :";

    private List<Player> playerList; // don't modify;read only
    private List<Player> waitingPlayerList;
    private Map<Long, PlayerDelta> playerDelta;

    UpdateEngine() {
        System.out.println(TAG + "Init GameEngine...");
        this.playerList = new ArrayList<Player>();
        this.waitingPlayerList = new ArrayList<>();
        // this.locations = new float[100][3];
    }

    public void setWaitingPlayerList(List<Player> playerList) {
        /*
         * if previous updates unable to send to players, ignore current updates until previous
         * update sent.
         */

        this.waitingPlayerList = playerList;

    }

    @Override
    public void run() {
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pushUpdate();
            }
        });

        timer.start();
    }

    public void pushUpdate() {
        playerDelta = new HashMap<Long, PlayerDelta>();
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
     * This method can replace with region query in 4.14 box2D manual
     * 
     * @param player
     * @return ArrayList<PlayerDelta>
     */
    private ArrayList<PlayerDelta> getAreaOfInterest(Player p) {
        ArrayList<PlayerDelta> delta = new ArrayList<PlayerDelta>();
        // Add players own details
        // delta.add(this.playerDelta.get(p.getUserID()));

        // going through all players and check their locations
        // inefficient
        for (Player player : playerList) {
            if (p.getX() - p.getAOIWidth() <= player.getX()
                    && player.getX() <= p.getX() + p.getAOIWidth()
                    && p.getY() - p.getAOIHeight() <= player.getY()
                    && player.getY() <= p.getY() + p.getAOIHeight()) {
                delta.add(this.playerDelta.get(player.getUserID()));
            }
        }

        return delta;
    }

}
