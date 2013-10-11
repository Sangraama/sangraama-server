package org.sangraama.controller.clientprotocol;

import java.util.ArrayList;
import java.util.List;

public class PlayerDelta extends SendProtocol{
    private float dx, dy, da;
    private List<BulletDelta> bulletDeltaList;
    private float health;
    private float score;

    public PlayerDelta(long userID, float dx, float dy, float da, float health, float score) {
        super(1, userID);
        this.dx = dx;
        this.dy = dy;
        this.da = da;
        this.bulletDeltaList = new ArrayList<>();
        this.health = health;
        this.score = score;
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

    public float getHealth(){
        return health;
    }
    
    public float getScore(){
        return score;
    }
}
