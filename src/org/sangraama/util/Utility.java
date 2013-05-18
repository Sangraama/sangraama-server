package org.sangraama.util;

import org.sangraama.common.Constants;


public class Utility {
			
    //Convert game world x coordinate to a client pixel x coordinate
    public static float toPixelPosX(float posX,float gameWorldWidth) {
        float x = Constants.canvasWidth*posX / gameWorldWidth;
        return x;
    }
     
    //Convert client pixel x coordinate to a game world x coordinate
    public static float toPosX(float posX,float gameWorldWidth) {
        float x =   (posX*gameWorldWidth*1.0f)/Constants.canvasWidth;
        return x;
    }
     
    //Convert game world y coordinate to a client pixel y coordinate
    public static float toPixelPosY(float posY,float gameWorldHeight) {
        float y = Constants.canvasHeight - (1.0f*Constants.canvasHeight) * posY / gameWorldHeight;
        return y;
    }
     
    //Convert client pixel y coordinate to a game world y coordinate
    public static float toPosY(float posY,float gameWorldHeight) {
        float y = gameWorldHeight - ((posY * gameWorldHeight*1.0f) /Constants.canvasHeight) ;
        return y;
    }
     
    //Convert game world width to pixel width
    public float toPixelWidth(float width,float gameWorldWidth) {
        return Constants.canvasWidth*width / gameWorldWidth;
    }
     
    //Convert game world height to pixel height
    public float toPixelHeight(float height,float gameWorldHeight) {
        return Constants.canvasHeight*height/gameWorldHeight;      
    } 
    
    
}
