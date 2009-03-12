/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccube.ui;

import magiccube.game.animation.IAnimation;

/**
 *
 * @author Administrator
 */
public class ButtonAnimation implements IAnimation{

    public void before_end() {
        this.button.setSelected(false);
    }

    public void before_start(int totalTime) {
        this.button.setSelected(true);
    }

    public void process(int spendTime, int currentTime) {

    }

    private AbstractButton button;
    public ButtonAnimation(AbstractButton btn)
    {
        this.button = btn;
    }
}
