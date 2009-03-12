/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import javax.microedition.lcdui.Graphics;

/**
 * 管理层对象
 * @author Administrator
 */
public class LayerManager {

    /**
     * 什么也不做的一层
     */
    private class BaseLayer extends GLayer {

        public BaseLayer() {
            super(0, 0);
        }

        public boolean acceptKey(int key) {

            return false;
        }

        public void draw(Graphics g) {
        }

        public void acceptPointers(int[][] pointers){}
    }
    private GLayer baseLayer;
    private GLayer top;

    public LayerManager() {
        baseLayer = new BaseLayer();
        top = baseLayer;
    }

    public GLayer getTopLayer() {
        return top;
    }

    public boolean isActiveLayer(GLayer layer) {
        GLayer cur = layer;
        while (cur != null) {
            if (cur == baseLayer) //寻找到基本的层，就是active的
            {
                return true;
            }

            cur = cur.getUpperLayer();
        }

        return false;
    }

    public void addLayer(GLayer upperLayer) {
        upperLayer.setUpperLayer(this.top);
        this.top = upperLayer;
    }

    public void removeTopLayer() {
        if (this.top != this.baseLayer) {
            GLayer temp = this.top;
            this.top = this.top.getUpperLayer();
            temp.setUpperLayer(null);

        }
    }
}
