package magiccube.game.scene;

import javax.microedition.lcdui.Display;
import magiccube.game.engine.CubeBasedEngine;
import magiccube.game.engine.MagicCubeEngine;
import magiccube.game.animation.AnimationEngine;
import magiccube.game.*;
import magiccube.game.layer.SceneLayer;
import magiccube.game.layer.MenuLayer;
import magiccube.game.layer.ReaderLayer;
import magiccube.game.layer.MessageBoxLayer;
import magiccube.game.layer.LayerManager;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import magiccube.game.engine.FaceCanvas;
import magiccube.game.engine.SceneRotator;



/**
 * 游戏场景
 * @author Anic
 */
public class GScene implements IScene, MessageBoxLayer.IEventListener {

    private GameEngine game;
    /**
     * 魔方引擎
     */
    private MagicCubeEngine cubeEngine;
    private SceneRotator rotator;
    /**
     * 本地动画引擎
     */
    private AnimationEngine localAnimationEngine;
    /**
     * 层管理
     */
    private LayerManager layerManager = new LayerManager();
    private MenuLayer menuLayer;
    private SceneLayer sceneLayer;
    private ReaderLayer tutorialLayer;
    private boolean switching = false;
    private MessageBoxLayer taskFinishLayer;
    private FaceCanvas fcanvas;

    private class TaskSavingThread implements Runnable {

        public void run() {
            game.getTaskLoader().save();
        }
    }

    /**
     * 创建场景
     * @param g 游戏引擎
     */
    public GScene(GameEngine g) {
        game = g;

        menuLayer = new MenuLayer(g);
        sceneLayer = new SceneLayer(g);
        tutorialLayer = new ReaderLayer(Common.FILE_TUTORIAL,
                g,
                this.layerManager,
                ReaderLayer.MODE_NORMAL,
                g.getCanvas().getWidth(),
                g.getCanvas().getHeight(),
                Common.TUTORIAL_WIDTH,
                Common.TUTORIAL_HEIGHT);

        layerManager.addLayer(sceneLayer);
        layerManager.addLayer(menuLayer);

        //先创建动画引擎
        localAnimationEngine = new AnimationEngine();

        //真正创建场景部分
        createScene(true);
    }

    private float getCameraRadius() {
        //Debug.println("game Size = " + cubeEngine.getSize());
        return 2.5f * cubeEngine.getSize();
    }

    /**
     * 创建场景
     * @param load 是否是加载式的创建
     */
    private void createScene(boolean load) {

        //先清除旧的内容
        localAnimationEngine.clearAnimations();


        //创建魔方方块
        cubeEngine = new CubeBasedEngine(game, this.localAnimationEngine);
        cubeEngine.init(load);
        cubeEngine.setMode(MagicCubeEngine.FACE_AXIS_X, 0, MagicCubeEngine.MODE_CLOCKWISE);

        //定位摄影机
        rotator = new SceneRotator(cubeEngine.getCamera());
        rotator.resetCamera(game.getCanvas().getWidth(), game.getCanvas().getHeight(), getCameraRadius());

        proxy = new SceneSwitchProxy(cubeEngine.getCamera());
        //测试作用
        fcanvas = new FaceCanvas(((CubeBasedEngine) cubeEngine).getFaceEngine(), game.getMIDlet(), game.getCanvas());
    }

    /**
     * 是否能用左右转
     * @return 是否能用左右转
     */
    public boolean canRotRight() {
        return rotator.canRotRight();
    }

    /**
     * 重置摄影机
     */
    public void resetCamera() {
        rotator.resetCamera(game.getCanvas().getWidth(), game.getCanvas().getHeight(), getCameraRadius());
    }

    private void rotateSelection() {
        game.getAnimationEngine().addAnimation(cubeEngine, Common.TIME_CUBE_ROTATE, 0);
    }

    public void draw(Graphics g, int width, int height) {

        cubeEngine.draw(g, width, height);
        if (!switching) {
            layerManager.getTopLayer().draw(g);
        }
    }

    public boolean acceptAction() {
        //return !cubeEngine.isRotating();
        return !game.getAnimationEngine().isMoving();
    }

    //执行
    public void process(int spendTime) {
        localAnimationEngine.process(spendTime);

        if (game.getStatus() == GameEngine.STATUS_PLAYING) {
            GTask task = game.getGameTask();

            if (task != null) {
                boolean finish = task.checkFinish(cubeEngine.countSameColorFaces());
                if (finish) {
                    task.setFinished(true);
                    game.setTaskId(GameEngine.ID_NONE_TASK);
                    //保存任务
                    //game.getTaskLoader().save();
                    taskFinishLayer = new MessageBoxLayer(game.getCanvas().getWidth(), game.getCanvas().getHeight(), "恭喜你，任务" + (task.getId() + 1) + "完成", "任务完成", new String[]{"确定"}, this);
                    layerManager.addLayer(taskFinishLayer);
                //设置成为不显示的

                }
            }
        }

    }

    public void processAction(int key) {

        //先进行 层的输入判别
        if (layerManager.getTopLayer().acceptKey(key)) {
            return;
        }

        switch (key) {
            case GameCanvas.UP:
                rotator.rotByUp(true);
                sceneLayer.setLastDirection(SceneLayer.INDEX_UP, this.localAnimationEngine);
                break;
            case GameCanvas.DOWN:
                rotator.rotByUp(false);
                sceneLayer.setLastDirection(SceneLayer.INDEX_DOWN, this.localAnimationEngine);
                break;
            case GameCanvas.RIGHT:
                //不能用左右转
                if (canRotRight()) {
                    rotator.rotByRight(true);
                    sceneLayer.setLastDirection(SceneLayer.INDEX_RIGHT, this.localAnimationEngine);
                }
                break;
            case GameCanvas.LEFT:
                //Debug.println("left");
                if (canRotRight()) {
                    rotator.rotByRight(false);
                    sceneLayer.setLastDirection(SceneLayer.INDEX_LEFT, this.localAnimationEngine);
                }
                break;
            case GameCanvas.KEY_NUM9:
                cubeEngine.switchSelection(false);
                break;
            case GameCanvas.KEY_NUM7:
                cubeEngine.switchSelection(true);
                break;
            case GameCanvas.FIRE:
                rotateSelection();
                setHistory();
                break;
            case GameCanvas.KEY_NUM0:
                rotator.resetCamera(game.getCanvas().getWidth(), game.getCanvas().getHeight(), getCameraRadius());
                break;
            case GameCanvas.KEY_POUND:
                rollBackHistory();
                break;
            case GameCanvas.KEY_STAR:
                cubeEngine.setClockwise(MagicCubeEngine.notClockwise(cubeEngine.getClockwise()));
                break;
            case GCanvas.KEY_RIGHT_MENU:
                showTutorial();
                break;
            case GameCanvas.KEY_NUM3:
                fcanvas.render();
                Display.getDisplay(game.getMIDlet()).setCurrent(fcanvas);
                break;
            default:
                break;
        }
    }

    /**
     * 重置场景
     */
    public void resetScene() {
        createScene(false);
    }

    /**
     * 获得魔方引擎
     * @return 魔方引擎
     */
    public MagicCubeEngine getCubeEngine() {
        return cubeEngine;
    }

    private void setHistory() {
        int mode = cubeEngine.getMode();
        int mode_index = cubeEngine.getModeIndex();
        int clockwise = cubeEngine.getClockwise();
        game.getHistoryRecorder().addHistory(mode, mode_index, clockwise);
    }

    /**
     * 回退历史操作
     */
    public void rollBackHistory() {
        if (game.getHistoryRecorder().hasHistory()) {
            int[] params = game.getHistoryRecorder().popHistory();

            if (params[2] == MagicCubeEngine.MODE_CLOCKWISE) {
                params[2] = MagicCubeEngine.MODE_NOT_CLOCKWISE;
            } else {
                params[2] = MagicCubeEngine.MODE_CLOCKWISE;
            }
            cubeEngine.setMode(params[0], params[1], params[2]);
            rotateSelection();
        }
    }

    /**
     * 获得Layer管理对象
     * @return Layer管理对象
     */
    public LayerManager getLayerManager() {
        return layerManager;
    }

    /**
     * 显示教程
     */
    public void showTutorial() {
        tutorialLayer.setStatus(ReaderLayer.STATUS_MENU);
        if (!layerManager.isActiveLayer(tutorialLayer)) {
            layerManager.addLayer(tutorialLayer);

        }
    }

    public void onMessageLayerConfirm(MessageBoxLayer layer, int result) {
        layerManager.removeTopLayer();
        if (layer.equals(this.taskFinishLayer)) {
            TaskCanvas.run("进度保存中", new Runnable[]{new TaskSavingThread()}, game.getMIDlet(), game.getCanvas());
        }
    }

    public void processEvent(int[][] events) {
        layerManager.getTopLayer().acceptPointers(events);
    }
    private SceneSwitchProxy proxy;

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
