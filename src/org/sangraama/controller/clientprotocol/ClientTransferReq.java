package org.sangraama.controller.clientprotocol;

public class ClientTransferReq {
    private int type = 2;
    private long userID = 0;
    private String url = "";
    private int port = 0;
    private String dir = "";

    public ClientTransferReq(long userID, String newServerURL, int newServerPort, String dir) {
        this.userID = userID;
        this.url = newServerURL;
        this.port = newServerPort;
        this.dir = dir;
    }
}
