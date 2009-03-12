/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.ui;

import magiccube.game.Common;

/**
 *
 * @author Administrator
 */
public abstract class AbstractButton extends AbstractUI {

    /**
     * 事件监听接口
     */
    public interface IEventListener {

        /**
         * 按钮点击事件
         * @param source 按钮对象
         */
        public void onButtonClick(AbstractButton source);

        /**
         * 按钮选择状态改变事件
         * @param source 按钮对象
         */
        public void onSelectedChanged(AbstractButton source);
    }


    /**
     * 是否回调事件
     */
    protected boolean update = true;

    /**
     * 是否选中
     */
    protected boolean selected = false;

    
    /**
     * 监听对象
     */
    protected IEventListener callback;
    

    /**
     * 创建按钮
     * @param x 开始X
     * @param y 开始Y
     * @param width 宽度
     * @param height 高度
     * @param callback 回调对象
     */
    public AbstractButton(int x, int y, int width, int height, IEventListener callback) {
        super(x, y, width, height);
        this.callback = callback;
    }

    /**
     * 打开关闭回调选项
     * @param value
     */
    public void enableUpdate(boolean value) {
        update = value;
    }

    /**
     * 点击事件
     * @param event 事件
     * @param px 点击的点x
     * @param py 点击的点y
     */
    public void performAction(int event, int px, int py) {
        if (!enable) {
            return;
        }

        if (!selected) {
            setSelected(true);
        }

        if (event == Common.EVENT_ENTER || event == Common.EVENT_RELEASED) {
            if (callback != null && update) {
                callback.onButtonClick(this);
            }
        }
    }

    /**
     * 设置为选中
     * @param value 是否选中
     */
    public void setSelected(boolean value) {
        this.selected = value;
        if (callback != null && update) {
            callback.onSelectedChanged(this);

        }
    }

    /**
     * 判断是否处于选中状态
     * @return 是否处于选中状态
     */
    public boolean isSelected() {
        return this.selected;
    }
    
}
