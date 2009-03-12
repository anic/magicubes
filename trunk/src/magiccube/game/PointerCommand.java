/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class PointerCommand {

    private Vector vector;

    private class PointerItem {

        public int event;
        public int px;
        public int py;

        public PointerItem(int event, int px, int py) {
            this.event = event;
            this.px = px;
            this.py = py;
        }
    }

    public PointerCommand() {
        vector = new Vector();
    }

    public void save(int event, int px, int py) {
        vector.addElement(new PointerItem(event, px, py));
    }

    public boolean isEmpty() {
        return vector.isEmpty();
    }

    public int[][] getPointers() {
        if (isEmpty()) {
            return null;
        }

        int[][] result = new int[vector.size()][];
        for (int i = 0; i < vector.size(); ++i) {
            result[i] = decode((PointerItem) vector.elementAt(i));
        }
        vector.removeAllElements();
        return result;
    }

    private int[] decode(PointerItem item) {
        return new int[]{item.event, item.px, item.py};
    }
}
