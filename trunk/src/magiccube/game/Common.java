/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import javax.microedition.lcdui.Font;
import javax.microedition.m3g.Graphics3D;

/**
 * 常量定义类
 * @author Anic
 */
public class Common {

    /**
     * 调试信息
     */
    public static String DEBUG_INFO;
    static {
        DEBUG_INFO = "";
    }
    public static final int TIME_SCENE_SWITCH = 800;
    public static final int TIME_CUBE_ROTATE = 500;
    public static final int TIME_INIT = 5000;
    /**
     * 教程显示的宽度
     */
    public static final int TUTORIAL_WIDTH = 125; //教程显示的宽度
    /**
     * 教程显示的高度
     */
    public static final int TUTORIAL_HEIGHT = 100; //教程显示的高度
    public static final int WORDS_PER_LINE = 20;
    /**
     * 阅读器中的脚本定义，分割符号和新一段的标志
     */
    public static final String SYMBOL_SPLIT = "\r\n";
    /**
     * 阅读器中的脚本定义，图片标志
     */
    public static final String PREFIX_IMAGE = "<img>";
    /**
     * 阅读器中的脚本定义，分页标志
     */
    public static final String PREFIX_NEW_PAGE = "<page>";
    /**
     * 阅读器中的脚本定义，粗体标志
     */
    public static final String PREFIX_TITLE = "<b>";
    /**
     * 教程文件地址
     */
    public static final String FILE_TUTORIAL = "/tutorial/tutorial.txt";
    /**
     * 帮助文件地址
     */
    public static final String FILE_HELP = "/help/help.txt";
    /**
     * 游戏任务文件地址
     */
    public static final String FILE_TASK = "/task.dat";
    /**
     * 按钮边框颜色，白色
     */
    public static final int COLOR_BAR = 0x00ffffff;
    /**
     * 按钮选中的颜色
     */
    public static final int COLOR_SELECTED = 0x000090ff;
    /**
     * 屏幕上的字的颜色，白色
     */
    public static final int COLOR_TEXT_ON_SCREEN = 0x00ffffff;
    /**
     * 屏幕颜色
     */
    public static final int COLOR_SCREEN = 0x0;
    /**
     * 游戏默认字体
     */
    public static final Font FONT_DEFAULT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    /**
     * 游戏的标题字体
     */
    public static final Font FONT_TITLE = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    /**
     * ICON 的高度
     */
    public static final int HEIGHT_ICON = 20;
    /**
     * ICON 的宽度
     */
    public static final int WIDTH_ICON = 20;
    /**
     * 提示框的结果，确定
     */
    public static final int RESULT_OK = 0;
    /**
     * 提示框的结果，取消
     */
    public static final int RESULT_CANCEL = 1;
    /**
     * 事件类型定义，按键确认
     */
    public static final int EVENT_ENTER = 0;
    public static final int EVENT_PRESSED = 1;
    public static final int EVENT_RELEASED = 2;
    public static final int EVENT_DRAGGED = 3;
    public static final int RENDERING_HINTS = Graphics3D.ANTIALIAS | Graphics3D.TRUE_COLOR | Graphics3D.DITHER;
    /**
     * 当前版本号码
     */
    public static final String VERSION = "1";
}
