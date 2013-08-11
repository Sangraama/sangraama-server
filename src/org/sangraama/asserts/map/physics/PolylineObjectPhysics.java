package org.sangraama.asserts.map.physics;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;


public class PolylineObjectPhysics {
	//private BodyDef bodyDef;
	//private FixtureDef fixtureDef;
	private ChainShape chainShape;
	private Vec2[] vector;
	public ChainShape getChainShape() {
		return chainShape;
	}
	public void setChainShape(ChainShape chainShape) {
		this.chainShape = chainShape;
	}
	public Vec2[] getVector() {
		return vector;
	}
	public void setVector(Vec2[] vector) {
		this.vector = vector;
		
	}
	public void createChain(Vec2[] vertices, int count){
		chainShape=new ChainShape();
		chainShape.createChain(vertices, count);
		chainShape.createLoop(vertices, count);
	}
}
