package org.sangraama.controller.clientprotocol;

public class SyncPlayer extends SendProtocol{
    private float x, y, v_x, v_y, a, w, h;

    /**
     * For player
     * @param userID
     * @param x
     * @param y
     * @param v_x virtual x location
     * @param v_y virtual y location
     * @param a
     * @param w
     * @param h
     */
    public SyncPlayer(long userID, float x, float y, float v_x, float v_y, float a, float w, float h) {
        super(10 , userID);
        this.x = x;
        this.y = y;
        this.v_x = v_x;
        this.v_y = v_y;
        this.a = a;
        this.w = w;
        this.h = h;
    }
    
    /**
     * For Dummy Player
     * @param userID
     * @param v_x
     * @param v_y
     * @param a
     * @param w
     * @param h
     */
    public SyncPlayer(long userID, float v_x, float v_y, float a, float w, float h) {
        super(10 , userID);
        this.v_x = v_x;
        this.v_y = v_y;
        this.a = a;
        this.w = w;
        this.h = h;
    }
}
