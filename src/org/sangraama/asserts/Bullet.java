package org.sangraama.asserts;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class Bullet {

    private String TAG="Bullet : ";
    private long shooterID;
    private BodyDef bulletBodyDef = null;
    private FixtureDef bulletFixDef = null;
    private float shootingPos_x, shootingPos_y;
    private Vec2 bulletVelocity;
    private Body bulletBody = null;

    public Bullet(long shooterID, float x, float y) {
        this.shooterID = shooterID;
        this.shootingPos_x = x;
        this.shootingPos_y = y;
        this.bulletBodyDef = this.createBodyDef();
        this.bulletFixDef = this.createFixDef();
    }

    public BodyDef createBodyDef() {
        BodyDef bd = new BodyDef();
        bd.position.set(this.shootingPos_x, this.shootingPos_y);
        bd.type = BodyType.DYNAMIC;
        return bd;
    }

    public FixtureDef createFixDef() {
        CircleShape circle = new CircleShape();
        circle.m_radius = 0.25f;
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.friction = 0.2f;
        fd.restitution = 0.5f;
        return fd;
    }

    public void setBulletBody(Body bulletBody) {
        if(bulletBody==null){
            this.bulletBody = bulletBody;
        }
        this.bulletBody.setLinearVelocity(new Vec2(2f,0f));
    }
    
    public Body getBulletBody(){
            return this.bulletBody;
    }

}
