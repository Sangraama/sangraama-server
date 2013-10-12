package org.sangraama.controller.clientprotocol;

public class BulletDelta extends AbsDelta {
    private long id;

    public BulletDelta(float dx, float dy, float a, long userID, long id) {
        this.dx = dx;
        this.dy = dy;
        this.da = a;
        this.userID = userID;
        this.id = id;
        this.type = 5;

    }

    public long getId() {
        return id;
    }

}
