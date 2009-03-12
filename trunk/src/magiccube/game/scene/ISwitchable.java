/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.scene;

/**
 *
 * @author Administrator
 */
public interface ISwitchable {

    /**
     * 进入前，做变换前处理
     * @param total 总时间
     */
    public void beforeEntering(int total);

    /**
     * 进入完成后
     */
    public void afterEntering();


    /**
     * 离开前，做变换前处理
     * @param total 总时间
     */
    public void beforeLeaving(int total);

    public void afterLeaving();

    public void processEntering(int spendTime, int currentTime);

    public void processLeaving(int spendTime, int currentTime);
}
