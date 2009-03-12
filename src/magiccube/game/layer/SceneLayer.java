/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import magiccube.game.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import magiccube.game.animation.AnimationEngine;
import magiccube.ui.ArrowButton;
import magiccube.ui.ButtonAnimation;
import magiccube.ui.AbstractButton;
import magiccube.ui.TextButton;

/**
 *
 * @author Administrator
 */
public class SceneLayer extends GLayer implements AbstractButton.IEventListener {

    public static final int INDEX_UP = 0;
    public static final int INDEX_DOWN = 1;
    public static final int INDEX_LEFT = 2;
    public static final int INDEX_RIGHT = 3;
    public static final int INDEX_7 = 4;
    public static final int INDEX_9 = 5;
    public static final int INDEX_5 = 6;
    public static final int DEFAULT_SHOW_ARROW_TIME = 300;
    private GameEngine game;
    private int[] keys = new int[]{
        GameCanvas.UP,
        GameCanvas.DOWN,
        GameCanvas.LEFT,
        GameCanvas.RIGHT,
        GameCanvas.KEY_NUM7,
        GameCanvas.KEY_NUM9,
        GameCanvas.FIRE
    };
    private AbstractButton[] buttons;

    public SceneLayer(GameEngine game) {
        super(game.getCanvas().getWidth(), game.getCanvas().getHeight());
        this.game = game;

        initButton();
    }

    private void initButton() {
        buttons = new AbstractButton[7];
        buttons[INDEX_UP] = new ArrowButton(width / 2 - ArrowButton.ARROW_WIDTH / 2, 0, ArrowButton.ARROW_UP, this);
        buttons[INDEX_DOWN] = new ArrowButton(width / 2 - ArrowButton.ARROW_WIDTH / 2, height - ArrowButton.ARROW_HEIGHT - 1, ArrowButton.ARROW_DOWN, this);
        buttons[INDEX_LEFT] = new ArrowButton(0, height / 2 - ArrowButton.ARROW_HEIGHT / 2, ArrowButton.ARROW_LEFT, this);
        buttons[INDEX_RIGHT] = new ArrowButton(width - ArrowButton.ARROW_WIDTH - 1, height / 2 - ArrowButton.ARROW_HEIGHT / 2, ArrowButton.ARROW_RIGHT, this);

        int space = 5;
        int x = space;
        int y = height - Common.HEIGHT_ICON - space;
        buttons[INDEX_7] = new TextButton(x, y, Common.WIDTH_ICON, Common.HEIGHT_ICON, "7", Common.FONT_DEFAULT, this);
        x += Common.WIDTH_ICON + space;
        buttons[INDEX_9] = new TextButton(x, y, Common.WIDTH_ICON, Common.HEIGHT_ICON, "9", Common.FONT_DEFAULT, this);
        x += Common.WIDTH_ICON + space;
        buttons[INDEX_5] = new TextButton(x, y, Common.WIDTH_ICON, Common.HEIGHT_ICON, "5", Common.FONT_DEFAULT, this);

    }

    public boolean acceptKey(int key) {
        return false;
    }

    public void draw(Graphics g) {

        //绘制箭头和完成度
        g.setColor(Common.COLOR_TEXT_ON_SCREEN);
        g.drawString(game.getGameScene().getCubeEngine().countSameColorFaces() + " / 6", 0, 0, 0);

        //绘制上下箭头
        buttons[INDEX_UP].draw(g);
        buttons[INDEX_DOWN].draw(g);

        if (game.getGameScene().canRotRight()) {
            //绘制左右
            buttons[INDEX_LEFT].draw(g);
            buttons[INDEX_RIGHT].draw(g);
        }

        buttons[INDEX_7].draw(g);
        buttons[INDEX_9].draw(g);
        buttons[INDEX_5].draw(g);


    }

    public void acceptPointers(int[][] pointers) {
        AbstractButton[] btnArray1 = new AbstractButton[]{buttons[INDEX_UP], buttons[INDEX_DOWN]};
        AbstractButton[] btnArray2 = new AbstractButton[]{buttons[INDEX_LEFT], buttons[INDEX_RIGHT]};
        AbstractButton[] btnArray3 = new AbstractButton[]{buttons[INDEX_7], buttons[INDEX_9], buttons[INDEX_5]};
        for (int i = 0; i < pointers.length; ++i) {
            handlePointers(pointers[i], btnArray1);

            if (game.getGameScene().canRotRight()) {
                handlePointers(pointers[i], btnArray2);
            }

            handlePointers(pointers[i], btnArray3);
        }
    }

    public void onButtonClick(AbstractButton source) {
        for (int i = 0; i < buttons.length; ++i) {
            if (source == buttons[i]) {
                game.getCommand().push(keys[i]);
            }
        }
    }

    public void onSelectedChanged(AbstractButton source) {
        AbstractButton[] btnArray3 = new AbstractButton[]{buttons[INDEX_7], buttons[INDEX_9], buttons[INDEX_5]};
        handleSelection(btnArray3, source);
    }

    public void setLastDirection(int dir, AnimationEngine ae) {
        ae.addAnimation(new ButtonAnimation(buttons[dir]), SceneLayer.DEFAULT_SHOW_ARROW_TIME, 0);
    }
}
