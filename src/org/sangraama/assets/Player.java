package org.sangraama.assets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.common.Constants;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.DefeatMsg;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.controller.clientprotocol.SendProtocol;
import org.sangraama.controller.clientprotocol.SyncPlayer;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Player extends AbsPlayer {

    // Debug
    // Local Debug or logs
    private static final Logger log = LoggerFactory.getLogger(Ship.class);
    private static final String TAG = "player : ";
    static Random generator = new Random();
    Body body;

    // Player Dynamic Parameters
    float angle;// actual angle
    float oldAngle;// actual angle

    float v_x, v_y;
    float health;
    float score;

    int a_rate = 2;
    float angularVelocity;
    int type;// image type of the player
    int bulletType;// bullet type of the player

    /* Player moving parameters */
    // Player speed factor

    float v_rate = 0.5f;
    float bullet_v_rate = 0.5f;
    Vec2 v = new Vec2(0.0f, 0.0f);
    PlayerDelta delta;

    private float subTileEdgeX = 0.0f; // Store value of subTileOriginX + subtileWidth
    private float subTileEdgeY = 0.0f; // Store value of subTileOriginY + subtileHeight

    public Player(long userID, float x, float y, float w, float h, float health, float score,
            WebSocketConnection con, int type, int bulletType) {
        super(userID, x, y, w, h);
        super.isPlayer = 1;
        super.con = con;
        /* Set sub tile edge values without method */
        this.subTileEdgeX = (x - (x % sangraamaMap.getSubTileWidth()))
                + sangraamaMap.getSubTileWidth();
        this.subTileEdgeY = (y - (y % sangraamaMap.getSubTileHeight()))
                + sangraamaMap.getSubTileHeight();
        this.health = health;
        this.score = score;
        this.gameEngine.addToPlayerQueue(this);
        this.type = type;
        this.bulletType = bulletType;
    }

    public PlayerDelta getPlayerDelta() {
        // if (!isUpdate) {

        if ((this.body.getPosition().x - this.x) != 0f || (this.body.getPosition().y - this.y) != 0) {
            /*
             * System.out.print(TAG + "id : " + this.userID + " x:" + x * Constants.scale + " y:" +
             * y Constants.scale + " angle:" + this.body.getAngle() + " & " +
             * this.body.getAngularVelocity() + " # "); System.out.println(" x_virtual:" +
             * this.x_virtual * Constants.scale + " y_virtual:" + this.y_virtual * Constants.scale);
             */
        }

        // this.delta = new PlayerDelta(this.body.getPosition().x - this.x,
        // this.body.getPosition().y - this.y, this.userID);
        // System.out.println(TAG + "id : " + this.userID + " x:" + x + " y:" + y + " health:" +
        // this.getHealth() + " Score:"+this.getScore());
        this.delta = new PlayerDelta(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.userID, this.health, this.score, this.type);
        /*
         * for (Bullet bullet : this.removedBulletList) {
         * delta.getBulletDeltaList().add(bullet.getBulletDelta(2)); }
         */
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;
        this.oldAngle = this.body.getAngle();
        // Check whether player is inside the tile or not
        /*
         * Gave this responsibility to client if (!this.isInsideMap(this.x, this.y)) {
         * PlayerPassHandler.INSTANCE.setPassPlayer(this); }
         */

        // isUpdate = true;
        // }
        if (!isInsideServerSubTile(this.x, this.y)) {
            PlayerPassHandler.INSTANCE.setPassPlayer(this);
            System.out.println(TAG + userID + " outside of the subtile detected");
        }
        return this.delta;
    }

    public void applyUpdate() {
        this.body.setLinearVelocity(this.getV());
        this.body.setAngularVelocity(0.0f);
        if (this.angularVelocity == 0) {
            this.body.setTransform(this.body.getPosition(), this.angle);
        } else {
            this.body.setTransform(this.body.getPosition(), this.oldAngle + this.angularVelocity);
        }

    }

    /*
     * This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public void removeWebSocketConnection() {
        con = null;
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
        // System.out.println(TAG + userID + " is inside "+x+":"+y);
        if (sangraamaMap.getOriginX() <= x && x <= sangraamaMap.getEdgeX()
                && sangraamaMap.getOriginY() <= y && y <= sangraamaMap.getEdgeY()) {
            return true;
        } else {
            System.out.println(TAG + userID + " Outside of map : " + sangraamaMap.getEdgeX() + ":"
                    + sangraamaMap.getEdgeY());
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
    protected boolean isInsideServerSubTile(float x, float y) {
        boolean insideServerSubTile = true;
        // Note : Inefficient code. Is it necessary to calculate at each iteration.
        float subTileOriX = x - (x % sangraamaMap.getSubTileWidth());
        float subTileOriY = y - (y % sangraamaMap.getSubTileHeight());
        // System.out.println(TAG + currentSubTileOriginX + ":" + currentSubTileOriginY + " with "
        // + subTileOriX + ":" + subTileOriY);
        if (currentSubTileOriginX != subTileOriX || currentSubTileOriginY != subTileOriY) {
            currentSubTileOriginX = subTileOriX;
            currentSubTileOriginY = subTileOriY;
            if (!sangraamaMap.getHost().equals(TileCoordinator.INSTANCE.getSubTileHost(x, y))) {
                insideServerSubTile = false;
                System.out.println(TAG + userID + " player is not inside a subtile of "
                        + sangraamaMap.getHost());
            }
        }

        return insideServerSubTile;
    }

    // Need to check before use
    protected boolean isInsideServerSubTiles(float x, float y) {
        if (currentSubTileOriginX <= x && x <= this.subTileEdgeX && currentSubTileOriginY <= y
                && y <= this.subTileEdgeY) { // true if player is in current sub tile
            return true;
        } else { // execute when player isn't in the current sub tile
            // Assign new sub tile origin coordinates
            currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
            currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
            this.setSubtileEgdeValues(); // update edge values
            // check whether players coordinates are in current map
            if (!sangraamaMap.getHost().equals(TileCoordinator.INSTANCE.getSubTileHost(x, y))) {
                System.out.println(TAG + userID + " player is not inside a subtile of "
                        + sangraamaMap.getHost());
                return false;
            }
            return true;
        }
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
        if (!isInsideServerSubTile(x, y) && isInsideTotalMap(x, y)) {
            PlayerPassHandler.INSTANCE.setPassConnection(x, y, this);
        }
    }

    public void sendUpdate(List<SendProtocol> deltaList) {
        if (this.con != null) {
            try {
                con.sendUpdate(deltaList);
            } catch (IOException e) {
                this.gameEngine.addToRemovePlayerQueue(this);
                this.isPlayer = 0;
                e.printStackTrace();
            }
        } else if (this.isPlayer == 1) {
            this.gameEngine.addToRemovePlayerQueue(this);
            this.isPlayer = 0;
            System.out.println(TAG + userID + " Unable to send updates,coz con :" + this.con
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + userID + "  waiting for remove (1) id:" + userID
                    + " player type:" + super.isPlayer);
            this.gameEngine.addToRemovePlayerQueue(this);
        }
    }

    /**
     * Send New connection Address and other details to Client
     * 
     * @param transferReq
     *            Object of Client transferring protocol
     */
    public void sendPassConnectionInfo(SendProtocol transferReq) {
        if (super.con != null) {
            ArrayList<SendProtocol> transferReqList = new ArrayList<SendProtocol>();
            transferReqList.add(transferReq);
            con.sendNewConnection(transferReqList);
            /* Changed player type into dummy player and remove from the world */
            this.gameEngine.addToRemovePlayerQueue(this);
            con.setDummyPlayer(new DummyPlayer(userID, screenWidth, screenHeight, con));
        } else if (super.isPlayer == 1) {
            this.gameEngine.addToRemovePlayerQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + userID + " Unable to send new connection,coz con :"
                    + super.con + ". Add to remove queue.");
        } else {
            System.out.println(TAG + userID + "  waiting for remove (2) id:" + userID
                    + " player type:" + super.isPlayer);
        }
    }

    /**
     * Send update server connection Address and other details to Client to fulfill the AOI
     * 
     * @param transferReq
     *            Object of Client transferring protocol
     */
    public void sendUpdateConnectionInfo(SendProtocol transferReq) {
        if (this.con != null) {
            ArrayList<SendProtocol> transferReqList = new ArrayList<SendProtocol>();
            transferReqList.add(transferReq);
            con.sendNewConnection(transferReqList);
        } else if (super.isPlayer == 1) {
            this.gameEngine.addToRemovePlayerQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + userID + " Unable to send new connection,coz con :"
                    + super.con + ". Add to remove queue.");
        } else {
            System.out.println(TAG + userID + "  waiting for remove (3) id:" + userID
                    + " player type:" + super.isPlayer);
        }
    }

    /**
     * This method is used to send the information of the transferring object to the neighbor
     * server.
     * 
     * @param transferReq
     *            message which contains the information of the transferring object
     */
    public void sendTransferringGameObjectInfo(SendProtocol transferReq) {
        if (this.con != null) {
            ArrayList<SendProtocol> transferReqList = new ArrayList<SendProtocol>();
            transferReqList.add(transferReq);
            con.sendPassGameObjInfo(transferReqList);
        }
    }

    public void sendSyncData(List<SendProtocol> syncData) {
        if (this.con != null) {
            try {
                con.sendUpdate(syncData);
            } catch (IOException e) {
                this.gameEngine.addToRemovePlayerQueue(this);
                this.isPlayer = 0;
                e.printStackTrace();
            }
        } else if (super.isPlayer == 1) {
            this.gameEngine.addToRemovePlayerQueue(this);
            this.isPlayer = 0;
            System.out.println(TAG + userID + " Unable to send syncdata,coz con :" + this.con
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + userID + "  waiting for remove (4) id:" + userID
                    + " player type:" + super.isPlayer);
        }
    }

    public void shoot(float s) {
        float r = 3;
        if (s == 1) {
            float x = this.body.getPosition().x;
            float y = this.body.getPosition().y;
            if (0 <= this.angle && this.angle <= 90) {
                float ang = this.angle * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));
                x = x + rX;
                y = y + rY;
            } else if (90 <= this.angle && this.angle <= 180) {
                float ang = (180 - this.angle) * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));

                x = x - rX;
                y = y + rY;
            } else if (180 <= this.angle && this.angle <= 270) {
                float ang = (this.angle - 180) * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));

                x = x - rX;
                y = y - rY;
            } else if (270 <= this.angle && this.angle <= 360) {
                float ang = (360 - this.angle) * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));

                x = x + rX;
                y = y - rY;
            }
            long id = (long) (generator.nextInt(10000));
            Vec2 bulletVelocity = new Vec2((x - this.body.getPosition().x) * this.bullet_v_rate,
                    (y - this.body.getPosition().y) * this.bullet_v_rate);
            Bullet bullet = new Bullet(id, this.userID, x, y, bulletVelocity,
                    this.body.getPosition().x, this.body.getPosition().y, this.getScreenWidth(),
                    this.getScreenHeight(), this.bulletType);
            this.gameEngine.addToBulletQueue(bullet);
            System.out.println(TAG + userID + " : Added a new bullet");
        }
    }

    public abstract BodyDef getBodyDef();

    public abstract FixtureDef getFixtureDef();

    /**
     * Getters and Setters
     */

    public boolean setVirtualPoint(float x_vp, float y_vp) {
        /*
         * Validate data before set virtual point. Idea: Virtual point can't go beyond edges of Full
         * map (the map which divide into sub tiles) with having half of the size of AOI. Then
         * possible virtual point setting will validate by server side. #gihan
         */
        // System.out.println(TAG + userID + "  want to set vp x:" + x_vp + " y:" + y_vp);

        /*
         * If asking for same virtual point, then ignore it
         */
        if (this.x_virtual == x_vp && this.y_virtual == y_vp)
            return false;

        this.x_virtual = x_vp;
        this.y_virtual = y_vp;

        /**
         * Check whether player is in permitted area [check by server]
         */
        if (!isInsideTotalMap(x_vp, y_vp)) {
            if (x_vp < totOrgX) {
                x_virtual = totOrgX;
            }
            if (y_vp < totOrgY) {
                y_virtual = totOrgY;
            }
            if (totEdgeX < x_vp) {
                x_virtual = totEdgeX;
            }
            if (totEdgeY < y_vp) {
                y_virtual = totEdgeY;
            }
            System.out.println(TAG + userID + "  But set as vp x:" + x_vp + " y:" + y_vp);
        }

        List<SendProtocol> data = new ArrayList<SendProtocol>();
        data.add(new SyncPlayer(userID, x, y, x_virtual, y_virtual, angle, screenWidth,
                screenHeight));
        // System.out.println(TAG + userID + " Virtual point x" + x_virtual + " y" + y_virtual);
        this.sendSyncData(data);

        return true;
    }

    private void setSubtileEgdeValues() {
        this.subTileEdgeX = currentSubTileOriginX + sangraamaMap.getSubTileWidth();
        this.subTileEdgeY = currentSubTileOriginY + sangraamaMap.getSubTileHeight();
    }

    /**
     * 
     * @param body
     */
    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody(Body body) {
        return this.body;
    }

    public Body getBody() {
        return body;
    }

    public Vec2 getV() {
        return this.v;
    }

    public void setV(float x, float y) {
        // Fixed: if client send x value greater than 1
        this.v.set(x % 2 * v_rate, y % 2 * v_rate);
        // System.out.println(TAG + userID + "  set V :" + this.v.x + ":" + this.v.y);
    }

    public void setAngle(float a) {
        this.angle = a;
        // System.out.println(TAG + userID + "  set angle : " + a + " > " + this.angle);
    }

    public void setAngularVelocity(float da) {
        this.angularVelocity = da % 2 * a_rate;
        // System.out.println(TAG + userID + "  set angular velocity : " + da + " > "
        // + this.angularVelocity);
    }

    public float getAngle() {
        return angle;
    }

    public void setHealth(float healthChange) {
        if ((this.health + healthChange) > 0) {
            this.health += healthChange;
        } else {
            this.health = 0;
            this.setScore(-200);
            gameEngine.addToDefaetList(this);
            gameEngine.addToRemovePlayerQueue(this);
        }
    }

    public float getHealth() {
        if (this.health < 0)
            return 0;
        else
            return this.health;
    }

    public float getScore() {
        if (this.score > 0) {
            return this.score;
        } else {
            return 0;
        }
    }

    public int getType() {
        return type;
    }

    public void setScore(float scoreChange) {
        if ((this.score + scoreChange) > 0) {
            this.score += scoreChange;
        } else {
            this.score = 0;
        }
    }

    public DefeatMsg getDefeatMsg() {
        return new DefeatMsg(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.userID, this.score, 6, this.type);
    }

}
