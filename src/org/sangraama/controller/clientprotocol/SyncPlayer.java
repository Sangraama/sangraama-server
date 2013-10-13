package org.sangraama.controller.clientprotocol;

public class SyncPlayer extends SendProtocol {
    private float x, y, x_v, y_v, a, w, h;

    /**
     * For player
     * 
     * @param userID
     * @param x
     * @param y
     * @param v_x
     *            virtual x location
     * @param v_y
     *            virtual y location
     * @param a
     * @param w
     * @param h
     */
    public SyncPlayer(long userID, float x, float y, float v_x, float v_y, float a, float w, float h) {
        super(10, userID);
        this.x = x;
        this.y = y;
        this.x_v = v_x;
        this.y_v = v_y;
        this.a = a;
        this.w = w;
        this.h = h;
    }

    /**
     * For Dummy Player
     * 
     * @param userID
     * @param v_x
     * @param v_y
     * @param a
     * @param w
     * @param h
     */
    public SyncPlayer(long userID, float v_x, float v_y, float w, float h) {
        super(11, userID);
        this.x_v = v_x;
        this.y_v = v_y;
        this.w = w;
        this.h = h;
    }
}
