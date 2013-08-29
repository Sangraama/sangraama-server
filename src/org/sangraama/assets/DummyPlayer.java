package org.sangraama.assets;

import java.util.ArrayList;
import java.util.List;

import org.sangraama.controller.DummyWebScocketConnection;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.controller.clientprotocol.SangraamaTile;
import org.sangraama.controller.clientprotocol.TileInfo;
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
        super.conDummy = con;
        this.gameEngine.addToDummyQueue(this);
        this.newBulletList = new ArrayList<Bullet>();
        this.bulletList = new ArrayList<Bullet>();
    }

    public DummyPlayer(long userID, float x, float y, WebSocketConnection con) {
        super(userID, x, y);
        super.isPlayer = 2;
        super.conDummy = con;
        this.gameEngine.addToDummyQueue(this);
        this.newBulletList = new ArrayList<Bullet>();
        this.bulletList = new ArrayList<Bullet>();
    }

    /**
     * This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public void removeWebSocketConnection() {
        super.conDummy = null;
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
            PlayerPassHandler.INSTANCE.setPassConnection(this);
        }
    }

    public void sendUpdate(List<PlayerDelta> deltaList) {
        if (super.conDummy != null) {
            conPlayer.sendUpdate(deltaList);
        } else if (super.isPlayer == 2) {
            this.gameEngine.addToRemoveDummyQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send updates,coz con :" + super.conDummy
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + " waiting for remove");
        }
    }

    public void sendNewConnection(ClientTransferReq transferReq) {
        if (super.conDummy != null) {
            ArrayList<ClientTransferReq> transferReqList = new ArrayList<ClientTransferReq>();
            transferReqList.add(transferReq);
            conPlayer.sendNewConnection(transferReqList);
        } else if (super.isPlayer == 2) {
            this.gameEngine.addToRemoveDummyQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send new connection,coz con :" + super.conDummy
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + " waiting for remove");
        }
    }

    /**
     * Send details about the size of the tile on current server
     * 
     * @param tiles
     *            ArrayList of sub-tile details
     */
    public void sendTileSizeInfo(ArrayList<SangraamaTile> tiles) {
        super.conDummy.sendTileSizeInfo(new TileInfo(this.userID, tiles));
    }

    /**
     * Send details about the size of the tile on current server. Sub-tiles sizes may access during
     * TileInfo Object creation
     * 
     */
    public void sendTileSizeInfo() {
        super.conDummy.sendTileSizeInfo(new TileInfo(this.userID));
    }

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