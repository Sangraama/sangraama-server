package org.sangraama.controller.clientprotocol;

public class PlayerDelta {
    private int type=1;
    private float dx, dy,da;
    private long userID;

    public PlayerDelta(float dx, float dy,float da, long userID) {
	this.dx = dx;
	this.dy = dy;
	this.da = da;
	this.userID = userID;
    }
    
    public long getUserID(){
	return this.userID;
    }

    public float getDa() {
		return da;
	}

	public void setDa(float da) {
		this.da = da;
	}

	public float getDx() {
	return dx;
    }

    public void setDx(int dx) {
	this.dx = dx;
    }

    public float getDy() {
	return dy;
    }

    public void setDy(int dy) {
	this.dy = dy;
    }
}
