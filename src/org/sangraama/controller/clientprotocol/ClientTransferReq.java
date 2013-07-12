package org.sangraama.controller.clientprotocol;

import org.sangraama.util.SignMsg;

import com.google.gson.Gson;

public class ClientTransferReq {
    private int type = 2;
    private long userID;
    private String url = "";
    private int port;
    private String dir = "";
    private String info;
    private String signedInfo;

    public ClientTransferReq(long userID, String newServerURL, int newServerPort, String dir) {
        this.userID = userID;
        this.url = newServerURL;
        this.port = newServerPort;
        this.dir = dir;
    }

    public ClientTransferReq(long userID, float x, float y, String newHost) {
        this.userID = userID;
        ClientTransferInfo clientInfo = new ClientTransferInfo(x, y, newHost);
        Gson gson = new Gson();
        info = gson.toJson(clientInfo);
        signedInfo = SignMsg.INSTANCE.signMessage(info).toString();
    }

    private class ClientTransferInfo {
        private float positionX;
        private float positionY;
        private String url = "";

        public ClientTransferInfo(float x, float y, String newHost) {
            this.positionX = x;
            this.positionY = y;
            this.url = newHost;
        }
    }
}
