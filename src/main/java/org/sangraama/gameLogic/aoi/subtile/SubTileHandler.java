package org.sangraama.gameLogic.aoi.subtile;

import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;
import org.sangraama.common.Constants;

import java.util.concurrent.ConcurrentHashMap;

public enum SubTileHandler {
    INSTANCE;

    final int hashFactor = Constants.subTileHashFactor;
    /* None block during readings: http://docs.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/ConcurrentHashMap.html */
    private ConcurrentHashMap<Float, SubTile> subTilesHashMap;

    SubTileHandler() {
        this.subTilesHashMap = new ConcurrentHashMap<>();
    }

    public void getPlayersInAOI(float index, Rectangle r, TraceBackNode v) {
        if (this.subTilesHashMap.containsKey(index)) {
            this.subTilesHashMap.get(index).contains(r, v);
        }
    }

    /**
     * Check whether given subtile is locate in this server
     *
     * @param index subtile index
     * @return true if subtile is available in this server, false otherwise
     */
    public boolean isAvailSubTile(float index) {
        return this.subTilesHashMap.containsKey(index);
    }

    /**
     * Create a new subtile
     *
     * @param orgX origin x coordinate
     * @param orgY origin y coordinate
     */
    public void addSubTile(float orgX, float orgY) {
        this.subTilesHashMap.put(orgY * hashFactor + orgX, new SubTile());
    }

    /**
     * Create a new subtile
     *
     * @param orgX origin x coordinate
     * @param orgY origin y coordinate
     */
    public void removeSubTile(float orgX, float orgY) {
        this.subTilesHashMap.remove(orgY * hashFactor + orgX);
    }

    /********** Player ***************/

    /**
     * Add player to subtile which it belongs to
     *
     * @param orgX   x origin coordination of subtile to be add player
     * @param orgY   y origin coordination of subtile to be add player
     * @param player Player
     * @return true if added the player, false otherwise
     */
    public boolean addPlayer(float orgX, float orgY, Player player) {
        // Not necessary to check. In Player class will make sure of it
        return this.subTilesHashMap.containsKey(orgY * hashFactor + orgX) && this.subTilesHashMap.get(orgY * hashFactor + orgX).addPlayer(player);
    }

    /**
     * Add player to subtile which it belongs to
     *
     * @param index  index of subtile to be add player
     * @param player Player
     * @return true if added the player, false otherwise
     */
    public boolean addPlayer(float index, Player player) {
        // Not necessary to check. In Player class will make sure of it
        // System.out.println("Add player to subtile " + index);
        return this.subTilesHashMap.containsKey(index) && this.subTilesHashMap.get(index).addPlayer(player);
    }

    /**
     * Remove player from subtile which it belongs to
     *
     * @param orgX   x origin coordination of subtile to be remove player
     * @param orgY   y origin coordination of subtile to be remove player
     * @param player Player
     * @return true if removed the player, false otherwise
     */
    public boolean removePlayer(float orgX, float orgY, Player player) {
        // Not necessary to check. In Player class will make sure of it
        return this.subTilesHashMap.containsKey(orgY * hashFactor + orgX) && this.subTilesHashMap.get(orgY * hashFactor + orgX).removePlayer(player);
    }

    /**
     * Remove player from subtile which it belongs to
     *
     * @param index  index of subtile to be remove player
     * @param player Player
     * @return true if removed the player, false otherwise
     */
    public boolean removePlayer(float index, Player player) {
        // Not necessary to check. In Player class will make sure of it
        // System.out.println("Remove player from subtile " + index);
        return this.subTilesHashMap.containsKey(index) && this.subTilesHashMap.get(index).removePlayer(player);
    }

    /************* Bullet ****************/

    /**
     * Add player to subtile which it belongs to
     *
     * @param orgX   x origin coordination of subtile to be add player
     * @param orgY   y origin coordination of subtile to be add player
     * @param bullet Player
     * @return true if added the player, false otherwise
     */
    public boolean addBullet(float orgX, float orgY, Bullet bullet) {
        // Not necessary to check. In Player class will make sure of it
        return this.subTilesHashMap.containsKey(orgY * hashFactor + orgX) && this.subTilesHashMap.get(orgY * hashFactor + orgX).addBullet(bullet);
    }

    /**
     * Add player to subtile which it belongs to
     *
     * @param index  index of subtile to be add player
     * @param bullet Player
     * @return true if added the player, false otherwise
     */
    public boolean addBullet(float index, Bullet bullet) {
        // Not necessary to check. In Player class will make sure of it
        // System.out.println("Add bullet to subtile " + index);
        return this.subTilesHashMap.containsKey(index) && this.subTilesHashMap.get(index).addBullet(bullet);
    }

    /**
     * Remove player from subtile which it belongs to
     *
     * @param orgX   x origin coordination of subtile to be remove player
     * @param orgY   y origin coordination of subtile to be remove player
     * @param bullet Player
     * @return true if removed the player, false otherwise
     */
    public boolean removeBullet(float orgX, float orgY, Bullet bullet) {
        // Not necessary to check. In Player class will make sure of it
        return this.subTilesHashMap.containsKey(orgY * hashFactor + orgX) && this.subTilesHashMap.get(orgY * hashFactor + orgX).removeBullet(bullet);
    }

    /**
     * Remove player from subtile which it belongs to
     *
     * @param index  index of subtile to be remove player
     * @param bullet Player
     * @return true if removed the player, false otherwise
     */
    public boolean removeBullet(float index, Bullet bullet) {
        // Not necessary to check. In Player class will make sure of it
        // System.out.println("Remove bullet from subtile " + index);
        return this.subTilesHashMap.containsKey(index) && this.subTilesHashMap.get(index).removeBullet(bullet);
    }
}
