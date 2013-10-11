package org.sangraama.controller.clientprotocol;

import java.util.ArrayList;
import java.util.List;

public class PlayerDelta {
    private int type = 1;
    private float dx, dy, da;
    private long userID;
    private List<BulletDelta> bulletDeltaList;
    private float health;
    private float score;

    public PlayerDelta(float dx, float dy, float da, long userID, float health, float score) {
        this.dx = dx;
        this.dy = dy;
        this.da = da;
        this.userID = userID;
        this.bulletDeltaList = new ArrayList<>();
        this.health = health;
        this.score = score;
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

    public void setBulletDeltaList(List<BulletDelta> bulletDeltaList) {
        this.bulletDeltaList = bulletDeltaList;
    }

    public float getHealth() {
        return health;
    }

    public float getScore() {
        return score;
    }
}
