/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.util;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.World;
import magiccube.game.Common;

/**
 *
 * @author Administrator
 */
public class DrawingUtil {

    public static void drawMenuIcon(Graphics g, int x, int y, int width, int height, int barColor, int bgColor, Image img) {
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);


        int w = img.getWidth();
        int h = img.getHeight();
        g.drawImage(img, x + width / 2 - w / 2, y + height / 2 - h / 2, 0);


        //新的颜色是白色
        g.setColor(barColor);
        g.drawRect(x, y, width, height);
    }

    public static void drawMenuIcon(Graphics g, int x, int y, int width, int height, int barColor, int bgColor, String text, Font font) {
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);

        //g.setColor(255, 255, 255);
        g.setColor(barColor);
        g.setFont(font);
        int w = font.charsWidth(text.toCharArray(), 0, text.length());
        int h = font.getHeight();
        g.drawString(text, x + width / 2 - w / 2, y + height / 2 - h / 2, 0);
        g.drawRect(x, y, width, height);
    }

    public static void devideColor(int color, byte[] result) {

        result[0] = (byte) (color / 256 / 256);  //r
        result[1] = (byte) (color / 256 % 256); //g
        result[2] = (byte) (color % 256); //b
    }

    public static void drawArrow(Graphics g, int[] arrows, int offset_x, int offset_y, int barColor, int bgColor) {

        g.setColor(bgColor);
        g.fillTriangle(offset_x + arrows[0], offset_y + arrows[1], offset_x + arrows[2], offset_y + arrows[3], offset_x + arrows[4], offset_y + arrows[5]);

        g.setColor(barColor);
        g.drawLine(offset_x + arrows[0], offset_y + arrows[1], offset_x + arrows[2], offset_y + arrows[3]);
        g.drawLine(offset_x + arrows[2], offset_y + arrows[3], offset_x + arrows[4], offset_y + arrows[5]);
        g.drawLine(offset_x + arrows[4], offset_y + arrows[5], offset_x + arrows[0], offset_y + arrows[1]);
    }

    /**
     * 绘制3D场景
     * @param g
     * @param w
     */
    public static void draw3DByWorld(Graphics g, World w) {
        if (w == null) {
            return;
        }
        Graphics3D g3d = Graphics3D.getInstance();
        g3d.bindTarget(g, true, Common.RENDERING_HINTS);
        try {
            g3d.render(w);
        } finally {
            g3d.releaseTarget();
        }
    }
}
