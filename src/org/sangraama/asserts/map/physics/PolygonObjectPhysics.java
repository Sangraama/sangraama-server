package org.sangraama.asserts.map.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PolygonObjectPhysics {
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private PolygonShape polygonShape;
	
	public BodyDef getBodyDef() {
		return bodyDef;
	}
	public void setBodyDef(BodyDef bodyDef) {
		this.bodyDef = bodyDef;
	}
	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}
	public void setFixtureDef(FixtureDef fixtureDef) {
		this.fixtureDef = fixtureDef;
	}
	public PolygonShape getPolygonShape() {
		return polygonShape;
	}
	public void setPolygonShape(PolygonShape polygonShape) {
		this.polygonShape = polygonShape;
	} 
	public void createBodyDef(int x,int y) {
		bodyDef=new BodyDef();
		bodyDef.type=BodyType.STATIC;
		bodyDef.position.set(x, y);	
	}
	public void createFixtureDef(Vec2[] vertices, int count) {
		polygonShape=new PolygonShape();
		polygonShape.set(vertices, count);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
	}
}
