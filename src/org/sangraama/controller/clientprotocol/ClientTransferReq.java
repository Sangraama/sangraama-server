package org.sangraama.controller.clientprotocol;

import org.sangraama.util.SignMsg;

import com.google.gson.Gson;

public class ClientTransferReq extends SendProtocol{
    /**
     * type details: 30 = Client pass to another server and sent events (change primary server) 31 =
     * Client connect to another server only for getting updates of AOI
     */
    private String info;
    private byte[] signedInfo;

    public ClientTransferReq(int type, long userID, float x, float y, float health, float score, String newHost) {
        super(type, userID);
        this.type = type;
        this.userID = userID;
        ClientTransferInfo clientInfo = new ClientTransferInfo(x, y, health, score, newHost);
        Gson gson = new Gson();
        info = gson.toJson(clientInfo);
        signedInfo = SignMsg.INSTANCE.signMessage(info);
    }

    private class ClientTransferInfo {
        private float positionX;
        private float positionY;
        private float health;
        private float score;
        private String url = "";

        public ClientTransferInfo(float x, float y, float health, float score, String newHost) {
            this.positionX = x;
            this.positionY = y;
            this.health = health;
            this.score = score;
            this.url = newHost;
        }
    }
}
