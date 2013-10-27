package org.sangraama.controller.clientprotocol;

import org.sangraama.util.SignMsg;

import com.google.gson.Gson;

public class BulletTransferReq extends SendProtocol{

    private String info;
    private byte[] signedInfo;
    
    public BulletTransferReq(int type, long playerID,long id,float x,float y, String newHost) {
        super(type, playerID);
        BulletTransferInfo bulletInfo = new BulletTransferInfo(id, playerID, x, y, newHost);
        Gson gson = new Gson();
        info = gson.toJson(bulletInfo);
        signedInfo = SignMsg.INSTANCE.signMessage(info);
    }
    
    private class BulletTransferInfo {
        private long id;
        private long playerID;
        private float positionX;
        private float positionY;
        private String url = "";

        public BulletTransferInfo(long id, long playerID, float x, float y, String newHost) {
            this.id = id;
            this.playerID = playerID;
            this.positionX = x;
            this.positionY = y;
            this.url = newHost;
        }
    }
}
