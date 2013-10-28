package org.sangraama.assets;

import org.sangraama.common.Constants;

public enum SangraamaMap {
    INSTANCE;
    private String TAG = "SangraamaMap : ";
    private float originX = 0.0f;
    private float originY = 0.0f;
    private float edgeX = 0.0f; // Store value of originX + mapWidth
    private float edgeY = 0.0f; // Store value of originY + mapHeight
    private float mapWidth;
    private float mapHeight;
    private float subTileWidth;
    private float subTileHeight;
    private String host = "";

    /* Total map */
    private float maxWidth, maxHeight;

    private SangraamaMap() {

    }

    public void setMap(float originX, float originY, float width, float height, String host,
            float maxWidth, float maxHeight) {
        // Next milestone: map will load via DB
        this.originX = originX / Constants.scale;
        this.originY = originY / Constants.scale;
        this.mapWidth = width / Constants.scale;
        this.mapHeight = height / Constants.scale;
        this.host = host;
        this.maxWidth = maxWidth / Constants.scale;
        this.maxHeight = maxHeight / Constants.scale;

        this.edgeX = this.originX + this.mapWidth;
        this.edgeY = this.originY + this.mapHeight;
        System.out.println(TAG + " created with x:" + this.originX + " y:" + this.originY + "  x':" + edgeX
                + " y':" + edgeY);
    }

    public void setSubTileProperties(float width, float height) {
        this.subTileWidth = width / Constants.scale;
        this.subTileHeight = height / Constants.scale;
        System.out.println(TAG + " subtile w:" + subTileWidth + " h:" + subTileHeight);
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

    /**
     * Get value of originX + mapWidth
     * 
     * @return float (originX + mapWidth)
     */
    public float getEdgeX() {
        return this.edgeX;
    }

    /**
     * Get value of originY + mapWidth
     * 
     * @return float (originY + mapHeight)
     */
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

    public float getMaxWidth() {
        return maxWidth;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

}
