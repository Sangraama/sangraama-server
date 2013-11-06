package org.sangraama.controller.clientprotocol;

public class PlayerDelta extends AbsDelta {
    private float health;
    private float score;

    public PlayerDelta(float dx, float dy, float da, long userID, float health, float score,
            int imageType) {
        super(1, userID, dx, dy, da, imageType);
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
