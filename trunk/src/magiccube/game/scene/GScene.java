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
 * ��Ϸ����
 * @author Anic
 */
public class GScene implements IScene, MessageBoxLayer.IEventListener {

    private GameEngine game;
    /**
     * ħ������
     */
    private MagicCubeEngine cubeEngine;
    private SceneRotator rotator;
    /**
     * ���ض�������
     */
    private AnimationEngine localAnimationEngine;
    /**
     * �����
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
     * ��������
     * @param g ��Ϸ����
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

        //�ȴ�����������
        localAnimationEngine = new AnimationEngine();

        //����������������
        createScene(true);
    }

    private float getCameraRadius() {
        //Debug.println("game Size = " + cubeEngine.getSize());
        return 2.5f * cubeEngine.getSize();
    }

    /**
     * ��������
     * @param load �Ƿ��Ǽ���ʽ�Ĵ���
     */
    private void createScene(boolean load) {

        //������ɵ�����
        localAnimationEngine.clearAnimations();


        //����ħ������
        cubeEngine = new CubeBasedEngine(game, this.localAnimationEngine);
        cubeEngine.init(load);
        cubeEngine.setMode(MagicCubeEngine.FACE_AXIS_X, 0, MagicCubeEngine.MODE_CLOCKWISE);

        //��λ��Ӱ��
        rotator = new SceneRotator(cubeEngine.getCamera());
        rotator.resetCamera(game.getCanvas().getWidth(), game.getCanvas().getHeight(), getCameraRadius());

        proxy = new SceneSwitchProxy(cubeEngine.getCamera());
        //��������
        fcanvas = new FaceCanvas(((CubeBasedEngine) cubeEngine).getFaceEngine(), game.getMIDlet(), game.getCanvas());
    }

    /**
     * �Ƿ���������ת
     * @return �Ƿ���������ת
     */
    public boolean canRotRight() {
        return rotator.canRotRight();
    }

    /**
     * ������Ӱ��
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

    //ִ��
    public void process(int spendTime) {
        localAnimationEngine.process(spendTime);

        if (game.getStatus() == GameEngine.STATUS_PLAYING) {
            GTask task = game.getGameTask();

            if (task != null) {
                boolean finish = task.checkFinish(cubeEngine.countSameColorFaces());
                if (finish) {
                    task.setFinished(true);
                    game.setTaskId(GameEngine.ID_NONE_TASK);
                    //��������
                    //game.getTaskLoader().save();
                    taskFinishLayer = new MessageBoxLayer(game.getCanvas().getWidth(), game.getCanvas().getHeight(), "��ϲ�㣬����" + (task.getId() + 1) + "���", "�������", new String[]{"ȷ��"}, this);
                    layerManager.addLayer(taskFinishLayer);
                //���ó�Ϊ����ʾ��

                }
            }
        }

    }

    public void processAction(int key) {

        //�Ƚ��� ��������б�
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
                //����������ת
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
     * ���ó���
     */
    public void resetScene() {
        createScene(false);
    }

    /**
     * ���ħ������
     * @return ħ������
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
     * ������ʷ����
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
     * ���Layer�������
     * @return Layer�������
     */
    public LayerManager getLayerManager() {
        return layerManager;
    }

    /**
     * ��ʾ�̳�
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
            TaskCanvas.run("���ȱ�����", new Runnable[]{new TaskSavingThread()}, game.getMIDlet(), game.getCanvas());
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
