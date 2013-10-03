package org.sangraama.asserts.map;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.map.physics.PolygonObjectPhysics;
import org.sangraama.asserts.map.physics.PolylineObjectPhysics;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;
import org.sangraama.assets.SangraamaMap;

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
				vertices[i]=new Vec2();
				vertices[i].set(staticObject.getCoordinates().get(i).getX(), staticObject.getCoordinates().get(i).getY());
			}
			
			polygon.createFixtureDef(vertices, vertices.length);
			this.fixtureDef=polygon.getFixtureDef();
		}
		if(staticObject.getType().equals("polyline")){
		PolylineObjectPhysics polyline=new PolylineObjectPhysics();
		polyline.createBodyDef(staticObject.getX(), staticObject.getY());
		this.bodyDef=polyline.getBodyDef();
		Vec2[] vertices=new Vec2[staticObject.getCoordinates().size()];
		
		for(int i=0;i<vertices.length;i++){
		
			vertices[i]=new Vec2();
			vertices[i].set(staticObject.getCoordinates().get(i).getX(), staticObject.getCoordinates().get(i).getY());
			
		}
		polyline.createFixtureDef(vertices, vertices.length);
		
		this.fixtureDef=polyline.getFixtureDef();
		
		System.out.println("XXXXXXXXXX"+staticObject.getX());
		System.out.println("YYYYYYYYYYYYYYY"+staticObject.getY());
		
		}
	}
	
	public void applyPhysics(List<StaticObject> staticObjects,World world){
		float xLimit=SangraamaMap.INSTANCE.getOriginX()+SangraamaMap.INSTANCE.getMapWidth(); 	//The limit in X-axis of the map related to the server.
    	float yLimit=SangraamaMap.INSTANCE.getOriginY()+SangraamaMap.INSTANCE.getMapHeight();	//The limit in Y-axis of the map related to the server.
    	for(int i=0;i<staticObjects.size();i++){ // for each static object
    		int count=0;
    			for(int k=0;k<staticObjects.get(i).getCoordinates().size();k++){ //for each coordinate of the object
    				if(staticObjects.get(i).getCoordinates().get(k).getX()<xLimit && staticObjects.get(i).getCoordinates().get(k).getY()<yLimit){ //if the x and y coordiantes of the object is within the map 
    					count++;
    					applyPhysics(staticObjects.get(i)); //apply the physics to that object.
        				Body newStaticObjectBody=world.createBody(this.getBodyDef()); // add the static object to the game world.
        				newStaticObjectBody.createFixture(this.getFixtureDef());
    				}
    				if(count>0)
    					break;
    				}	
    		
    			//if(count>0 && count<=staticObjects.get(i).getCoordinates().size()){ //if at least one coordinate is within the map or all the coordinates of the object is within the map
    				
    				
    			//}
    		
    		}
	}
	
	public void applyPhysics(Player player,World world){
		Body newPlayerBody = world.createBody(player.getBodyDef());
        newPlayerBody.createFixture(player.getFixtureDef());
        player.setBody(newPlayerBody);
	}
	public void applyPhysics(Bullet bullet,World world){
		 Body newBulletBody = world.createBody(bullet.getBodyDef());
		
		/*Body newPlayerBody = world.createBody(player.getBodyDef());
        newPlayerBody.createFixture(player.getFixtureDef());
        player.setBody(newPlayerBody);*/
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