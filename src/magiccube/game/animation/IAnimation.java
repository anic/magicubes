package magiccube.game.animation;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * �ɶ������Ľӿ�
 * @author Anic
 */
public interface IAnimation {

    /**
     * ����������
     * @param spendTime ���ϴε���֮�侭����ʱ�䣬����
     * @param currentTime �ӿ�ʼ�����ھ�����ʱ��ͣ�����
     */
    public void process(int spendTime,int currentTime);

    /**
     * ������ʼǰ
     * @param totalTime �������Ǽ�
     */
    public void before_start(int totalTime);

    /**
     * ��������ǰ
     */
    public void before_end();

}
