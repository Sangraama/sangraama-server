package org.sangraama.controller.clientprotocol;

import org.jbox2d.common.Vec2;
import org.sangraama.assets.Bullet;
import org.sangraama.util.SignMsg;

import com.google.gson.Gson;

public class BulletTransferReq extends SendProtocol {

    private String info;
    private byte[] signedInfo;

    /**
     * This method create the information needed to pass the bullet. Then the information is signed.
     * The information and signed information are kept together to send the neighbor server.
     * 
     * 
     * @param type
     * @param playerID
     * @param BulletId
     * @param x
     * @param y
     * @param velocity
     * @param originX
     * @param originY
     * @param screenHeight
     * @param screenWidth
     * @param newHost
     */
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

    /**
     * This method is used to extracted the information of the bullet which is passed. This returns the bullet to generated it.  
     * 
     * @param info
     *          GSON string of the bullet information 
     * @return bullet
     *          Bullet object after extracting the information
     */
    public Bullet reCreateBullet(String info) {
        Bullet bullet = null;
        Gson gson = new Gson();
        BulletTransferInfo bulletInfo = gson.fromJson(info, BulletTransferInfo.class);
        bullet = new Bullet(bulletInfo.id, bulletInfo.playerID, bulletInfo.positionX,
                bulletInfo.positionY, bulletInfo.velocity, bulletInfo.originX, bulletInfo.originY,
                bulletInfo.screenHeight, bulletInfo.screenWidth);
        return bullet;
    }

    /**
     * 
     * This class has the information about the bullet which passed between the servers. 'url' is
     * the URL of the server which is bullet going to be transferred. originX and orginY are denotes
     * the position where bullet was fired.
     * 
     */
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
