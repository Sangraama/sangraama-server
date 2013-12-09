package org.sangraama.asserts.map.tileeditor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TileLoader {
    private List<Tile> backgroundTiles = new ArrayList<Tile>();
    private List<Tile> foregroundTiles = new ArrayList<Tile>();
    private List<Tile> topTiles = new ArrayList<Tile>();
    JAXBContext jc;
    private int mapHeight;

    private int mapWidth;

    private List<Layer> layers = new ArrayList<Layer>();
    private List<Tile> backgroundLayerTiles = new ArrayList<Tile>();
    private List<Tile> foregroundLayerTiles = new ArrayList<Tile>();
    private List<Tile> topLayerTiles = new ArrayList<Tile>();
    private List<Object> objects = new ArrayList<Object>();
    private List<TiledPolygon> polygons = new ArrayList<TiledPolygon>();
    private List<TiledRectangle> rectangles = new ArrayList<TiledRectangle>();

    public List<TiledPolygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<TiledPolygon> polygons) {
        this.polygons = polygons;
    }

    public List<TiledRectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<TiledRectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public void ParseMapFile() {
        try {
            jc = JAXBContext.newInstance(Map.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            InputStream inputstream = this.getClass().getResourceAsStream("/asset/worldMap.tmx");

            Map obj = (Map) unmarshaller.unmarshal(inputstream);

            mapHeight = obj.getMapheight() * obj.getTileheight();
            mapWidth = obj.getMapwidth() * obj.getTilewidth();
            layers = obj.getLayerList();
            // loadTiles(layers);
            objects = obj.getObjectGroup().getObjectList();
            // arrangeShapes(objects);

        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void arrangeShapes(List<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getPolygon() != null) {
                polygons.add(objects.get(i).getPolygon());

            } else {
                rectangles.add((TiledRectangle) objects.get(i));
            }
        }
    }

    /*
     * public void loadTiles(List<Layer> layers){
     * 
     * for(int i=0;i<layers.size();i++){ switch(layers.get(i).getName()){ case "Background":
     * backgroundTiles=layers.get(i).getData().getTiles(); for(int
     * k=0;k<backgroundTiles.size();k++){ if(backgroundTiles.get(k).getGid()>0){
     * backgroundLayerTiles.add(backgroundTiles.get(k)); } } break; case "Foreground":
     * foregroundTiles=layers.get(i).getData().getTiles(); for(int
     * k=0;k<foregroundTiles.size();k++){ if(foregroundTiles.get(k).getGid()>0){
     * foregroundLayerTiles.add(foregroundTiles.get(k)); } } break; case "Top":
     * topTiles=layers.get(i).getData().getTiles(); for(int k=0;k<topTiles.size();k++){
     * if(topTiles.get(k).getGid()>0){ topLayerTiles.add(topTiles.get(k)); } } break; }
     * 
     * }
     * 
     * 
     * 
     * }
     */

    public List<Tile> getBackgroundLayerTiles() {
        return backgroundLayerTiles;
    }

    public void setBackgroundLayerTiles(List<Tile> backgroundLayerTiles) {
        this.backgroundLayerTiles = backgroundLayerTiles;
    }

    public List<Tile> getForegroundLayerTiles() {
        return foregroundLayerTiles;
    }

    public void setForegroundLayerTiles(List<Tile> foregroundLayerTiles) {
        this.foregroundLayerTiles = foregroundLayerTiles;
    }

    public List<Tile> getTopLayerTiles() {
        return topLayerTiles;
    }

    public void setTopLayerTiles(List<Tile> topLayerTiles) {
        this.topLayerTiles = topLayerTiles;
    }

}
