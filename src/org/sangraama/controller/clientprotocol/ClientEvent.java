package org.sangraama.controller.clientprotocol;

import java.util.Arrays;

public class ClientEvent {
    private String type;
    private long userID;
    private float x;
    private float y;
    private float w; // Area of Interest width
    private float h; // Area of Interest height
    private float v_x;
    private float v_y;
    private float a; // actual angle
    private float da; // delta angle
    private float s;
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

    public float getS() {
        return s;
    }

    public String getInfo() {
        return info;
    }

    public byte[] getSignedInfo() {
        return signedInfo;
    }

    /**
     * Get Area of interest width
     * @return AOI width
     */
    public float getW() {
        return w;
    }

    /**
     * Get Area of interest height
     * @return AOI height
     */
    public float getH() {
        return h;
    }

    public float getA() {
        return a;
    }

    public float getDa() {
        return da;
    }

    @Override
    public String toString() {
        return "ClientEvent [type=" + type + ", userID=" + userID + ", x=" + x + ", y=" + y
                + ", w=" + w + ", h=" + h + ", v_x=" + v_x + ", v_y=" + v_y + ", a=" + a + ", da="
                + da + ", s=" + s + ", info=" + info + ", signedInfo="
                + Arrays.toString(signedInfo) + "]";
    }

}
