/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccube.game.reader;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Administrator
 */
public interface ISegment {
    public void draw(Graphics g,int x,int y,int width,int height);

    public int getWdith();

    public int getHeight();
}
