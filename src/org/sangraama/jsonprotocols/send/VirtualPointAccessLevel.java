package org.sangraama.jsonprotocols.send;

public class VirtualPointAccessLevel {
    private int xl, yl;

    /**
     * @x level : 0 - nothing; 1 - left; 2 - right
     * @y level : 0 - nothing; 1 - upper; 2 - lower
     */

    public VirtualPointAccessLevel() {
        /**
         * Initialized with SAFE settings. This will not affect on reseting virtual point. ASSUME:
         * Player can't have (-) negative coordinates
         */
        this.xl = 0;
        this.yl = 0;
    }

    public VirtualPointAccessLevel(int x_level, int y_level) {
        this.xl = x_level;
        this.yl = y_level;
    }

    public void setVirtualPointAccessLevel(char c, int direction) {
        if (c == 'x' || c == 'X') {
            this.xl = direction;
        } else if (c == 'y' || c == 'Y') {
            this.yl = direction;
        }
    }
}
