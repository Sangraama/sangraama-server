package org.sangraama.asserts.map;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.sangraama.asserts.map.tileeditor.TileLoader;

/**
 * This class acts as an interface for game engine and map editor module.
 * It extracts static game objects from the map editor and provide them for
 * the game engine to apply physics.
 */
public class MapAPI {
    private int mapHeight;
    private int mapWidth;
    private List<StaticObject> staticObjects = new ArrayList<>();

    /**
     * get map details from the tile editor module and store all the static objects.
     */
    public void getMapDetails() {
        TileLoader tileLoader = new TileLoader();     //This can be replaced by any other map generating module
        tileLoader.ParseMapFile();     //load the game objects into object hierarchy.
        mapHeight = tileLoader.getMapHeight();
        mapWidth = tileLoader.getMapWidth();
        for (int i = 0; i < tileLoader.getObjects().size(); i++) {  //loop through all the static objects
            StaticObject staticObject = new StaticObject();
            staticObject.setName(tileLoader.getObjects().get(i).getName());      //set the name of the object
            staticObject.setType(tileLoader.getObjects().get(i).getType());      //set the type of the object
            int x = tileLoader.getObjects().get(i).getX();
            int y = tileLoader.getObjects().get(i).getY();
            staticObject.setX(x);      //set the x coordinate of the object
            staticObject.setY(y);        //set the y coordinate of the object

            String points = tileLoader.getObjects().get(i).getPolyline().getPoints();  //get the vertices of the object
            String regex = "\\s";
            Pattern p = Pattern.compile(regex);
            String[] items = p.split(points);   //store the coordinate pairs of the vertices
            List<Points> coordinates = new ArrayList<Points>();
            for (String s : items) {      //extract the x, y coordinate of each vertex.
                Pattern pattern = Pattern.compile(",");
                String[] digits = pattern.split(s);
                int digit1 = Integer.parseInt(digits[0]);
                int digit2 = Integer.parseInt(digits[1]);
                Points point = new Points();
                point.setX(digit1);
                point.setY(digit2);
                coordinates.add(point);     //add the point objects to a list

            }
            staticObject.setCoordinates(coordinates);   //set the coordinates of the object
            staticObjects.add(staticObject);   //add this object to the list of objects to be passed to game engine
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
