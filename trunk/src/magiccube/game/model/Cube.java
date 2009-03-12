package magiccube.game.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;
import magiccube.util.ArrayUtil;
import magiccube.util.Converter;
import magiccube.util.Debug;
import magiccube.util.MathUtil;

/**
 *
 * @author cya
 */
public class Cube extends CubeBase {

    /**
     * Ĭ�ϵ���ɫ����
     * These are the colors for the vertices
     */
    protected static byte[] defaultCol = {
        // Left white
        (byte) 255, (byte) 255, (byte) 255,
        (byte) 255, (byte) 255, (byte) 255,
        (byte) 255, (byte) 255, (byte) 255,
        (byte) 255, (byte) 255, (byte) 255,
        // Right yellow
        (byte) 255, (byte) 255, 0,
        (byte) 255, (byte) 255, 0,
        (byte) 255, (byte) 255, 0,
        (byte) 255, (byte) 255, 0,
        // Bottom green
        0, (byte) 255, 0,
        0, (byte) 255, 0,
        0, (byte) 255, 0,
        0, (byte) 255, 0,
        // Top blue
        0, 0, (byte) 255,
        0, 0, (byte) 255,
        0, 0, (byte) 255,
        0, 0, (byte) 255,
        // Back orange
        (byte) 255, (byte) 128, 0,
        (byte) 255, (byte) 128, 0,
        (byte) 255, (byte) 128, 0,
        (byte) 255, (byte) 128, 0,
        // Front red
        (byte) 255, 0, 0,
        (byte) 255, 0, 0, //220
        (byte) 255, 0, 0, //180
        (byte) 255, 0, 0,};
    /**
     * Cubeԭ����ID
     */
    private int id;
    /**
     * Mesh����
     */
    private Mesh m;
    //����
    private static int[][] pointsPerFace = {{4}, {4}, {4}, {4}, {4}, {4}};
    private int[] faces;

    /**
     * ����һ��Cube����
     */
    protected Cube() {
        x = y = z = id = 0;
    }

    public Cube(int id, int faces[]) {
        this.id = id;
        x = y = z = 0;
        createMesh(faces);
    }

    public void setPosition(float x, float y, float z) {
        m.setTranslation(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Mesh getMesh() {
        return m;
    }

    protected void createMesh(int[] faces) {

        this.faces = new int[faces.length];
        ArrayUtil.copyArray(faces, 0, faces.length, this.faces, 0);
        //Debug.println("faces " + String.valueOf(faces.length));

        //VertexArray����һ������ ����;
        int numVertices = faces.length * 4;
        VertexArray vaPos = new VertexArray(numVertices, 3, 2);

        short[] vertexPos = new short[numVertices * 3];
        byte[] vertexCol = new byte[numVertices * 3];
        int index_vertexPos = 0;
        int nOfFace = 12; //һ����4���㣬ÿ����3������
        for (int i = 0; i < faces.length; ++i) {
            ArrayUtil.copyArray(aPos, faces[i] * nOfFace, nOfFace, vertexPos, index_vertexPos);
            ArrayUtil.copyArray(defaultCol, faces[i] * nOfFace, nOfFace, vertexCol, index_vertexPos);
            index_vertexPos += nOfFace;
        }
        //Debug.println("vertexPos ");
        //Debug.println(Debug.toString(vertexPos));
        //Debug.println("numVertices " + numVertices);
        vaPos.set(0, numVertices, vertexPos);

        VertexBuffer vertexBuffer = new VertexBuffer();
        vertexBuffer.setDefaultColor(0xFFFFFFFF);
        vertexBuffer.setPositions(vaPos, 0.40f, null);

        VertexArray vaCols = new VertexArray(numVertices, 3, 1);
        vaCols.set(0, numVertices, vertexCol);
        vertexBuffer.setColors(vaCols);

        //��������
        IndexBuffer iba[] = new IndexBuffer[faces.length];
        Appearance appa[] = new Appearance[faces.length];
        for (int i = 0; i < iba.length; ++i) {
            iba[i] = new TriangleStripArray(i * 4, pointsPerFace[i]);
            //Debug.println("create Cube iba " + i * 4 + " " + aaStripLengths[i][0]);
            appa[i] = s_app;
        }

        m = new Mesh(vertexBuffer, iba, appa);
        m.setTranslation(x, y, z);

    }

    /**
     * ����͸����
     * @param alpha ��0��1֮��
     */
    public void setAlpha(float alpha) {
        m.setAlphaFactor(alpha);

    }

    public void beforeRotation() {
    }

    /**
     * ��תCube
     * @param angle ��ת�Ƕ�
     * @param ax ��ת����X����
     * @param ay ��ת����Y����
     * @param az ��ת����Z����
     */
    public void rotate(float angle, float ax, float ay, float az) {
        //λ�ñ仯
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

        //Debug.println("x " + x + " y" + y + " z" + z);
        m.setTranslation(x, y, z);
        m.preRotate(angle, ax, ay, az);//�Ƕȱ任
    }

    /**
     * ���ԭ����ID
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * ��Byte���ж�ȡ���������
     * @param source ������
     * @param offset
     * @param len
     * @return
     */
    public static Cube fromBytes(byte[] source, int offset, int len) {

        float[] matrix = new float[4];
        ByteArrayInputStream bais = new ByteArrayInputStream(source, offset, len);
        java.io.DataInputStream is;// = new java.io.DataOutputStream();
        Cube result = new Cube();
        try {
            is = new DataInputStream(bais);

            result.x = is.readFloat();
            result.y = is.readFloat();
            result.z = is.readFloat();
            result.id = is.readInt();

            for (int i = 0; i < matrix.length; ++i) {
                matrix[i] = is.readInt();
            }

            int faces_length = is.available() / Converter.BYTES_PER_INT;
            int[] tempFaces = new int[faces_length];

            for (int i = 0; i < faces_length; ++i) {
                tempFaces[i] = is.readInt();
            }

            result.createMesh(tempFaces);
            result.m.setOrientation(matrix[0], matrix[1], matrix[2], matrix[3]);
            result.m.setTranslation(result.x, result.y, result.z);
            return result;
        } catch (Exception ex) {
            Debug.handleException(ex);
            return null;
        }

    }

    /**
     * ����������
     * @return
     */
    public byte[] toBytes() {

        //x,y,z,id,matrix(orientation),faces
        float[] matrix = new float[4];
        m.getOrientation(matrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        java.io.DataOutputStream os;// = new java.io.DataOutputStream();
        try {
            os = new java.io.DataOutputStream(baos);
            os.writeFloat(x);
            os.writeFloat(y);
            os.writeFloat(z);
            os.writeInt(id);
            for (int i = 0; i < matrix.length; ++i) {
                //��Ϊ�Ǳ�׼λ�ã�����ת��Int�洢����ʧ����.��Ȼ����90.0001,0.755,0.755,0�������ת��int�󣬱��90,1,1,0,�����ͬ
                os.writeInt(MathUtil.round(matrix[i]));
            }

            for (int i = 0; i < faces.length; ++i) {
                os.writeInt(faces[i]);
            }
            return baos.toByteArray();
        } catch (Exception ex) {
            Debug.handleException(ex);
            return null;
        }
    }
}
