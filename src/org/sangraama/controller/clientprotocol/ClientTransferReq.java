package org.sangraama.controller.clientprotocol;

public class ClientTransferReq {
    private int type = 2;
    private long userID;
    private String url = "";
    private int port;
    private String dir = "";

    public ClientTransferReq(long userID, String newServerURL, int newServerPort, String dir) {
        this.userID = userID;
        this.url = newServerURL;
        this.port = newServerPort;
        this.dir = dir;
    }
}
