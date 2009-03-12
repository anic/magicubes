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
     * ����ǰ�����任ǰ����
     * @param total ��ʱ��
     */
    public void beforeEntering(int total);

    /**
     * ������ɺ�
     */
    public void afterEntering();


    /**
     * �뿪ǰ�����任ǰ����
     * @param total ��ʱ��
     */
    public void beforeLeaving(int total);

    public void afterLeaving();

    public void processEntering(int spendTime, int currentTime);

    public void processLeaving(int spendTime, int currentTime);
}
