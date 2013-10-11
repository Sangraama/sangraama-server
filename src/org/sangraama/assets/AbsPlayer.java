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
    /*
     * Virtual point: Create a virtual point in server side. Then server will send updates to client
     * side around that point (not around player). This concept is using to create concept of
     * virtual sliding window (instead having a center view).
     * #gihan
     */
    float x_virtual, y_virtual;

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
        this.screenWidth = w; // / Constants.scale; not enable scaling
        this.screenHeight = h; // / Constants.scale;
        this.halfWidth = screenWidth / 2;
        this.halfHieght = screenHeight / 2;
        // System.out.println(TAG +
        // "AOI w:"+screenWidth+" h:"+screenHeight+" = half w:"+halfWidth+" h:"+halfHieght);

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
    
    public void setVirtualPoint(float x_v, float y_v){
        this.x_virtual = x_v;
        this.y_virtual = y_v;
    }
    
    public float getXVirtualPoint(){
        return this.x_virtual;
    }
    
    public float getYVirtualPoint(){
        return this.y_virtual;
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

    public float getScreenHeight() {
        return screenHeight;
    }

}
