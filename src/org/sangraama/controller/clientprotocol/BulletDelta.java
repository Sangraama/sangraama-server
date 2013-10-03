package org.sangraama.controller.clientprotocol;

public class BulletDelta {
    private int type = 1;
    private float dx, dy, a;
    private long playerID;
    private long id;

    public BulletDelta(float dx, float dy, float a, long playerID, long id, int type) {
        this.dx = dx;
        this.dy = dy;
        this.a = a;
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

    public float getA() {
        return a;
    }

    public long getPlayerID() {
        return playerID;
    }

}
