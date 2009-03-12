/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.util;

/**
 *
 * @author Administrator
 */
public class Converter {

    public static final int BYTES_PER_INT = 4;
    public static final int BYTES_PER_FLOAT = 4;

    public static void floatToBytes(float f, byte[] buffer, int offset) {
        intToBytes2(Float.floatToIntBits(f), buffer, offset);
    }

    public static float bytesToFloat(byte[] buffer, int offset) {
        int nResult = byteToInt2(buffer, offset);
        return Float.intBitsToFloat(nResult);
    }

    public static byte[] floatToBytes(float f) {
        byte[] result = new byte[4];
        floatToBytes(f,result,0);
        return result;
    }

    public static float bytesToFloat(byte[] buffer) {
        return bytesToFloat(buffer,0);
    }

//    ���� Float.floatToIntBits(f)ת����int
//    ��ͨ�����·���ת��byte []
    /**
     * ��int���͵�����ת��Ϊbyte����
     * ԭ����int�����е��ĸ�byteȡ�����ֱ�洢
     * @param n int����
     * @return ���ɵ�byte����
     */
    public static byte[] intToBytes2(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    public static void intToBytes2(int n, byte[] buffer, int offset) {

        for (int i = 0; i < 4; i++) {
            buffer[offset + i] = (byte) (n >> (24 - i * 8));
        }
    }

    /**
     * ��byte����ת��Ϊint����
     * @param b �ֽ�����
     * @return ���ɵ�int����
     */
    public static int byteToInt2(byte[] b, int offset) {
        return rightShift(b[offset + 0], 24) + rightShift(b[offset + 1], 16) + rightShift(b[offset + 2], 8) + rightShift(b[offset + 3], 0);
    }

    public static int byteToInt2(byte[] b) {
        return byteToInt2(b, 0);
    }

    //���Ʋ�����offset�����Ƶ���
    public static int rightShift(byte b, int offset) {
        return (b >= 0) ? ((int) b << offset) : ((int) (256 + b) << offset);
    }
}
