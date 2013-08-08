package org.sangraama.controller.clientprotocol;

import java.util.ArrayList;
import java.util.List;

public class PlayerDelta {
    private int type = 1;
    private float dx, dy, da;
    private long userID;
    private List<BulletDelta> bulletDeltaList;

    public PlayerDelta(float dx, float dy, float da, long userID) {
        this.dx = dx;
        this.dy = dy;
        this.da = da;
        this.userID = userID;
        this.bulletDeltaList = new ArrayList<BulletDelta>();
    }

    public long getUserID() {
        return this.userID;
    }

    public float getDa() {
        return da;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public List<BulletDelta> getBulletDeltaList() {
        return bulletDeltaList;
    }

    
    
}
