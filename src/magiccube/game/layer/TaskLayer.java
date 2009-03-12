/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import magiccube.game.*;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import magiccube.ui.ArrowButton;
import magiccube.ui.AbstractButton;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class TaskLayer extends MessageBoxLayer implements AbstractButton.IEventListener {

    private static final int INDEX_OK = 0;
    private static final int INDEX_CANCEL = 1;
    private static final int INDEX_CHOOSE_TASK_LEFT = 2;
    private static final int INDEX_CHOOSE_TASK_RIGHT = 3;
    private GameEngine game;
    private int dis_y = 5;
    private int dis_x = 10;
    private int text_x = 5;
    private int w = 50;

    public TaskLayer(GameEngine game, IEventListener callback) {
        super(game.getCanvas().getWidth(), game.getCanvas().getHeight(), "", "挑战任务", new String[]{"确定", "取消"}, callback);

        this.game = game;

        int new_height_content = 100;
        //修改按鈕高度
        for (int i = 0; i < buttons.length; ++i) {
            buttons[i].setLoacation(buttons[i].getX(), buttons[i].getY() + (new_height_content - height_content) / 2);
        }
        this.height_content = new_height_content;

        initButtons();
    }

    private void initButtons() {
        AbstractButton[] newButtons = new AbstractButton[4];
        newButtons[INDEX_OK] = buttons[INDEX_OK];
        newButtons[INDEX_CANCEL] = buttons[INDEX_CANCEL];

        int total = height_title + height_content + BUTTON_HEIGHT;
        int y = height / 2 - total / 2 + 1;
        int x = text_x - 1;
        int text_height = Common.FONT_DEFAULT.getHeight();

        y += dis_y;
        y += dis_y + text_height;
        y += dis_y + text_height;
        y += dis_y + text_height;

        newButtons[INDEX_CHOOSE_TASK_LEFT] = new ArrowButton(x + width / 2 - w / 2 - ArrowButton.ARROW_WIDTH - dis_x, y, ArrowButton.ARROW_LEFT, this);
        newButtons[INDEX_CHOOSE_TASK_RIGHT] = new ArrowButton(x + width / 2 + w / 2 + dis_x, y, ArrowButton.ARROW_RIGHT, this);
        buttons = newButtons;
        selected_length = buttons.length;
    }

    public boolean acceptKey(int key) {
        switch (key) {
            case GameCanvas.RIGHT:

                if (selected_index == INDEX_OK) {
                    this.setSelectedIndex(INDEX_CANCEL);
                } else if (selected_index == INDEX_CHOOSE_TASK_LEFT) {
                    this.setSelectedIndex(INDEX_CHOOSE_TASK_RIGHT);
                } else if (selected_index == INDEX_CHOOSE_TASK_RIGHT) {
                    performAction(selected_index);
                }
                break;
            case GameCanvas.LEFT:
                if (selected_index == INDEX_CANCEL) {
                    this.setSelectedIndex(INDEX_OK);
                } else if (selected_index == INDEX_CHOOSE_TASK_RIGHT) {
                    this.setSelectedIndex(INDEX_CHOOSE_TASK_LEFT);
                } else if (selected_index == INDEX_CHOOSE_TASK_LEFT) {
                    performAction(selected_index);
                }
                break;
            case GameCanvas.UP:
                if (selected_index == INDEX_OK) {
                    this.setSelectedIndex(INDEX_CHOOSE_TASK_LEFT);
                } else if (selected_index == INDEX_CANCEL) {
                    this.setSelectedIndex(INDEX_CHOOSE_TASK_RIGHT);
                }
                break;
            case GameCanvas.DOWN:
                if (selected_index == INDEX_CHOOSE_TASK_LEFT) {
                    this.setSelectedIndex(INDEX_OK);
                } else if (selected_index == INDEX_CHOOSE_TASK_RIGHT) {
                    this.setSelectedIndex(INDEX_CANCEL);
                }
                break;
            case GameCanvas.FIRE:
                performAction(selected_index);
                break;
        }
        return true;
    }

    protected void performAction(int index) {
        switch (index) {
            case INDEX_OK:
            case INDEX_CANCEL:
                if (callback != null) {
                    callback.onMessageLayerConfirm(this, index);
                }
                break;
            case INDEX_CHOOSE_TASK_LEFT:
                game.getTaskLoader().nextTask(false);
                break;
            case INDEX_CHOOSE_TASK_RIGHT:
                game.getTaskLoader().nextTask(true);
                break;
        }
    }

    protected void drawContent(Graphics g, int x, int y, int width, int height, int barColor, int bgColor, String text, Font font) {

        super.drawContent(g, x, y, width, height, barColor, bgColor, text, font);

        int text_height = font.getHeight();

        y += dis_y;
        x += text_x;

        g.setFont(font);
        int total = game.getTaskLoader().getTaskCount();
        int finish = game.getTaskLoader().getFinishCount();

        g.drawString("你已经完成任务 " + finish + "/" + total, x, y, 0);
        y += dis_y + text_height;

        int currentIndex = game.getTaskLoader().getCurrentIndex() + 1; //显示1-10好看

        GTask task = game.getTaskLoader().getCurrentTask();
        if (task != null) {
            g.drawString("选择任务：", x, y, 0);
            g.drawString(task.getDescription(), x, y + 2 * dis_y + text_height + ArrowButton.ARROW_WIDTH, 0);
        }

        y += dis_y + text_height;

        String str = currentIndex + "/" + total;
        DrawingUtil.drawMenuIcon(g, x + width / 2 - w / 2, y, w, ArrowButton.ARROW_WIDTH, barColor, bgColor, str, font);


    }

    public void onButtonClick(AbstractButton source) {
        for (int i = 0; i < buttons.length; ++i) {
            if (buttons[i] == source) {
                performAction(i);
                return;
            }
        }
    }
}
