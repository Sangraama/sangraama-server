package org.sangraama.controller.clientprotocol;

public class PlayerDelta {
    private float dx = 0, dy = 0;
    private int userID = 0;

    public PlayerDelta(float dx, float dy, int userID) {
	this.dx = dx;
	this.dy = dy;
	this.userID = userID;
    }
    
    public int getUserID(){
	return this.userID;
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
