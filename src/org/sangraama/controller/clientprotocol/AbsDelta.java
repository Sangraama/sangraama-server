package org.sangraama.controller.clientprotocol;

public abstract class AbsDelta extends SendProtocol {
    float dx, dy, da;
    
    public AbsDelta(int type, long userID, float dx, float dy, float da){
        super(type, userID);
        this.dx = dx;
        this.dy = dy;
        this.da = da;
    }
    
    public float getDx(){
        return this.dx;
    }
    
    public float getDy(){
        return this.dy;
    }
}
