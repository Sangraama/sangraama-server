package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.common.Constants;
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
    private List<DummyPlayer> dummyList;
    /* Should be atomic operation. */
    private volatile List<Player> updatedPlayerList;
    private Map<Long, PlayerDelta> playerDelta;

    UpdateEngine() {
        System.out.println(TAG + "Init Update Engine ...");
        this.playerList = new ArrayList<Player>();
        this.bulletList = new ArrayList<Bullet>();
        this.dummyList = new ArrayList<DummyPlayer>();
        this.updatedPlayerList = new ArrayList<Player>();
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
        this.playerList = this.updatedPlayerList;
        for (Player player : playerList) {

            playerDelta.put(player.getUserID(), player.getPlayerDelta());
        }

        try {
            // Send updates for player
            for (Player player : playerList) {
                List<SendProtocol> deltaList = this.getAreaOfInterest(player);
                if (deltaList.size() > 0) {
                    player.sendUpdate(deltaList);
                }
            }
            // Send updates for Dummy Player
            for (DummyPlayer dummy : dummyList) {
                List<SendProtocol> deltaList = this.getAreaOfInterest(dummy);
                if (deltaList.size() > 0) {
                    dummy.sendUpdate(deltaList);
                }
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
    private List<SendProtocol> getAreaOfInterest(Player p) {
        List<SendProtocol> delta = new ArrayList<>();

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

            BulletDelta bulletDelta = bullet.getBulletDelta();
            if (p.getXVirtualPoint() - p.getAOIWidth() <= bulletDelta.getDx()
                    && bulletDelta.getDx() <= p.getXVirtualPoint() + p.getAOIWidth()
                    && p.getYVirtualPoint() - p.getAOIHeight() <= bulletDelta.getDy()
                    && bulletDelta.getDy() <= p.getYVirtualPoint() + p.getAOIHeight()) {
                delta.add(bullet.getBulletDelta());
            }
        }
        return delta;
    }

    /**
     * This method can replace with region query in 4.14 box2D manual
     * 
     * @param player
     * @return ArrayList<PlayerDelta>
     */
    private List<SendProtocol> getAreaOfInterest(DummyPlayer d) {
        List<SendProtocol> delta = new ArrayList<>();

        for (Player player : playerList) {
            if (d.getXVirtualPoint() - d.getAOIWidth() <= player.getX()
                    && player.getX() <= d.getXVirtualPoint() + d.getAOIWidth()
                    && d.getYVirtualPoint() - d.getAOIHeight() <= player.getY()
                    && player.getY() <= d.getYVirtualPoint() + d.getAOIHeight()) {
                if (player.getUserID() != d.getUserID()) {
                    delta.add(this.playerDelta.get(player.getUserID()));
                }
            }
        }
        for (Bullet bullet : bulletList) {

            BulletDelta bulletDelta = bullet.getBulletDelta();
            if (d.getXVirtualPoint() - d.getAOIWidth() <= bulletDelta.getDx()
                    && bulletDelta.getDx() <= d.getXVirtualPoint() + d.getAOIWidth()
                    && d.getYVirtualPoint() - d.getAOIHeight() <= bulletDelta.getDy()
                    && bulletDelta.getDy() <= d.getYVirtualPoint() + d.getAOIHeight()) {
                delta.add(bullet.getBulletDelta());
            }
        }

        return delta;
    }

    /**
     * NOTE: For Dynamic load handling :: When sub-tiles moving around the servers, size of map get
     * change. Send notifications to clients
     * 
     * @param tiles
     *            Details of sub-tiles
     */
    public void pushTileSizeInfo(ArrayList<SangraamaTile> tiles) {
        List<Player> playerLists = this.updatedPlayerList;
        for (Player player : playerLists) {
            player.sendTileSizeInfo(tiles);
        }
    }

    /**
     * Add a player in order to get updates
     * 
     * @NOTE : {@linkGameEngine} dummies have to add via GameEngine if there are multiple Update
     *       Engines @author gihan
     * 
     * @param player
     *            Player want to get updates
     * @return true if added to the list, false otherwise
     */
    public synchronized boolean addToDummyQueue(DummyPlayer player) {
        player.sendTileSizeInfo();
        return this.dummyList.add(player);
    }

    public synchronized boolean addToRemoveDummyQueue(DummyPlayer player) {
        return this.dummyList.remove(player);
    }

    public synchronized boolean setStop() {
        this.playerList.clear();
        this.bulletList.clear();
        this.dummyList.clear();
        this.updatedPlayerList.clear();

        this.isRun = false;
        return this.isRun;
    }

    public void setUpdatedPlayerList(List<Player> playerList) {
        /*
         * if previous updates unable to send to players, ignore current updates until previous
         * update sent.
         */
        this.updatedPlayerList = playerList;
        this.isUpdate = true;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.bulletList = bulletList;
    }

}