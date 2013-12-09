package org.sangraama.util;

import org.sangraama.assets.Wall;
import org.sangraama.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        worldWidth = Float.parseFloat(prop.getProperty("maxlength")) / Constants.scale;
        worldHeight = Float.parseFloat(prop.getProperty("maxheight")) / Constants.scale;
        mapOriX = Float.parseFloat(prop.getProperty("maporiginx")) / Constants.scale;
        mapOriY = Float.parseFloat(prop.getProperty("maporiginy")) / Constants.scale;
        mapHeight = Float.parseFloat(prop.getProperty("mapwidth")) / Constants.scale;
        mapWidth = Float.parseFloat(prop.getProperty("mapheight")) / Constants.scale;
    }

    private void generateWalls() {
        if (mapOriX == 0 || mapOriY == 0) {
            if (mapOriX == 0 && mapOriY == 0) {
                wallList.add(new Wall(mapOriX, mapOriY, mapWidth, 1));
                wallList.add(new Wall(mapOriX, mapOriY, 1, mapHeight));
            } else if (mapOriX == 0) {
                wallList.add(new Wall(0, mapOriY, 1, mapHeight));
            } else if (mapOriY == 0) {
                wallList.add(new Wall(mapOriX, 0, mapWidth, 1));
            }
        }

        if (mapOriX == (worldWidth - mapWidth)) {
            wallList.add(new Wall(mapOriX + mapWidth, mapOriY, 1, mapHeight));
        }
        if (mapOriY == (worldHeight - mapHeight)) {
            wallList.add(new Wall(mapOriX, mapOriY + mapHeight, mapWidth, 1));
        }
    }

}
