/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import magiccube.game.reader.GPage;
import magiccube.game.reader.GReader;
import magiccube.game.*;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class ReaderLayer extends GLayer {

    //GPage page;
    private GReader reader;
    private static final int DISTANCE_X = 3;
    private static final int DISTANCE_Y = 3;
    private Image[] menus;
    private int menu_icon_width,  menu_icon_height;
    private static final int INDEX_SHOW = 0;
    private static final int INDEX_EXIT = 1;
    private static final int INDEX_VIEW = 2;
    private static final int INDEX_MAX = 3;
    private static final int INDEX_PREVIOUS = 4;
    private static final int INDEX_NEXT = 5;
    private int space = 2;
    private int selected_index = 0;
    private String[] tips;
    public static final int STATUS_SHOW = 0;    //仅仅是看，没有任何动作
    public static final int STATUS_HIDE = 1;    //不显示，不捕获动作，应该不存在这个的
    public static final int STATUS_VIEW = 2;    //看内容，上下左右控制内容的左右
    public static final int STATUS_MENU = 3;    //菜单，上下左右控制菜单。
    private int status = STATUS_MENU;
    private LayerManager layerManager;
    private int frame_width,  frame_height;
    private int screenWidth,  screenHeight;

    //应用于setmode的参数
    public static final int MODE_NORMAL = 0;
    public static final int MODE_MAX = 1;
    private int layer_mode; //模式
    private boolean bMaxOnly; //是否只能最大化

    public ReaderLayer(String file, GameEngine game, LayerManager layerManager, int mode, int width, int height, int frame_width, int frame_height) {
        this(file, game, layerManager, mode, width, height, frame_width, frame_height, false);
    }

    public ReaderLayer(String file, GameEngine game, LayerManager layerManager, int mode, int width, int height, int frame_width, int frame_height, boolean maxOnly) {
        super(width, height);
        this.layerManager = layerManager;
        this.bMaxOnly = maxOnly;

        menus = new Image[6];
        menus[INDEX_SHOW] = game.getResourceLoader().getImage(ResourceLoader.IMAGE_TUTORIAL_DOCK);
        menus[INDEX_EXIT] = game.getResourceLoader().getImage(ResourceLoader.IMAGE_TUTORIAL_EXIT);
        menus[INDEX_VIEW] = game.getResourceLoader().getImage(ResourceLoader.IMAGE_TUTORIAL_VIEW);
        menus[INDEX_MAX] = game.getResourceLoader().getImage(ResourceLoader.IMAGE_TUTORIAL_MAX);
        menus[INDEX_PREVIOUS] = game.getResourceLoader().getImage(ResourceLoader.IMAGE_TUTORIAL_PREVIOUS);
        menus[INDEX_NEXT] = game.getResourceLoader().getImage(ResourceLoader.IMAGE_TUTORIAL_NEXT);
        menu_icon_width = menus[0].getWidth();
        menu_icon_height = menus[0].getHeight();

        tips = new String[menus.length];
        tips[INDEX_SHOW] = "浮动";
        tips[INDEX_EXIT] = "隐藏";
        tips[INDEX_VIEW] = "查看";
        tips[INDEX_MAX] = "切换模式";
        tips[INDEX_PREVIOUS] = "上一页";
        tips[INDEX_NEXT] = "下一页";

        this.frame_width = frame_width;
        this.frame_height = frame_height;

        this.screenWidth = game.getCanvas().getWidth();
        this.screenHeight = game.getCanvas().getHeight();
        this.layer_mode = mode;

        this.setMode(layer_mode);

        reader = new GReader();
        reader.load(file, frame_width, frame_height);
    }

    public boolean acceptKey(int key) {

        GPage page = reader.getCurrentPage();
        if (page == null) {
            return this.getUpperLayer().acceptKey(key);
        }

        if (key == GCanvas.KEY_RIGHT_MENU) {
            if (this.status != STATUS_MENU) {
                this.status = STATUS_MENU;
            } else if (this.status == STATUS_MENU) {
                this.status = STATUS_HIDE;
                layerManager.removeTopLayer();
            }

            return true;
        }

        if (this.status == STATUS_VIEW) {
            switch (key) {
                case GameCanvas.UP:
                    rollReaderDown(false);
                    break;

                case GameCanvas.DOWN:
                    rollReaderDown(true);
                    break;

                case GameCanvas.LEFT:
                    rollReaderLeft(true);
                    break;

                case GameCanvas.RIGHT:
                    rollReaderLeft(false);
                    break;

            }
            return true;
        } else if (status == STATUS_SHOW) {
            return this.getUpperLayer().acceptKey(key);
        } else if (status == STATUS_MENU) {
            switch (key) {
                case GameCanvas.LEFT:
                    this.selected_index = (this.selected_index - 1 + menus.length) % menus.length;
                    break;

                case GameCanvas.RIGHT:
                    this.selected_index = (this.selected_index + 1) % menus.length;
                    break;

                case GameCanvas.FIRE:
                    performAction();
                    break;

                case GameCanvas.UP:
                    rollReaderDown(false);
                    break;

                case GameCanvas.DOWN:
                    rollReaderDown(true);
                    break;

            }
            return true;
        } else {
            return true;
        }

    }

    public void draw(Graphics g) {

        if (layer_mode == MODE_NORMAL) {
            this.getUpperLayer().draw(g);
        }

        int defaultCol = g.getColor();
        Font defaultFont = g.getFont();

        int cur_x = width - frame_width - 2 * DISTANCE_X;
        int cur_y = height - frame_height - 2 * DISTANCE_Y;

        //绘制菜单
        if (this.status == STATUS_MENU) {
            drawMenu(g, cur_x, cur_y - this.menu_icon_height, frame_width + 2 * DISTANCE_X, this.menu_icon_height);
        } else {
            drawShortMenu(g, cur_x, cur_y - this.menu_icon_height, frame_width + 2 * DISTANCE_X, this.menu_icon_height);
        }


        //绘制框架
        drawFramework(g, cur_x, cur_y, frame_width + 2 * DISTANCE_X - 1, frame_height + 2 * DISTANCE_Y - 1);

        cur_x += DISTANCE_X;
        cur_y += DISTANCE_Y;

        //绘制内容
        drawContent(g, cur_x, cur_y, frame_width, frame_height);

        g.setColor(defaultCol);
        g.setFont(defaultFont);
    }

    private void drawContent(Graphics g, int x, int y, int width, int height) {
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipWidth = g.getClipWidth();
        int clipHeight = g.getClipHeight();

        //锁定绘制区域
        g.setClip(x, y, width, height);

        g.setFont(Common.FONT_DEFAULT);
        g.setColor(0);

        GPage page = reader.getCurrentPage();
        if (page != null) {
            page.draw(g, x, y, width, height);
        }

        //复原绘制区域
        g.setClip(clipX, clipY, clipWidth, clipHeight);
    }

    private void drawShortMenu(Graphics g, int x, int y, int width, int height) {
        if (this.status == STATUS_VIEW) {
            DrawingUtil.drawMenuIcon(g, x + width - menu_icon_width, y, menu_icon_width, menu_icon_height, Common.COLOR_BAR, Common.COLOR_SELECTED, menus[INDEX_VIEW]);
            DrawingUtil.drawMenuIcon(g, x + width - 2 * menu_icon_width - space, y, menu_icon_width, menu_icon_height, Common.COLOR_BAR, Common.COLOR_SELECTED, "3", Common.FONT_DEFAULT);
        } else if (this.status == STATUS_SHOW) {
            DrawingUtil.drawMenuIcon(g, x + width - menu_icon_width, y, menu_icon_width, menu_icon_height, Common.COLOR_BAR, Common.COLOR_SELECTED, menus[INDEX_SHOW]);
            DrawingUtil.drawMenuIcon(g, x + width - 2 * menu_icon_width - space, y, menu_icon_width, menu_icon_height, Common.COLOR_BAR, Common.COLOR_SELECTED, "3", Common.FONT_DEFAULT);
        }

    }

    private void drawTextTip(Graphics g, int x, int y, int width, int height) {

        if (selected_index == INDEX_PREVIOUS || selected_index == INDEX_NEXT) {
            DrawingUtil.drawMenuIcon(g, x, y, width, height, Common.COLOR_BAR, 0, tips[selected_index] + " " + (reader.getCurrentIndex() + 1) + "/" + reader.getPageCount(), Common.FONT_DEFAULT);
        } else {
            DrawingUtil.drawMenuIcon(g, x, y, width, height, Common.COLOR_BAR, 0, tips[selected_index], Common.FONT_DEFAULT);
        }

    }

    private void drawMenu(Graphics g, int x, int y, int width, int height) {

        int tipX = 0, tipY = 0, tipWidth = 0;
        if (layer_mode == MODE_NORMAL) {
            tipWidth = menus.length * menu_icon_width + space * (menus.length - 1);
            tipY = y - menu_icon_height - space;
            tipX = x;
        } else if (layer_mode == MODE_MAX) {

            tipX = x + (menu_icon_width + space) * menus.length;
            tipY = y;
            tipWidth = width - tipX - 1;
        }

        drawTextTip(g, tipX, tipY, tipWidth, menu_icon_height);



        for (int i = 0; i <
                menus.length; ++i) {
            if (i == selected_index) {
                DrawingUtil.drawMenuIcon(g, x, y, menu_icon_width, menu_icon_height, Common.COLOR_BAR, Common.COLOR_SELECTED, menus[i]);
            } else {
                DrawingUtil.drawMenuIcon(g, x, y, menu_icon_width, menu_icon_height, Common.COLOR_BAR, Common.COLOR_SCREEN, menus[i]);
            }

            x += menu_icon_width + space;
        }

    }

    private void drawFramework(Graphics g, int x, int y, int width, int height) {
        g.setColor(Common.COLOR_BAR);
        g.fillRect(x, y, width, height);

        g.setColor(Common.COLOR_SELECTED);
        g.drawRect(x, y, width, height);
    }

    private int rangeAdd(int current, int distance, int min, int max) {
        int target = current + distance;
        if (target < min) {
            return min;
        } else if (target > max) {
            return max;
        } else {
            return target;
        }

    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    private void performAction() {
        switch (selected_index) {
            case INDEX_SHOW:
                if (!bMaxOnly) {
                    this.status = STATUS_SHOW;
                }
                break;
            case INDEX_EXIT:
                layerManager.removeTopLayer();
                this.status = STATUS_HIDE;
                break;
            case INDEX_VIEW:
                this.status = STATUS_VIEW;
                break;

            case INDEX_PREVIOUS:
                this.reader.previousPage();
                break;

            case INDEX_NEXT:
                this.reader.nextPage();
                break;

            case INDEX_MAX:
                if (layer_mode == MODE_NORMAL) {
                    this.setMode(MODE_MAX);
                } else {
                    this.setMode(MODE_NORMAL);
                }
                break;
        }

    }

    private void rollReaderDown(boolean down) {
        GPage page = reader.getCurrentPage();
        int dis = frame_height / 3;
        int pageX, pageY, pageHeight;
        pageX =
                page.getX();
        pageY =
                page.getY();
        pageHeight =
                page.getHeight() - frame_height > 0 ? page.getHeight() - frame_height : 0;

        if (down) {
            page.setPosition(pageX, rangeAdd(pageY, dis, 0, pageHeight));
        } else {
            page.setPosition(pageX, rangeAdd(pageY, -dis, 0, pageHeight));
        }

    }

    private void rollReaderLeft(boolean left) {
        GPage page = reader.getCurrentPage();
        int dis = frame_width / 3;
        int pageX, pageY, pageWidth;
        pageX = page.getX();
        pageY = page.getY();
        pageWidth = page.getWidth() - frame_width > 0 ? page.getWidth() - frame_width : 0;

        if (left) {
            page.setPosition(rangeAdd(pageX, -dis, 0, pageWidth), pageY);
        } else {
            page.setPosition(rangeAdd(pageX, dis, 0, pageWidth), pageY);
        }

    }

    private void setMode(int mode) {
        if (bMaxOnly) {
            this.layer_mode = MODE_MAX; //只允许最大化
        } else {
            this.layer_mode = mode;
        }

        if (this.layer_mode == MODE_NORMAL) {
            this.frame_width = Common.TUTORIAL_WIDTH;
            this.frame_height = Common.TUTORIAL_HEIGHT;
        } else if (this.layer_mode == MODE_MAX) {
            this.frame_width = screenWidth - 2 * DISTANCE_X;
            this.frame_height = screenHeight - space - this.menu_icon_height - 2 * DISTANCE_Y;
        }
    }

    public void acceptPointers(int[][] pointers) {
    }
}
