/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import magiccube.game.GameEngine;

/**
 * 保存时候显示的层
 * @author Administrator
 */
public class SavingLayer extends MessageBoxLayer implements Runnable {

    public void run() {
        game.save();
        layerManger.removeTopLayer();
        //Debug.threadSleep(2000);
    }

    public boolean acceptKey(int key) {
        return true;
    }

    public void acceptPointers(int[][] pointers) {
    }
    private GameEngine game;
    private LayerManager layerManger;

    public SavingLayer(GameEngine game, LayerManager layerManager, int width, int height) {
        super(width, height, "保存中，请稍后...", "保存", new String[]{}, null);
        this.game = game;
        this.layerManger = layerManager;
        Thread thread = new Thread(this);
        thread.start();
        
    }




}
