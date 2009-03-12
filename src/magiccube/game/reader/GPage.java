/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.reader;

import magiccube.game.*;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Administrator
 */
public class GPage {

    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected Vector segments = new Vector();
    protected boolean bInit; //�Ƿ����

    public GPage() {
        width = height = 0;
        x = y = 0;
        bInit = false;

    }
    protected String[] source;
    protected int offset;
    protected int len;

    public void set(String[] strSegments, int offset, int len) {
        source = strSegments;
        this.offset = offset;
        this.len = len;
    }

    public void load() {
        ISegment cur;
        for (int i = offset; i < offset + len; ++i) {
            //Debug.println("load in page :" + source[i]);
            if (source[i].startsWith(Common.PREFIX_IMAGE)) {

                cur = new ImageSegment(source[i].substring(Common.PREFIX_IMAGE.length()));
            } else if (source[i].startsWith(Common.PREFIX_TITLE)) {
                cur = new TextSegment(source[i].substring(Common.PREFIX_TITLE.length()), Common.FONT_TITLE, true);
            } else {
                cur = new TextSegment(source[i], Common.FONT_DEFAULT, false);
            }
            segments.addElement(cur);
            this.height += cur.getHeight(); //�ۼ�
            this.width = (cur.getWdith() > this.width) ? cur.getWdith() : this.width; //ѡ��������Ϊ���
        }
        bInit = true;
    }

    //����Ļ��offsetX��offsetY��ʼ������drawWidth��drawHeight�ߵ�����
    public void draw(Graphics g, int offsetX, int offsetY, int drawWidth, int drawHeight) {
        int disY = 0;
        for (int i = 0; i < segments.size(); ++i) {
            ISegment seg = (ISegment) segments.elementAt(i);
            seg.draw(g, offsetX - x, offsetY - y + disY, drawWidth, drawHeight);
            disY += seg.getHeight();
        }
    }

    public void setPosition(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.x = x;
            this.y = y;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isLoaded() {
        return bInit;
    }
}
