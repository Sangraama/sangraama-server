package org.sangraama.controller.clientprotocol;

public class SyncPlayer extends SendProtocol {
    private float x, y, x_vp, y_vp, a, w, h;

    /**
     * For player
     * 
     * @param userID
     * @param x
     * @param y
     * @param x_vp
     *            virtual x location
     * @param y_vp
     *            virtual y location
     * @param a
     * @param w
     * @param h
     */
    public SyncPlayer(long userID, float x, float y, float x_vp, float y_vp, float a, float w, float h) {
        super(10, userID);
        this.x = x;
        this.y = y;
        this.x_vp = x_vp;
        this.y_vp = y_vp;
        this.a = a;
        this.w = w;
        this.h = h;
    }

    /**
     * For Dummy Player
     * 
     * @param userID
     * @param x_vp
     * @param y_vp
     * @param a
     * @param w
     * @param h
     */
    public SyncPlayer(long userID, float x_vp, float y_vp, float w, float h) {
        super(11, userID);
        this.x_vp = x_vp;
        this.y_vp = y_vp;
        this.w = w;
        this.h = h;
    }
}
