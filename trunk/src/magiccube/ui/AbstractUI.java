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
     * ���ϽǺ�����
     */
    protected int x;
    /**
     * ���Ͻ�������
     */
    protected int y;
    /**
     * ���
     */
    protected int width;
    /**
     * �߶�
     */
    protected int height;
    /**
     * �Ƿ񼤻�
     */
    protected boolean enable;

    /**
     * ��������
     * @param x ���ϽǺ�����
     * @param y ���Ͻ�������
     * @param width ���
     * @param height �߶�
     */
    public AbstractUI(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enable = true;
    }

    /**
     * �ж��Ƿ񼤻���
     * @return �Ƿ񼤻���
     */
    public boolean getEnable() {
        return this.enable;
    }

    /**
     * ���ü���״̬
     * @param value �Ƿ񼤻�
     */
    public void setEnable(boolean value) {
        this.enable = value;
    }

    /**
     * ��ú�����
     * @return ������
     */
    public int getX() {
        return x;
    }

    /**
     * ���������
     * @return ������
     */
    public int getY() {
        return y;
    }

    /**
     * ��ÿ��
     * @return ���
     */
    public int getWidth() {
        return width;
    }

    /**
     * ��ø߶�
     * @return �߶�
     */
    public int getHeight() {
        return height;
    }

    /**
     * ���ƿؼ�
     * @param g
     */
    public void draw(Graphics g) {
    }

    /**
     * �ж��Ƿ�����
     * @param px �����ʺ�����
     * @param py ������������
     * @return �Ƿ����
     */
    public boolean isPressed(int px, int py) {
        if (!enable) {
            return false;
        }
        
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    /**
     * �����¼�
     * @param event �¼����ͣ���Common
     * @param px �¼���������Ļλ�ã�x
     * @param py �¼���������Ļλ�ã�y
     */
    public void performAction(int event, int px, int py) {
    }

    /**
     * �������ÿؼ�λ��
     * @param x ������
     * @param y ������
     */
    public void setLoacation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
