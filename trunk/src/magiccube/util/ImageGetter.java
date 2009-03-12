/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.util;

import java.util.Hashtable;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Administrator
 */
public class ImageGetter {

    protected Hashtable images;

    protected ImageGetter() {
        images = new Hashtable();
    }

    public Image getImage(String filename) {
        if (images.containsKey(filename)) {
            return (Image) images.get(filename);
        } else {
            try {
                Image image = Image.createImage(filename);
                images.put(filename, image);
                return image;
            } catch (Exception e) {
                Debug.handleException(e);
                //Debug.println("FILE " + filename + " not found");
                return null;
            }
        }
    }
    public static final ImageGetter Instance = new ImageGetter();
}
