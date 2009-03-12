/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.scene;

import magiccube.game.*;
import javax.microedition.lcdui.Graphics;
import magiccube.game.animation.IAnimation;
import javax.microedition.m3g.Camera;
import magiccube.util.Debug;

/**
 *
 * @author Administrator
 */
public class SwitcherScene implements IAnimation, IScene {

    private GameEngine game;
    private IScene srcScene; //源场景
    private IScene dstScene; //目标场景
    private int phase;
    private static final int PHASE_SOURCE = 0;
    private static final int PHASE_DESTINATION = 1;
    private int totalTime;
    private int currentTime; //相对于某个Scene的经过的总时间
    private IScene current; //当前显示的Scene

    public SwitcherScene(GameEngine game) {
        this.game = game;
    }

    public void startSwitchScene(IScene source, IScene destination) {

        this.srcScene = source;
        this.dstScene = destination;
        this.current = srcScene;

        srcScene.beforeLeaving(Common.TIME_SCENE_SWITCH / 2);
        dstScene.beforeEntering(Common.TIME_SCENE_SWITCH / 2);
        phase = PHASE_SOURCE;
    }

    public void before_end() {

        dstScene.afterEntering();
        game.setScene(dstScene);
    }

    public void before_start(int totalTime) {
        this.totalTime = totalTime;
        this.currentTime = 0;
    }

    public void process(int spendTime, int currentTime) {
        this.currentTime += spendTime;
        if (phase == PHASE_SOURCE && currentTime > totalTime / 2) {
            //应该是srcScene
            srcScene.afterLeaving();
            phase = PHASE_DESTINATION;
            current = dstScene;
            this.currentTime = currentTime - totalTime / 2;
        }
        if (phase == PHASE_SOURCE) {
            //传入的应该是this.currentTime，因为它是记住当前阶段的currentTime
            current.processLeaving(spendTime, this.currentTime);
        } else {
            current.processEntering(spendTime, this.currentTime);
        }
    }

    public void beforeEntering(int total) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void beforeLeaving(int total) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void processEntering(int spendTime, int currentTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void processLeaving(int spendTime, int currentTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean acceptAction() {
        return false;
    }

    public void draw(Graphics g, int width, int height) {
        //DrawingUtil.draw3DByWorld(g, game.getWorld());
        current.draw(g, width, height);
    }

    public Camera getCamera() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void process(int spendTime) {
    }

    public void processAction(int key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void processEvent(int[][] events) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void afterEntering() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void afterLeaving() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
