package org.sangraama.gameLogic.aoi;

import org.sangraama.assets.AbsPlayer;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.common.Constants;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.gameLogic.aoi.subtile.Rectangle;
import org.sangraama.gameLogic.aoi.subtile.SubTileHandler;
import org.sangraama.gameLogic.aoi.subtile.TraceBackNode;
import org.sangraama.gameLogic.queue.PlayerQueue;
import org.sangraama.jsonprotocols.SendProtocol;
import org.sangraama.jsonprotocols.send.BulletDelta;
import org.sangraama.jsonprotocols.send.PlayerDelta;
import org.sangraama.jsonprotocols.send.SangraamaTile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public enum AOIEngine implements Runnable {
    INSTANCE;
    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    private volatile boolean isRun = true;
    private AtomicBoolean isUpdate;

    private List<Player> playerList; // don't modify;read only
    private List<Bullet> bulletList;
    private List<DummyPlayer> dummyList;
    /* Should be atomic operation. */
    private List<Player> updatedPlayerList;
    private Map<Long, PlayerDelta> playerDelta;
    private Map<Long, BulletDelta> bulletDelta;
    private List<Player> defeatedPlayerList;

    private BlockingQueue<AbsPlayer> sendPlayerQueue;
    private ArrayList<UpdateEngine> updateSend;

    AOIEngine() {
        this.isUpdate = new AtomicBoolean(false);
        this.playerList = new ArrayList<>();
        this.bulletList = new ArrayList<>();
        this.dummyList = new Vector<>();
        this.updatedPlayerList = new ArrayList<>();
        this.defeatedPlayerList = new ArrayList<>();
        this.sendPlayerQueue = new LinkedBlockingQueue<>(1000);// max allowed queue size
        this.updateSend = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            this.updateSend.add(new UpdateEngine(this.sendPlayerQueue));
            this.updateSend.get(i).start();
        }
    }

    @Override
    public void run() {
        Timer timer = new Timer(Constants.simulatingDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (isUpdate.compareAndSet(true, true)) {
                    pushUpdate();
                }
            }
        });
        timer.start();
    }

    public void pushUpdate() {
        this.playerDelta = new HashMap<>();
        this.bulletDelta = new HashMap<>();

        // Make a clone of Updates which need to send
        this.playerList = this.updatedPlayerList;
        for (Player player : playerList) {
            playerDelta.put(player.getUserID(), player.getPlayerDelta());
        }
        // Update bullet data
        for (Bullet bullet : this.bulletList) {
            this.bulletDelta.put(bullet.getId(), bullet.getBulletDelta());
        }

        try {
            // Send updates for player
            for (Player player : playerList) {
                player.setDeltaList(this.getAreaOfInterest(player));
                this.sendPlayerQueue.offer(player, 10000, TimeUnit.MICROSECONDS);
            }
            // Send updates for Dummy Player
            for (DummyPlayer dummy : dummyList) {
                dummy.setDeltaList(this.getAreaOfInterest(dummy));
                this.sendPlayerQueue.offer(dummy, 10000, TimeUnit.MICROSECONDS);
            }

            for (Player defeatedPlayer : this.defeatedPlayerList) {
                PlayerQueue.INSTANCE.addToRemovePlayerQueue(defeatedPlayer);
            }
            this.defeatedPlayerList.clear(); // Remove defeated Players
            this.isUpdate.set(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method can replace with region query in 4.14 box2D manual
     *
     * @param p Player
     * @return ArrayList<PlayerDelta>
     */
    private List<SendProtocol> getAreaOfInterest(Player p) {
        List<SendProtocol> delta = new ArrayList<>();

        /*for (Player player : playerList) {
            if (p.getXVPLeft() <= player.getX() && player.getX() <= p.getXVPRight()
                    && p.getYVPUp() <= player.getY() && player.getY() <= p.getYVPDown()) {
                delta.add(this.playerDelta.get(player.getUserID()));
            }
        }*/
        
        SaveToList v = new SaveToList();
        for (float index : p.getSubTileIndexesInAOI()) {
            SubTileHandler.INSTANCE.getPlayersInAOI(index, p.getAOIAsRect(), v);
        }
        for (long id : v.getPlayersIds()) {
            delta.add(this.playerDelta.get(id));
        }
        for(long id : v.getBulletIds()){
            delta.add(this.bulletDelta.get(id));
        }

        /*for (Bullet bullet : bulletList) {
            if (p.getXVPLeft() <= bullet.getX() && bullet.getX() <= p.getXVPRight()
                    && p.getYVPUp() <= bullet.getY() && bullet.getY() <= p.getYVPDown()) {
                delta.add(this.bulletDelta.get(bullet.getId()));
            }
        }*/
        
        
        for (Player player : defeatedPlayerList) {
            if (p.getXVPLeft() <= player.getX() && player.getX() <= p.getXVPRight()
                    && p.getYVPUp() <= player.getY() && player.getY() <= p.getYVPDown()) {
                delta.add(player.getDefeatMsg());
            }
        }
        return delta;
    }

    /**
     * This method can replace with region query in 4.14 box2D manual
     *
     * @param d DummyPlayer
     * @return ArrayList<PlayerDelta>
     */
    private List<SendProtocol> getAreaOfInterest(DummyPlayer d) {
        List<SendProtocol> delta = new ArrayList<>();

        /*for (Player player : playerList) {
            if (d.getXVPLeft() <= player.getX() && player.getX() <= d.getXVPRight()
                    && d.getYVPUp() <= player.getY() && player.getY() <= d.getYVPDown()) {
                delta.add(this.playerDelta.get(player.getUserID()));
            }
        }*/
        SaveToList v = new SaveToList();
        for (float index : d.getSubTileIndexesInAOI()) {
            SubTileHandler.INSTANCE.getPlayersInAOI(index, d.getAOIAsRect(), v);
        }
        for (long id : v.getPlayersIds()) {
            delta.add(this.playerDelta.get(id));
        }
        for(long id : v.getBulletIds()){
            delta.add(this.bulletDelta.get(id));
        }

        /*for (Bullet bullet : bulletList) {
            if (d.getXVPLeft() <= bullet.getX() && bullet.getX() <= d.getXVPRight()
                    && d.getYVPUp() <= bullet.getY() && bullet.getY() <= d.getYVPDown()) {
                delta.add(this.bulletDelta.get(bullet.getId()));
            }
        }*/

        // The size of this list small compare to others
        for (Player player : defeatedPlayerList) {
            if (d.getXVPLeft() <= player.getX() && player.getX() <= d.getXVPRight()
                    && d.getYVPUp() <= player.getY() && player.getY() <= d.getYVPDown()) {
                delta.add(player.getDefeatMsg());
            }
        }

        return delta;
    }

    /**
     * NOTE: For Dynamic load handling :: When sub-tiles moving around the servers, size of map get
     * change. Send notifications to clients
     *
     * @param tiles Details of sub-tiles
     */
    public void pushTileSizeInfo(ArrayList<SangraamaTile> tiles) {
        List<Player> playerLists = this.updatedPlayerList;
        for (Player player : playerLists) {
            player.sendTileSizeInfo(tiles);
        }
    }

    public void setUpdatedPlayerList(List<Player> playerList) {
        /*
         * if previous updates unable to send to players, ignore current updates until previous
         * update sent.
         */
        this.updatedPlayerList = playerList;
        this.isUpdate.lazySet(true);
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

    public synchronized boolean setStop() {
        this.playerList.clear();
        this.bulletList.clear();
        this.dummyList.clear();
        this.updatedPlayerList.clear();
        for (UpdateEngine t : this.updateSend)
            t.setStop();

        this.isRun = false;
        return this.isRun;
    }

    // other rectangle. The rtree returns the results of a query
    // by calling the execute() method on a TIntProcedure.
    // In this example we want to save the results of the query
    // into a list, so that's what the execute() method does.
    class SaveToList implements TraceBackNode {
        private List<Long> players = new ArrayList<>();
        private List<Long> bullets = new ArrayList<>();

        public boolean executePlayer(long id) {
            players.add(id);
            return true;
        }
        
        public boolean executeBullet(long id) {
            bullets.add(id);
            return true;
        }

        public boolean execute(PlayerDelta playerDelta) {
            return true;
        }

        public List<Long> getPlayersIds() {
            return players;
        }
        
        public List<Long> getBulletIds(){
            return bullets;
        }
    }

}