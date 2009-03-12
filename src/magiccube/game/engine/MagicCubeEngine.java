/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Camera;
import magiccube.game.animation.IAnimation;

/**
 * ħ������
 * @author Anic
 */
public abstract class MagicCubeEngine implements IAnimation {

    
    /**
     * �ṩ��setface�Ĳ�����û������ת����
     */
    public static final int FACE_NONE = -1;
    /**
     * �ṩ��setface�Ĳ���������X��
     */
    public static final int FACE_AXIS_X = 0;
    /**
     * �ṩ��setface�Ĳ���������Y��
     */
    public static final int FACE_AXIS_Y = 1;
    /**
     * �ṩ��setface�Ĳ���������Z��
     */
    public static final int FACE_AXIS_Z = 2;
    /**
     * ת������ģʽ��˳ʱ��
     */
    public static final int MODE_CLOCKWISE = 1;
    /**
     * ת������ģʽ����ʱ��
     */
    public static final int MODE_NOT_CLOCKWISE = 0;
    /**
     * �¼�����
     */
    protected ICubeEngineEventListener eventListener;
    /**
     * ��ǰģʽ
     */
    private int currentMode;
    /**
     * ģʽ�µ�����
     */
    private int currentModeIndex;
    /**
     * ��ת����ģʽ
     */
    private int currentModeClockwise;
    /**
     * ħ������
     */
    protected int size;

    /**
     * �¼������ӿ�
     */
    public interface ICubeEngineEventListener {

        /**
         * ��ħ��ѡ�����ı䣬���лص�
         * @param obj ħ������
         */
        public void onSelectionChanged(MagicCubeEngine obj);
    }

    /**
     * ����ħ������
     * @param size ħ����С
     * @param eventListener ħ���¼������ӿ�
     */
    public MagicCubeEngine(int size, ICubeEngineEventListener eventListener) {
        this.size = size;
        this.eventListener = eventListener;


        currentMode = FACE_AXIS_X;
        currentModeIndex = 0;
        currentModeClockwise = MODE_CLOCKWISE;
    }

    /**
     * ���õ�ǰģʽ
     * @param mode ת����
     * @param index ת��������������ת��������С����
     * @param clockwise ת������
     */
    public void setMode(int mode, int index, int clockwise) {

        //�ɵı�ѡ�е�cube
        setSelection(false);

        //�޸�ѡ�е�cube
        beforeModifyCubeByMode(mode, index, clockwise);
        this.currentMode = mode;
        this.currentModeIndex = index;
        this.currentModeClockwise = clockwise;

        //�޸��µ�CubeΪѡ��ģʽ
        setSelection(true);

        if (eventListener != null) {
            eventListener.onSelectionChanged(this);
        }
    }

    /**
     * ����ʵ�֣�ֻ��Ҫ�޸�selectedCube���ɣ�����Ҫ�޸��Ƿ����
     * @param mode ת����
     * @param index ת��������
     * @param clockwise ת������
     */
    protected abstract void beforeModifyCubeByMode(int mode, int index, int clockwise);

    /**
     * ��õ�ǰģʽ
     * @return ��FACE_AXIS_X��FACE_AXIS_Y��FACE_AXIS_Z��ȡֵ
     */
    public int getMode() {
        return currentMode;
    }

    /**
     * ��õ�ǰ��Mode���棬ѡ�еڼ���Index
     * @return
     */
    public int getModeIndex() {
        return currentModeIndex;
    }

    /**
     * �ж��Ƿ�ĳ����ͬһ����ɫ
     * @param face_index �������
     * @return 
     */
    public abstract boolean isFaceSameColor(int face_index);

    /**
     * ��ʼ��
     * @param load �Ƿ��Ǽ���ģʽ
     */
    public abstract void init(boolean load);

    /**
     * �����ж��ٸ����ͬ����ɫ��
     * @return ����ͬ��ɫ������
     */
    public abstract int countSameColorFaces();

    /**
     * ���浱ǰ��ħ��λ��
     */
    public abstract void save();

    /**
     * ����ת������
     * @param clockwiseMode ��MODE_CLOCKWISE��MODE_NOT_CLOCKWISE��ѡ��һ��
     */
    public void setClockwise(int clockwiseMode) {
        currentModeClockwise = clockwiseMode;
        if (eventListener != null) {
            eventListener.onSelectionChanged(this);
        }
    }

    /**
     * ���ת������
     * @return ΪMODE_CLOCKWISE����MODE_NOT_CLOCKWISE
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
     * ���ת����
     * @param result_axis ת����
     */
    public void getAxis(int[] result_axis) {
        getAxis(getMode(), result_axis);

    }

    /**
     * ����ѡ�е�����
     * @param selection
     */
    public abstract void setSelection(boolean selection);

    /**
     * ���clockwise������һ��ֵ
     * @param clockwise ��MODE_CLOCKWISE��MODE_NOT_CLOCKWISE��ȡֵ
     * @return clockwise������һ��ֵ
     */
    public static int notClockwise(int clockwise) {
        return 1 - clockwise;
    }

    /**
     * Ҫ��ʵ�ֻ���
     * @param g
     * @param width
     * @param height
     */
    public abstract void draw(Graphics g, int width, int height);

    /**
     * ѡ����һ��ѡ��
     * @param next
     */
    public void switchSelection(boolean next) {
        int mode = getMode();
        int mode_index = getModeIndex();

        if (mode == -1) {
            mode = 0;
            mode_index = -1; //Ϊ�˼��ܱ��0
        }

        if (next) {
            if (mode_index == size - 1) {
                mode = (mode + 1) % 3; //������ǳ���size����Ϊ��3��mode
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
     * ���ħ���Ľ���
     * @return
     */
    public int getSize()
    {
        return size;
    }


}
