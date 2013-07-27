package org.sangraama.controller.clientprotocol;

import org.sangraama.util.SignMsg;

import com.google.gson.Gson;

public class ClientTransferReq {
    /**
     * type details: 2 =Client pass to another server and sent events (change primary server) 3=
     * Client connect to another server only for getting updates of AOI
     */
    private int type;
    private long userID;
    private String info;
    private String signedInfo;

    public ClientTransferReq(int type, long userID, float x, float y, String newHost) {
        this.type = type;
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
