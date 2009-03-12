/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.util;

/**
 *
 * @author Administrator
 */
public class MathUtil {

    //¸¡µãÊýµÄÎó²î
    static final float ERROR_VERTEX = 0.0001f;

    public static boolean areEqual(float f1, float f2) {
        return java.lang.Math.abs(f1 - f2) < ERROR_VERTEX;
    }

    public static void getCrossProduct(float[] vector1, float[] vector2, float[] out) {
        //i:b1c2-b2c1,
        //j:c1a2-a1c2,
        //k:a1b2-a2b1
        out[0] = vector1[1] * vector2[2] - vector2[1] * vector1[2];
        out[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        out[2] = vector1[0] * vector2[1] - vector2[0] * vector1[1];
    }

    public static int roundZeroOrOne(float f) {
        if (f >= 0) {
            if (f < ERROR_VERTEX) {
                return 0;
            } else {
                return 1;
            }
        } else //f<0
        {
            if (f > -ERROR_VERTEX) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public static int round(float f) {
        int nValue = (int) f;
        boolean positive = f > 0;
        if (positive) {

            if (f - nValue > 0.5f) {
                return nValue + 1;
            } else {
                return nValue;
            }

        } else {
            if (nValue - f > 0.5f) {
                return nValue - 1;
            } else {
                return nValue;
            }
        }
    }
}
