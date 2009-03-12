/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import javax.microedition.m3g.Node;
import magiccube.game.Common;
import magiccube.game.animation.AnimationEngine;
import magiccube.game.animation.IAnimation;
import magiccube.game.model.ArrowCube;
import magiccube.util.ArrayUtil;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class ArrowSelector implements MagicCubeEngine.ICubeEngineEventListener {

    private AnimationEngine global;
    private AnimationEngine local;

    //无论gamesize是多少，一定是3个箭头
    private ArrowCube[] arrows;
    private int size;

    //箭头旋转的动画
    private class ArrowRotator implements IAnimation {

        private int[] axis;//围绕的轴
        private ArrowCube arrow;
        private static final float ANGLE_PER_MILLISECOND = 360f / 10000;

        public ArrowRotator(ArrowCube arrow, int[] axis) {
            this.axis = new int[axis.length];
            ArrayUtil.copyArray(axis, 0, axis.length, this.axis, 0);
            this.arrow = arrow;
        }

        public void before_end() {
        }

        public void before_start(int totalTime) {
        }

        public void process(int spendTime, int currentTime) {
            arrow.rotate(ANGLE_PER_MILLISECOND * spendTime, axis[0], axis[1], axis[2]);
        }
    }

    /**
     * 创造箭头的选择器
     * @param size 魔方阶数
     * @param global 全局动画引擎
     * @param local 局部动画引擎
     */
    public ArrowSelector(int size, AnimationEngine global, AnimationEngine local) {
        this.global = global;
        this.local = local;
        this.size = size;
        init();
    }

    private void init() {
        byte[] arrowColor = new byte[3];
        DrawingUtil.devideColor(Common.COLOR_SELECTED, arrowColor);

        float radius = 2.5f * size / 3f;
        arrows = new ArrowCube[6];
        arrows[0] = new ArrowCube(arrowColor[0], arrowColor[1], arrowColor[2]);
        arrows[0].setPosition(0, -radius, 0);
        arrows[0].getMesh().preRotate(90f, 0, 1, 0);
        arrows[0].getMesh().preRotate(90f, 0, 0, 1);

        arrows[1] = new ArrowCube(arrowColor[0], arrowColor[1], arrowColor[2]);
        arrows[1].setPosition(0, 0, radius);

        arrows[2] = new ArrowCube(arrowColor[0], arrowColor[1], arrowColor[2]);
        arrows[2].setPosition(radius, 0, 0);
        arrows[2].getMesh().preRotate(90f, 0, 1, 0);
        arrows[2].getMesh().preRotate(90f, 1, 0, 0);

        arrows[3] = new ArrowCube(arrowColor[0], arrowColor[1], arrowColor[2]);
        arrows[3].getMesh().preRotate(180f, 0, 0, 1);
        arrows[3].setPosition(0, -radius, 0);
        arrows[3].getMesh().preRotate(90f, 0, 1, 0);
        arrows[3].getMesh().preRotate(90f, 0, 0, 1);


        arrows[4] = new ArrowCube(arrowColor[0], arrowColor[1], arrowColor[2]);
        arrows[4].getMesh().preRotate(180f, 0, 0, 1);
        arrows[4].setPosition(0, 0, radius);


        arrows[5] = new ArrowCube(arrowColor[0], arrowColor[1], arrowColor[2]);
        arrows[5].getMesh().preRotate(180f, 0, 0, 1);
        arrows[5].setPosition(radius, 0, 0);
        arrows[5].getMesh().preRotate(90f, 0, 1, 0);
        arrows[5].getMesh().preRotate(90f, 1, 0, 0);
    }

    public void onSelectionChanged(MagicCubeEngine engine) {
        setRotateArrow(engine.getMode(), engine.getModeIndex(), engine.getClockwise(), engine);
    }

    /**
     * 获得所有添加到World的对象
     * @return 3D对象
     */
    public Node[] getObject3D() {
        Node[] result = new Node[arrows.length];
        for (int i = 0; i < arrows.length; ++i) {
            result[i] = arrows[i].getMesh();
        }
        return result;
    }

    private void setRotateArrow(int mode, int mode_index, int clockwise, MagicCubeEngine engine) {

        for (int i = 0; i < arrows.length; ++i) {
            arrows[i].setAlpha(0f);
        }

        float[] position = new float[3];
        int arrow_index = (clockwise == MagicCubeEngine.MODE_CLOCKWISE) ? mode : mode + 3;
        arrows[arrow_index].getPosition(position);

        switch (mode) {
            case MagicCubeEngine.FACE_AXIS_X:
                position[0] = mode_index + 0.5f - (float) size / 2;
                break;
            case MagicCubeEngine.FACE_AXIS_Y:
                position[1] = mode_index + 0.5f - (float) size / 2;
                break;
            case MagicCubeEngine.FACE_AXIS_Z:
                position[2] = mode_index + 0.5f - (float) size / 2;
                break;
        }
        arrows[arrow_index].setPosition(position[0], position[1], position[2]);
        arrows[arrow_index].setAlpha(1f);
        int[] axis = new int[3];
        engine.getAxis(axis);

        if (clockwise == MagicCubeEngine.MODE_NOT_CLOCKWISE) {
            axis[0] = -axis[0];
            axis[1] = -axis[1];
            axis[2] = -axis[2];
        }

        local.clearAnimations();
        local.addAnimation(new ArrowRotator(arrows[arrow_index], axis), AnimationEngine.LOOP_TIME, 0);
    }
}
