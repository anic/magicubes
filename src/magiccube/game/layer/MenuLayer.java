/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import magiccube.game.*;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import magiccube.ui.AbstractButton;
import magiccube.ui.DrawerButton;
import magiccube.ui.TextButton;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class MenuLayer extends GLayer implements MessageBoxLayer.IEventListener, AbstractButton.IEventListener {

    private AbstractButton[] menus;
    private AbstractButton[] tip_menus;
    private String[] tips;
    private int menu_icon_width,  menu_icon_height;
    private static final int space = 7;
    private int selected_index = 0;
    private static final int INDEX_RESUME = 0;
    private static final int INDEX_BACK = 1;
    private static final int INDEX_REVERSE = 2;
    private static final int INDEX_TUTORIAL = 3;
    private static final int INDEX_TASK = 4;
    private static final int INDEX_SAVE = 5;
    private static final int INDEX_CAMERA = 6;
    private static final int INDEX_SHOW_MENU = 0;
    private static final int INDEX_SHOW_TASK = 1;
    private GameEngine game;
    private int max_tip_width;
    private boolean bShowMenu;

    private class SavingThread implements Runnable {

        public void run() {
            game.save();
        }
    }

    public MenuLayer(GameEngine g) {
        super(g.getCanvas().getWidth(), g.getCanvas().getHeight());

        this.game = g;
        initButtons(g.getResourceLoader());
        initTips();
        setShowable(false);
    }

    private void initTips() {
        //初始化宽度一次
        tips = new String[menus.length];
        tips[INDEX_RESUME] = "继续游戏(1)";
        tips[INDEX_BACK] = "回主菜单";
        tips[INDEX_REVERSE] = "撤销上步(#)";
        tips[INDEX_TUTORIAL] = "显示教程(右)";
        tips[INDEX_TASK] = "任务信息";
        tips[INDEX_SAVE] = "保存进度";
        tips[INDEX_CAMERA] = "重置镜头(0)";

        Font font = Common.FONT_DEFAULT;
        for (int i = 0; i < menus.length; ++i) {
            int w = font.charsWidth(tips[i].toCharArray(), 0, tips[i].length());
            max_tip_width = w > max_tip_width ? w : max_tip_width;
        }
        max_tip_width += 10;
    }

    private void initButtons(ResourceLoader loader) {

        Image firstImage = loader.getImage(ResourceLoader.IMAGE_MENU_ICON_RESUME);
        menu_icon_width = firstImage.getWidth();
        menu_icon_height = firstImage.getHeight();

        int x = width - menu_icon_width - space;
        int y = space;
        menus = new AbstractButton[7];
        tip_menus = new AbstractButton[2];

        tip_menus[INDEX_SHOW_MENU] = new TextButton(x, y, menu_icon_width, menu_icon_height, "1", Common.FONT_DEFAULT, this);
        menus[INDEX_RESUME] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_RESUME), this);
        y += menu_icon_height + space;
        tip_menus[INDEX_SHOW_TASK] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_TASK), this);
        menus[INDEX_BACK] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_BACK), this);
        y += menu_icon_height + space;
        menus[INDEX_REVERSE] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_REVERSE), this);
        y += menu_icon_height + space;
        menus[INDEX_TUTORIAL] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_HELP), this);
        y += menu_icon_height + space;
        menus[INDEX_TASK] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_TASK), this);
        y += menu_icon_height + space;
        menus[INDEX_SAVE] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_SAVE), this);
        y += menu_icon_height + space;
        menus[INDEX_CAMERA] = new DrawerButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_ICON_CAMERA), this);

    }

    public boolean acceptKey(int key) {

//        if (key == GCanvas.KEY_LEFT_MENU || key == GameCanvas.KEY_NUM1) {
        if (key == GCanvas.KEY_LEFT_MENU) {
            setShowable(!bShowMenu);
            return true;
        }

        if (!bShowMenu) //不是处于展开状态
        {
            return this.getUpperLayer().acceptKey(key);
        }

        switch (key) {
            case GameCanvas.UP:
                setSelectedMenu((selected_index + menus.length - 1) % menus.length);
                break;
            case GameCanvas.DOWN:
                setSelectedMenu((selected_index + 1) % menus.length);
                break;
            case GameCanvas.FIRE:
                performAction(selected_index);
                break;
            default:
                break;
        }
        //不允许下面获得键盘事件
        return true;
    }

    public void draw(Graphics g) {

        //绘制上层
        this.getUpperLayer().draw(g);

        int color = g.getColor();
        int style = g.getStrokeStyle();

        g.setFont(Common.FONT_DEFAULT);
        int x = width - menus[0].getWidth() - space;
        int y = space;

        if (bShowMenu) {
            //绘制提示字
            DrawingUtil.drawMenuIcon(g, x - max_tip_width - space, y, max_tip_width, menu_icon_height, Common.COLOR_BAR, Common.COLOR_SCREEN, tips[selected_index], Common.FONT_DEFAULT);
            //绘制按钮
            for (int i = 0; i < menus.length; ++i) {
                menus[i].draw(g);
            }
        } else {

            tip_menus[INDEX_SHOW_MENU].draw(g);
            //如果有任务
            if (game.inTask()) {
                tip_menus[INDEX_SHOW_TASK].draw(g);
            }

        }
        g.setColor(color);
        g.setStrokeStyle(style);
    }

    private void performAction(int index) {
        switch (index) {
            case INDEX_RESUME:
                setShowable(false);
                break;
            case INDEX_BACK:
                setShowable(false);
                game.switchScene();
                //game.switchScene(game.getScene(), game.getMenuScene());
                break;
            case INDEX_REVERSE:
                game.getGameScene().rollBackHistory();
                break;
            case INDEX_TUTORIAL:
                setShowable(false);
                game.getGameScene().showTutorial();
                break;
            case INDEX_TASK:
                GTask task = game.getGameTask();
                String content;
                MessageBoxLayer mb;
                if (task != null) {
                    content = task.getDescription();
                    mb = new MessageBoxLayer(this.width, this.height, content, "任务信息 任务" + (task.getId() + 1), new String[]{"确定"}, this);

                } else {
                    content = "当前游戏不在任务中";
                    mb = new MessageBoxLayer(this.width, this.height, content, "任务信息", new String[]{"确定"}, this);
                }
                game.getGameScene().getLayerManager().addLayer(mb);
                break;
            case INDEX_CAMERA:
                setShowable(false);
                game.getGameScene().resetCamera();
                break;
            case INDEX_SAVE:
                setShowable(false);
//                game.getGameScene().getLayerManager().addLayer(new SavingLayer(
//                        game,
//                        game.getGameScene().getLayerManager(),
//                        this.width,
//                        this.height));
                //game.save();
                game.getHistoryRecorder().clearHistory();
                TaskCanvas.run("保存中", new Runnable[]{new SavingThread()}, game.getMIDlet(), game.getCanvas());
                break;
        }
    }

    private void setShowable(boolean show) {
        bShowMenu = show;
        if (show) {
            for (int i = 0; i < tip_menus.length; ++i) {
                tip_menus[i].enableUpdate(false);
                tip_menus[i].setSelected(false);
                tip_menus[i].enableUpdate(true);
            }
            //顺序不能反
            menus[selected_index].setSelected(true);

        } else {
            for (int i = 0; i < tip_menus.length; ++i) {
                tip_menus[i].enableUpdate(false);
                tip_menus[i].setSelected(true);
                tip_menus[i].enableUpdate(true);
            }
        }
    }

    private void setSelectedMenu(int index) {
        menus[index].setSelected(true);
    }

    public void onMessageLayerConfirm(MessageBoxLayer layer, int result) {
        game.getGameScene().getLayerManager().removeTopLayer();
    }

    public void onButtonClick(AbstractButton source) {
        for (int i = 0; i < menus.length; ++i) {
            if (source == menus[i]) {
                performAction(i);
                return;
            }
        }

        if (source == tip_menus[INDEX_SHOW_MENU]) {
            this.setShowable(true);
        } else if (source == tip_menus[INDEX_SHOW_TASK] && game.inTask()) {
            performAction(INDEX_TASK);
        }
    }

    public void onSelectedChanged(AbstractButton source) {
        selected_index = handleSelection(menus, source);
    }

    public void acceptPointers(int[][] pointers) {
        for (int i = 0; i < pointers.length; ++i) {
            if (bShowMenu) {
                boolean result = handlePointers(pointers[i], menus);
                if (result) {
                    break;
                } else {
                    this.getUpperLayer().acceptPointers(pointers);
                }
            } else {
                boolean result = handlePointers(pointers[i], tip_menus);
                if (result) {
                    break;
                } else {
                    this.getUpperLayer().acceptPointers(pointers);
                }
            }
        }
    }
}
