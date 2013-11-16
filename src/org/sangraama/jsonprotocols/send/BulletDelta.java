package org.sangraama.jsonprotocols.send;

import org.sangraama.jsonprotocols.AbsDelta;

public class BulletDelta extends AbsDelta {
    private long id;

    public BulletDelta(float dx, float dy, float a, long userID, long id, int imageType) {
        super(5, userID, dx, dy, a, imageType);
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
