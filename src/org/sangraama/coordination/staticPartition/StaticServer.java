package org.sangraama.coordination.staticPartition;

import org.sangraama.coordination.ServerLocation;

public class StaticServer {
    private Tile[] tiles;

    public StaticServer() {
	this.tiles[0] = new Tile(0, "localhost", 7911, 0, 0);
	this.tiles[1] = new Tile(1, "localhost", 7912, 1000, 0);
    }

    public ServerLocation getServerLocation(float x, float y) {
	ServerLocation serverLoc = null;
	for (int i = 0; i < this.tiles.length; i++) {
	    if (this.tiles[i].isInTile(x, y)) {
		serverLoc = this.tiles[i].getServerLocation(i);
	    }
	}
	return serverLoc;
    }

    private class Tile {
	int tileID;
	String URL;
	int port;
	float originX;
	float originY;
	float width;
	float height;

	public Tile(int tileID, String URL, int port, float originX,
		float originY) {
	    this.URL = URL;
	    this.port = port;
	    this.originX = originX;
	    this.originY = originY;
	    this.width = 1000f;
	    this.height = 1000f;
	}

	public Tile(String URL, int port, float originX, float originY,
		float width, float height) {
	    this.URL = URL;
	    this.port = port;
	    this.originX = originX;
	    this.originY = originY;
	    this.width = width;
	    this.height = height;
	}

	public boolean isInTile(float x, float y) {

	    if (originX < x && x < originX + width && originY < y
		    && y < originY + height) {
		return true;
	    } else {
		return false;
	    }
	}

	public ServerLocation getServerLocation(int tileID) {
	    if (this.tileID == tileID) {
		return new ServerLocation(URL, port);
	    } else {
		return null;
	    }
	}
    }
}
