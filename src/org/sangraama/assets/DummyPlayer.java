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

    public DummyPlayer(long userID, float x, float y, float w, float h, WebSocketConnection con) {
        super(userID, x, y, w, h);
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

    // Need refactoring
    public void sendConnectionInfo(ClientTransferReq transferReq) {
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