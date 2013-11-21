package org.sangraama.jsonprotocols.send;

public class SangraamaTile {
        private float x,y,w,h; // origin X, origin Y, width and height of the sub-tile
        
        public SangraamaTile(float x,float y,float width,float heigth){
            this.x = x;
            this.y = y;
            this.w = width;
            this.h = heigth;
        }
}
