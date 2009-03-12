/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import magiccube.game.Common;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class DrawerButton extends AbstractButton {

    protected Image image;

    public DrawerButton(int x, int y, Image image, IEventListener callback) {
        super(x, y, image.getWidth(), image.getHeight(), callback);
        this.image = image;
        this.callback = callback;
    }

    public void draw(Graphics g) {
        if (!enable) {
            return;
        }

        if (selected) {
            DrawingUtil.drawMenuIcon(g, x, y, width, height, Common.COLOR_BAR, Common.COLOR_SELECTED, image);
        } else {
            DrawingUtil.drawMenuIcon(g, x, y, width, height, Common.COLOR_BAR, Common.COLOR_SCREEN, image);
        }
    }
}
