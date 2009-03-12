/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.reader;

import magiccube.game.reader.ISegment;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import magiccube.util.ImageGetter;

/**
 *
 * @author Administrator
 */
public class ImageSegment implements ISegment {

    private Image image;
    private int width,  height;


    public ImageSegment(String name) {
        image = ImageGetter.Instance.getImage(name);
        if (image!=null)
        {
            width = image.getWidth();
            height = image.getHeight();
        }
    }

    public void draw(Graphics g, int x, int y, int width, int height) {

        if (image != null) {
            g.drawImage(image, x, y, 0);
        }
    }

    public int getWdith() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
