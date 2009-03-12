/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.layer;

import magiccube.game.*;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import magiccube.ui.AbstractButton;
import magiccube.ui.TextButton;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class MessageBoxLayer extends GLayer implements AbstractButton.IEventListener {

    /**
     * 事件回调接口
     */
    public interface IEventListener {

        /**
         * 回调接口
         * @param layer 回调的层对象
         * @param index 第几个按钮被触发
         */
        public void onMessageLayerConfirm(MessageBoxLayer layer, int index);
    }
    /**
     * 内容文本
     */
    protected String text;
    /**
     * 标题文本
     */
    protected String title;
    /**
     * 选中按钮的下表
     */
    protected int selected_index;
    /**
     * 按钮的个数
     */
    protected int selected_length;
    /**
     * 监听事件对象
     */
    protected IEventListener callback;
    /**
     * 正文高度
     */
    protected int height_content;
    /**
     * 标题高度
     */
    protected int height_title;

    //private static final int BUTTON_WIDTH = Common.FONT_DEFAULT.charsWidth(new String("确定").toCharArray(), 0, 2) + 5;
    /**
     * 按钮宽度
     */
    protected static final int BUTTON_WIDTH = 50;
    /**
     * 按钮高度
     */
    protected static final int BUTTON_HEIGHT = (int) (Common.FONT_DEFAULT.getHeight() * 1.5f);
    /**
     * 标题文字字体
     */
    protected static final Font FONT_TITLE = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    /**
     * 按钮对象
     */
    protected AbstractButton[] buttons;

    /**
     * 创建对象
     * @param width 宽度
     * @param height 高度
     * @param text 正文
     * @param title 标题
     * @param btnText 按钮文字
     * @param callback 回调事件
     */
    public MessageBoxLayer(int width, int height, String text, String title, String[] btnText, IEventListener callback) {
        super(width, height);
        this.text = text;
        this.title = title;
        this.callback = callback;
        this.height_content = (int) (Common.FONT_DEFAULT.getHeight() * 2.5f);
        this.height_title = (title.equals("")) ? 0 : (int) (FONT_TITLE.getHeight() * 1.3f);

        this.buttons = new AbstractButton[btnText.length];
        this.selected_length = buttons.length;
        initButtons(btnText);
    }

    private void initButtons(String[] btnText) {
        int height_confirm = BUTTON_HEIGHT;
        int height_Message = this.height_content + this.height_title + height_confirm;
        int space = 10;
        int total_w = buttons.length * BUTTON_WIDTH + (buttons.length - 1) * space;
        int bx = width / 2 - total_w / 2;
        int by = height / 2 + height_Message / 2 - height_confirm + 1;
        for (int i = 0; i < buttons.length; ++i) {
            buttons[i] = new TextButton(bx, by, BUTTON_WIDTH, BUTTON_HEIGHT, btnText[i], Common.FONT_DEFAULT, this);
            bx += BUTTON_WIDTH + space;
        }
        selected_index = 0;
        if (buttons.length > 0) {
            buttons[0].setSelected(true);
        }
    }

    /**
     * 创建默认对象
     * @param width 宽度
     * @param height 高度
     * @param text 正文
     */
    public MessageBoxLayer(int width, int height, String text) {
        this(width, height, text, "", new String[]{"确定"}, null);
    }

    public boolean acceptKey(int key) {
        switch (key) {
            case GameCanvas.RIGHT:
                this.setSelectedIndex((selected_index + 1) % selected_length);
                break;
            case GameCanvas.LEFT:
                this.setSelectedIndex((selected_index - 1 + selected_length) % selected_length);
                break;
            case GameCanvas.FIRE:
                performAction(selected_index);
                break;
        }
        return true;
    }

    /**
     *
     * @param index
     */
    protected void performAction(int index) {
        if (callback != null) {
            callback.onMessageLayerConfirm(this, index);
        }
    }

    /**
     * 判断是否有标题
     * @return 是否有标题
     */
    public boolean hasTitle() {
        return !this.title.equals("");
    }

    public void draw(Graphics g) {

        getUpperLayer().draw(g);

        int aWidth = this.width - 1; //显示边线
        int height_confirm = BUTTON_HEIGHT;
        int height_Message = this.height_content + this.height_title + height_confirm;
        int y = height / 2 - height_Message / 2;
        if (hasTitle()) {
            drawTitle(g, 0, y, aWidth, height_title, Common.COLOR_BAR, Common.COLOR_SELECTED, title, FONT_TITLE);
        }
        y += height_title;
        drawContent(g, 0, y, aWidth, this.height_content, Common.COLOR_BAR, Common.COLOR_SCREEN, this.text, Common.FONT_DEFAULT);
        y += height_content;
        drawButtons(g, 0, y, aWidth, height_confirm);
    }

    /**
     * 绘制按钮
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     */
    protected void drawButtons(Graphics g, int x, int y, int width, int height) {
        DrawingUtil.drawMenuIcon(g, x, y, width, height, Common.COLOR_BAR, Common.COLOR_SCREEN, "", Common.FONT_DEFAULT);

        for (int i = 0; i < buttons.length; ++i) {
            buttons[i].draw(g);

        }
    }

    /**
     * 绘制标题
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     * @param barColor
     * @param bgColor
     * @param text
     * @param font
     */
    protected void drawTitle(Graphics g, int x, int y, int width, int height, int barColor, int bgColor, String text, Font font) {
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);

        //g.setColor(255, 255, 255);
        g.setColor(barColor);
        g.setFont(font);
        int h = font.getHeight();
        g.drawString(text, x + 2, y + height / 2 - h / 2, 0);
        g.drawRect(x, y, width, height);
    }

    /**
     * 绘制正文
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     * @param barColor
     * @param bgColor
     * @param text
     * @param font
     */
    protected void drawContent(Graphics g, int x, int y, int width, int height, int barColor, int bgColor, String text, Font font) {
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);

        //g.setColor(255, 255, 255);
        g.setColor(barColor);
        g.setFont(font);
        int w = font.charsWidth(text.toCharArray(), 0, text.length());
        int h = font.getHeight();

        //居中
        g.drawString(text, x + width / 2 - w / 2, y + height / 2 - h / 2, 0);
        g.drawRect(x, y, width, height);
    }

    /**
     * 设置选中按钮的下下标
     * @param index
     */
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < this.selected_length) {
            buttons[index].setSelected(true);
        }
    }

    public void acceptPointers(int[][] pointers) {
        for (int i = 0; i < pointers.length; ++i) {
            if (handlePointers(pointers[i], buttons)) {
                break;
            } else {
                this.getUpperLayer().acceptPointers(pointers);
            }
        }
    }

    public void onButtonClick(AbstractButton source) {
        for (int i = 0; i < buttons.length; ++i) {
            if (source == buttons[i]) {
                performAction(i);
                return;
            }
        }
    }

    public void onSelectedChanged(AbstractButton source) {
        selected_index = handleSelection(buttons, source);
    }
}
