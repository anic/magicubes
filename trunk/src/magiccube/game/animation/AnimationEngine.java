/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.animation;

import java.util.Vector;

/**
 * ��������
 * @author Anic
 */
public class AnimationEngine {

    /**
     * ��������
     */
    protected class AnimationItem {

        IAnimation animation;   //���󣬿��Զ����Ķ���
        int total;      //��ʼ
        int delay;      //�ӳ�
        int current;    //��ǰ

        /**
         * ������������
         * @param ia �ɶ������Ķ���
         * @param total ������ʱ��
         * @param delay �ӳ�ʱ��
         */
        public AnimationItem(IAnimation ia, int total, int delay) {
            this.total = total;
            this.current = 0;
            this.delay = delay;
            this.animation = ia;
        }
    }

    private Vector animations;

    /**
     * ��ʾ������ѭ�����ŵ�
     */
    public static final int LOOP_TIME = -1;

    /**
     * ������������
     */
    public AnimationEngine() {
        animations = new Vector();
    }

    private int getCount() {
        return animations.size();
    }

    /**
     * ��ʱ��ļӷ�
     * @param aniItem ��������
     * @param spendFrame �������¼�
     * @return ����������ʱ��
     */
    private int addFrames(AnimationItem aniItem, int spendFrame) {

        if (aniItem.delay > 0) //������ӳ�
        {
            if (spendFrame < aniItem.delay) {
                aniItem.delay -= spendFrame;
                return 0;
            } else {
                int tempResult = spendFrame - aniItem.delay;
                aniItem.delay = 0;
                return tempResult;
            }
        }

        if (aniItem.total == LOOP_TIME) {
            return spendFrame;
        }

        if (aniItem.current + spendFrame > aniItem.total) {
            int temp = aniItem.total - aniItem.current;
            aniItem.current = aniItem.total;
            return temp;
        } else {
            aniItem.current += spendFrame;
            return spendFrame;
        }
    }

    
    /**
     * ��Ӷ���
     * @param animation �ɶ���������
     * @param total ����ʱ�䣻���LOOP_TIME,���ʾѭ��
     * @param delay �ӳ�ʱ��
     */
    public void addAnimation(IAnimation animation, int total, int delay) {
        animations.addElement(new AnimationItem(animation, total, delay));
        animation.before_start(total);
    
    }

    
    /**
     * �ж��Ƿ����˶�
     * @return �Ƿ����˶�
     */
    public boolean isMoving() {
        return this.getCount() > 0;

    }

    /**
     * ������ж���
     */
    public void clearAnimations()
    {
        animations.removeAllElements();
    }

    /**
     * ����
     * @param spendFrame ���ϴ�����ʱ��
     */
    public void process(int spendFrame) {

        int size = animations.size();
        Vector toBeRemoved = new Vector();
        for(int i=0;i<size;++i)
        {
            AnimationItem cur = (AnimationItem)animations.elementAt(i);
            int realSpendFrame = addFrames(cur, spendFrame);
            if (realSpendFrame > 0) {
                cur.animation.process(realSpendFrame, cur.current);
            }
        }

        for(int i=0;i<size;++i)
        {
            AnimationItem cur = (AnimationItem)animations.elementAt(i);
            if (cur.total != LOOP_TIME && cur.current == cur.total) //�Ѿ�ִ����
            {
                cur.animation.before_end();
                toBeRemoved.addElement(cur);
            }
        }

        size = toBeRemoved.size();
        for(int i=0;i<size;++i)
        {
            AnimationItem cur = (AnimationItem)toBeRemoved.elementAt(i);
            animations.removeElement(cur);
        }
    }
}
