package org.sangraama.jsonprotocols.send;

public class VirtualPointAccessLevel {
    private int x, y;
    private boolean left, up;

    public VirtualPointAccessLevel(char c, int level, boolean direction) {
        if (c == 'x' || c == 'X') {
            this.x = level;
            this.left = direction;
        } else if (c == 'y' || c == 'Y') {
            this.y = level;
            this.up = direction;
        }
    }

    public VirtualPointAccessLevel(int x, boolean left, int y, boolean up) {
        this.x = x;
        this.left = left;
        this.y = y;
        this.up = up;
    }
}
