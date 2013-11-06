package org.sangraama.controller.clientprotocol;

public abstract class AbsDelta extends SendProtocol {
    float dx, dy, da;
    int it = 1;// image type

    public AbsDelta(int type, long userID, float dx, float dy, float da, int imageType) {
        super(type, userID);
        this.dx = dx;
        this.dy = dy;
        this.da = da;
        this.it = imageType;

    }

    public float getDx() {
        return this.dx;
    }

    public float getDy() {
        return this.dy;
    }
}
