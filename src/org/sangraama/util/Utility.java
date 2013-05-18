package org.sangraama.util;

import org.sangraama.common.Constants;

public class Utility {
	
	//Screen width and height
    private int canvasWidth = Constants.canvasWidth;
    private int canvasHeight = Constants.canvasHeight;
    
    //Convert a JBox2D x coordinate to a JavaFX pixel x coordinate
    public float toPixelPosX(float posX) {
        float x = canvasWidth*posX / 100.0f;
        return x;
    }
     
    //Convert a JavaFX pixel x coordinate to a JBox2D x coordinate
    public float toPosX(float posX) {
        float x =   (posX*100.0f*1.0f)/canvasWidth;
        return x;
    }
     
    //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
    public float toPixelPosY(float posY) {
        float y = canvasHeight - (1.0f*canvasHeight) * posY / 100.0f;
        return y;
    }
     
    //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
    public float toPosY(float posY) {
        float y = 100.0f - ((posY * 100*1.0f) /canvasHeight) ;
        return y;
    }
     
    //Convert a JBox2D width to pixel width
    public float toPixelWidth(float width) {
        return canvasWidth*width / 100.0f;
    }
     
    //Convert a JBox2D height to pixel heights
    public float toPixelHeight(float height) {
        return canvasHeight*height/100.0f;      
    } 
    
    
}
