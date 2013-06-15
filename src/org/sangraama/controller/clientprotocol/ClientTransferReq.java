package org.sangraama.controller.clientprotocol;

public class ClientTransferReq {

    private long userID = 0;
    private int type = 2;
    private String newServerURL = "";

    public ClientTransferReq(long userID, String newServerURL, int newServerPort) {
        this.userID = userID;
        this.newServerURL = newServerURL;
        if (newServerPort == 8080) {
            this.newServerURL = "ws://localhost:8080/sangraama-server/sangraama/player";
        } else {
            this.newServerURL = "ws://localhost:8081/sangraama-server-clone/sangraama/player";
        }
    }
}
