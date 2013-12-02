package org.sangraama.gameLogic.aoi.subtile;

/**
 * Currently hardcoded to 2 dimensions, but could be extended..
 *
 * @author: Gihan Karunarathne
 * Date: 12/1/13
 * Time: 9:00 PM
 * @email: gckarunarathne@gmail.com
 */

public class Node {
    long id = 0;
    float x, y;

    public Node(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
