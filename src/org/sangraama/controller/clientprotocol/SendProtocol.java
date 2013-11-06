package org.sangraama.controller.clientprotocol;

public abstract class SendProtocol {
    int type = 1;

    long userID;

    public SendProtocol(int type, long userID) {
        this.type = type;
        this.userID = userID;

    }

    @Override
    public String toString() {
        return "SendProtocol [type=" + type + ", userID=" + userID + "]";
    }

}
