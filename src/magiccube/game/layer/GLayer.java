/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import javax.microedition.lcdui.Graphics;
import magiccube.ui.AbstractButton;
import magiccube.game.Common;

/**
 * ��Ϸ2D������
 * @author Administrator
 */
public abstract class GLayer {

    private GLayer upper = null;
    

    /**
     * ����ϲ�
     * @return �ϲ����
     */
    public GLayer getUpperLayer() {
        return upper;
    }
    /**
     * ����
     */
    protected int width;
    /**
     * ��߶�
     */
    protected int height;

    /**
     * ����������
     * @param width ���
     * @param height �߶�
     */
    public GLayer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    
    /**
     * �����ϲ�
     * @param upper �ϲ����
     */
    public void setUpperLayer(GLayer upper) {
        this.upper = upper;
    }

    
    /**
     * ����
     * @param g ͼ�ζ���
     */
    public abstract void draw(Graphics g);

    
    /**
     * �ж��Ƿ���ռ����¼�
     * @param key ����
     * @return �Ƿ���ռ����¼�
     */
    public abstract boolean acceptKey(int key);

    //
    /**
     * ���������¼�
     * @param pointers �������¼�
     */
    public abstract void acceptPointers(int[][] pointers);

    /**
     * Ĭ�ϵĴ��������¼��ķ���
     * @param pointers �������¼�
     * @param buttons ��ť
     * @return �Ƿ�����
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
     * Ĭ�ϵĴ���ťѡ���¼��ķ���
     * @param buttons ��ť��
     * @param source ����Դ
     * @return �µı�ѡ�еİ�ť���±�
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
