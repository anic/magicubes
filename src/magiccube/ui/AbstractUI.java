/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.ui;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Administrator
 */
public abstract class AbstractUI {

    /**
     * 左上角横坐标
     */
    protected int x;
    /**
     * 左上角纵坐标
     */
    protected int y;
    /**
     * 宽度
     */
    protected int width;
    /**
     * 高度
     */
    protected int height;
    /**
     * 是否激活
     */
    protected boolean enable;

    /**
     * 创建对象
     * @param x 左上角横坐标
     * @param y 左上角纵坐标
     * @param width 宽度
     * @param height 高度
     */
    public AbstractUI(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enable = true;
    }

    /**
     * 判断是否激活中
     * @return 是否激活中
     */
    public boolean getEnable() {
        return this.enable;
    }

    /**
     * 设置激活状态
     * @param value 是否激活
     */
    public void setEnable(boolean value) {
        this.enable = value;
    }

    /**
     * 获得横坐标
     * @return 横坐标
     */
    public int getX() {
        return x;
    }

    /**
     * 获得纵坐标
     * @return 纵坐标
     */
    public int getY() {
        return y;
    }

    /**
     * 获得宽度
     * @return 宽度
     */
    public int getWidth() {
        return width;
    }

    /**
     * 获得高度
     * @return 高度
     */
    public int getHeight() {
        return height;
    }

    /**
     * 绘制控件
     * @param g
     */
    public void draw(Graphics g) {
    }

    /**
     * 判断是否点击中
     * @param px 触摸笔横坐标
     * @param py 触摸笔纵坐标
     * @return 是否击中
     */
    public boolean isPressed(int px, int py) {
        if (!enable) {
            return false;
        }
        
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    /**
     * 处理事件
     * @param event 事件类型，见Common
     * @param px 事件发生的屏幕位置，x
     * @param py 事件发生的屏幕位置，y
     */
    public void performAction(int event, int px, int py) {
    }

    /**
     * 重新设置控件位置
     * @param x 横坐标
     * @param y 纵坐标
     */
    public void setLoacation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
