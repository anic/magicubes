/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import magiccube.game.engine.MagicCubeEngine;
import magiccube.game.animation.IAnimation;
import java.util.Random;
import magiccube.util.ArrayUtil;
import magiccube.util.Debug;

/**
 *
 * @author Administrator
 */
public class GameCreator implements IAnimation {

    //不做任何事情，就是占据AnimationEngine的位置
    protected class CreatorEmptyItem implements IAnimation {

        public void before_end() {
        }

        public void before_start(int totalTime) {
        }

        public void process(int spendTime, int currentTime) {
        }
    }

    public void before_end() {
        cubeEngine.before_end();
        if (current < count) {
            setNext();
            game.getAnimationEngine().addAnimation(this, time_per_rotate, 0);
            current++;
        } else {
            game.continueGame();
        }
    }

    public void before_start(int totalTime) {
        cubeEngine.before_start(totalTime);
    }

    public void process(int spendTime, int currentTime) {
        cubeEngine.process(spendTime, currentTime);

    }
    private int count = 0;
    private int current = 0;
    private int time_per_rotate = 0;
    private GameEngine game;
    private MagicCubeEngine cubeEngine;
    private Random random = new Random();
    private boolean bRandom = true;
    private int[] init_array;
    private int init_index = 0;

    public GameCreator(GameEngine game, MagicCubeEngine cubeEngine) {
        this.game = game;
        this.cubeEngine = cubeEngine;
    }

    public void randomRotate(int count) {
        //Debug.println(count + " random ");
        this.bRandom = true;
        this.count = count;
        this.current = 1;
        this.time_per_rotate = Common.TIME_INIT / count;
        if (time_per_rotate == 0) {
            time_per_rotate = 10;
        }
        setNext();
        game.getAnimationEngine().addAnimation(this, time_per_rotate, Common.TIME_SCENE_SWITCH);
    //game.getAnimationEngine().addAnimation(new CreatorEmptyItem(), Common.TIME_INIT, Common.TIME_SCENE_SWITCH + 1000);
    }

    public void taskRotate(int[] array) {
        //Debug.println("taskRotate " + Debug.toString(array));
        if (array.length == 1 && array[0] < 0) {
            randomRotate(-array[0]);//随机-array[0];
        } else {
            this.bRandom = false;
            init_index = 0;
            init_array = new int[array.length];
            ArrayUtil.copyArray(array, 0, array.length, this.init_array, 0);
            this.count = init_array.length;
            this.current = 1;
            this.time_per_rotate = Common.TIME_INIT / count;
            if (time_per_rotate == 0) {
                time_per_rotate = 10;
            }
            setNext();
            game.getAnimationEngine().addAnimation(this, time_per_rotate, Common.TIME_SCENE_SWITCH);
        }

    }

    private void setNext() {
        int num;
        //num (0-1)表示顺时针逆时针|(0-2)表示mode|(0-2)表示index
        if (bRandom) {
            //3是mode的常量
            num = random.nextInt(game.getGameSize() * 3 * 2);//2是顺时针或者逆时针
        } else {
            num = init_array[init_index++];
        }
        int mode = num / game.getGameSize() % 3;
        int mode_index = num % game.getGameSize();
        int clock_wise = num / 3 / game.getGameSize() % 2;
        cubeEngine.setMode(mode, mode_index, clock_wise);
    }
}
