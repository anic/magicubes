package magiccube.game.model;

import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;
import magiccube.util.ArrayUtil;

/**
 *
 * @author cya
 */
public class CoreCube extends CubeBase {

    private Mesh m;
    private VertexBuffer vertexBuffer;
    public final static int COLOR_DEFAULT = 0;
    private static int[][] aaStripLengths = {{4}, {4}, {4}, {4}, {4}, {4}};

    /**
     * ����һ��ͬһ����ɫ��Cube
     * @param r ��ɫ��������ɫ
     * @param g ��ɫ��������ɫ
     * @param b ��ɫ��������ɫ
     */
    public CoreCube(byte r, byte g, byte b, int size) {

        createMesh(new int[]{
                    FACE_FONT,
                    FACE_BOTTOM,
                    FACE_TOP,
                    FACE_RIGHT,
                    FACE_LEFT,
                    FACE_BACK
                }, r, g, b, size);
    }

    public Mesh getMesh() {
        return m;
    }

    protected void createMesh(int[] faces, byte r, byte g, byte b, int size) {

        //VertexArray����һ������ ����;
        int numVertices = faces.length * 4;
        VertexArray vaPos = new VertexArray(numVertices, 3, 2);

        short[] vertexPos = new short[numVertices * 3];
        byte[] vertexCol = new byte[numVertices * 3];
        int index_vertexPos = 0;
        int nOfFace = 12; //һ����4���㣬ÿ����3������
        for (int i = 0; i < faces.length; ++i) {
            ArrayUtil.copyArray(aPos, faces[i] * nOfFace, nOfFace, vertexPos, index_vertexPos);
            //ArrayUtil.copyArray(defaultCol, faces[i] * nOfFace, nOfFace, vertexCol, index_vertexPos);
            index_vertexPos += nOfFace;
        }
        //����Ϊ��ɫ
        for (int i = 0; i < vertexCol.length; ++i) {
            if (i % 3 == 0) //r
            {
                vertexCol[i] = r;
            } else if (i % 3 == 1) {
                vertexCol[i] = g;
            } else {
                vertexCol[i] = b;
            }
        }


        vaPos.set(0, numVertices, vertexPos);

        vertexBuffer = new VertexBuffer();
        vertexBuffer.setDefaultColor(0xFFFFFFFF);

        //�Ŵ���
        vertexBuffer.setPositions(vaPos, 0.5f * (size - 1), null);
        VertexArray vaCols = new VertexArray(numVertices, 3, 1);
        vaCols.set(0, numVertices, vertexCol);
        vertexBuffer.setColors(vaCols);

        //��������
        IndexBuffer iba[] = new IndexBuffer[faces.length];
        Appearance appa[] = new Appearance[faces.length];
        for (int i = 0; i < iba.length; ++i) {
            iba[i] = new TriangleStripArray(i * 4, aaStripLengths[i]);
            //Debug.println("create Cube iba " + i * 4 + " " + aaStripLengths[i][0]);
            appa[i] = s_app;
        }

        m = new Mesh(vertexBuffer, iba, appa);
        m.setTranslation(x, y, z);

    }
}
