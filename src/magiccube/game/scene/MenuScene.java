/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.scene;

import magiccube.game.model.Cube;
import magiccube.game.*;
import magiccube.game.layer.ReaderLayer;
import magiccube.game.layer.MessageBoxLayer;
import magiccube.game.layer.TaskLayer;
import magiccube.game.layer.LayerManager;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.World;
import magiccube.ui.AbstractButton;
import magiccube.ui.ImageButton;
import magiccube.game.layer.GLayer;
import magiccube.util.DrawingUtil;

/**
 *
 * @author Administrator
 */
public class MenuScene implements IScene, MessageBoxLayer.IEventListener, AbstractButton.IEventListener {

    private Camera camera;
    private Image image_title;
    private AbstractButton[] buttons;
    private AbstractButton btn_right;
    private AbstractButton btn_left;
    private int currentMenuItem = 0;
    private GameEngine game;
    private Cube cube;
    private LayerManager layerManager;
    private static final int INDEX_RESUME = 0;
    private static final int INDEX_NEW_GAME = 1;
    private static final int INDEX_TASK = 2;
    private static final int INDEX_HELP = 3;
    private static final int INDEX_EXIT = 4;
    private MessageBoxLayer newGameLayer;
    private TaskLayer taskLayer;
    private ReaderLayer helpLayer;
    private MenuSceneLayer basicLayer;
    private MessageBoxLayer sizeSelectionLayer;
    private World world;
    private SceneSwitchProxy proxy;
    private boolean switching = false;

    private class MenuSceneLayer extends GLayer {

        public MenuSceneLayer() {
            super(game.getCanvas().getWidth(), game.getCanvas().getHeight());
        }

        public boolean acceptKey(int key) {
            switch (key) {
                case GameCanvas.RIGHT:
                    if (currentMenuItem < buttons.length - 1) {
                        currentMenuItem = (currentMenuItem + 1) % buttons.length;
                    }
                    break;
                case GameCanvas.LEFT:
                    if (currentMenuItem > 0) {
                        currentMenuItem = (currentMenuItem - 1 + buttons.length) % buttons.length;
                    }
                    break;
                case GameCanvas.FIRE:
                    performAction(currentMenuItem);
                    break;
            }
            return true;
        }

        public void acceptPointers(int[][] pointers) {
            for (int i = 0; i < pointers.length; ++i) {
                handlePointers(pointers[i], new AbstractButton[]{buttons[currentMenuItem]}); //只处理当前的Button

                if (currentMenuItem > 0) {
                    handlePointers(pointers[i], new AbstractButton[]{btn_left});
                }
                if (currentMenuItem < buttons.length - 1) {
                    handlePointers(pointers[i], new AbstractButton[]{btn_right});
                }
            }
        }

        public void draw(Graphics g) {
        }
    }

    public MenuScene(GameEngine game) {
        this.game = game;
        createScene();
        image_title = game.getResourceLoader().getImage(ResourceLoader.IMAGE_MENU_TITLE);


        initMenu(game.getResourceLoader(), game.getCanvas().getWidth(), game.getCanvas().getHeight());

        layerManager = new LayerManager();

        taskLayer = new TaskLayer(game, this);
        //先进行测试
        helpLayer = new ReaderLayer(Common.FILE_HELP, game, this.layerManager, ReaderLayer.MODE_MAX, game.getCanvas().getWidth(), game.getCanvas().getHeight(), game.getCanvas().getWidth(), game.getCanvas().getHeight(), true);
        newGameLayer = new MessageBoxLayer(game.getCanvas().getWidth(), game.getCanvas().getHeight(), "旧记录会被删除，是否新建？", "新建游戏", new String[]{"确定", "取消"}, this);

        basicLayer = new MenuSceneLayer();
        layerManager.addLayer(basicLayer);

        sizeSelectionLayer = new MessageBoxLayer(game.getCanvas().getWidth(), game.getCanvas().getHeight(), "选择魔方大小", "准备开始游戏", new String[]{"2阶", "3阶", "4阶", "5阶"}, this);
    }

    private void initMenu(ResourceLoader loader, int width, int height) {

        Image firstImage = loader.getImage(ResourceLoader.IMAGE_MENU_RESUME);
        Image imgLeft = loader.getImage(ResourceLoader.IMAGE_ARROW_LEFT);
        Image imgRight = loader.getImage(ResourceLoader.IMAGE_ARROW_RIGHT);

        int y = height / 4 * 3 - firstImage.getHeight() / 2;
        int x = width / 2 - firstImage.getWidth() / 2;

        buttons = new AbstractButton[5];
        buttons[INDEX_RESUME] = new ImageButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_RESUME), this);
        buttons[INDEX_NEW_GAME] = new ImageButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_START), this);
        buttons[INDEX_TASK] = new ImageButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_TASK), this);
        buttons[INDEX_HELP] = new ImageButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_HELP), this);
        buttons[INDEX_EXIT] = new ImageButton(x, y, loader.getImage(ResourceLoader.IMAGE_MENU_EXIT), this);



        btn_left = new ImageButton(width / 2 - buttons[0].getWidth() / 2 - imgLeft.getWidth(), y, imgLeft, this);

        btn_right = new ImageButton(width / 2 + buttons[0].getWidth() / 2, y, imgRight, this);


    }

    public boolean acceptAction() {
        return true;
    }

    public void draw(Graphics g, int width, int height) {

        DrawingUtil.draw3DByWorld(g, world);

        //如果不是在变换中，绘制2d场景
        if (!switching) {
            g.drawImage(image_title, width - image_title.getWidth(), height / 2 - image_title.getHeight() / 2, 0);
            buttons[currentMenuItem].draw(g);
            if (currentMenuItem > 0) {
                btn_left.draw(g);
            }
            if (currentMenuItem < buttons.length - 1) {
                btn_right.draw(g);
            }
            layerManager.getTopLayer().draw(g);
        }
    }

    public void process(int spendTime) {
        cube.getMesh().postRotate(45f / 1000 * spendTime, 0f, 1f, 0f);
    }

    public void processAction(int key) {

        layerManager.getTopLayer().acceptKey(key);
    }

    private void performAction(int index) {
        switch (index) {
            case INDEX_RESUME:
                game.switchScene();
                break;
            case INDEX_NEW_GAME:
                //因为第二个是“取消”按钮
                newGameLayer.setSelectedIndex(1);
                layerManager.addLayer(newGameLayer);
                break;
            case INDEX_HELP:
                helpLayer.setStatus(ReaderLayer.STATUS_MENU);
                layerManager.addLayer(helpLayer);
                break;
            case INDEX_EXIT:
                game.endGame();
                break;
            case INDEX_TASK:
                layerManager.addLayer(taskLayer);
                break;
        }
    }
    //创建场景

    private void createScene() {
        world = new World();
        //rootGroup = new Group();
        cube = new Cube(0, new int[]{Cube.FACE_BACK,
                    Cube.FACE_BOTTOM,
                    Cube.FACE_FONT,
                    Cube.FACE_LEFT,
                    Cube.FACE_RIGHT,
                    Cube.FACE_TOP});
        cube.rotate(30, 1f, 0f, 0f);
        cube.rotate(45f, 0f, 1f, 0f);
        cube.setPosition(-2, 0, 0);
        world.addChild(cube.getMesh());

        camera = new Camera();
        camera.setTranslation(0, 0, 3);
        camera.setOrientation(0, 0, 0, 0);
        camera.setPerspective(90.0f, 1.0f, 2f, 10f);
        world.addChild(camera);
        world.setActiveCamera(camera);

        proxy = new SceneSwitchProxy(camera);
    }

    public void onMessageLayerConfirm(MessageBoxLayer layer, int result) {
        if (layer.equals(newGameLayer)) {
            layerManager.removeTopLayer();
            if (result == Common.RESULT_OK) {
                //选择大小
                layerManager.addLayer(sizeSelectionLayer);
            }
        } else if (layer.equals(taskLayer)) {
            if (result == Common.RESULT_OK) {
                GTask task = game.getTaskLoader().getCurrentTask();
                if (task == null) {
                    return;
                }
                //默认的任务都是针对3的魔方
                game.setGameSize(3);
                game.newGame();
                game.setTaskId(task.getId());
                game.switchScene();
                GameCreator creator = new GameCreator(game, game.getGameScene().getCubeEngine());
                creator.taskRotate(task.getTaskInitArray());
            }
            layerManager.removeTopLayer();
        } else if (layer.equals(sizeSelectionLayer)) {
            //新建游戏
            game.setGameSize(result + 2);
            game.newGame();
            game.switchScene();
            GameCreator creator = new GameCreator(game, game.getGameScene().getCubeEngine());
            creator.randomRotate(10);
            layerManager.removeTopLayer();
        }

    }

    public void processEvent(int[][] events) {
        layerManager.getTopLayer().acceptPointers(events);
    }

    public void onButtonClick(AbstractButton source) {
        for (int i = 0; i < buttons.length; ++i) {
            if (buttons[i] == source) {
                performAction(i);
                return;
            }
        }

        if (source == btn_left) {
            game.getCommand().push(GameCanvas.LEFT);
        } else if (source == btn_right) {
            game.getCommand().push(GameCanvas.RIGHT);
        }
    }

    public void onSelectedChanged(AbstractButton source) {
    }

    public void afterEntering() {
        proxy.afterEntering();
        switching = false;
    }

    public void afterLeaving() {
        proxy.afterLeaving();
        switching = false;
    }

    public void beforeEntering(int total) {
        proxy.beforeEntering(total);
        currentMenuItem = INDEX_RESUME;
        switching = true;
    }

    public void beforeLeaving(int total) {
        proxy.beforeLeaving(total);
        switching = true;
    }

    public void processEntering(int spendTime, int currentTime) {
        proxy.processEntering(spendTime, currentTime);
    }

    public void processLeaving(int spendTime, int currentTime) {
        proxy.processLeaving(spendTime, currentTime);
    }
}
