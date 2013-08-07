package org.sangraama.asserts.map;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.sangraama.asserts.map.tileeditor.TileLoader;





public class MapAPI {
	private int mapHeight;
	private int mapWidth;
	private List<StaticObject> staticObjects=new ArrayList<StaticObject>();
	
	

	public void getMapDetails(){
		TileLoader tileLoader=new TileLoader();
		tileLoader.ParseMapFile(); 
		mapHeight=tileLoader.getMapHeight();
		mapWidth=tileLoader.getMapWidth();
		for(int i=0;i<tileLoader.getObjects().size();i++){
			StaticObject staticObject=new StaticObject();
			staticObject.setX(tileLoader.getObjects().get(i).getX());
			staticObject.setY(tileLoader.getObjects().get(i).getY());
			staticObject.setHeight(tileLoader.getObjects().get(i).getHeight());
			staticObject.setWidth(tileLoader.getObjects().get(i).getWidth());
			String points=tileLoader.getObjects().get(i).getPolygon().getPoints();
			String regex="\\s";
			Pattern p= Pattern.compile(regex);
			String[] items = p.split(points);
			List<Points> coordinates= new ArrayList<Points>();
			for(String s : items) {
				Pattern pattern= Pattern.compile(",");
				String[] digits = pattern.split(s);
				Integer digit1=Integer.parseInt(digits[0]);
				Integer digit2=Integer.parseInt(digits[1]);
				Points point=new Points();
				point.setX(digit1);
				point.setY(digit2);
				coordinates.add(point);
	            //System.out.println(s);
	        }
			staticObject.setCoordinates(coordinates);
			staticObjects.add(staticObject);
		}
		
		
	}




	public List<StaticObject> getStaticObjects() {
		return staticObjects;
	}




	public void setStaticObjects(List<StaticObject> staticObjects) {
		this.staticObjects = staticObjects;
	}




	public int getMapHeight() {
		return mapHeight;
	}




	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}




	public int getMapWidth() {
		return mapWidth;
	}




	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}
	




}
