package org.sangraama.controller.clientprotocol;

public class AbsDelta {
    int type = 1;
    float dx, dy, da;
    long userID;

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

    public long getUserId() {
        return userID;
    }

}
