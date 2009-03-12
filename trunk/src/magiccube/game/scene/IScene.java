/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.scene;

import javax.microedition.lcdui.Graphics;

/**
 * 场景接口
 * @author Anic
 */
public interface IScene extends ISwitchable{

    /**
     * 处理动作，一次处理一个按键
     * @param key 按键值
     */
    public void processAction(int key);

    /**
     * 处理触摸笔动作，事件类型定义见Common events=int[]{event,px,py}
     * @param events
     */
    public void processEvent(int[][] events);

    /**
     * 判断是否接受动作
     * @return 是否接受动作
     */
    public boolean acceptAction();

    
    /**
     * 执行
     * @param spendTime 执行了多少毫秒
     */
    public void process(int spendTime);

    /**
     * 绘制2D图形
     * @param g
     * @param width
     * @param height
     */
    public void draw(Graphics g, int width, int height);

    
}
