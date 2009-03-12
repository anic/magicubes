/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.scene;

import javax.microedition.m3g.Camera;
import magiccube.util.ArrayUtil;
import magiccube.util.Debug;

/**
 *
 * @author Administrator
 */
public class SceneSwitchProxy implements ISwitchable {

    private float[] startPosition;
    private float[] endPosition;
    private float[] currentPosition;
    private float awayFactor = 10f;
    private Camera camera;
    private int totalTime;

    public SceneSwitchProxy(Camera camera) {
        startPosition = new float[3];
        endPosition = new float[3];
        currentPosition = new float[3];
        this.camera = camera;
    }

    public void process(int currentTime) {
        //线性变化
        float factor = (float) currentTime / totalTime;
        
        currentPosition[0] = (1 - factor) * startPosition[0] + factor * endPosition[0];
        currentPosition[1] = (1 - factor) * startPosition[1] + factor * endPosition[1];
        currentPosition[2] = (1 - factor) * startPosition[2] + factor * endPosition[2];

        camera.setTranslation(currentPosition[0], currentPosition[1], currentPosition[2]);
    }

    public void beforeEntering(int total) {
        beforeLeaving(total);
        float[] swap = startPosition;
        startPosition = endPosition;
        endPosition = swap;
    }

    public void beforeLeaving(int total) {
        camera.getTranslation(startPosition);
        ArrayUtil.copyArray(startPosition, 0, 3, endPosition, 0);
        endPosition[0] *= awayFactor;
        endPosition[1] *= awayFactor;
        endPosition[2] *= awayFactor;
        this.totalTime = total;
    }

    public void processEntering(int spendTime, int currentTime) {
        process(currentTime);
    }

    public void processLeaving(int spendTime, int currentTime) {
        process(currentTime);
    }

    public void afterEntering() {
        camera.setTranslation(endPosition[0], endPosition[1], endPosition[2]);
    }

    public void afterLeaving() {
        camera.setTranslation(startPosition[0], startPosition[1], startPosition[2]);
    }
}
