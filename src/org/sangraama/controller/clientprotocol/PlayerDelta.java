package org.sangraama.controller.clientprotocol;

public class PlayerDelta extends AbsDelta {
    private float health;
    private float score;

    public PlayerDelta(float dx, float dy, float da, long userID, float health, float score) {
        this.type = 1;
        this.dx = dx;
        this.dy = dy;
        this.da = da;
        this.userID = userID;
        this.score = score;
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    public float getScore() {
        return score;
    }
}
