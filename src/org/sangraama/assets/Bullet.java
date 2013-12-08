package org.sangraama.assets;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.common.Constants;
import org.sangraama.controller.BulletPassHandler;
import org.sangraama.coordination.MapCoordinator;
import org.sangraama.coordination.SangraamaMap;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.aoi.subtile.Point;
import org.sangraama.gameLogic.aoi.subtile.SubTileHandler;
import org.sangraama.jsonprotocols.send.BulletDelta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bullet {
    // Debug
    // Local Debug or logs
    private static final Logger log = LoggerFactory.getLogger(Bullet.class);
    private String TAG = "Bullet : ";
    private long playerId;

    private float originX, originY;
    private float x, y;
    private Vec2 velocity;
    private Body body;
    private BulletDelta bulletDelta;
    private long id;
    float screenWidth, screenHeight;

    private SangraamaMap sangraamaMap;
    private float currentSubTileOriginX;
    private float currentSubTileOriginY;
    // Current subtile index at SubTileHandler: for efficient retrieval
    private float currentSubTileIndex;
    private float currentSubTileEndX;
    private float currentSubTileEndY;
    private int type; // bullet type

    public Bullet(long id, long playerId, float x, float y, Vec2 velocity, float originX,
            float originY, float w, float h, int bulletType) {
        this.id = id;
        this.playerId = playerId;
        this.originX = originX;
        this.originY = originY;
        this.screenHeight = h;
        this.screenWidth = w;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.type = bulletType;
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        this.currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        this.currentSubTileIndex = this.currentSubTileOriginY * Constants.subTileHashFactor
                + this.currentSubTileOriginX;
        this.currentSubTileEndX = (x - (x % sangraamaMap.getSubTileWidth()))
                + sangraamaMap.getSubTileWidth();
        this.currentSubTileEndY = (y - (y % sangraamaMap.getSubTileHeight()))
                + sangraamaMap.getSubTileHeight();
    }

    public void setBody(Body bulletBody) {
        this.body = bulletBody;
    }

    public Body getBody() {
        return this.body;
    }

    public BodyDef getBodyDef() {
        BodyDef bd = new BodyDef();
        bd.position.set(this.x, this.y);
        bd.type = BodyType.DYNAMIC;
        bd.bullet = true;
        // bd.fixedRotation = true;
        return bd;
    }

    public FixtureDef getFixtureDef() {
        CircleShape circle = new CircleShape();
        circle.m_radius = 0.5f;
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = 0.1f;
        // fd.shape = circle;
        fd.userData = this;
        // fd.friction = 0.2f;
        fd.restitution = 0.5f;
        fd.filter.groupIndex = 2;
        return fd;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
    
    /**
     * Get Player location as a point
     *
     * @return player location point
     */
    public Point getCoordination() {
        return new Point(x, y);
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public long getId() {
        return id;
    }

    public long getPlayerId() {
        return this.playerId;
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public BulletDelta getBulletDelta() {
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;
        this.bulletDelta = new BulletDelta(this.x, this.y, this.body.getAngle(), this.playerId,
                this.id, this.type);
        if (!isInsideSeverSubTile(this.x, this.y)) {
            BulletPassHandler.INSTANCE.passBullets(this);
        }
        return bulletDelta;
    }

    public int getType() {
        return type;
    }

    /**
     * This method check whether the x,y coordinates are out of the server controlled area or not
     * 
     * @param x
     *            x coordination of the position
     * @param y
     *            y coordination of the position
     * @return true if coordinate is inside the server assingned area.
     */
    private boolean isInsideSeverSubTile(float x, float y) {
        if (currentSubTileOriginX <= x && x <= currentSubTileEndX && currentSubTileOriginY <= y
                && y <= currentSubTileEndY) {
            return true;
        } else {
            SubTileHandler.INSTANCE.removeBullet(this.currentSubTileIndex, this);
            currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
            currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
            currentSubTileEndX = (x - (x % sangraamaMap.getSubTileWidth()))
                    + sangraamaMap.getSubTileWidth();
            currentSubTileEndY = (y - (y % sangraamaMap.getSubTileHeight()))
                    + sangraamaMap.getSubTileHeight();
            
            if (!sangraamaMap.getHost().equals(TileCoordinator.INSTANCE.getSubTileHost(x, y))) {
                /*
                 * log.info(TAG + "Bullet is not inside a subtile of " + sangraamaMap.getHost());
                 */
                return false;
            }
            this.currentSubTileIndex = this.currentSubTileOriginY * Constants.subTileHashFactor
                    + this.currentSubTileOriginX;
            SubTileHandler.INSTANCE.addBullet(this.currentSubTileIndex, this);
            return true;
        }
    }
    
    public boolean addToSubTileHandler() {
        return SubTileHandler.INSTANCE.addBullet(this.currentSubTileIndex, this);
    }

    public boolean removeFromSubTileHandler() {
        return SubTileHandler.INSTANCE.removeBullet(this.currentSubTileIndex, this);
    }
}
