package org.sangraama.controller.clientprotocol;

import org.jbox2d.common.Vec2;
import org.sangraama.assets.Bullet;
import org.sangraama.util.SignMsg;

import com.google.gson.Gson;

public class BulletTransferReq extends SendProtocol {

    private String info;
    private byte[] signedInfo;

    public BulletTransferReq(int type, long playerID, long BulletId, float x, float y,
            Vec2 velocity, float originX, float originY, float screenHeight, float screenWidth,
            String newHost) {
        super(type, playerID);
        BulletTransferInfo bulletInfo = new BulletTransferInfo(BulletId, playerID, x, y, velocity,
                originX, originY, screenHeight, screenWidth, newHost);
        Gson gson = new Gson();
        info = gson.toJson(bulletInfo);
        signedInfo = SignMsg.INSTANCE.signMessage(info);
    }

    public Bullet reCreateBullet(String info) {
        Bullet bullet = null;
        Gson gson = new Gson();
        BulletTransferInfo bulletInfo = gson.fromJson(info, BulletTransferInfo.class);
        bullet = new Bullet(bulletInfo.id, bulletInfo.playerID, bulletInfo.positionX,
                bulletInfo.positionY, bulletInfo.velocity, bulletInfo.originX, bulletInfo.originY,
                bulletInfo.screenHeight, bulletInfo.screenWidth);
        return bullet;
    }

    private class BulletTransferInfo {
        private long id;
        private long playerID;
        private float positionX;
        private float positionY;
        private Vec2 velocity;
        private float screenHeight;
        private float screenWidth;
        private float originX;
        private float originY;
        private String url = "";

        public BulletTransferInfo(long id, long playerID, float x, float y, Vec2 velocity,
                float originX, float originY, float screenHeight, float screenWidth, String newHost) {
            this.id = id;
            this.playerID = playerID;
            this.positionX = x;
            this.positionY = y;
            this.velocity = velocity;
            this.originX = originX;
            this.originY = originY;
            this.screenHeight = screenHeight;
            this.screenWidth = screenWidth;
            this.url = newHost;
        }
    }
}
