package org.sangraama.assets;

import org.sangraama.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SangraamaMap {
    INSTANCE;
    private static final Logger log = LoggerFactory.getLogger(SangraamaMap.class);
    private Float originX;
    private Float originY;
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

    public void setMap(String originX, String originY, float width, float height, String host,
                       float maxWidth, float maxHeight) {
        // Next milestone: map will load via DB
        this.mapWidth = width / Constants.scale;
        this.mapHeight = height / Constants.scale;
        this.host = host;
        this.maxWidth = maxWidth / Constants.scale;
        this.maxHeight = maxHeight / Constants.scale;
        if (originX != null && !originX.equals("")) {
            this.originX = Float.valueOf(originX) / Constants.scale;
            this.edgeX = this.originX + this.mapWidth;

        } else {
            this.originX = null;
            this.edgeX = this.mapWidth;
        }
        if (originY != null && !originY.equals("")) {
            this.originY = Float.valueOf(originY) / Constants.scale;
            this.edgeY =  this.originY+ this.mapHeight;

        } else {
            this.edgeY = this.mapHeight;
            this.originY = null;
        }
        log.info("created with x:" + this.originX + " y:" + this.originY + "  x':" + edgeX + " y':"
                + edgeY);
    }

    public void setSubTileProperties(float width, float height) {
        this.subTileWidth = width / Constants.scale;
        this.subTileHeight = height / Constants.scale;
        log.info("subtile w:" + subTileWidth + " h:" + subTileHeight);
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public Float getOriginX() {
        return originX;
    }

    public Float getOriginY() {
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
