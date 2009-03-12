/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import magiccube.util.Debug;

/**
 *
 * @author Administrator
 */
public class FaceCanvas extends GameCanvas {

    private FaceEngine faceEngine;
    private Canvas canvas;
    private MIDlet midlet;
    public static final int FACE_LEFT = 0x00ffffff;
    public static final int FACE_RIGHT = 0x00ffff00;
    public static final int FACE_BOTTOM = 0x0000ff00;
    public static final int FACE_TOP = 0x0000ff;
    public static final int FACE_BACK = 0x00ff7f00;
    public static final int FACE_FONT = 0x00ff0000;
    public static final int[] COLOR = new int[]{FACE_LEFT, FACE_RIGHT, FACE_BOTTOM, FACE_TOP, FACE_BACK, FACE_FONT};

    public FaceCanvas(FaceEngine faceEngine, MIDlet midlet, Canvas canvas) {
        super(false);
        this.setFullScreenMode(true);
        this.faceEngine = faceEngine;
        this.midlet = midlet;
        this.canvas = canvas;
    }

    public void render() {
        Graphics g = this.getGraphics();
        g.setColor(0);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        int space = 10;
        int edge = ((this.getWidth() / 2) < (this.getHeight() / 3)) ? this.getWidth() / 2 : this.getHeight() / 3;
        edge -= space;

        int[] colors = faceEngine.getFace(0);
        int x, y;
        x = 0;
        y = 0;
        drawColorPiece(g, x, y, edge, edge, colors);
        drawString(g, x, y, "��");

        x += edge + space;
        colors = faceEngine.getFace(1);
        drawColorPiece(g, x, y, edge, edge, colors);
        drawString(g, x, y, "��");


        x = 0;
        y += edge + space;
        colors = faceEngine.getFace(2);
        drawColorPiece(g, x, y, edge, edge, colors);
        drawString(g, x, y, "��");

        x += edge + space;
        colors = faceEngine.getFace(3);
        drawColorPiece(g, x, y, edge, edge, colors);
        drawString(g, x, y, "��");

        x = 0;
        y += edge + space;
        colors = faceEngine.getFace(4);
        drawColorPiece(g, x, y, edge, edge, colors);
        drawString(g, x, y, "��");

        x += edge + space;
        colors = faceEngine.getFace(5);
        drawColorPiece(g, x, y, edge, edge, colors);
        drawString(g, x, y, "ǰ");
        flushGraphics();

    }

    private void drawString(Graphics g, int x, int y, String text) {
        g.setColor(128, 0, 0);
        g.drawString(text, x, y, 0);
    }

    private void drawColorPiece(Graphics g, int x, int y, int width, int height, int[] pieces) {
        int size = (int) Math.sqrt(pieces.length);
        int pWidth = width / size;
        int pHeight = height / size;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                g.setColor(COLOR[pieces[i * size + j]]);
                g.fillRect(x + j * pWidth, y + i * pHeight, pWidth - 1, pHeight - 1);
            }
        }



    }

    protected void keyPressed(int key) {
        Display.getDisplay(midlet).setCurrent(canvas);
    }
}
