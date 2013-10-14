package org.sangraama.controller.clientprotocol;

public class DefeatMsg {

    private int type;
    private long userID;
    private float dx;
    private float dy;
    private float score;
    
    
    public DefeatMsg(int type, long userID, float rebornX, float rebornY, float score){
        this.type = type;
        this.userID = userID;
        this.dx = rebornX;
        this.dy = rebornY;
        this.score = score;
    }
}
