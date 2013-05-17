package org.sangraama.asserts;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PlayerDef {
  private BodyDef bodyDef = null;
  private FixtureDef fixtureDef = null;
  private Body body = null;
  
  public PlayerDef() {
    this.bodyDef = createBodyDef();
    this.fixtureDef = createFixtureDef();
  }
  
  private BodyDef createBodyDef(){
    BodyDef bd = new BodyDef();
    bd.position.set(50, 50);
    bd.type = BodyType.DYNAMIC;
    return bd;
  }
  
  public BodyDef getBodyDef() {
    return this.bodyDef;
  }
  
  private FixtureDef createFixtureDef(){
    CircleShape circle = new CircleShape();
    circle.m_radius = 1f;
    
    FixtureDef fd = new FixtureDef();
    fd.density = 0.5f;
    fd.shape = circle;
    fd.friction = 0.2f;
    fd.restitution = 0.5f;
    return fd;
  }
  
  public FixtureDef getFixtureDef(){
    return this.fixtureDef;
  }
  
  public void setBody(Body body){
    this.body = body;
  }
  
  public Body getBody(){
    return this.body;
  }
  
}
