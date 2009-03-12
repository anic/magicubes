/*
 * CubeTest.java
 * JMUnit based test
 *
 * Created on 2009-2-1, 14:50:10
 */
package magiccube.test;

import jmunit.framework.cldc10.*;
import magiccube.util.Converter;
import magiccube.util.MathUtil;
import magiccube.util.ReaderUtil;
import magiccube.util.StringUtil;

/**
 * @author Administrator
 */
public class CubeTest extends TestCase {

    public CubeTest() {
        //The first parameter of inherited constructor is the number of test cases
        super(9, "CubeTest");
    }
    float[] testFloat = new float[]{0f, 1f, -1f, 1.5f, -2.5f};

    public void test(int testNumber) throws Throwable {
        switch (testNumber) {
            case 0:
                for (int i = 0; i < testFloat.length; ++i) {
                    bitsIntTest(testFloat[i]);
                }
                break;
            case 1:
                for (int i = 0; i < testFloat.length; ++i) {
                    byteConvertTest(testFloat[i]);
                }
                break;
            case 2:
                //testStore();
//                stackTest(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                break;
            case 3:
//                dstackTest(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, 20);
                break;
            case 4:
//                dstackTest(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, 5);
                break;
            case 5:
                float[] f1 = new float[]{0f, 1f, -1f, 1.25f, -5.75f, 90.0025f, 0.02f};
                int[] n1 = new int[]{0, 1, -1, 1, -6, 90, 0};
                for (int i = 0; i < f1.length; ++i) {
                    assertEquals(n1[i], MathUtil.round(f1[i]));
                }
                break;
            case 6:
                //²âÊÔ
                ReaderUtil reader = new ReaderUtil();
                String text = reader.loadUTF8Text("/test.txt");
                text = text.substring(1);
                //Debug.println(text + " " + text.length());
                assertEquals(text, "test²âÊÔÖÐÎÄ");

                break;
            case 7:
                String testStr = "*abcde$*sdfasdf$*abcd$";
                String[] result1 = new String[]{"abcde$", "sdfasdf$", "abcd$"}; //*
                String[] result2 = new String[]{"*abcde", "*sdfasdf", "*abcd"}; //$
                String[] result3 = new String[]{"*abcde", "sdfasdf", "abcd$"};//$*

                String[] test1 = StringUtil.split(testStr, "*");
                String[] test2 = StringUtil.split(testStr, "$");
                String[] test3 = StringUtil.split(testStr, "$*");

//                Debug.println(Debug.toString(test1));
//                Debug.println(Debug.toString(test2));
//                Debug.println(Debug.toString(test3));

                testStringArray(test1, result1);
                testStringArray(test2, result2);
                testStringArray(test3, result3);
                break;
            case 8:
                String a = "abcdefghijklmn";
                testStringArray(StringUtil.splitByNumbers(a, 3),new String[]{"abc","def","ghi","jkl","mn"});
                testStringArray(StringUtil.splitByNumbers(a, 10),new String[]{"abcdefghij","klmn"});
                testStringArray(StringUtil.splitByNumbers(a, 20),new String[]{"abcdefghijklmn"});
                break;

        }
    }

    public void testStringArray(String[] a, String[] b) {
        //Debug.println(Debug.toString(a));
        //Debug.println(Debug.toString(b));
        assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; ++i) {
            assertEquals(a[i], b[i]);
        }
    }

//    private void testStore() {
//        GameRecorder gr = new GameRecorder("test");
//        gr.init();
//        try {
//            byte[] testByte = new byte[testFloat.length * Converter.BYTES_PER_FLOAT];
//            int offset = 0;
//            for (int i = 0; i < testFloat.length; ++i) {
//                Converter.floatToBytes(testFloat[i], testByte, offset);
//                offset += Converter.BYTES_PER_FLOAT;
//            }
//            int id = gr.getRecordStore().addRecord(testByte, 0, testByte.length);
//            Debug.println("ID = " + id);
//            byte[] result = gr.getRecordStore().getRecord(id);
//
//            assertEquals(result.length, testByte.length);
//            for (int i = 0; i < result.length; ++i) {
//                Debug.println(result[i] + " " + testByte[i]);
//                assertEquals(result[i], testByte[i]);
//            }
//            gr.release();
//        } catch (Exception e) {
//            Debug.handleException(e);
//        }
//    }
    private void byteConvertTest(float f) {
        byte[] b = Converter.floatToBytes(f);
        float newfloat = Converter.bytesToFloat(b);
        //Debug.println(f + " " + newfloat);
        assertTrue(f == newfloat);
    }

    private void bitsIntTest(float f) {
        int n = Float.floatToIntBits(f);
        byte[] b = Converter.intToBytes2(n);
        int newN = Converter.byteToInt2(b);
        assertEquals(n, newN);
    }

//    private void stackTest(int[] array) {
//    }
//
//    private void dstackTest(int[] array, int capacity) {
//        LoopStack_Int stack = new LoopStack_Int(capacity);
//        for (int i = 0; i < array.length; ++i) {
//            stack.push(array[i]);
//        }
//
//        if (capacity >= array.length) {
//            for (int i = array.length - 1; i >= 0; --i) {
//                assertEquals(array[i], stack.pop());
//            }
//        } else {
//            for (int i = array.length - 1; i >= array.length - capacity; --i) {
//                assertEquals(array[i], stack.pop());
//            }
//        }
//
//        assertTrue(stack.empty());
//    }
}
