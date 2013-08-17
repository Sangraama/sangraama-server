package org.sangraama.controller.clientprotocol;

public class ClientEvent {
    private String type;
    private long userID;
    private float x;
    private float y;
    private float v_x;
    private float v_y;
    private float v_a;
    private float s;
    private float aoi_w;
    private float aoi_h;
    private String info;
    private byte[] signedInfo;

    public String getType() {
        return type;
    }

    public long getUserID() {
        return userID;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getV_x() {
        return v_x;
    }

    public float getV_y() {
        return v_y;
    }

    public float getV_a() {
        return v_a;
    }

    public float getS() {
        return s;
    }
    
    public float getAoi_w() {
        return aoi_w;
    }

    public float getAoi_h() {
        return aoi_h;
    }

    public String getInfo() {
        return info;
    }
    
    public byte[] getSignedInfo() {
        return signedInfo;
    }

}
