package org.sangraama.asserts.map;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.asserts.map.physics.PolygonObjectPhysics;
import org.sangraama.asserts.map.physics.PolylineObjectPhysics;

public class PhysicsAPI {
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	public void applyPhysics(StaticObject staticObject){
		if(staticObject.getType().equals("polygon")){
			PolygonObjectPhysics polygon=new PolygonObjectPhysics();
			polygon.createBodyDef(staticObject.getX(), staticObject.getY());
			this.bodyDef=polygon.getBodyDef();
			Vec2[] vertices=new Vec2[staticObject.getCoordinates().size()];
			for(int i=0;i<vertices.length;i++){
				vertices[i].set(staticObject.getCoordinates().get(i).getX(), staticObject.getCoordinates().get(i).getY());
			}
			
			polygon.createFixtureDef(vertices, vertices.length);
			this.fixtureDef=polygon.getFixtureDef();
		}
		if(staticObject.getType().equals("polyline")){
		PolylineObjectPhysics polyline=new PolylineObjectPhysics();
		Vec2[] vertices=new Vec2[staticObject.getCoordinates().size()];
		//vertices[0]=new Vec2();
		//System.out.println("Vec2"+vertices[0].x);
		//System.out.println("polyline");
		//System.out.println("length"+vertices.length);
		//System.out.println(staticObject.getName());
		//System.out.println(staticObject.getType());
		//System.out.println(g.getStaticObjects().get(i).getHeight());
		//System.out.println(g.getStaticObjects().get(i).getWidth());
		//System.out.println(staticObject.getX());
		//System.out.println(staticObject.getY());
		//for(int k=0;k<staticObject.getCoordinates().size();k++){
		///System.out.println(staticObject.getCoordinates().get(k).getX());
		//System.out.println(staticObject.getCoordinates().get(k).getY());
		//}
		for(int i=0;i<vertices.length;i++){
		//	System.out.println("in for loop");
			//System.out.println("x="+staticObject.getCoordinates().get(i).getX());
			//System.out.println("y="+staticObject.getCoordinates().get(i).getY());
			vertices[i]=new Vec2();
			vertices[i].set(staticObject.getCoordinates().get(i).getX(), staticObject.getCoordinates().get(i).getY());
			//System.out.println("x="+vertices[i].x);
			//System.out.println("*");
			
			//System.out.println("y="+vertices[i].y);
		}
		//vertices[vertices.length-1]=new Vec2();
		//vertices[vertices.length-1].set(staticObject.getCoordinates().get(0).getX(), staticObject.getCoordinates().get(0).getY());
	//	System.out.println("xx="+vertices[vertices.length-1].x);
		//System.out.println("yy="+vertices[vertices.length-1].y);
		polyline.createChain(vertices, vertices.length);
		}
	}
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
	
}
