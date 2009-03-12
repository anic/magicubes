/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.model;

import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.Transform;

/**
 *
 * @author Administrator
 */
public abstract class CubeBase {
    //面的常量
//    public static final int FACE_FONT = 0;
//    public static final int FACE_BOTTOM = 1;
//    public static final int FACE_TOP = 2;
//    public static final int FACE_RIGHT = 3;
//    public static final int FACE_LEFT = 4;
//    public static final int FACE_BACK = 5;
//
    public static final int FACE_LEFT = 0;
    public static final int FACE_RIGHT = 1;
    public static final int FACE_BOTTOM = 2;
    public static final int FACE_TOP = 3;
    public static final int FACE_BACK = 4;
    public static final int FACE_FONT = 5;


    //样式
    protected static Appearance s_app = new Appearance();
    protected static PolygonMode mode = new PolygonMode();
    //初始化样式


    static {
        //双面显示
        mode.setCulling(PolygonMode.CULL_NONE);
        //mode.setCulling(PolygonMode.CULL_BACK);
        s_app.setPolygonMode(mode);
        CompositingMode cmode = new CompositingMode();
        cmode.setAlphaThreshold(0.01f);
        cmode.setBlending(CompositingMode.ALPHA);
        s_app.setCompositingMode(cmode);

    }
    /**
     * 顶点数组
     *   E/-----------/H
     *   /|          /|
     * A|-----------|D|
     *  | |         | |
     *  | |         | |
     *  |/F---------| /G
     * B|-----------|C
     */
    protected static short[] aPos = {
        // Left
        -1, -1, 1, // B
        -1, 1, 1, // A
        -1, -1, -1, // F
        -1, 1, -1, // E
        // Right
        1, 1, 1, // D
        1, -1, 1, // C
        1, 1, -1, // H
        1, -1, -1, // G
        // Bottom
        -1, -1, -1, // F
        1, -1, -1, // G
        -1, -1, 1, // B
        1, -1, 1, // C
        // Top
        -1, 1, 1, // A
        1, 1, 1, // D
        -1, 1, -1, // E
        1, 1, -1, // H
        // Back
        1, -1, -1, // G
        -1, -1, -1, // F
        1, 1, -1, // H
        -1, 1, -1, // E
        // Front
        -1, -1, 1, // B
        1, -1, 1, // C
        -1, 1, 1, // A
        1, 1, 1, // D
    };
    protected float x = 0;
    protected float y = 0;
    protected float z = 0;

    public abstract Mesh getMesh();

    /**
     * 设置透明度
     * @param alpha 透明度，在0，1之间
     */
    public void setAlpha(float alpha)
    {
        getMesh().setAlphaFactor(alpha);
    }

    public void rotate(float angle, float ax, float ay, float az) {
        //位置变化
        float[] matrix = new float[]{
            x, y, z, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        };

        Transform t = new Transform();
        t.set(matrix);
        t.postRotate(-angle, ax, ay, az);
        t.get(matrix);
        x = matrix[0];
        y = matrix[1];
        z = matrix[2];
        getMesh().setTranslation(x, y, z);
        getMesh().preRotate(angle, ax, ay, az);//角度变换
    }

    public void getPosition(float[] position) {
        position[0] = x;
        position[1] = y;
        position[2] = z;
    }

    public void setPosition(float px,float py,float pz)
    {
        x = px;
        y = py;
        z = pz;
        getMesh().setTranslation(px, py, pz);
    }
}
