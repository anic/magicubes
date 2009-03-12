/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.scene;

import javax.microedition.lcdui.Graphics;

/**
 * �����ӿ�
 * @author Anic
 */
public interface IScene extends ISwitchable{

    /**
     * ��������һ�δ���һ������
     * @param key ����ֵ
     */
    public void processAction(int key);

    /**
     * �������ʶ������¼����Ͷ����Common events=int[]{event,px,py}
     * @param events
     */
    public void processEvent(int[][] events);

    /**
     * �ж��Ƿ���ܶ���
     * @return �Ƿ���ܶ���
     */
    public boolean acceptAction();

    
    /**
     * ִ��
     * @param spendTime ִ���˶��ٺ���
     */
    public void process(int spendTime);

    /**
     * ����2Dͼ��
     * @param g
     * @param width
     * @param height
     */
    public void draw(Graphics g, int width, int height);

    
}
