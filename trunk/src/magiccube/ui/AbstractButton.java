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
     * �¼������ӿ�
     */
    public interface IEventListener {

        /**
         * ��ť����¼�
         * @param source ��ť����
         */
        public void onButtonClick(AbstractButton source);

        /**
         * ��ťѡ��״̬�ı��¼�
         * @param source ��ť����
         */
        public void onSelectedChanged(AbstractButton source);
    }


    /**
     * �Ƿ�ص��¼�
     */
    protected boolean update = true;

    /**
     * �Ƿ�ѡ��
     */
    protected boolean selected = false;

    
    /**
     * ��������
     */
    protected IEventListener callback;
    

    /**
     * ������ť
     * @param x ��ʼX
     * @param y ��ʼY
     * @param width ���
     * @param height �߶�
     * @param callback �ص�����
     */
    public AbstractButton(int x, int y, int width, int height, IEventListener callback) {
        super(x, y, width, height);
        this.callback = callback;
    }

    /**
     * �򿪹رջص�ѡ��
     * @param value
     */
    public void enableUpdate(boolean value) {
        update = value;
    }

    /**
     * ����¼�
     * @param event �¼�
     * @param px ����ĵ�x
     * @param py ����ĵ�y
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
     * ����Ϊѡ��
     * @param value �Ƿ�ѡ��
     */
    public void setSelected(boolean value) {
        this.selected = value;
        if (callback != null && update) {
            callback.onSelectedChanged(this);

        }
    }

    /**
     * �ж��Ƿ���ѡ��״̬
     * @return �Ƿ���ѡ��״̬
     */
    public boolean isSelected() {
        return this.selected;
    }
    
}
