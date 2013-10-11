package org.sangraama.assets;

public enum SangraamaMap {
    INSTANCE;

    private float originX = 0.0f;
    private float originY = 0.0f;
    private float edgeX = 0.0f; // Store value of originX + mapWidth
    private float edgeY = 0.0f; // Store value of originY + mapHeight
    private float mapWidth;
    private float mapHeight;
    private float subTileWidth;
    private float subTileHeight;
    private String host = "";

    private SangraamaMap() {

    }

    public void setMap(float originX, float originY, float width, float height, String host) {
        // Next milestone: map will load via DB
        this.originX = originX;
        this.originY = originY;
        this.mapWidth = width;
        this.mapHeight = height;
        this.host = host;
        this.edgeX = originX + width;
        this.edgeY = originY + height;
    }

    public void setSubTileProperties(float width, float height) {
        this.subTileWidth = width;
        this.subTileHeight = height;
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public float getEdgeX() {
        return this.edgeX;
    }

    public float getEdgeY() {
        return this.edgeY;
    }

    public float getSubTileWidth() {
        return subTileWidth;
    }

    public float getSubTileHeight() {
        return subTileHeight;
    }

    public String getHost() {
        return host;
    }

}
