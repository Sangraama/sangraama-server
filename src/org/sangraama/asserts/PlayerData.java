package org.sangraama.asserts;


//only test
public class PlayerData {
    private int x = 0, y = 0;
    private float v_x = 0f, v_y = 0f;

    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }

    public PlayerData(int x, int y) {
	this.x = x;
	this.y = y;
    }
}
