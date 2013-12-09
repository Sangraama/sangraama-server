package org.sangraama.gameLogic.aoi.subtile;

import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;

import java.util.ArrayList;

/**
 * Currently hardcoded to 2 dimensions, but could be extended..
 *
 * @author: Gihan Karunarathne
 * Date: 12/1/13
 * Time: 9:00 PM
 * @email: gckarunarathne@gmail.com
 */

public class SubTile {
    /**
     * TODO: Optimize using a BSP tree instead of an ArrayList
     */
    private ArrayList<Player> playerList;
    private ArrayList<Bullet> bulletList;

    SubTile() {
        this.playerList = new ArrayList<>();
        this.bulletList = new ArrayList<>();
    }

    /**
     * Find all players located inside the given rectangular
     *
     * @param r Rectangular of Area of Interest
     * @param v Trace Back
     */
    public void contains(Rectangle r, TraceBackNode v) {
        // go through every entry in the subtile to check if it is contained by the passed rectangle
        for (int i = 0; i < this.playerList.size(); i++) {
            if (r.contains(this.playerList.get(i).getCoordination())) {
                if (!v.executePlayer(this.playerList.get(i).getUserID())) {
                    return;
                }
            }
        }

        // go through every entry in the subtile to check if it is contained by the passed rectangle
        for (int i = 0; i < this.bulletList.size(); i++) {
            if (r.contains(this.bulletList.get(i).getCoordination())) {
                if (!v.executeBullet(this.bulletList.get(i).getId())) {
                    return;
                }
            }
        }
    }

    /**
     * Add a new player to subtile
     *
     * @param player details about player player
     * @return true if added new player, false otherwise
     */
    public boolean addPlayer(Player player) {
        // System.out.println("Adding player to subtile " + player.getUserID() + " @@@@@@@@@@@@@@");
        return this.playerList.add(player);
    }

    /**
     * Remove player form subtile
     *
     * @param player player details player
     * @return true if removed the player, false otherwise
     */
    public boolean removePlayer(Player player) {
        return this.playerList.remove(player);
    }

    /**
     * Add a new player to subtile
     *
     * @param bullet details about player player
     * @return true if added new player, false otherwise
     */
    public boolean addBullet(Bullet bullet) {
        return this.bulletList.add(bullet);
    }

    /**
     * Remove player form subtile
     *
     * @param bullet player details player
     * @return true if removed the player, false otherwise
     */
    public boolean removeBullet(Bullet bullet) {
        return this.bulletList.remove(bullet);
    }
}
