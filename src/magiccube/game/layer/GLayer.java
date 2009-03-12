/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import javax.microedition.lcdui.Graphics;
import magiccube.ui.AbstractButton;
import magiccube.game.Common;

/**
 * 游戏2D场景层
 * @author Administrator
 */
public abstract class GLayer {

    private GLayer upper = null;
    

    /**
     * 获得上层
     * @return 上层对象
     */
    public GLayer getUpperLayer() {
        return upper;
    }
    /**
     * 层宽度
     */
    protected int width;
    /**
     * 层高度
     */
    protected int height;

    /**
     * 创建场景层
     * @param width 宽度
     * @param height 高度
     */
    public GLayer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    
    /**
     * 设置上层
     * @param upper 上层对象
     */
    public void setUpperLayer(GLayer upper) {
        this.upper = upper;
    }

    
    /**
     * 绘制
     * @param g 图形对象
     */
    public abstract void draw(Graphics g);

    
    /**
     * 判断是否接收键盘事件
     * @param key 按键
     * @return 是否接收键盘事件
     */
    public abstract boolean acceptKey(int key);

    //
    /**
     * 处理触摸笔事件
     * @param pointers 触摸笔事件
     */
    public abstract void acceptPointers(int[][] pointers);

    /**
     * 默认的处理触摸笔事件的方法
     * @param pointers 触摸笔事件
     * @param buttons 按钮
     * @return 是否处理了
     */
    public static boolean handlePointers(int[] pointers, AbstractButton[] buttons) {
        if (pointers[0] != Common.EVENT_RELEASED) {
            return false;
        }

        for (int i = 0; i < buttons.length; ++i) {
            if (buttons[i].isPressed(pointers[1], pointers[2])) {
                buttons[i].performAction(pointers[0], pointers[1], pointers[2]);
                return true;
            }
        }
        return false;
    }

    /**
     * 默认的处理按钮选择事件的方法
     * @param buttons 按钮组
     * @param source 触发源
     * @return 新的被选中的按钮的下标
     */
    public static int handleSelection(AbstractButton[] buttons, AbstractButton source) {
        int result = 0;
        for (int i = 0; i < buttons.length; ++i) {
            buttons[i].enableUpdate(false);
            if (source == buttons[i]) {
                buttons[i].setSelected(true);
                result = i;
            } else {
                buttons[i].setSelected(false);
            }
            buttons[i].enableUpdate(true);
        }
        return result;
    }
}
