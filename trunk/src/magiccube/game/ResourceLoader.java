/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import magiccube.util.Debug;
import javax.microedition.lcdui.Image;

/**
 * 资源加载类
 * @author Anic
 */
public class ResourceLoader {

    private static String[] strResources = new String[]{
        "/img_arrow_up.png",
        "/img_arrow_down.png",
        "/img_arrow_right.png",
        "/img_arrow_left.png",
        "/img_task.png",
        "/img_resume.png",
        "/img_start.png",
        "/img_exit.png",
        "/img_title.png",
        "/img_help.png",
        "/img_menu_icon_back.png",
        "/img_menu_icon_reverse.png",
        "/img_menu_icon_help.png",
        "/img_menu_icon_resume.png",
        "/img_tutorial_dock.png",
        "/img_tutorial_exit.png",
        "/img_tutorial_next.png",
        "/img_tutorial_view.png",
        "/img_menu_icon_task.png",
        "/img_menu_icon_save.png",
        "/img_menu_icon_camera.png",
        "/img_tutorial_max.png"
    };
    private static String IMAGE_DIR = "/image";
    public static final String IMAGE_ARROW_UP = strResources[0];
    public static final String IMAGE_ARROW_DOWN = strResources[1];
    public static final String IMAGE_ARROW_RIGHT = strResources[2];
    public static final String IMAGE_ARROW_LEFT = strResources[3];
    public static final String IMAGE_MENU_TASK = strResources[4];
    public static final String IMAGE_MENU_RESUME = strResources[5];
    public static final String IMAGE_MENU_START = strResources[6];
    public static final String IMAGE_MENU_EXIT = strResources[7];
    public static final String IMAGE_MENU_TITLE = strResources[8];
    public static final String IMAGE_MENU_HELP = strResources[9];
    public static final String IMAGE_MENU_ICON_BACK = strResources[10];
    public static final String IMAGE_MENU_ICON_REVERSE = strResources[11];
    public static final String IMAGE_MENU_ICON_HELP = strResources[12];
    public static final String IMAGE_MENU_ICON_RESUME = strResources[13];
    public static final String IMAGE_TUTORIAL_DOCK = strResources[14];
    public static final String IMAGE_TUTORIAL_EXIT = strResources[15];
    public static final String IMAGE_TUTORIAL_NEXT = strResources[16];
    public static final String IMAGE_TUTORIAL_PREVIOUS = IMAGE_MENU_ICON_RESUME;
    public static final String IMAGE_TUTORIAL_VIEW = strResources[17];
    public static final String IMAGE_MENU_ICON_TASK = strResources[18];
    public static final String IMAGE_MENU_ICON_SAVE = strResources[19];
    public static final String IMAGE_MENU_ICON_CAMERA = strResources[20];
    public static final String IMAGE_TUTORIAL_MAX = strResources[21];
    private Image[] images;

    public void Init() {
        try {
            images = new Image[strResources.length];
            for (int i = 0; i < strResources.length; ++i) {
                images[i] = Image.createImage(IMAGE_DIR + strResources[i]);
            }
        } catch (Exception e) {
            Debug.handleException(e);
        }
    }

    public Image getImage(String strImage) {
        for (int i = 0; i < strResources.length; ++i) {
            if (strImage.equalsIgnoreCase(strResources[i])) {
                return images[i];
            }
        }

        return null;
    }
}
