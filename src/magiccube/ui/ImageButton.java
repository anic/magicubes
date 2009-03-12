/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Administrator
 */
public class ImageButton extends AbstractButton {

    protected Image image;

    public ImageButton(int x, int y, Image image, IEventListener callback) {
        super(x, y, image.getWidth(), image.getHeight(), callback);
        this.image = image;
    }

    public void draw(Graphics g) {
        g.drawImage(this.image, x, y, 0);
    }
}
