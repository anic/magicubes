/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import magiccube.game.Common;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class TextButton extends AbstractButton {

    protected String text;
    protected Font font;

    public TextButton(int x, int y, int width, int height, String text, Font font, IEventListener callback) {
        super(x, y, width, height, callback);
        this.text = text;
        this.font = font;
        this.callback = callback;
    }

    public void draw(Graphics g) {
        if (!enable) {
            return;
        }

        if (selected) {
            DrawingUtil.drawMenuIcon(g, x, y, width, height, Common.COLOR_BAR, Common.COLOR_SELECTED, text, font);
        } else {
            DrawingUtil.drawMenuIcon(g, x, y, width, height, Common.COLOR_BAR, Common.COLOR_SCREEN, text, font);
        }

    }
}
