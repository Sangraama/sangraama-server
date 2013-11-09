package org.sangraama.assets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.SangraamaTile;
import org.sangraama.controller.clientprotocol.SendProtocol;
import org.sangraama.controller.clientprotocol.TileInfo;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbsPlayer {

    // Debug
    // Local Debug or logs
    public static final Logger log = LoggerFactory.getLogger(Ship.class);
    private static final String TAG = "AbsPlayer : ";

    long userID;

    GameEngine gameEngine;
    SangraamaMap sangraamaMap;
    // WebSocket Connection
    WebSocketConnection con;

    volatile boolean isUpdate = false;
    short isPlayer = 2; // if player type is 1: primary connection player 2: dummy player (to get
                        // updates)

    // Player Dynamic Parameters
    float x, y; // Player current location
    float health;
    float score;
    /*
     * Virtual point: Create a virtual point in server side. Then server will send updates to client
     * side around that point (not around player). This concept is using to create concept of
     * virtual sliding window (instead having a center view). #gihan
     */
    float x_virtual = 0.0f, y_virtual = 0.0f;

    /*
     * To check whether virtual point is setting inside the total map. Total map origin x,y and
     * total map edge x,y
     */
    float totOrgX = 0.0f, totOrgY = 0.0f, totEdgeX = 0.0f, totEdgeY = 0.0f;

    // Area of Interest
    float screenWidth = 0.0f, screenHeight = 0.0f;
    float halfWidth = 0.0f; // half width of AOI
    float halfHieght = 0.0f; // half height of AOI

    // player current sub-tile information
    float currentSubTileOriginX = 0.0f;
    float currentSubTileOriginY = 0.0f;

    public boolean isUpdate() {
        return this.isUpdate;
    }

    public AbsPlayer(long userID, float x, float y, float w, float h) {
        this.userID = userID;
        this.x = x;
        this.y = y;
        this.setAOI(w, h);

        this.sangraamaMap = SangraamaMap.INSTANCE;
        /*
         * Note: this should replace by sangraama map method. Player shouldn't responsible for
         * Deciding it's sub-tile
         */
        this.currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        this.currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        this.gameEngine = GameEngine.INSTANCE;

        System.out.println(TAG + " init player : " + userID + " x-" + x + " : y-" + y + " w:"
                + screenWidth + " h:" + screenHeight);
    }

    /**
     * This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public abstract void removeWebSocketConnection();

    public abstract void sendUpdate(List<SendProtocol> deltaList);

    /**
     * Check whether given location (the virtual point to be set) it inside the total map
     * 
     * @param x
     * @param y
     * @return true if it's inside the map, otherwise false
     */
    protected boolean isInsideTotalMap(float x, float y) {
        if (totOrgX <= x && x <= totEdgeX && totOrgY <= y && y <= totEdgeY) {
            return true;
        }
        return false;
    }

    /**
     * Check whether player is inside current sub-tile
     * 
     * @param x
     *            Player's current x coordination
     * @param y
     *            Player's current y coordination
     * @return if inside sub-tile return true, else false
     */
    private boolean isInsideServerSubTile(float x, float y) {
        boolean insideServerSubTile = true;
        float subTileOriX = x - (x % sangraamaMap.getSubTileWidth());
        float subTileOriY = y - (y % sangraamaMap.getSubTileHeight());
        // System.out.println(TAG + currentSubTileOriginX + ":" + currentSubTileOriginY + " with "
        // + subTileOriX + ":" + subTileOriY);
        if (currentSubTileOriginX != subTileOriX || currentSubTileOriginY != subTileOriY) {
            currentSubTileOriginX = subTileOriX;
            currentSubTileOriginY = subTileOriY;
            if (!sangraamaMap.getHost().equals(TileCoordinator.INSTANCE.getSubTileHost(x, y))) {
                insideServerSubTile = false;
                System.out.println(TAG + "player is not inside a subtile of "
                        + sangraamaMap.getHost());
            }
        }

        return insideServerSubTile;
    }

    /**
     * Request for client's Area of Interest around player. When player wants to fulfill it's Area
     * of Interest, it will ask for the updates of that area. This method checked in following
     * sequence, 1) check on own sub-tile 2) check whether location is inside current 3) check for
     * the server which own that location and send connection tag
     * 
     * @param x
     *            x coordination of interest location
     * @param y
     *            y coordination of interest location
     */
    public abstract void reqInterestIn(float x, float y);

    public abstract void sendPassConnectionInfo(SendProtocol transferReq);

    public abstract void sendUpdateConnectionInfo(SendProtocol transferReq);

    /**
     * Player and Dummy Player should have different implementation of sync data Ex: player x ,y
     * coordinates which dummy donesn't have
     * 
     * @param syncData
     */
    public abstract void sendSyncData(List<SendProtocol> syncData);

    /**
     * Send details about the size of the tile on current server
     * 
     * @param tiles
     *            ArrayList of sub-tile details
     */
    public void sendTileSizeInfo(ArrayList<SangraamaTile> tiles) {
        this.con.sendTileSizeInfo(new TileInfo(this.userID, tiles));
    }

    /**
     * Send details about the size of the tile on current server. Sub-tiles sizes may access during
     * TileInfo Object creation
     * 
     */
    public void sendTileSizeInfo() {
        this.con.sendTileSizeInfo(new TileInfo(this.userID));
    }

    /**
     * Abstract setter methods. Implementation will depends on whether it is a instance of player or
     * dummy player
     */
    public abstract void setV(float x, float y);

    public abstract void setAngle(float angle);

    public abstract void setAngularVelocity(float da);

    public abstract void shoot(float s);

    /**
     * Getter and Setters
     */

    public float getX() {
        return x;
    }

    public float getY() {
        return this.y;
    }

    public abstract boolean setVirtualPoint(float x_v, float y_v);

    public float getXVirtualPoint() {
        return this.x_virtual;
    }

    public float getYVirtualPoint() {
        return this.y_virtual;
    }

    public long getUserID() {
        return this.userID;
    }

    public void setAOI(float width, float height) {
        System.out.println(TAG + " set AOI as w:" + width + " h:" + height);
        this.screenWidth = width;
        this.screenHeight = height;
        this.halfWidth = width / 2;
        this.halfHieght = height / 2;
        // Set point which virtual point can holds
        this.totOrgX = this.halfWidth + 1.0f;
        this.totOrgY = this.halfHieght + 1.0f;
        this.totEdgeX = SangraamaMap.INSTANCE.getMaxWidth() - (this.halfWidth + 1.0f);
        this.totEdgeY = SangraamaMap.INSTANCE.getMaxHeight() - (this.halfHieght + 1.0f);
    }

    public float getAOIWidth() {
        return this.halfWidth;
    }

    public float getAOIHeight() {
        return this.halfHieght;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public float getScore() {
        return score;
    }

    public float getHealth() {
        return health;
    }

}
