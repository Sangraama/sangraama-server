package org.sangraama.asserts.map;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private int mapHeight;
    private int mapWidth;

    private List<StaticObject> staticObjects = new ArrayList<StaticObject>();

    public List<StaticObject> getStaticObjects() {
        return staticObjects;
    }

    public void setStaticObjects(List<StaticObject> staticObjects) {
        this.staticObjects = staticObjects;
    }

    private static GameMap gameMap = new GameMap();

    private GameMap() {

    }

    public static GameMap getMap() {
        return gameMap;
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

    public void generate() {
        MapAPI m = new MapAPI();
        m.getMapDetails();
        this.mapHeight = m.getMapHeight();
        this.mapWidth = m.getMapWidth();
        this.staticObjects = m.getStaticObjects();

    }

}
