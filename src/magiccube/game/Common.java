/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import javax.microedition.lcdui.Font;
import javax.microedition.m3g.Graphics3D;

/**
 * ����������
 * @author Anic
 */
public class Common {

    /**
     * ������Ϣ
     */
    public static String DEBUG_INFO;
    static {
        DEBUG_INFO = "";
    }
    public static final int TIME_SCENE_SWITCH = 800;
    public static final int TIME_CUBE_ROTATE = 500;
    public static final int TIME_INIT = 5000;
    /**
     * �̳���ʾ�Ŀ��
     */
    public static final int TUTORIAL_WIDTH = 125; //�̳���ʾ�Ŀ��
    /**
     * �̳���ʾ�ĸ߶�
     */
    public static final int TUTORIAL_HEIGHT = 100; //�̳���ʾ�ĸ߶�
    public static final int WORDS_PER_LINE = 20;
    /**
     * �Ķ����еĽű����壬�ָ���ź���һ�εı�־
     */
    public static final String SYMBOL_SPLIT = "\r\n";
    /**
     * �Ķ����еĽű����壬ͼƬ��־
     */
    public static final String PREFIX_IMAGE = "<img>";
    /**
     * �Ķ����еĽű����壬��ҳ��־
     */
    public static final String PREFIX_NEW_PAGE = "<page>";
    /**
     * �Ķ����еĽű����壬�����־
     */
    public static final String PREFIX_TITLE = "<b>";
    /**
     * �̳��ļ���ַ
     */
    public static final String FILE_TUTORIAL = "/tutorial/tutorial.txt";
    /**
     * �����ļ���ַ
     */
    public static final String FILE_HELP = "/help/help.txt";
    /**
     * ��Ϸ�����ļ���ַ
     */
    public static final String FILE_TASK = "/task.dat";
    /**
     * ��ť�߿���ɫ����ɫ
     */
    public static final int COLOR_BAR = 0x00ffffff;
    /**
     * ��ťѡ�е���ɫ
     */
    public static final int COLOR_SELECTED = 0x000090ff;
    /**
     * ��Ļ�ϵ��ֵ���ɫ����ɫ
     */
    public static final int COLOR_TEXT_ON_SCREEN = 0x00ffffff;
    /**
     * ��Ļ��ɫ
     */
    public static final int COLOR_SCREEN = 0x0;
    /**
     * ��ϷĬ������
     */
    public static final Font FONT_DEFAULT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    /**
     * ��Ϸ�ı�������
     */
    public static final Font FONT_TITLE = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    /**
     * ICON �ĸ߶�
     */
    public static final int HEIGHT_ICON = 20;
    /**
     * ICON �Ŀ��
     */
    public static final int WIDTH_ICON = 20;
    /**
     * ��ʾ��Ľ����ȷ��
     */
    public static final int RESULT_OK = 0;
    /**
     * ��ʾ��Ľ����ȡ��
     */
    public static final int RESULT_CANCEL = 1;
    /**
     * �¼����Ͷ��壬����ȷ��
     */
    public static final int EVENT_ENTER = 0;
    public static final int EVENT_PRESSED = 1;
    public static final int EVENT_RELEASED = 2;
    public static final int EVENT_DRAGGED = 3;
    public static final int RENDERING_HINTS = Graphics3D.ANTIALIAS | Graphics3D.TRUE_COLOR | Graphics3D.DITHER;
    /**
     * ��ǰ�汾����
     */
    public static final String VERSION = "1";
}
