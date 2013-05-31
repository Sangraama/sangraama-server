package org.sangraama.asserts;

public enum SangraamaMap {
    INSTANCE;

    private float originX = 0f;
    private float originY = 0f;
    private float mapWidth;
    private float mapHeight;

    private SangraamaMap() {

    }

    public void setMap(float originX, float originY, float width, float height) {
        // Next milestone: map will load via DB
        this.originX = originX;
        this.originY = originY;
        this.mapWidth = width;
        this.mapHeight = height;
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

}
