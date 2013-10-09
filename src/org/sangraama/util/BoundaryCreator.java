package org.sangraama.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.sangraama.assets.Wall;

public class BoundaryCreator {

    private List<Wall> wallList = new ArrayList<Wall>();
    private float worldWidth;
    private float worldHeight;
    private float mapOriX;
    private float mapOriY;
    private float mapHeight;
    private float mapWidth;

    public List<Wall> calculateWallBoundary() {
        readMapAndWorldDim();
        generateWalls();
        return wallList;
    }

    private void readMapAndWorldDim() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        worldWidth = Float.parseFloat(prop.getProperty("maxlength"));
        worldHeight = Float.parseFloat(prop.getProperty("maxheight"));
        mapOriX = Float.parseFloat(prop.getProperty("maporiginx"));
        mapOriY = Float.parseFloat(prop.getProperty("maporiginy"));
        mapHeight = Float.parseFloat(prop.getProperty("mapwidth"));
        mapWidth = Float.parseFloat(prop.getProperty("mapheight"));
    }

    private void generateWalls() {
        if (mapOriX == 0) {
            wallList.add(new Wall(mapOriX,mapOriY,mapWidth,1));
        }
        if (mapOriY == 0) {
            wallList.add(new Wall(mapOriX,mapOriY,1,mapHeight));
        }
        if(mapOriX == (worldWidth-mapWidth)){
            wallList.add(new Wall(mapOriX+mapWidth,mapOriY,1,mapHeight));
        }
        if(mapOriY == (worldHeight-mapHeight)){
            wallList.add(new Wall(mapOriX,mapOriY+mapHeight,mapWidth,1));
        }
    }

}
