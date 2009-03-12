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
     * �¼��ص��ӿ�
     */
    public interface IEventListener {

        /**
         * �ص��ӿ�
         * @param layer �ص��Ĳ����
         * @param index �ڼ�����ť������
         */
        public void onMessageLayerConfirm(MessageBoxLayer layer, int index);
    }
    /**
     * �����ı�
     */
    protected String text;
    /**
     * �����ı�
     */
    protected String title;
    /**
     * ѡ�а�ť���±�
     */
    protected int selected_index;
    /**
     * ��ť�ĸ���
     */
    protected int selected_length;
    /**
     * �����¼�����
     */
    protected IEventListener callback;
    /**
     * ���ĸ߶�
     */
    protected int height_content;
    /**
     * ����߶�
     */
    protected int height_title;

    //private static final int BUTTON_WIDTH = Common.FONT_DEFAULT.charsWidth(new String("ȷ��").toCharArray(), 0, 2) + 5;
    /**
     * ��ť���
     */
    protected static final int BUTTON_WIDTH = 50;
    /**
     * ��ť�߶�
     */
    protected static final int BUTTON_HEIGHT = (int) (Common.FONT_DEFAULT.getHeight() * 1.5f);
    /**
     * ������������
     */
    protected static final Font FONT_TITLE = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    /**
     * ��ť����
     */
    protected AbstractButton[] buttons;

    /**
     * ��������
     * @param width ���
     * @param height �߶�
     * @param text ����
     * @param title ����
     * @param btnText ��ť����
     * @param callback �ص��¼�
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
     * ����Ĭ�϶���
     * @param width ���
     * @param height �߶�
     * @param text ����
     */
    public MessageBoxLayer(int width, int height, String text) {
        this(width, height, text, "", new String[]{"ȷ��"}, null);
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
     * �ж��Ƿ��б���
     * @return �Ƿ��б���
     */
    public boolean hasTitle() {
        return !this.title.equals("");
    }

    public void draw(Graphics g) {

        getUpperLayer().draw(g);

        int aWidth = this.width - 1; //��ʾ����
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
     * ���ư�ť
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
     * ���Ʊ���
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
     * ��������
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

        //����
        g.drawString(text, x + width / 2 - w / 2, y + height / 2 - h / 2, 0);
        g.drawRect(x, y, width, height);
    }

    /**
     * ����ѡ�а�ť�����±�
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
