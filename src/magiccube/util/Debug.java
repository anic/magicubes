package magiccube.util;

import magiccube.game.Common;

/**
 *
 * @author Administrator
 */
public class Debug {

    public static void threadSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
        }
    }

    /**
     * Ĭ�ϴ����쳣�ķ���
     * @param e
     */
    public static void handleException(Exception e) {
        Common.DEBUG_INFO = e.getMessage();
        e.printStackTrace();
    }

    //�ڿ���̨���
    public static void println(String content) {
        System.out.println(content);
    }

    public static String toString(String[] vector) {
        String result = "";
        for (int i = 0; i < vector.length; ++i) {
            result += vector[i] + " ";
        }
        return result;
    }

    public static String toString(short[] vector) {
        String result = "";
        for (int i = 0; i < vector.length; ++i) {
            result += String.valueOf(vector[i]) + " ";
        }
        return result;
    }

    public static String toString(int[] vector) {
        String result = "";
        for (int i = 0; i < vector.length; ++i) {
            result += String.valueOf(vector[i]) + " ";
        }
        return result;
    }

    public static String toString(float[] vector) {
        String result = "";
        for (int i = 0; i < vector.length; ++i) {
            result += String.valueOf(vector[i]) + " ";
        }
        return result;
    }

    public static String toString(byte[] vector) {
        String result = "";
        for (int i = 0; i < vector.length; ++i) {
            result += String.valueOf(vector[i]) + " ";
        }
        return result;
    }
}
