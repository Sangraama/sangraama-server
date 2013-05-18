package org.sangraama.controller.clientprotocol;

public class PlayerDelta {
    private float dx = 0, dy = 0;
    
    public PlayerDelta(float dx, float dy){
	this.dx = dx;
	this.dy = dy;
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
