package magiccube.game.animation;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 可动画化的接口
 * @author Anic
 */
public interface IAnimation {

    /**
     * 动画进行中
     * @param spendTime 与上次调用之间经过的时间，毫秒
     * @param currentTime 从开始到现在经过的时间和，毫秒
     */
    public void process(int spendTime,int currentTime);

    /**
     * 动画开始前
     * @param totalTime 动画总是见
     */
    public void before_start(int totalTime);

    /**
     * 动画结束前
     */
    public void before_end();

}
