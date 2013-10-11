package org.sangraama.assets;

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
import org.sangraama.controller.clientprotocol.AbsDelta;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.controller.clientprotocol.SangraamaTile;
import org.sangraama.controller.clientprotocol.TileInfo;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Player extends AbsPlayer {

    // Debug
    // Local Debug or logs
    public static final Logger log = LoggerFactory.getLogger(Ship.class);
    private static final String TAG = "player : ";
    static Random generator = new Random();
    Body body;

    // Player Dynamic Parameters
    float angle;// actual angle
    float oldAngle;// actual angle

    float v_x, v_y, midX, midY;
    float health;
    float score;

    int a_rate = 2;
    float angularVelocity;

    /* Player moving parameters */
    // Player speed factor
    int v_rate = 200;
    Vec2 v = new Vec2(0f, 0f);
    PlayerDelta delta;

    public Player(long userID, WebSocketConnection con) {
        super(userID);
        super.isPlayer = 1;
        System.out.println(TAG + " player added to queue");
        this.gameEngine.addToPlayerQueue(this);
    }

    public Player(long userID, float x, float y, float w, float h, WebSocketConnection con) {
        super(userID, x, y, w, h);
        super.isPlayer = 1;
        super.conPlayer = con;
        this.gameEngine.addToPlayerQueue(this);
        this.health = 100;
        this.score = 0;
    }

    public void removeWebSocketConnection() {
        super.conPlayer = null;
    }

    public PlayerDelta getPlayerDelta() {
        // if (!isUpdate) {
        if ((this.body.getPosition().x - this.x) != 0f || (this.body.getPosition().y - this.y) != 0) {
            System.out.println(TAG + "id : " + this.userID + " x:" + x + " " + "y:" + y + " angle:"
                    + this.body.getAngle() + "&" + this.body.getAngularVelocity());
        }

        // this.delta = new PlayerDelta(this.body.getPosition().x - this.x,
        // this.body.getPosition().y - this.y, this.userID);
        this.delta = new PlayerDelta(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.userID, this.health, this.score);
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
            System.out.println(TAG + "outside of the subtile detected");
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
    protected boolean isInsideServerSubTile(float x, float y) {
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
        this.midX = x;
        this.midY = y;
        if (!isInsideServerSubTile(x, y)) {
            PlayerPassHandler.INSTANCE.setPassConnection(x, y, this);
        }
    }

    public void sendUpdate(List<AbsDelta> deltaList) {
        if (super.conPlayer != null) {
            conPlayer.sendUpdate(deltaList);
        } else if (super.isPlayer == 1) {
            this.gameEngine.addToRemovePlayerQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send updates,coz con :" + super.conPlayer
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + " waiting for remove");
        }
    }

    /**
     * Send New connection Address and other details to Client
     * 
     * @param transferReq
     *            Object of Client transferring protocol
     */
    public void sendNewConnection(ClientTransferReq transferReq) {
        if (super.conPlayer != null) {
            ArrayList<ClientTransferReq> transferReqList = new ArrayList<ClientTransferReq>();
            transferReqList.add(transferReq);
            conPlayer.sendNewConnection(transferReqList);
        } else if (super.isPlayer == 1) {
            this.gameEngine.addToRemovePlayerQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send new connection,coz con :" + super.conPlayer
                    + ". Add to remove queue.");
        } else {
            System.out.println(TAG + " waiting for remove");
        }
    }

    /**
     * Send update server connection Address and other details to Client to fulfill the AOI
     * 
     * @param transferReq
     *            Object of Client transferring protocol
     */
    public void sendConnectionInfo(ClientTransferReq transferReq) {
        if (super.conPlayer != null) {
            ArrayList<ClientTransferReq> transferReqList = new ArrayList<ClientTransferReq>();
            transferReqList.add(transferReq);
            conPlayer.sendNewConnection(transferReqList);
        } else if (super.isPlayer == 1) {
            this.gameEngine.addToRemovePlayerQueue(this);
            super.isPlayer = 0;
            System.out.println(TAG + "Unable to send new connection,coz con :" + super.conPlayer
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
        super.conPlayer.sendTileSizeInfo(new TileInfo(this.userID, tiles));
    }

    /**
     * Send details about the size of the tile on current server. Sub-tiles sizes may access during
     * TileInfo Object creation
     * 
     */
    public void sendTileSizeInfo() {
        super.conPlayer.sendTileSizeInfo(new TileInfo(this.userID));
    }

    public void shoot(float s) {
        float r = 50;
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
            Bullet bullet = new Bullet(id, this.userID, x, y, this.body.getPosition().x,
                    this.body.getPosition().y, this.getScreenWidth(), this.getScreenHeight());
            this.gameEngine.addToBulletQueue(bullet);
            System.out.println(TAG + ": Added a new bullet");
        }
    }

    public abstract BodyDef getBodyDef();

    public abstract FixtureDef getFixtureDef();

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
        System.out.println(TAG + " set V :" + this.v.x + ":" + this.v.y);
    }

    public void setAngle(float a) {
        this.angle = a;
        System.out.println(TAG + " set angle : " + a + " > " + this.angle);
    }

    public void setAngularVelocity(float da) {
        this.angularVelocity = da % 2 * a_rate;
        System.out.println(TAG + " set angular velocity : " + da + " > " + this.angularVelocity);
    }

    public float getAngle() {
        return angle;
    }

    public void setHealth(float healthChange) {
        if ((this.health + healthChange) > 0) {
            this.health += healthChange;
        } else {
            this.health = 0;
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

    public void setScore(float scoreChange) {
        if ((this.score + scoreChange) > 0) {
            this.score += scoreChange;
        } else {
            this.score = 0;
        }
    }

    public float getMidX() {
        return midX;
    }

    public float getMidY() {
        return midY;
    }

}
