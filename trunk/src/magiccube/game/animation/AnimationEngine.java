/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.animation;

import java.util.Vector;

/**
 * 动画引擎
 * @author Anic
 */
public class AnimationEngine {

    /**
     * 动画对象
     */
    protected class AnimationItem {

        IAnimation animation;   //对象，可以动画的对象
        int total;      //开始
        int delay;      //延迟
        int current;    //当前

        /**
         * 创建动画对象
         * @param ia 可动画化的对象
         * @param total 动画总时间
         * @param delay 延迟时间
         */
        public AnimationItem(IAnimation ia, int total, int delay) {
            this.total = total;
            this.current = 0;
            this.delay = delay;
            this.animation = ia;
        }
    }

    private Vector animations;

    /**
     * 表示动画是循环播放的
     */
    public static final int LOOP_TIME = -1;

    /**
     * 创建动画引擎
     */
    public AnimationEngine() {
        animations = new Vector();
    }

    private int getCount() {
        return animations.size();
    }

    /**
     * 做时间的加法
     * @param aniItem 动画对象
     * @param spendFrame 经过的事件
     * @return 真正经过的时间
     */
    private int addFrames(AnimationItem aniItem, int spendFrame) {

        if (aniItem.delay > 0) //如果有延迟
        {
            if (spendFrame < aniItem.delay) {
                aniItem.delay -= spendFrame;
                return 0;
            } else {
                int tempResult = spendFrame - aniItem.delay;
                aniItem.delay = 0;
                return tempResult;
            }
        }

        if (aniItem.total == LOOP_TIME) {
            return spendFrame;
        }

        if (aniItem.current + spendFrame > aniItem.total) {
            int temp = aniItem.total - aniItem.current;
            aniItem.current = aniItem.total;
            return temp;
        } else {
            aniItem.current += spendFrame;
            return spendFrame;
        }
    }

    
    /**
     * 添加动画
     * @param animation 可动画化对象
     * @param total 动画时间；如果LOOP_TIME,则表示循环
     * @param delay 延迟时间
     */
    public void addAnimation(IAnimation animation, int total, int delay) {
        animations.addElement(new AnimationItem(animation, total, delay));
        animation.before_start(total);
    
    }

    
    /**
     * 判断是否在运动
     * @return 是否在运动
     */
    public boolean isMoving() {
        return this.getCount() > 0;

    }

    /**
     * 清空所有动画
     */
    public void clearAnimations()
    {
        animations.removeAllElements();
    }

    /**
     * 过程
     * @param spendFrame 与上次相差的时间
     */
    public void process(int spendFrame) {

        int size = animations.size();
        Vector toBeRemoved = new Vector();
        for(int i=0;i<size;++i)
        {
            AnimationItem cur = (AnimationItem)animations.elementAt(i);
            int realSpendFrame = addFrames(cur, spendFrame);
            if (realSpendFrame > 0) {
                cur.animation.process(realSpendFrame, cur.current);
            }
        }

        for(int i=0;i<size;++i)
        {
            AnimationItem cur = (AnimationItem)animations.elementAt(i);
            if (cur.total != LOOP_TIME && cur.current == cur.total) //已经执行完
            {
                cur.animation.before_end();
                toBeRemoved.addElement(cur);
            }
        }

        size = toBeRemoved.size();
        for(int i=0;i<size;++i)
        {
            AnimationItem cur = (AnimationItem)toBeRemoved.elementAt(i);
            animations.removeElement(cur);
        }
    }
}
