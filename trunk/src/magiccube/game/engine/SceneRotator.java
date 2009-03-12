/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import javax.microedition.m3g.Camera;
import javax.microedition.m3g.Transform;
import magiccube.util.MathUtil;

//TODO:�Ż�ʱ�����ø���������
/**
 * ������Ϸ����ת������
 * @author Anic
 */
public class SceneRotator {

    //X,Y,Z���±�
    private static final int INDEX_X = 0;
    private static final int INDEX_Y = 1;
    private static final int INDEX_Z = 2;
    /**
     * ��¼right,up,camera position�ľ���
     */
    private Transform screenTrans;
    /**
     * ��Ӧ�ľ���
     */
    private float[] screenMatrix;   
    /**
     * ��¼transform �;����Ƿ�任��
     */
    private boolean matrixChanged;  
    private float[][] last;
    private int[][] lastAxis;
    private Camera camera;

    
    /**
     * ��position�е������±�,�ҷ���
     */
    private static final int INDEX_RIGHT = 0;

    /**
     * ��position�е������±�,�Ϸ���
     */
    private static final int INDEX_UP = 1;


    /**
     * ��position�е������±�,���������
     */
    private static final int INDEX_CAMERA = 2;

    /**
     * ��������
     * @param camera
     */
    public SceneRotator(Camera camera) {
        this.camera = camera;

        matrixChanged = false;

        last = new float[3][];
        last[INDEX_RIGHT] = new float[3];
        last[INDEX_UP] = new float[3];
        last[INDEX_CAMERA] = new float[3];

        lastAxis = new int[2][];
        lastAxis[INDEX_UP] = new int[3];
        lastAxis[INDEX_RIGHT] = new int[3];

        screenTrans = new Transform();
    }

    /**
     * ��þ�ͷλ����������
     * @param index_axis ��ʾ���X��Y����Z������
     * @return
     */
    private float getCameraPosition(int index_axis) {
        return getVertex(INDEX_CAMERA, index_axis);
    }

    
    /**
     * ��á��ҡ��������������
     * @param index_axis ��ʾ���X��Y����Z������
     * @return
     */
    private float getRight(int index_axis) {
        return getVertex(INDEX_RIGHT, index_axis);
    }

    /**
     * ��á��ϡ��������������
     * @param index_axis ��ʾ���X��Y����Z������
     * @return
     */
    private float getUp(int index_axis) {
        return getVertex(INDEX_UP, index_axis);
    }

    /**
     * �����ﱣ�桰�ϴΡ���Up��Right��������ʹ��getVertex��صĺ���
     * @param index_dir
     */
    private void saveToLastDir(int index_dir) {

        last[index_dir][INDEX_X] = screenMatrix[index_dir * 4 + INDEX_X];
        last[index_dir][INDEX_Y] = screenMatrix[index_dir * 4 + INDEX_Y];
        last[index_dir][INDEX_Z] = screenMatrix[index_dir * 4 + INDEX_Z];
    }

    
    /**
     * ��þ����еĶ�Ӧ����
     * @param index_dir ��ʾ����ϡ��һ�����Ӱ��λ��
     * @param index_axis �Ǳ�ʾ���X��Y����Z������
     * @return
     */
    private float getVertex(int index_dir, int index_axis) {
        if (matrixChanged) {
            screenTrans.get(screenMatrix);
            matrixChanged = false;
        }
        return screenMatrix[index_dir * 4 + index_axis];
    }


    /**
     * ���ϼ����任�����
     * @param up �ϻ�����
     */
    public void rotByUp(boolean up) {

        //showAllPos();

        float positionAngle = up ? 45f : -45f;
        //����������
        //saveToLastDir();

        //��ͷλ�øı�
        screenTrans.postRotate(positionAngle, this.getRight(INDEX_X), this.getRight(INDEX_Y), this.getRight(INDEX_Z));
        matrixChanged = true;
        camera.setTranslation(this.getCameraPosition(INDEX_X), this.getCameraPosition(INDEX_Y), this.getCameraPosition(INDEX_Z));

        //��ͷ��ת
        float cameraAngle = up ? -45f : 45f;
        //��Ϊ�Ϸ���ֻ�ߴ�Բ�����Բ���Ҫ�ж�
        camera.preRotate(cameraAngle, this.getRight(INDEX_X), this.getRight(INDEX_Y), this.getRight(INDEX_Z));
        //Debug.println("rot up");

        //��������
        adjustVertexes();
    //showAllPos();

    }

    
    /**
     * �ж��Ƿ���������ת
     * @return �Ƿ���������ת
     */
    public boolean canRotRight() {
        if (isParallelWithAxis(INDEX_CAMERA) &&
                !isParallelWithAxis(INDEX_RIGHT) &&
                !isParallelWithAxis(INDEX_UP)) {
            //���ϵ����������
            /*  /\
             * /  \
             * \  /
             *  \/
             */
            return false;
        } else {
            return true;
        }
    }

    
    /**
     * ���Ҽ����任�����
     * @param right �ұ߻������
     */
    public void rotByRight(boolean right) {

        float positionAngle = right ? -45f : 45f;
        //���������ᣬ��Ϊ����ֻ�����right�ľ�����
        saveToLastDir(INDEX_RIGHT);

        //��ͷλ�øı�
        screenTrans.postRotate(positionAngle, lastAxis[INDEX_UP][INDEX_X], lastAxis[INDEX_UP][INDEX_Y], lastAxis[INDEX_UP][INDEX_Z]);
        matrixChanged = true;
        camera.setTranslation(this.getCameraPosition(INDEX_X), this.getCameraPosition(INDEX_Y), this.getCameraPosition(INDEX_Z));


        //��ͷ��ת
        float cameraAngle = right ? 45f : -45f;
        if (isParallelWithAxis(INDEX_UP)) {
            camera.preRotate(cameraAngle, this.getUp(INDEX_X), this.getUp(INDEX_Y), this.getUp(INDEX_Z));
        } else //���ڱ�׼λ��
        {
            float updownAngle;
            if (!this.isUpperOfBigRound(lastAxis[INDEX_UP])) {
                updownAngle = 45f;
            //Debug.println("upper");
            } else {
                updownAngle = -45f;
            //Debug.println("downer");
            }
            //camera pseudo down
            camera.preRotate(updownAngle, last[INDEX_RIGHT][INDEX_X], last[INDEX_RIGHT][INDEX_Y], last[INDEX_RIGHT][INDEX_Z]);
            //camera pseudo right/left
            camera.preRotate(cameraAngle, lastAxis[INDEX_UP][INDEX_X], lastAxis[INDEX_UP][INDEX_Y], lastAxis[INDEX_UP][INDEX_Z]);
            //camera pseudo up
            camera.preRotate(-updownAngle, this.getRight(INDEX_X), this.getRight(INDEX_Y), this.getRight(INDEX_Z));

        }

        //��������
        adjustVertexes();

    }

    
    /**
     * ��������
     * @param p
     * @return
     */
    private float adjustVertex(float p) {
        if (MathUtil.areEqual(p, 0f)) {
            return 0f;
        } else if (MathUtil.areEqual(p, 1.0f)) {
            return 1.0f;
        } else {
            return p;
        }
    }

    
    /**
     * Ϊ�˱��������֣�ÿ����ת����е������
     */
    private void adjustVertexes() {
        float[] matrix = new float[16];
        screenTrans.get(matrix);
        for (int i = INDEX_RIGHT; i <= INDEX_CAMERA; ++i) {
            matrix[i * 4] = adjustVertex(matrix[i * 4]);
            matrix[i * 4 + 1] = adjustVertex(matrix[i * 4 + 1]);
            matrix[i * 4 + 2] = adjustVertex(matrix[i * 4 + 2]);
        }
        screenTrans.set(matrix);

        if (this.isParallelWithAxis(INDEX_UP)) {
            this.setParallelAxis(INDEX_UP, lastAxis[INDEX_UP]);
        }

        if (this.isParallelWithAxis(INDEX_RIGHT)) {
            this.setParallelAxis(INDEX_RIGHT, lastAxis[INDEX_RIGHT]);
        }

    }

    
    /**
     * ���ã��罫�����ϡ�������������Ϊ��axis�ϡ���֮ǰ��Ҫ����isParallelWithAxis
     * @param index_dir
     * @param axis
     */
    private void setParallelAxis(int index_dir, int[] axis) {
        axis[INDEX_X] = MathUtil.roundZeroOrOne(this.getVertex(index_dir, INDEX_X));
        axis[INDEX_Y] = MathUtil.roundZeroOrOne(this.getVertex(index_dir, INDEX_Y));
        axis[INDEX_Z] = MathUtil.roundZeroOrOne(this.getVertex(index_dir, INDEX_Z));
    }

    
    /**
     * �жϷ������������ϡ����ǣ����������ƽ��
     * @param index_dir
     * @return
     */
    private boolean isParallelWithAxis(int index_dir) {
        int count_zero = 0;
        count_zero = (MathUtil.areEqual(this.getVertex(index_dir, INDEX_X), 0f)) ? count_zero + 1 : count_zero;
        count_zero = (MathUtil.areEqual(this.getVertex(index_dir, INDEX_Y), 0f)) ? count_zero + 1 : count_zero;
        count_zero = (MathUtil.areEqual(this.getVertex(index_dir, INDEX_Z), 0f)) ? count_zero + 1 : count_zero;
        return count_zero == 2;
    }

    
    /**
     * �ж��Ƿ��ڴ�Բ��x-y��y-z��z-x��ƽ����
     * @param axis
     * @return
     */
    private boolean isUpperOfBigRound(int[] axis) {
        int index_axis;
        if (axis[INDEX_X] != 0) {
            index_axis = INDEX_X;
        } else if (axis[INDEX_Y] != 0) {
            index_axis = INDEX_Y;
        } else {
            index_axis = INDEX_Z;
        }
        boolean positive = axis[index_axis] > 0;

        float pos_axis = this.getCameraPosition(index_axis);
        if (positive) {
            return pos_axis > 0;
        } else {
            return pos_axis < 0;
        }
    }

    
    /**
     * ������Ӱ��
     * @param width ��Ļ���
     * @param height ��Ļ�߶�
     * @param d ������뿪ԭ��ľ���뾶
     */
    public void resetCamera(int width, int height, float d) {


        //��������
        screenMatrix = new float[]{
                    1, 0, 0, 0, //right
                    0, 1, 0, 0, //up
                    0, 0, d, 0, //camera screenTrans
                    0, 0, 0, 0
                };
        screenTrans.set(screenMatrix);

        //������Ӱ��
        camera.setTranslation(this.getCameraPosition(INDEX_X), this.getCameraPosition(INDEX_Y), this.getCameraPosition(INDEX_Z));
        camera.setOrientation(0, 0, 0, 0);

        //�������
        float aspectRatio = (float) (width) / height;

        //��һ��������͸�ӽǶȣ��ڶ��������ǳ���ȣ�Ȼ���ǽ������Զ����
        camera.setPerspective(45.0f, aspectRatio, 2.0f, 20f);
        //camera.setParallel(45.0f, aspectRatio, 2.0f, 20.0f);

        lastAxis[INDEX_UP][INDEX_X] = 0;
        lastAxis[INDEX_UP][INDEX_Y] = 1;
        lastAxis[INDEX_UP][INDEX_Z] = 0;

        lastAxis[INDEX_RIGHT][INDEX_X] = 1;
        lastAxis[INDEX_RIGHT][INDEX_Y] = 0;
        lastAxis[INDEX_RIGHT][INDEX_Z] = 0;
    }
}
