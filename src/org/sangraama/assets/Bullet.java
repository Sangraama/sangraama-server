package org.sangraama.assets;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.controller.BulletPassHandler;
import org.sangraama.controller.clientprotocol.BulletDelta;
import org.sangraama.coordination.staticPartition.TileCoordinator;

public class Bullet {

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
    private float currentSubTileEndX;
    private float currentSubTileEndY;

    public Bullet(long id, long playerId, float x, float y, Vec2 velocity, float originX, float originY, float w, float h) {
        this.id = id;
        this.playerId = playerId;
        this.originX = originX;
        this.originY = originY;
        this.screenHeight = h;
        this.screenWidth = w;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        this.currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        this.currentSubTileEndX = (x - (x % sangraamaMap.getSubTileWidth())) + sangraamaMap.getSubTileWidth();
        this.currentSubTileEndY = (y - (y % sangraamaMap.getSubTileHeight())) + sangraamaMap.getSubTileHeight();
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

    public float getX(){
        return this.body.getPosition().x;
    }
    
    public float getY(){
        return this.body.getPosition().y;
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
        bulletDelta = new BulletDelta(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.playerId, this.id);
        if(!isInsideSeverSubTile(this.body.getPosition().x, this.body.getPosition().y)){
            BulletPassHandler.INSTANCE.passBullets(this);
        }
        return bulletDelta;
    }
    
    /**
     * This method check whether the x,y coordinates are out of the server controlled area or not
     * 
     * @param x
     *      x coordination of the position
     * @param y
     *      y coordination of the position
     * @return
     *      true if coordinate is inside the server assingned area.
     */
    private boolean isInsideSeverSubTile(float x,float y){
        if (currentSubTileOriginX <= x && x <= currentSubTileEndX && currentSubTileOriginY <= y && y <= currentSubTileEndY) { 
            return true;
        }
        else{
            currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
            currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
            currentSubTileEndX = (x - (x % sangraamaMap.getSubTileWidth())) + sangraamaMap.getSubTileWidth();
            currentSubTileEndY = (y - (y % sangraamaMap.getSubTileHeight())) + sangraamaMap.getSubTileHeight();
            if (!sangraamaMap.getHost().equals(TileCoordinator.INSTANCE.getSubTileHost(x, y))) {
                System.out.println(TAG + "Bullet is not inside a subtile of "
                        + sangraamaMap.getHost());
                return false;
            }
            return true;
        }
    }
}
