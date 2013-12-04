package org.sangraama.assets;

import org.sangraama.controller.WebSocketConnection;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.jsonprotocols.SendProtocol;
import org.sangraama.jsonprotocols.send.SangraamaTile;
import org.sangraama.jsonprotocols.send.TileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * **************************************************************************
 * This class contains abstract interface for Player and dummy player classes.
 *
 * @version : v1.2
 * @author: Gihan Karunarathne
 * @email: gckarunarathne@gmail.com
 * Date: 12/5/2013 9:00 PM
 * ***************************************************************************
 */

public abstract class AbsPlayer {

    // Local Debug or logs
    private static final Logger log = LoggerFactory.getLogger(AbsPlayer.class);
    private static final String TAG = "AbsPlayer : ";
    long userID;
    GameEngine gameEngine;
    SangraamaMap sangraamaMap;
    // WebSocket Connection
    WebSocketConnection con;
    volatile boolean isUpdate = false;
    short isPlayer = 2;
    /*
     * if player type is 1: primary connection player
     * 2: dummy player (to get updates)
     */
    // Player Dynamic Parameters
    float x, y; // Player current location
    float health;
    float score;
    /*
     * Virtual point: Create a virtual point in server side. Then server will
     * send updates to client side around that point (not around player). This
     * concept is using to create concept of virtual sliding window (instead
     * having a center view).
     */
    float x_virtual = 0.0f, y_virtual = 0.0f;
    /*
     * Store Left and right along x and up and down along y corners values for efficient retrieve.
     */
    float x_vp_l, x_vp_r, y_vp_u, y_vp_d;
    /*
     * To check whether "virtual point" is setting inside the total map where it
     * possible. Total map origin x,y and total map edge x,y
     */
    float totOrgX = 0.0f, totOrgY = 0.0f, totEdgeX = 0.0f, totEdgeY = 0.0f;
    // Area of Interest
    float screenWidth = 0.0f, screenHeight = 0.0f;
    float halfAOIWidth = 0.0f; // half width of AOI
    float halfAOIHieght = 0.0f; // half height of AOI
    List<SendProtocol> deltaList; // Store delta list
    // player current sub-tile information
    float currentSubTileOriginX = 0.0f;
    float currentSubTileOriginY = 0.0f;

    /**
     * Create abstract player
     *
     * @param userID Plater User ID
     * @param x      x coordination of the player
     * @param y      y coordination of the player
     * @param w      width of the AOI
     * @param h      height of the AOI
     */
    public AbsPlayer(long userID, float x, float y, float w, float h) {
        this.userID = userID;
        this.x = x;
        this.y = y;
        this.setAOI(w, h);

        this.sangraamaMap = SangraamaMap.INSTANCE;
        /*
         * Note: this should replace by sangraama map method. Player shouldn't
		 * responsible for Deciding it's sub-tile
		 */
        this.currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        this.currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        this.gameEngine = GameEngine.INSTANCE;

		/*log.info(TAG + " init player : " + userID + " x-" + x + " : y-" + y
                + " w:" + screenWidth + " h:" + screenHeight);*/
    }

    /**
     * check whether player has new state details
     *
     * @return true if new data is available, false otherwise
     */
    public boolean isUpdate() {
        return this.isUpdate;
    }

    /**
     * Check whether player of not
     *
     * @return true if this is player instance, false otherwise
     */
    public boolean isPlayer() {
        if (this.isPlayer == 1)
            return true;
        else
            return false;
    }

    /**
     * Remove Web socket connection for player
     * TODO This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public abstract void removeWebSocketConnection();

    /**
     * Send update to client
     *
     * @param deltaList List of Objects inherited from SendProtocol class
     */
    public abstract void sendUpdate(List<SendProtocol> deltaList);

    /**
     * Check whether given location (the virtual point to be set) it inside the total map
     *
     * @param x x coordinates of the location
     * @param y y coordinates of the location
     * @return true if it's inside the map, otherwise false
     */
    protected boolean isInsideTotalMap(float x, float y) {
        return totOrgX <= x && x <= totEdgeX && totOrgY <= y && y <= totEdgeY;
    }

    /**
     * Request for client's Area of Interest around player. When player wants to
     * fulfill it's Area of Interest, it will ask for the updates of that area.
     * This method checked in following sequence,
     * 1) check on own sub-tile
     * 2) check whether location is inside current
     * 3) check for the server which own that location and send connection tag
     *
     * @param x x coordination of interest location
     * @param y y coordination of interest location
     */
    public abstract void reqInterestIn(float x, float y);

    /**
     * @param transferReq List of Objects inherited from SendProtocol class
     */
    public abstract void sendPassConnectionInfo(SendProtocol transferReq);

    /**
     * @param transferReq List of Objects inherited from SendProtocol class
     */
    public abstract void sendUpdateConnectionInfo(SendProtocol transferReq);

    /**
     * Player and Dummy Player should have different implementation of sync data
     * Ex: player x ,y coordinates which dummy doesn't have
     *
     * @param syncData List of Objects inherited from SendProtocol class
     */
    public abstract void sendSyncData(List<SendProtocol> syncData);

    /**
     * Send details about the size of the tile on current server
     *
     * @param tiles ArrayList of sub-tile details
     */
    public void sendTileSizeInfo(ArrayList<SangraamaTile> tiles) {
        this.con.sendTileSizeInfo(new TileInfo(this.userID, tiles));
    }

    /**
     * Send details about the size of the tile on current server. Sub-tiles
     * sizes may access during TileInfo Object creation
     */
    public void sendTileSizeInfo() {
        this.con.sendTileSizeInfo(new TileInfo(this.userID));
    }

    /**
     * Abstract setter methods. Implementation will depends on whether it is a
     * instance of player or dummy player
     */
    public abstract void setV(float x, float y);

    /**
     * Set the Angle of the player
     *
     * @param angle Value of angle in degrees
     */
    public abstract void setAngle(float angle);

    /**
     * Set the Angular velocity of the player
     *
     * @param da Value of Angular velocity in degrees
     */
    public abstract void setAngularVelocity(float da);

    /**
     * Trigger event shoot for player
     *
     * @param s Value 1 if shoot, 0 otherwise
     */
    public abstract void shoot(float s);

    /**
     * *******************************
     * Getter and Setters            *
     * *******************************
     */

    /**
     * Get Player x coordination value
     *
     * @return Player location x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Get Player y coordination value
     *
     * @return Player location y coordinate
     */
    public float getY() {
        return this.y;
    }

    /**
     * Set virtual point of player
     *
     * @param x_v x coordinate of virtual point
     * @param y_v y coordinate of virtual point
     * @return true if virtual point set, false otherwise
     */
    public abstract boolean setVirtualPoint(float x_v, float y_v);

    /**
     * Get x coordinate of virtual point
     *
     * @return x coordinate of virtual point
     */
    public float getXVirtualPoint() {
        return x_virtual;
    }

    /**
     * Get y coordinate of virtual point
     *
     * @return y coordinate of virtual point
     */
    public float getYVirtualPoint() {
        return x_virtual;
    }

    /**
     * Get x value of left limit of the AOI
     *
     * @return x value of left limit
     */
    public float getXVPLeft() {
        return this.x_vp_l;
    }

    /**
     * Get y value of left limit of the AOI
     *
     * @return y value of left limit
     */
    public float getXVPRight() {
        return this.x_vp_r;
    }

    /**
     * Get y value of upper limit of the AOI
     *
     * @return y value of upper limit
     */
    public float getYVPUp() {
        return this.y_vp_u;
    }

    /**
     * Get y value of lower limit of the AOI
     *
     * @return y value of lower limit
     */
    public float getYVPDown() {
        return this.y_vp_d;
    }

    /**
     * Get Player ID
     *
     * @return player ID
     */
    public long getUserID() {
        return this.userID;
    }

    /**
     * Set Area of Interest (AOI)
     *
     * @param width  width of AOI
     * @param height height of AOI
     */
    public void setAOI(float width, float height) {
        // log.info(TAG + " set AOI as w:" + width + " h:" + height);
        this.screenWidth = width;
        this.screenHeight = height;
        this.halfAOIWidth = width / 2;
        this.halfAOIHieght = height / 2;
        // Set point which virtual point can holds
        this.totOrgX = this.halfAOIWidth + 0.2f;
        this.totOrgY = this.halfAOIHieght + 0.2f;
        this.totEdgeX = SangraamaMap.INSTANCE.getMaxWidth()
                - (this.halfAOIWidth + 0.2f);
        this.totEdgeY = SangraamaMap.INSTANCE.getMaxHeight()
                - (this.halfAOIHieght + 0.2f);
    }

    /**
     * Get half width of AOI
     *
     * @return half width of AOI
     */
    public float getAOIWidth() {
        return this.halfAOIWidth;
    }

    /**
     * Get half height of AOI
     *
     * @return half height of AOI
     */
    public float getAOIHeight() {
        return this.halfAOIHieght;
    }

    /**
     * Get screen width of player/client (means AOI width)
     *
     * @return width of AOI
     */
    public float getScreenWidth() {
        return screenWidth;
    }

    /**
     * Get screen width of player/client (means AOI width)
     *
     * @return width of AOI
     */
    public float getScreenHeight() {
        return screenHeight;
    }

    /**
     * Get current score of the player
     *
     * @return current score of player
     */
    public float getScore() {
        return score;
    }

    /**
     * Get current health of the player
     *
     * @return current health of player
     */
    public float getHealth() {
        return health;
    }

    /**
     * Get players update list
     *
     * @return List of delta updates
     */
    public List<SendProtocol> getDeltaList() {
        return this.deltaList;
    }

    /**
     * Set players update list
     *
     * @param deltaList of delta updates
     */
    public void setDeltaList(List<SendProtocol> deltaList) {
        this.deltaList = deltaList;
    }

}
