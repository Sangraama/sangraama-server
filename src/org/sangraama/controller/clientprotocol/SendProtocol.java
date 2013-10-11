package org.sangraama.controller.clientprotocol;

public abstract class SendProtocol {
    private int type;
    private long userID;

    public SendProtocol(int type, long userID) {
        this.type = type;
        this.userID = userID;
    }

    public int getType() {
        return this.type;
    }

    public long getUserID() {
        return this.userID;
    }
}
