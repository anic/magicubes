/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import javax.microedition.m3g.Camera;
import javax.microedition.m3g.Transform;
import magiccube.util.MathUtil;

//TODO:优化时不采用浮点数运算
/**
 * 控制游戏场景转动的类
 * @author Anic
 */
public class SceneRotator {

    //X,Y,Z的下标
    private static final int INDEX_X = 0;
    private static final int INDEX_Y = 1;
    private static final int INDEX_Z = 2;
    /**
     * 记录right,up,camera position的矩阵
     */
    private Transform screenTrans;
    /**
     * 对应的矩阵
     */
    private float[] screenMatrix;   
    /**
     * 记录transform 和矩阵是否变换了
     */
    private boolean matrixChanged;  
    private float[][] last;
    private int[][] lastAxis;
    private Camera camera;

    
    /**
     * 在position中的索引下标,右方向
     */
    private static final int INDEX_RIGHT = 0;

    /**
     * 在position中的索引下标,上方向
     */
    private static final int INDEX_UP = 1;


    /**
     * 在position中的索引下标,摄像机朝向
     */
    private static final int INDEX_CAMERA = 2;

    /**
     * 创建对象
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
     * 获得镜头位置向量分量
     * @param index_axis 表示获得X，Y还是Z的坐标
     * @return
     */
    private float getCameraPosition(int index_axis) {
        return getVertex(INDEX_CAMERA, index_axis);
    }

    
    /**
     * 获得“右”方向的向量分量
     * @param index_axis 表示获得X，Y还是Z的坐标
     * @return
     */
    private float getRight(int index_axis) {
        return getVertex(INDEX_RIGHT, index_axis);
    }

    /**
     * 获得“上”方向的向量分量
     * @param index_axis 表示获得X，Y还是Z的坐标
     * @return
     */
    private float getUp(int index_axis) {
        return getVertex(INDEX_UP, index_axis);
    }

    /**
     * 在这里保存“上次”的Up和Right，不能再使用getVertex相关的函数
     * @param index_dir
     */
    private void saveToLastDir(int index_dir) {

        last[index_dir][INDEX_X] = screenMatrix[index_dir * 4 + INDEX_X];
        last[index_dir][INDEX_Y] = screenMatrix[index_dir * 4 + INDEX_Y];
        last[index_dir][INDEX_Z] = screenMatrix[index_dir * 4 + INDEX_Z];
    }

    
    /**
     * 获得举行中的对应分量
     * @param index_dir 表示获得上、右还是摄影机位置
     * @param index_axis 是表示获得X，Y还是Z的坐标
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
     * 按上键，变换摄像机
     * @param up 上还是下
     */
    public void rotByUp(boolean up) {

        //showAllPos();

        float positionAngle = up ? 45f : -45f;
        //保存坐标轴
        //saveToLastDir();

        //镜头位置改变
        screenTrans.postRotate(positionAngle, this.getRight(INDEX_X), this.getRight(INDEX_Y), this.getRight(INDEX_Z));
        matrixChanged = true;
        camera.setTranslation(this.getCameraPosition(INDEX_X), this.getCameraPosition(INDEX_Y), this.getCameraPosition(INDEX_Z));

        //镜头旋转
        float cameraAngle = up ? -45f : 45f;
        //因为上方向只走大圆，所以不需要判断
        camera.preRotate(cameraAngle, this.getRight(INDEX_X), this.getRight(INDEX_Y), this.getRight(INDEX_Z));
        //Debug.println("rot up");

        //修正坐标
        adjustVertexes();
    //showAllPos();

    }

    
    /**
     * 判断是否能用左右转
     * @return 是否能用左右转
     */
    public boolean canRotRight() {
        if (isParallelWithAxis(INDEX_CAMERA) &&
                !isParallelWithAxis(INDEX_RIGHT) &&
                !isParallelWithAxis(INDEX_UP)) {
            //面上的情况，出现
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
     * 按右键，变换摄像机
     * @param right 右边还是左边
     */
    public void rotByRight(boolean right) {

        float positionAngle = right ? -45f : 45f;
        //保存坐标轴，因为后面只用这个right的旧坐标
        saveToLastDir(INDEX_RIGHT);

        //镜头位置改变
        screenTrans.postRotate(positionAngle, lastAxis[INDEX_UP][INDEX_X], lastAxis[INDEX_UP][INDEX_Y], lastAxis[INDEX_UP][INDEX_Z]);
        matrixChanged = true;
        camera.setTranslation(this.getCameraPosition(INDEX_X), this.getCameraPosition(INDEX_Y), this.getCameraPosition(INDEX_Z));


        //镜头旋转
        float cameraAngle = right ? 45f : -45f;
        if (isParallelWithAxis(INDEX_UP)) {
            camera.preRotate(cameraAngle, this.getUp(INDEX_X), this.getUp(INDEX_Y), this.getUp(INDEX_Z));
        } else //不在标准位置
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

        //修正坐标
        adjustVertexes();

    }

    
    /**
     * 修正定点
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
     * 为了避免误差出现，每次旋转后进行点的修正
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
     * 设置，如将方向“上”的向量，设置为“axis上”，之前需要调用isParallelWithAxis
     * @param index_dir
     * @param axis
     */
    private void setParallelAxis(int index_dir, int[] axis) {
        axis[INDEX_X] = MathUtil.roundZeroOrOne(this.getVertex(index_dir, INDEX_X));
        axis[INDEX_Y] = MathUtil.roundZeroOrOne(this.getVertex(index_dir, INDEX_Y));
        axis[INDEX_Z] = MathUtil.roundZeroOrOne(this.getVertex(index_dir, INDEX_Z));
    }

    
    /**
     * 判断方向向量，如上、右是，否和坐标轴平行
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
     * 判断是否在大圆（x-y，y-z，z-x）平面上
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
     * 重置摄影机
     * @param width 屏幕宽度
     * @param height 屏幕高度
     * @param d 摄像机离开原点的距离半径
     */
    public void resetCamera(int width, int height, float d) {


        //创建矩阵
        screenMatrix = new float[]{
                    1, 0, 0, 0, //right
                    0, 1, 0, 0, //up
                    0, 0, d, 0, //camera screenTrans
                    0, 0, 0, 0
                };
        screenTrans.set(screenMatrix);

        //设置摄影机
        camera.setTranslation(this.getCameraPosition(INDEX_X), this.getCameraPosition(INDEX_Y), this.getCameraPosition(INDEX_Z));
        camera.setOrientation(0, 0, 0, 0);

        //长宽比例
        float aspectRatio = (float) (width) / height;

        //第一个参数是透视角度，第二个参数是长宽比，然后是近截面和远截面
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
