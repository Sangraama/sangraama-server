package org.sangraama.asserts;

import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.gameLogic.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
    // Debug
    // Local Debug or logs
    private static boolean LL = true;
    private static boolean LD = true;
    public static final Logger log = LoggerFactory.getLogger(Player.class);
    private static final String TAG = "player : ";

    private long userID = 0;

    private BodyDef bodyDef = null;
    private FixtureDef fixtureDef = null;
    private Body body = null;
    private GameEngine gameEngine = null;
    private SangraamaMap sangraamaMap = null;
    // WebSocket Connection
    private WebSocketConnection con = null;
    private boolean isUpdate = false;

    // Player Dynamic Parameters
    private float x, y;
    public float v_x = 0, v_y = 0;
    private Vec2 v = new Vec2(0f, 0f);
    private PlayerDelta delta = null;

    // Area of Interest
    private float halfWidth = 10f;
    private float halfHieght = 1000f;

    public boolean isUpdate() {
        return this.isUpdate;
    }

    public Player(long userID, WebSocketConnection con) {
        Random r = new Random();
        this.createPlayer(userID, (float) r.nextInt(2) + 997f, (float) r.nextInt(999), con);
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
        // Add to game engine
        this.gameEngine.addToPlayerQueue(this);

        System.out.println(TAG + " init player : " + userID + " x-" + x + " : y-" + y);
    }
    
    /**
     * This method isn't secure. Have to inherit from a interface both this and WebSocketConnection
     */
    public void removeWebSocketConnection(){
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
                this.userID);
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;

        // Check whether player is inside the tile or not
        if (!this.isInsideMap(this.x, this.y)) {
            PlayerPassHandler.INSTANCE.setPassPlayer(this);
        }

        // isUpdate = true;
        // }
        return this.delta;
    }

    public void sendUpdate(ArrayList<PlayerDelta> deltaList) {
        if (this.con != null) {
            con.sendUpdate(deltaList);
        } else {
            this.gameEngine.addToRemovePlayerQueue(this);
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
            System.out
                    .println(TAG + sangraamaMap.getMapWidth() + ":" + sangraamaMap.getMapHeight());
            return false;
        }
    }

    public void sendNewConnection(ClientTransferReq transferReq) {
        con.sendNewConnection(transferReq);
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
        CircleShape circle = new CircleShape();
        circle.m_radius = 1f;

        FixtureDef fd = new FixtureDef();
        fd.density = 0.5f;
        fd.shape = circle;
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
        this.v.set(x, y);
        // System.out.println(TAG + " set V :");
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
}
