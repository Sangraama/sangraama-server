package org.sangraama.assets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.common.Constants;
import org.sangraama.controller.DummyWebScocketConnection;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.controller.clientprotocol.SangraamaTile;
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
    WebSocketConnection conPlayer;
    WebSocketConnection conDummy;

    volatile boolean isUpdate = false;
    short isPlayer = 2; // if player type is 1: primary connection player 2: dummy player (to get
                        // updates)

    // Player Dynamic Parameters
    float x, y; // Player current location

    // Area of Interest
    float screenWidth = 200.0f, screenHeight = 200.0f;
    float halfWidth = screenWidth / 2;
    float halfHieght = screenHeight / 2;

    // player current sub-tile information
    float currentSubTileOriginX;
    float currentSubTileOriginY;

    public boolean isUpdate() {
        return this.isUpdate;
    }

    public AbsPlayer(long userID) {
        Random r = new Random();

        this.userID = userID;
        this.x = (float) r.nextInt(1000);
        this.y = (float) r.nextInt(1000);
        this.sangraamaMap = SangraamaMap.INSTANCE;
        /*
         * Note: this should replace by sangraama map method. Player shouldn't responsible for
         * Deciding it's sub-tile
         */
        this.currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        this.currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        this.gameEngine = GameEngine.INSTANCE;

        System.out.println(TAG + " init player : " + userID + " x-" + x + " : y-" + y);
    }

    public AbsPlayer(long userID, float x, float y, float w, float h) {
        this.userID = userID;
        this.x = x;
        this.y = y;
        this.screenWidth = w;
        this.screenHeight = h;
        this.sangraamaMap = SangraamaMap.INSTANCE;
        /*
         * Note: this should replace by sangraama map method. Player shouldn't responsible for
         * Deciding it's sub-tile
         */
        this.currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        this.currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        this.gameEngine = GameEngine.INSTANCE;

        System.out.println(TAG + " init player : " + userID + " x-" + x + " : y-" + y);
    }

    /**
     * This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public abstract void removeWebSocketConnection();

    public abstract void sendUpdate(List<PlayerDelta> deltaList);

    /**
     * Check whether player is inside current tile
     * 
     * @param x
     *            Player's current x coordination
     * @param y
     *            Player's current y coordination
     * @return if inside tile return true, else false
     */
    private boolean isInsideMap(float x, float y) {
        // System.out.println(TAG + "is inside "+x+":"+y);
        if (0 <= x && x <= sangraamaMap.getMapWidth() && 0 <= y && y <= sangraamaMap.getMapHeight()) {
            return true;
        } else {
            System.out.println(TAG + "Outside of map : " + sangraamaMap.getMapWidth() + ":"
                    + sangraamaMap.getMapHeight());
            return false;
        }
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
    public void reqInterestIn(float x, float y) {
        if (!isInsideServerSubTile(x, y)) {
            PlayerPassHandler.INSTANCE.setPassConnection(x, y, this);
        }
    }

    public abstract void sendNewConnection(ClientTransferReq transferReq);

    public abstract void sendConnectionInfo(ClientTransferReq transferReq);

    /**
     * Send details about the size of the tile on current server
     * 
     * @param tiles
     *            ArrayList of sub-tile details
     */
    public void sendTileSizeInfo(ArrayList<SangraamaTile> tiles) {
        this.conPlayer.sendTileSizeInfo(new TileInfo(this.userID, tiles));
    }

    /**
     * Send details about the size of the tile on current server. Sub-tiles sizes may access during
     * TileInfo Object creation
     * 
     */
    public void sendTileSizeInfo() {
        this.conPlayer.sendTileSizeInfo(new TileInfo(this.userID));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return this.y;
    }

    public long getUserID() {
        return this.userID;
    }

    public void setAOI(float width, float height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.halfWidth = width / 2;
        this.halfHieght = height / 2;
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

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

}
