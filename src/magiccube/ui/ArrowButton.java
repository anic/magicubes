/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.ui;

import javax.microedition.lcdui.Graphics;
import magiccube.game.Common;
import magiccube.util.ArrayUtil;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class ArrowButton extends AbstractButton {

    private static int arrow_unit = 10;
    private static int[][] arrows = new int[][]{
        new int[]{arrow_unit, 0, 0, arrow_unit, 2 * arrow_unit, arrow_unit}, //up
        new int[]{0, arrow_unit, 2 * arrow_unit, arrow_unit, arrow_unit, 2 * arrow_unit}, //down
        new int[]{0, arrow_unit, arrow_unit, 0, arrow_unit, 2 * arrow_unit}, //left
        new int[]{arrow_unit, 0, 2*arrow_unit, arrow_unit, arrow_unit, 2 * arrow_unit} //right
    };
    protected int[] arrow_point;
    public static int ARROW_UP = 0;
    public static int ARROW_DOWN = 1;
    public static int ARROW_LEFT = 2;
    public static int ARROW_RIGHT = 3;
    public static int ARROW_WIDTH = 2*arrow_unit;
    public static int ARROW_HEIGHT = 2*arrow_unit;

    public ArrowButton(int x, int y, int type, IEventListener callback) {
        super(x, y, ARROW_WIDTH, ARROW_HEIGHT, callback);
        this.arrow_point = arrows[type];

    }

    public void draw(Graphics g) {
        if (selected) {
            DrawingUtil.drawArrow(g, arrow_point, x, y, Common.COLOR_BAR, Common.COLOR_SELECTED);
        } else {
            DrawingUtil.drawArrow(g, arrow_point, x, y, Common.COLOR_BAR, Common.COLOR_SCREEN);
        }

    }
}
