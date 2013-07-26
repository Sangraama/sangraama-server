package org.sangraama.asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.common.Constants;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.Hazelcast;

public class Player {

    // Debug
    // Local Debug or logs
    public static final Logger log = LoggerFactory.getLogger(Player.class);
    private static final String TAG = "player : ";

    private long userID;

    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Body body;
    private GameEngine gameEngine;
    private SangraamaMap sangraamaMap;
    // WebSocket Connection
    private WebSocketConnection con;
    private boolean isUpdate;

    // Player Dynamic Parameters
    private float x, y, angle;
    public float v_x, v_y;
    private Vec2 v = new Vec2(0f, 0f);
    private PlayerDelta delta;

    // Area of Interest
    private float halfWidth = 10f;
    private float halfHieght = 1000f;

    // bullets
    private List<Bullet> newBulletList;
    private List<Bullet> bulletList;

    public boolean isUpdate() {
        return this.isUpdate;
    }

    public Player(long userID, WebSocketConnection con) {
        Random r = new Random();
        this.createPlayer(userID, (float) r.nextInt(1000), (float) r.nextInt(999), con);
    }

    public Player(long userID, float x, float y, WebSocketConnection con) {
        this.createPlayer(userID, x, y, con);
    }

    private void createPlayer(long userID, float x, float y, WebSocketConnection con) {

        this.userID = userID;
        this.x = x;
        this.y = y;
        this.con = con;
        this.bodyDef = this.createBodyDef();
        this.fixtureDef = createFixtureDef();
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.gameEngine = GameEngine.INSTANCE;
        this.gameEngine.addToPlayerQueue(this);
        this.newBulletList = new ArrayList<Bullet>();
        this.bulletList = new ArrayList<Bullet>();

        System.out.println(TAG + " init player : " + userID + " x-" + x + " : y-" + y);
    }

    /**
     * This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public void removeWebSocketConnection() {
        this.con = null;
    }

    public PlayerDelta getPlayerDelta() {
        // if (!isUpdate) {
        if ((this.body.getPosition().x - this.x) != 0f || (this.body.getPosition().y - this.y) != 0) {
            System.out.println(TAG + "id : " + this.userID + " x:" + x + " " + "y:" + y);
        }

        // this.delta = new PlayerDelta(this.body.getPosition().x - this.x,
        // this.body.getPosition().y - this.y, this.userID);
        this.delta = new PlayerDelta(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.userID);
        for (Bullet bullet : this.bulletList) {
            delta.getBulletDeltaList().add(bullet.getBulletDelta());
        }
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;
        this.angle = this.body.getAngle();
        // Check whether player is inside the tile or not
        /*
         * Gave this responsibility to client if (!this.isInsideMap(this.x, this.y)) {
         * PlayerPassHandler.INSTANCE.setPassPlayer(this); }
         */

        // isUpdate = true;
        // }
        if(!isInsideServerSubTile(x, y)){
            PlayerPassHandler.INSTANCE.setPassPlayer(this);
        }
        return this.delta;
    }

    public void sendUpdate(List<PlayerDelta> deltaList) {
        if (this.con != null) {
            con.sendUpdate(deltaList);
        } else {
            this.gameEngine.addToRemovePlayerQueue(this);
            System.out.println(TAG + "Unable to send updates,coz con :" + this.con);
        }
    }

    public void applyUpdate() {
        this.body.setLinearVelocity(this.getV());
    }

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

    private boolean isInsideServerSubTile(float x, float y) {
        boolean insideServerSubTile = true;
        if (!sangraamaMap.getHost().equals(TileCoordinator.INSTANCE.getSubTileHost(x, y))) {
            insideServerSubTile = false;
            System.out.println(TAG + "player is not inside a subtile of " + sangraamaMap.getHost());
        }
        return insideServerSubTile;
    }

    public void setInterestingIn(float x, float y) {
        if (!isInsideMap(x, y)) {
            PlayerPassHandler.INSTANCE.setPassPlayer(this);
        }
    }

    public void sendNewConnection(ClientTransferReq transferReq) {
        if (this.con != null) {
            ArrayList<ClientTransferReq> transferReqList = new ArrayList<ClientTransferReq>();
            transferReqList.add(transferReq);
            con.sendNewConnection(transferReqList);
        } else {
            this.gameEngine.addToRemovePlayerQueue(this);
            System.out.println(TAG + "Unable to send new connection,coz con :" + this.con);
        }
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
            Bullet bullet = new Bullet(this.userID, x, y);
            this.newBulletList.add(bullet);
            System.out.println(TAG + ": Added a new bullet");
        }
    }

    public BodyDef createBodyDef() {
        BodyDef bd = new BodyDef();
        System.out.println(TAG + "create body def player x:" + this.x + " :" + this.y);
        bd.position.set(this.x, this.y);
        bd.type = BodyType.DYNAMIC;
        return bd;
    }

    public BodyDef getBodyDef() {
        return this.bodyDef;
    }

    private FixtureDef createFixtureDef() {
        // CircleShape circle = new CircleShape();
        // circle.m_radius = 1f;
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(5f, 5f);

        FixtureDef fd = new FixtureDef();
        fd.density = 0.5f;
        // fd.shape = circle;
        fd.shape = ps;
        fd.friction = 0.2f;
        fd.restitution = 0.5f;
        return fd;
    }

    public FixtureDef getFixtureDef() {
        return this.fixtureDef;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return this.body;
    }

    // public void setX(float x) {
    // if (x > 0) {
    // this.x = x;
    // }
    // }

    public float getX() {
        return x;
    }

    // public void setY(float y) {
    // if (y > 0) {
    // this.y = y;
    // }
    // }

    public float getY() {
        return this.y;
    }

    public Vec2 getV() {
        return this.v;
    }

    public void setV(float x, float y) {
        this.v.set(x * 20, y * 20);
        System.out.println(TAG + " set V :" + this.v.x + ":" + this.v.y);
    }

    public void setAngle(float a) {
        this.angle = a;
        this.angle %= 360;
        this.body.setTransform(body.getPosition(), angle);

    }

    public long getUserID() {
        return this.userID;
    }

    public void setAOI(float width, float height) {
        this.halfWidth = width / 2;
        this.halfHieght = height / 2;
    }

    public float getAOIWidth() {
        return this.halfWidth;
    }

    public float getAOIHeight() {
        return this.halfHieght;
    }

    public List<Bullet> getNewBulletList() {
        return newBulletList;
    }

    public List<Bullet> getBulletList() {
        return bulletList;
    }

    public float getAngle() {
        return angle;
    }

}
