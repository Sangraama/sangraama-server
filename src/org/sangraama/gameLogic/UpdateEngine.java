package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Timer;

import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.common.Constants;
import org.sangraama.gameLogic.queue.PlayerQueue;
import org.sangraama.jsonprotocols.SendProtocol;
import org.sangraama.jsonprotocols.send.BulletDelta;
import org.sangraama.jsonprotocols.send.PlayerDelta;
import org.sangraama.jsonprotocols.send.SangraamaTile;

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
    private List<Player> updatedPlayerList;
    private Map<Long, PlayerDelta> playerDelta;
    private List<Player> defeatedPlayerList;

    UpdateEngine() {
        System.out.println(TAG + "Init Update Engine ...");
        this.playerList = new ArrayList<>();
        this.bulletList = new ArrayList<>();
        this.dummyList = new Vector<DummyPlayer>();
        this.updatedPlayerList = new ArrayList<>();
        this.defeatedPlayerList = new ArrayList<>();
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
        this.playerDelta = new HashMap<Long, PlayerDelta>();

        // Make a clone of Updates which need to send
        this.playerList = this.updatedPlayerList;
        for (Player player : playerList) {
            playerDelta.put(player.getUserID(), player.getPlayerDelta());
        }

        try {
            // Send updates for player
            for (Player player : playerList) {
                player.sendUpdate(this.getAreaOfInterest(player));
            }
            // Send updates for Dummy Player
            for (DummyPlayer dummy : dummyList) {
                List<SendProtocol> deltaList = this.getAreaOfInterest(dummy);
                dummy.sendUpdate(deltaList);
            }

            for (Player defeatedPlayer : this.defeatedPlayerList) {
                PlayerQueue.INSTANCE.addToRemovePlayerQueue(defeatedPlayer);
            }
            this.defeatedPlayerList.clear(); // Remove defeated Players
            this.isUpdate = false;
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

        for (Player player : playerList) {
            if (p.getXVirtualPoint() - p.getAOIWidth() <= player.getX()
                    && player.getX() <= p.getXVirtualPoint() + p.getAOIWidth()
                    && p.getYVirtualPoint() - p.getAOIHeight() <= player.getY()
                    && player.getY() <= p.getYVirtualPoint() + p.getAOIHeight()) {
                delta.add(this.playerDelta.get(player.getUserID()));
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
        for (Player player : defeatedPlayerList) {
            if (p.getXVirtualPoint() - p.getAOIWidth() <= player.getX()
                    && player.getX() <= p.getXVirtualPoint() + p.getAOIWidth()
                    && p.getYVirtualPoint() - p.getAOIHeight() <= player.getY()
                    && player.getY() <= p.getYVirtualPoint() + p.getAOIHeight()) {
                delta.add(player.getDefeatMsg());
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
                delta.add(this.playerDelta.get(player.getUserID()));
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

        for (Player player : defeatedPlayerList) {
            if (d.getXVirtualPoint() - d.getAOIWidth() <= player.getX()
                    && player.getX() <= d.getXVirtualPoint() + d.getAOIWidth()
                    && d.getYVirtualPoint() - d.getAOIHeight() <= player.getY()
                    && player.getY() <= d.getYVirtualPoint() + d.getAOIHeight()) {
                delta.add(player.getDefeatMsg());
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
    /*
     * public boolean addToDummyQueue(DummyPlayer player) { player.sendTileSizeInfo(); return
     * this.dummyList.add(player); }
     * 
     * public boolean addToRemoveDummyQueue(long player) { for(DummyPlayer d : dummyList){
     * if(d.getUserID() == player) this.dummyList.remove(d); } return true; }
     * 
     * public boolean addToRemoveDummyQueue(DummyPlayer player) {
     * 
     * return this.dummyList.remove(player); }
     */

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

    public void setUpdatedDummyPlayerList(List<DummyPlayer> dummyList) {
        this.dummyList = dummyList;
    }

    public void setDefeatList(List<Player> playerList) {
        this.defeatedPlayerList = playerList;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.bulletList = bulletList;
    }

    public List<DummyPlayer> getDummyList() {
        return this.dummyList;
    }

}