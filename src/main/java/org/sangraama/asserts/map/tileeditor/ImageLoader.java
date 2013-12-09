package org.sangraama.asserts.map.tileeditor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageLoader {

    public void loadImage(List<TileSet> tilesetList) {

        for (int i = 0; i < tilesetList.size(); i++) {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File("res/" + tilesetList.get(i).getImage().getSource()));
                //  System.out.println(img.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
