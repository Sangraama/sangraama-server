package org.sangraama.controller.clientprotocol;

public class DefeatMsg extends AbsDelta {

    private float score;

    public DefeatMsg(float dx, float dy, float da, long userID, float score, int type, int imageType) {
        super(type, userID, dx, dy, da, imageType);
        this.score = score;
    }

    public float getScore() {
        return score;
    }
}
