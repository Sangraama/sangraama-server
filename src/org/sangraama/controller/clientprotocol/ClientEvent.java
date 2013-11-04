package org.sangraama.controller.clientprotocol;

import java.util.Arrays;

public class ClientEvent {
    private int type;
    private long userID;
    private float x; // Player current x location on the map
    private float y; // Player current y location on the map
    private float x_vp; // Virtual point's x coordination
    private float y_vp; // Virtual point's y coordination
    private float w; // Area of Interest width
    private float h; // Area of Interest height
    private float v_x; // Player's velocity on x direction
    private float v_y; // Player's velocity on y direction
    private float a; // actual angle
    private float da; // delta angle
    private float s;
    private String info;
    private byte[] signedInfo;
    private int st;// player image type
    private int bt;// player bullet image type

    public int getType() {
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

    public float getX_vp() {
        return x_vp;
    }

    public float getY_vp() {
        return y_vp;
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
     * 
     * @return AOI width
     */
    public float getW() {
        return w;
    }

    /**
     * Get Area of interest height
     * 
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

    public int getSt() {
        return st;
    }

    public int getBt() {
        return bt;
    }

    @Override
    public String toString() {
        return "ClientEvent [type=" + type + ", userID=" + userID + ", x=" + x + ", y=" + y
                + ", x_vp=" + x_vp + ", y_vp=" + y_vp + ", w=" + w + ", h=" + h + ", v_x=" + v_x
                + ", v_y=" + v_y + ", a=" + a + ", da=" + da + ", s=" + s + ", info=" + info
                + ", signedInfo=" + Arrays.toString(signedInfo) + ", st=" + st + ", bt=" + bt + "]";
    }

    // Update as necessary before using
    

}
