package org.sangraama.asserts.map;

import java.util.ArrayList;
import java.util.List;

public class StaticObject {
	private int x;
	private int y;
	private int width;
	private int height;
	private List<Points> coordinates= new ArrayList<>();
	private String name;
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public List<Points> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(List<Points> coordinates) {
		this.coordinates = coordinates;
	}
	
	/*public void setPhysics(){
		PhysicsAPI physicsAPI=new PhysicsAPI();
		if(this.type.equals("plygon")){
			physicsAPI.getPolygonPhysics();
		}
		
	}*/
}
