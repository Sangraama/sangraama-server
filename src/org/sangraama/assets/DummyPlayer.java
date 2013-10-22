package org.sangraama.assets;

import java.util.ArrayList;
import java.util.List;

import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.AbsDelta;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.controller.clientprotocol.SendProtocol;
import org.sangraama.controller.clientprotocol.SyncPlayer;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyPlayer extends AbsPlayer {

    // Debug
    // Local Debug or logs
    public static final Logger log = LoggerFactory.getLogger(Ship.class);
    private static final String TAG = "DummyPlayer : ";

    // Area of Interest
    private float halfWidth = 10f;
    private float halfHieght = 1000f;

    // bullets
    private List<Bullet> newBulletList;
    private List<Bullet> bulletList;

    // player current sub-tile information
    float currentSubTileOriginX;
    float currentSubTileOriginY;

    public boolean isUpdate() {
        return this.isUpdate;
    }

    public DummyPlayer(long userID, WebSocketConnection con) {
        super(userID);
        super.isPlayer = 2;
        super.con = con;
        this.gameEngine.addToDummyQueue(this);
        this.newBulletList = new ArrayList<Bullet>();
        this.bulletList = new ArrayList<Bullet>();
        // Send tile info
        sendTileSizeInfo();
    }

    /**
     * Create a dummy player in order to get updates to fulfill the player's AOI in client side
     * @param userID userID of the player in server side
     * @param x player's current location x coordinates
     * @param y player's current location y coordinates
     * @param w width of player's AOI
     * @param h height of player's AOI
     * @param con web socket Connection
     */
    public DummyPlayer(long userID, float x, float y, float w, float h, WebSocketConnection con) {
        super(userID, x, y, w, h);
        super.isPlayer = 2;
        super.con = con;
        this.gameEngine.addToDummyQueue(this);
        this.newBulletList = new ArrayList<Bullet>();
        this.bulletList = new ArrayList<Bullet>();
        // Send tile info
        sendTileSizeInfo();
    }

    /**
     * This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public void removeWebSocketConnection() {
        super.con = null;
    }

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

    public void sendUpdate(List<SendProtocol> deltaList) {
        if (super.con != null) {
            con.sendUpdate(deltaList);
        } else if (super.isPlayer == 2) {
            this.gameEngine.addToRemoveDummyQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send updates,coz con :" + super.con
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + " waiting for remove");
        }
    }

    public void sendPassConnectionInfo(SendProtocol transferReq) {
        /* dummy can't pass the player : no implementation */
    }

    // Need refactoring
    public void sendUpdateConnectionInfo(SendProtocol transferReq) {
        if (super.con != null) {
            ArrayList<SendProtocol> transferReqList = new ArrayList<SendProtocol>();
            transferReqList.add(transferReq);
            con.sendNewConnection(transferReqList);
        } else if (super.isPlayer == 2) {
            this.gameEngine.addToRemoveDummyQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send new connection,coz con :" + super.con
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + " waiting for remove");
        }
    }

    public void sendSyncData(List<SendProtocol> syncData) {
        if (super.con != null) {
            con.sendUpdate(syncData);
        } else if (super.isPlayer == 2) {
            this.gameEngine.addToRemoveDummyQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send syncdata,coz con :" + super.con
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + " waiting for remove");
        }
    }

    /* Getter Setter methis */

    public void setVirtualPoint(float x_vp, float y_vp) {
        /*
         * Validate data before set virtual point. Idea: Virtual point can't go beyond edges of Full
         * map (the map which divide into sub tiles) with having half of the size of AOI. Then
         * possible virtual point setting will validate by server side. #gihan
         */
        this.x_virtual = x_vp;
        this.y_virtual = y_vp;

        List<SendProtocol> data = new ArrayList<SendProtocol>();
        // Send updates which are related/interest to dummy player
        data.add(new SyncPlayer(userID, x_virtual, y_virtual, screenWidth, screenHeight));
        System.out.println(TAG + "Virtual point x" + x_vp + " y" + y_vp);
        this.sendSyncData(data);
    }

    /**
     * Setter methods which are not relevant to dummy player (but inherits)
     */

    public void setV(float x, float y) {
        /* Don't implement. Not relevant to dummy player */
    }

    public void setAngle(float angle) {
        /* Don't implement. Not relevant to dummy player */
    }

    public void setAngularVelocity(float da) {
        /* Don't implement. Not relevant to dummy player */
    }

    public void shoot(float s) {
        /* Don't implement. Not relevant to dummy player */
    }

    /*
     * Getter and setter methods
     */
    public void setX(float x) {
        if (x > 0) {
            this.x = x;
        }
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        if (y > 0) {
            this.y = y;
        }
    }

    public float getY() {
        return this.y;
    }

}