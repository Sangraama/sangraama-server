package org.sangraama.jsonprotocols;

public abstract class SendProtocol {
    protected int type = 1;
    protected long userID;

    public SendProtocol(int type, long userID) {
        this.type = type;
        this.userID = userID;

    }

    @Override
    public String toString() {
        return "SendProtocol [type=" + type + ", userID=" + userID + "]";
    }

}
