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
     * 默认的颜色数组
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
     * Cube原来的ID
     */
    private int id;
    /**
     * Mesh对象
     */
    private Mesh m;
    //坐标
    private static int[][] pointsPerFace = {{4}, {4}, {4}, {4}, {4}, {4}};
    private int[] faces;

    /**
     * 构造一个Cube对象
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

        //VertexArray：第一个参数 点数;
        int numVertices = faces.length * 4;
        VertexArray vaPos = new VertexArray(numVertices, 3, 2);

        short[] vertexPos = new short[numVertices * 3];
        byte[] vertexCol = new byte[numVertices * 3];
        int index_vertexPos = 0;
        int nOfFace = 12; //一个面4个点，每个点3个坐标
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

        //法向设置
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
     * 设置透明度
     * @param alpha 在0，1之间
     */
    public void setAlpha(float alpha) {
        m.setAlphaFactor(alpha);

    }

    public void beforeRotation() {
    }

    /**
     * 旋转Cube
     * @param angle 旋转角度
     * @param ax 旋转的轴X坐标
     * @param ay 旋转的轴Y坐标
     * @param az 旋转的轴Z坐标
     */
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

        //Debug.println("x " + x + " y" + y + " z" + z);
        m.setTranslation(x, y, z);
        m.preRotate(angle, ax, ay, az);//角度变换
    }

    /**
     * 获得原来的ID
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * 从Byte流中读取，构造对象
     * @param source 流数据
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
     * 生成流对象
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
                //因为是标准位置，所以转成Int存储，不失精度.虽然出现90.0001,0.755,0.755,0的情况，转成int后，变成90,1,1,0,结果相同
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
