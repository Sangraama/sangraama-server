package org.sangraama.gameLogic.aoi.subtile;

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
    private ArrayList<Player> arr;

    SubTile() {
        this.arr = new ArrayList<>();
    }

    /**
     * Find all players located inside the given rectangular
     *
     * @param r Rectangular of Area of Interest
     * @param v Trace Back
     */
    public void contains(Rectangle r, TraceBackNode v) {
        // go through every entry in the subtile to check if it is contained by the passed rectangle
        for (int i = 0; i < arr.size(); i++) {
            if (r.contains(arr.get(i).getCoord())) {
                if (!v.execute(arr.get(i).getUserID())) {
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
        return this.arr.add(player);
    }

    /**
     * Add a new player to subtile
     *
     * @param id user Id of the player
     * @param x  x coordination of the player
     * @param y  y coordination of the player
     * @return true if added the player, false otherwise
     * @deprecated
     */
    public boolean addPlayer(long id, float x, float y) {
        return true; //this.arr.add(new Node(id, x, y));
    }

    /**
     * Remove player from subtile
     *
     * @param id user Id of the player
     * @return true if removed the player, false otherwise
     * @deprecated
     */
    public boolean removePlayer(long id) {
        for (Player n : arr)
            if (n.getUserID() == id)
                return arr.remove(n);
        return false;
    }

    /**
     * Remove player form subtile
     *
     * @param player player details player
     * @return true if removed the player, false otherwise
     */
    public boolean removePlayer(Player player) {
        return this.arr.remove(player);
    }
}
