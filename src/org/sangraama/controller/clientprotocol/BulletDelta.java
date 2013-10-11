package org.sangraama.controller.clientprotocol;

public class BulletDelta {
    private int type = 1;
    private float dx, dy, da;
    private long playerID;
    private long id;

    public BulletDelta(float dx, float dy, float a, long playerID, long id, int type) {
        this.dx = dx;
        this.dy = dy;
        this.da = a;
        this.playerID = playerID;
        this.id = id;
        this.type = type;

    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public float getDa() {
        return da;
    }

    public long getPlayerID() {
        return playerID;
    }

}
