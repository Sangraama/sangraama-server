package org.sangraama.jsonprotocols.send;

import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.jsonprotocols.SendProtocol;

import com.google.gson.Gson;


public class SyncPlayer extends SendProtocol {
    private float x, y, x_vp, y_vp, a, w, h;
    private String al; // Access Level

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
    public SyncPlayer(long userID, float x, float y, float x_vp, float y_vp, float a, float w,
            float h, VirtualPointAccessLevel vp_al) {
        super(10, userID);
        this.x = x;
        this.y = y;
        this.x_vp = x_vp;
        this.y_vp = y_vp;
        this.a = a;
        this.w = w;
        this.h = h;
        this.al = new Gson().toJson(vp_al);
    }

    /**
     * For Dummy Player
     * 
     * @param userID
     * @param x_vp
     * @param y_vp
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

    public SyncPlayer(long userID) {
        super(4, userID);
    }

}
