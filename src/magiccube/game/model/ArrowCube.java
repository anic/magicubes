package magiccube.game.model;

import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;
import magiccube.util.ArrayUtil;

/**
 * 箭头的Mesh
 * @author cya
 */
public class ArrowCube extends CubeBase {

    protected static short z0 = 1;
    /**
     *
     *       |\
     * A-----D \
     * |     |  *
     * B-----C /
     *       |/
     */
    protected static short[] s_Pos = {
        -1, 1, z0, // A
        -1, -1, z0, // B
        1, 1, z0, // D
        1, -1, z0, // C
        1, -2, z0,
        3, 0, z0,
        1, 2, z0,};

    private byte[] defaultColor;
    private Mesh m;
    private VertexBuffer vertexBuffer;
    private static int[][] s_pointsPerFace = {{4, 3}};
    
    /**
     * 创建箭头
     * @param r 颜色，红色分量
     * @param g 颜色，绿色分量
     * @param b 颜色，蓝色分量
     */
    public ArrowCube(byte r, byte g, byte b) {
        defaultColor = new byte[]{r, g, b};
        x = y = z = 0;
        createMesh(new int[]{0});
    }

    public Mesh getMesh() {
        return m;
    }

    protected void createMesh(int[] faces) {

        //VertexArray：第一个参数 点数;
        int numVertices = 7;
        VertexArray vaPos = new VertexArray(numVertices, 3, 2);

        short[] vertexPos = new short[numVertices * 3];
        byte[] vertexCol = new byte[numVertices * 3];
        int index_vertexPos = 0;
        //int nOfFace = 12; //一个面4个点，每个点3个坐标

        int offset = 0;
        for (int i = 0; i < faces.length; ++i) {
            int nOfFace = ArrayUtil.sum(s_pointsPerFace[i]) * 3;
            ArrayUtil.copyArray(s_Pos, offset, nOfFace, vertexPos, index_vertexPos);
            index_vertexPos += nOfFace;
            offset += nOfFace;
        }


        for (int i = 0; i < vertexCol.length; ++i) {
            vertexCol[i] = defaultColor[i % defaultColor.length];
        }
        vaPos.set(0, numVertices, vertexPos);

        vertexBuffer = new VertexBuffer();
        vertexBuffer.setDefaultColor(0xFFFFFFFF);
        vertexBuffer.setPositions(vaPos, 0.3f, null);

        VertexArray vaCols = new VertexArray(numVertices, 3, 1);
        vaCols.set(0, numVertices, vertexCol);
        vertexBuffer.setColors(vaCols);

        //法向设置
        IndexBuffer iba[] = new IndexBuffer[faces.length];
        Appearance appa[] = new Appearance[faces.length];

        offset = 0;
        for (int i = 0; i < iba.length; ++i) {

            iba[i] = new TriangleStripArray(offset, s_pointsPerFace[i]);
            offset += ArrayUtil.sum(s_pointsPerFace[i]);
            appa[i] = s_app;
        }

        m = new Mesh(vertexBuffer, iba, appa);
        m.setTranslation(x, y, z);

    }
 
}
