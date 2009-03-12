/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Camera;
import magiccube.game.animation.IAnimation;

/**
 * 魔方引擎
 * @author Anic
 */
public abstract class MagicCubeEngine implements IAnimation {

    
    /**
     * 提供给setface的参数，没有设置转动面
     */
    public static final int FACE_NONE = -1;
    /**
     * 提供给setface的参数，绕着X轴
     */
    public static final int FACE_AXIS_X = 0;
    /**
     * 提供给setface的参数，绕着Y轴
     */
    public static final int FACE_AXIS_Y = 1;
    /**
     * 提供给setface的参数，绕着Z轴
     */
    public static final int FACE_AXIS_Z = 2;
    /**
     * 转动方向模式，顺时针
     */
    public static final int MODE_CLOCKWISE = 1;
    /**
     * 转动方向模式，逆时针
     */
    public static final int MODE_NOT_CLOCKWISE = 0;
    /**
     * 事件监听
     */
    protected ICubeEngineEventListener eventListener;
    /**
     * 当前模式
     */
    private int currentMode;
    /**
     * 模式下的索引
     */
    private int currentModeIndex;
    /**
     * 旋转方向模式
     */
    private int currentModeClockwise;
    /**
     * 魔方阶数
     */
    protected int size;

    /**
     * 事件监听接口
     */
    public interface ICubeEngineEventListener {

        /**
         * 当魔方选择发生改变，进行回调
         * @param obj 魔方引擎
         */
        public void onSelectionChanged(MagicCubeEngine obj);
    }

    /**
     * 创建魔方引擎
     * @param size 魔方大小
     * @param eventListener 魔方事件监听接口
     */
    public MagicCubeEngine(int size, ICubeEngineEventListener eventListener) {
        this.size = size;
        this.eventListener = eventListener;


        currentMode = FACE_AXIS_X;
        currentModeIndex = 0;
        currentModeClockwise = MODE_CLOCKWISE;
    }

    /**
     * 设置当前模式
     * @param mode 转动面
     * @param index 转动面索引，沿着转动面的轴从小往大
     * @param clockwise 转动方向
     */
    public void setMode(int mode, int index, int clockwise) {

        //旧的被选中的cube
        setSelection(false);

        //修改选中的cube
        beforeModifyCubeByMode(mode, index, clockwise);
        this.currentMode = mode;
        this.currentModeIndex = index;
        this.currentModeClockwise = clockwise;

        //修改新的Cube为选中模式
        setSelection(true);

        if (eventListener != null) {
            eventListener.onSelectionChanged(this);
        }
    }

    /**
     * 真正实现，只需要修改selectedCube即可，不需要修改是否高亮
     * @param mode 转动面
     * @param index 转动面索引
     * @param clockwise 转动方向
     */
    protected abstract void beforeModifyCubeByMode(int mode, int index, int clockwise);

    /**
     * 获得当前模式
     * @return 从FACE_AXIS_X，FACE_AXIS_Y和FACE_AXIS_Z中取值
     */
    public int getMode() {
        return currentMode;
    }

    /**
     * 获得当前的Mode下面，选中第几个Index
     * @return
     */
    public int getModeIndex() {
        return currentModeIndex;
    }

    /**
     * 判断是否某个面同一种颜色
     * @param face_index 面的索引
     * @return 
     */
    public abstract boolean isFaceSameColor(int face_index);

    /**
     * 初始化
     * @param load 是否是加载模式
     */
    public abstract void init(boolean load);

    /**
     * 计算有多少个面的同样颜色的
     * @return 有相同颜色的面数
     */
    public abstract int countSameColorFaces();

    /**
     * 保存当前的魔方位置
     */
    public abstract void save();

    /**
     * 设置转动方向
     * @param clockwiseMode 从MODE_CLOCKWISE和MODE_NOT_CLOCKWISE中选择一个
     */
    public void setClockwise(int clockwiseMode) {
        currentModeClockwise = clockwiseMode;
        if (eventListener != null) {
            eventListener.onSelectionChanged(this);
        }
    }

    /**
     * 获得转动方向
     * @return 为MODE_CLOCKWISE或者MODE_NOT_CLOCKWISE
     */
    public int getClockwise() {
        return currentModeClockwise;
    }

    /**
     *
     * @param mode
     * @param result_axis
     */
    public static void getAxis(int mode, int[] result_axis) {
        switch (mode) {
            case FACE_AXIS_X:
                result_axis[0] = 1;
                result_axis[1] = result_axis[2] = 0;
                break;
            case FACE_AXIS_Y:
                result_axis[1] = 1;
                result_axis[0] = result_axis[2] = 0;
                break;
            case FACE_AXIS_Z:
                result_axis[2] = 1;
                result_axis[0] = result_axis[1] = 0;
                break;
            case FACE_NONE:
                result_axis[0] = result_axis[1] = result_axis[2] = 0;
        }
    }

    /**
     * 获得转动轴
     * @param result_axis 转动轴
     */
    public void getAxis(int[] result_axis) {
        getAxis(getMode(), result_axis);

    }

    /**
     * 设置选中的区域
     * @param selection
     */
    public abstract void setSelection(boolean selection);

    /**
     * 获得clockwise的另外一个值
     * @param clockwise 从MODE_CLOCKWISE和MODE_NOT_CLOCKWISE中取值
     * @return clockwise的另外一个值
     */
    public static int notClockwise(int clockwise) {
        return 1 - clockwise;
    }

    /**
     * 要求实现绘制
     * @param g
     * @param width
     * @param height
     */
    public abstract void draw(Graphics g, int width, int height);

    /**
     * 选择下一个选择
     * @param next
     */
    public void switchSelection(boolean next) {
        int mode = getMode();
        int mode_index = getModeIndex();

        if (mode == -1) {
            mode = 0;
            mode_index = -1; //为了加能变成0
        }

        if (next) {
            if (mode_index == size - 1) {
                mode = (mode + 1) % 3; //这个不是除以size，因为有3种mode
            }
            mode_index = (mode_index + 1) % size;
        } else //previous
        {
            if (mode_index == 0) {
                mode = (mode - 1 + 3) % 3;
            }
            mode_index = (mode_index - 1 + size) % size;
        }
        int clockwise = getClockwise();
        setMode(mode, mode_index, clockwise);
    }

    public abstract Camera getCamera();

    /**
     * 获得魔方的阶数
     * @return
     */
    public int getSize()
    {
        return size;
    }


}
