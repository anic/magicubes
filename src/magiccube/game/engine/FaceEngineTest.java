/*
 * FaceEngineTest.java
 * JMUnit based test
 *
 * Created on 2009-2-28, 14:59:01
 */
package magiccube.game.engine;

import jmunit.framework.cldc10.*;
import magiccube.util.Debug;

/**
 * @author Administrator
 */
public class FaceEngineTest extends TestCase {

    public FaceEngineTest() {
        //The first parameter of inherited constructor is the number of test cases
        super(2, "FaceEngineTest");
    }

    public void test(int testNumber) throws Throwable {
        switch (testNumber) {
            case 0:
                testRotate();
                break;
            case 1:
                testRotate2();
                break;
            default:
                break;
        }
    }

    public void testRotate2() throws AssertionFailedException {
        FaceEngine instance = new FaceEngine(3);
        int mode_1 = 0;
        int modeIndex_1 = 0;
        int clockwise_1 = 1;
        instance.rotate(mode_1, modeIndex_1, clockwise_1);
        Debug.println("testRotate2");
        compare(instance, FaceEngine.FACE_FONT, new int[]{
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_FONT
                });
        compare(instance, FaceEngine.FACE_TOP, new int[]{
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_TOP
                });
        compare(instance, FaceEngine.FACE_BACK, new int[]{
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BACK
                });
        compare(instance, FaceEngine.FACE_LEFT, new int[]{
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT
                });
        compare(instance, FaceEngine.FACE_BOTTOM, new int[]{
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM
                });




        instance.rotate(1, 0, 1);
        compare(instance, FaceEngine.FACE_FONT, new int[]{
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_FONT
                });
        compare(instance, FaceEngine.FACE_TOP, new int[]{
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_TOP
                });

        compare(instance, FaceEngine.FACE_BACK, new int[]{
                    FaceEngine.FACE_RIGHT,
                    FaceEngine.FACE_RIGHT,
                    FaceEngine.FACE_RIGHT,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_BACK
                });

        compare(instance, FaceEngine.FACE_BOTTOM, new int[]{
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_FONT,
                    FaceEngine.FACE_FONT
                });

        compare(instance, FaceEngine.FACE_LEFT, new int[]{
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_BOTTOM,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT
                });

        instance.rotate(2, 2, 1);
        compare(instance, FaceEngine.FACE_LEFT, new int[]{
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_LEFT,
                    FaceEngine.FACE_BACK,
                    FaceEngine.FACE_TOP,
                    FaceEngine.FACE_TOP
                });
    }

    /**
     * testRotate 方法的测试（属于类 FaceEngine）。
     * @throws AssertionFailedException
     */
    public void testRotate() throws AssertionFailedException {

        FaceEngine instance = new FaceEngine(3);
        int mode_1 = 0;
        int modeIndex_1 = 0;
        int clockwise_1 = 1;
        instance.rotate(mode_1, modeIndex_1, clockwise_1);
        {
            compare(instance, FaceEngine.FACE_FONT, new int[]{
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT
                    });

            compare(instance, FaceEngine.FACE_TOP, new int[]{
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP
                    });

            compare(instance, FaceEngine.FACE_BACK, new int[]{
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BACK
                    });

        }

        instance.rotate(1, 0, 1);
        {
            compare(instance, FaceEngine.FACE_FONT, new int[]{
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT
                    });

            compare(instance, FaceEngine.FACE_TOP, new int[]{
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP
                    });

            compare(instance, FaceEngine.FACE_BACK, new int[]{
                        FaceEngine.FACE_RIGHT,
                        FaceEngine.FACE_RIGHT,
                        FaceEngine.FACE_RIGHT,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BACK
                    });

            compare(instance, FaceEngine.FACE_BOTTOM, new int[]{
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT
                    });

        }

        instance.rotate(2, 0, 1);
        {
            compare(instance, FaceEngine.FACE_FONT, new int[]{
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT
                    });

            compare(instance, FaceEngine.FACE_TOP, new int[]{
                        FaceEngine.FACE_RIGHT,
                        FaceEngine.FACE_RIGHT,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_TOP,
                        FaceEngine.FACE_TOP
                    });

            compare(instance, FaceEngine.FACE_BACK, new int[]{
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_RIGHT,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_RIGHT,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_RIGHT,});

            compare(instance, FaceEngine.FACE_BOTTOM, new int[]{
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_LEFT,
                        FaceEngine.FACE_BACK,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_BOTTOM,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT,
                        FaceEngine.FACE_FONT
                    });

        }
    }

    private void compare(FaceEngine f, int faceIndex, int[] target) {
        int[] src = f.getFace(faceIndex);

        for (int i = 0; i < src.length; ++i) {
            if (src[i] != target[i]) {
                fail();
            }
        }
    }
}
