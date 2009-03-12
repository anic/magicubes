/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.reader;

import magiccube.game.*;
import magiccube.game.reader.ISegment;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import magiccube.util.StringUtil;

/**
 *
 * @author Administrator
 */
public class TextSegment implements ISegment {

    private String[] strLines;
    private int width,  height,  height_per_line;
    private int space;
    public static final int LINES_UNSET = -1;
    private Font font;

    public TextSegment(String content, Font font, boolean oneLines) {
        if (oneLines) {
            createSingleLineTextSegment(content, font);
        } else {
            createCommonTextSegment(content, font);
        }

    }

    private void createSingleLineTextSegment(String content, Font font) {
        this.font = font;
        strLines = new String[]{content};
        this.space = font.getHeight() / 5;
        this.height_per_line = font.getHeight() + space;
        this.height = height_per_line * strLines.length;
    }

    private void createCommonTextSegment(String content, Font font) {

        this.font = font;
        int words_per_line = Common.WORDS_PER_LINE;
        strLines = StringUtil.splitByNumbers(content, words_per_line);

        for (int i = 0; i < strLines.length; ++i) {
            int wordWidth = font.charsWidth(strLines[i].toCharArray(), 0, strLines[i].length());
            if (wordWidth > this.width) {
                this.width = wordWidth;
            }
        }

        this.space = font.getHeight() / 5;
        this.height_per_line = font.getHeight() + space;
        this.height = height_per_line * strLines.length;

    }

    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setFont(font);
        for (int i = 0; i < strLines.length; ++i) {
            g.drawString(strLines[i], x, y + height_per_line * i, 0);
        }
    }

    public int getHeight() {
        return this.height;
    }

    public int getWdith() {
        return this.width;
    }
}
