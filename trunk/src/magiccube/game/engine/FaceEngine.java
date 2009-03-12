/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import magiccube.util.ArrayUtil;

/**
 *
 * @author Anic
 */
public class FaceEngine {

    public static final int FACE_LEFT = 0;
    public static final int FACE_RIGHT = 1;
    public static final int FACE_BOTTOM = 2;
    public static final int FACE_TOP = 3;
    public static final int FACE_BACK = 4;
    public static final int FACE_FONT = 5;
    private int[][] faces;
    private int size;
    private static final int INDEX_FACE = 0;
    private static final int INDEX_STARTID = 1;
    private static final int INDEX_STEP = 2;
    private static final int INDEX_NEXTMODE = 3;
    private int[][][] strips_ref;
    private int[][] faces_ref;

    private void init() {
        faces = new int[6][size * size];
        for (int i = 0; i <
                6; ++i) {
            for (int j = 0; j < size * size; ++j) {
                faces[i][j] = i;
            }
        }

        strips_ref = new int[][][]{
                    {
                        //�棬��ǰ�±꣬��һ���±�Ĳ�������һ��ģʽ�Ĳ�������
                        {FACE_FONT, 0, size, 1},
                        {FACE_TOP, size * (size - 1), -size, 1},
                        {FACE_BACK, size * (size - 1), -size, 1},
                        {FACE_BOTTOM, 0, size, 1}
                    },//FACE_AXIS_X,˳ʱ��
                    {
                        {FACE_FONT, size - 1, -1, size},
                        {FACE_LEFT, size * (size - 1), -size, 1},
                        {FACE_BACK, 0, 1, size},
                        {FACE_RIGHT, 0, size, 1}
                    },//FACE_AXIS_Y
                    {
                        {FACE_TOP, 0, 1, size},
                        {FACE_RIGHT, (size - 1), -1, size},
                        {FACE_BOTTOM, size - 1, -1, size},
                        {FACE_LEFT, 0, 1, size}
                    }//FACE_AXIS_Z
                };
        faces_ref = new int[][]{
                    {0, size * (size - 1), size * size - 1, size - 1}, //startId
                    {size, 1, -size, -1},//dx
                    {1, -size, -1, size}//dy
                };
    }

    /**
     * ����������
     * @param gamesize ħ������
     */
    public FaceEngine(int gamesize) {
        size = gamesize;
        init();
    }

    /**
     * ��ת
     * @param mode ��ת��
     * @param modeIndex ��ת������
     * @param clockwise ��ת����
     */
    public void rotate(int mode, int modeIndex, int clockwise) {
        int[] face_index = getFaceIndex(mode);
        int[] id = getStartId(mode, modeIndex);
        int[] step = getStep(mode);
        for (int i = 0; i < size; ++i) //ÿ����С
        {
            cycleShift(face_index, id, clockwise);
            setNextStep(face_index, id, step);
        }

        //ת��
        if (modeIndex == 0 || modeIndex == size - 1) {
            if (mode == MagicCubeEngine.FACE_AXIS_Y) {
                clockwise = 1 - clockwise;
            }
            swapFace(mode * 2 + ((modeIndex == 0) ? 0 : 1), clockwise);
        }
    }

    private void swapFace(int faceIndex, int clockwise) {
        int[] id = new int[faces_ref[0].length];
        ArrayUtil.copyArray(faces_ref[0], 0, id.length, id, 0);

        int[] face_index = new int[]{faceIndex, faceIndex, faceIndex, faceIndex};
        int[] face_index2 = new int[]{0, 1, 2, 3};
        int[] stepX = faces_ref[1];
        int[] stepY = faces_ref[2];
        for (int k = 0; k < size / 2; k++) {
            int[] tempId = new int[id.length];
            ArrayUtil.copyArray(id, 0, id.length, tempId, 0);
            for (int i = 0; i < (size + 1) / 2; ++i) {
                cycleShift(face_index, id, clockwise);
                setNextStep(face_index2, id, stepX);
            }
            id = tempId;
            setNextStep(face_index2, id, stepY);
        }
    }

    /**
     * �����һ����ID��
     * @param face_index
     * @param id ԭ����Id���Լ����ص�ID
     * @param step
     * @return �µ�ID
     */
    private void setNextStep(int[] face_index, int[] id, int step[]) {

        for (int i = 0; i < id.length; ++i) {
            id[i] = id[i] + step[face_index[i]];
        }
    }

    private int[] getStep(int mode) {
        int[] result = new int[]{0, 0, 0, 0, 0, 0};
        for (int i = 0; i < this.strips_ref[mode].length; ++i) {
            result[strips_ref[mode][i][INDEX_FACE]] = strips_ref[mode][i][INDEX_STEP];
        }
        return result;
    }

    private void cycleShift(int[] face_index, int[] id, int clockwise) {
        int valueOf0 = this.faces[face_index[0]][id[0]];
        if (clockwise == MagicCubeEngine.MODE_NOT_CLOCKWISE) {
            this.faces[face_index[0]][id[0]] = this.faces[face_index[3]][id[3]];
            this.faces[face_index[3]][id[3]] = this.faces[face_index[2]][id[2]];
            this.faces[face_index[2]][id[2]] = this.faces[face_index[1]][id[1]];
            this.faces[face_index[1]][id[1]] = valueOf0;
        } else {
            this.faces[face_index[0]][id[0]] = this.faces[face_index[1]][id[1]];
            this.faces[face_index[1]][id[1]] = this.faces[face_index[2]][id[2]];
            this.faces[face_index[2]][id[2]] = this.faces[face_index[3]][id[3]];
            this.faces[face_index[3]][id[3]] = valueOf0;
        }
    }

    private int[] getStartId(int mode, int modeIndex) {
        int[] result = new int[4];
        for (int i = 0; i < 4; ++i) {
            result[i] = this.strips_ref[mode][i][INDEX_STARTID] + modeIndex * this.strips_ref[mode][i][INDEX_NEXTMODE];

        }
        return result;
    }

    private int[] getFaceIndex(int mode) {
        int[] result = new int[4];
        for (int i = 0; i < 4; ++i) {
            result[i] = this.strips_ref[mode][i][INDEX_FACE];
        }
        return result;
    }

    /**
     * ���ֽ����д���FaceEngine
     * @param src �ֽ���
     * @param offset ƫ��
     * @param len ����
     * @return FaceEngine����
     */
    public static FaceEngine fromBytes(byte[] src, int offset, int len) {
        try {
            int index = offset;
            int aSize = src[index++];
            FaceEngine result = new FaceEngine(aSize);
            for (int i = 0; i < 6; ++i) {
                for (int j = 0; j < aSize * aSize; ++j) {
                    result.faces[i][j] = src[index++];
                }
            }
            return result;
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * ����Ϊ�ֽ���
     * @return �ֽ���
     */
    public byte[] toByte() {
        byte[] result = new byte[6 * size * size + 1];
        int index = 0;
        result[index++] = (byte) size;
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < size * size; ++j) {
                result[index++] = (byte) faces[i][j];
            }
        }
        return result;
    }

    /**
     * ���ĳ�����ȡֵ
     * @param faceIndex �������
     * @return ���ϸ���С���ȡֵ
     */
    public int[] getFace(int faceIndex) {
        return faces[faceIndex];
    }
}
