package org.sangraama.controller.clientprotocol;

public class ClientEvent {
    private String type;
    private long userID;
    private float v_x;
    private float v_y;
    
    public String getType() {
        return type;
    }
    public long getUserID() {
        return userID;
    }
    public float getV_x() {
        return v_x;
    }
    public float getV_y() {
        return v_y;
    }
    
}
