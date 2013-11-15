package org.sangraama.controller.clientprotocol;

public class DefeatMsg extends AbsDelta {

    private float score;

    public DefeatMsg(long userID,float dx, float dy, float da, float score, int imageType) {
        /**
         * If imageType is not using in client side, why is it using here ? #gihan
         */
        super(6, userID, dx, dy, da, imageType);
        this.score = score;
    }

    public float getScore() {
        return score;
    }
}
