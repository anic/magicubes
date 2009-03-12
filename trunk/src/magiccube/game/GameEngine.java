package magiccube.game;

import javax.microedition.midlet.MIDlet;
import magiccube.game.scene.SwitcherScene;
import magiccube.game.animation.AnimationEngine;
import magiccube.game.scene.MenuScene;
import magiccube.game.scene.IScene;
import magiccube.game.scene.GScene;
import magiccube.util.Debug;

/**
 * ��Ϸ����
 * @author Anic
 */
public class GameEngine {

    /**
     * Midlet����
     */
    private GMIDlet gameMidlet;
    /**
     * ��������
     */
    private GCanvas gameCanvas;


    /**
     * ���� 
     */
    private KeyCommand keyCommand;
    /**
     * ��д�ʹ켣
     */
    private PointerCommand pointerCommand;
    /**
     * ��Ϸ��С����ħ��������Ĭ��Ϊ3
     */
    private int gameSize = 3;
    /**
     * ��Ϸ״̬������������Ϸ
     */
    public static final int STATUS_PLAYING = 1;
    /**
     * ��Ϸ״̬��������ͣ��Ϸ
     */
    public static final int STATUS_PAUSE = 2;
    /**
     * ��Ϸ״̬������������Ϸ
     */
    public static final int STATUS_OVER = 3;
    /**
     * ״̬����
     */
    private int status;
    /**
     * ��ǰ����
     */
    private IScene currentScene;
    /**
     * ����ת������
     */
    private SwitcherScene sceneEngine;
    /**
     * ��Ϸ����
     */
    private GScene gameScene;
    /**
     * �˵�����
     */
    private MenuScene menuScene;
    /**
     * ��������
     */
    private AnimationEngine animationEngine;
    /**
     * ��Դ����
     */
    private ResourceLoader resourceLoader;
    /**
     * ��ʷ��¼��
     */
    private HistoryRecorder historyRecorder;
    /**
     * ��������� 
     */
    private TaskLoader taskLoader;
    /**
     * ��Ϸû�м�������ʱ�������ID��
     */
    public static final int ID_NONE_TASK = -1;
    private int taskId; //��ǰ��Ϸ�������id

    /**
     * ������Ϸ����
     * @param myMidlet Midlet����
     */
    public GameEngine(GMIDlet myMidlet) {
        gameMidlet = myMidlet;
        sceneEngine = new SwitcherScene(this);
        animationEngine = new AnimationEngine();
        resourceLoader = new ResourceLoader();
        historyRecorder = new HistoryRecorder(this);
        taskLoader = new TaskLoader(this);
    }

    /**
     * �����Դ������
     * @return ��Դ������
     */
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * ���ȫ�ֵĶ�������
     * @return ��������
     */
    public AnimationEngine getAnimationEngine() {
        return animationEngine;
    }

    /**
     * ��ð���
     * @return ����
     */
    public KeyCommand getCommand() {
        return this.keyCommand;
    }

    public MIDlet getMIDlet()
    {
        return this.gameMidlet;
    }

    /**
     * �����д������
     * @return ��д������
     */
    public PointerCommand getPointerCommand() {
        return this.pointerCommand;
    }

    

    /**
     * �����Ϸ����
     * @return ��Ϸ����
     */
    public GCanvas getCanvas() {
        return gameCanvas;
    }

    /**
     * ��õ�ǰ����
     * @return ��ǰ����
     */
    public IScene getScene() {
        return currentScene;
    }

    /**
     * �����ʷ��¼��
     * @return
     */
    public HistoryRecorder getHistoryRecorder() {
        return historyRecorder;
    }

    /**
     * ����ħ������
     * @param size ����
     */
    public void setGameSize(int size) {
        gameSize = size;

    }

    /**
     * �����Ϸ�е�ħ������
     * @return ħ������
     */
    public int getGameSize() {
        return gameSize;
    }

    /**
     * �ж��Ƿ����ڽ�����Ϸ
     * @return �Ƿ����ڽ�����Ϸ
     */
    public boolean isPlaying() {
        return this.status != STATUS_OVER;
    }

    /**
     * �����Ϸ��ǰ״̬����STATUS_*�ĳ�����ȡ��
     * @return ��ǰ״̬
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * ��ʼ����Ϸ
     */
    public void initGame() {

        resourceLoader.Init();

        //û�мƻ�
        taskId = ID_NONE_TASK;
        taskLoader.load();

        status = STATUS_PAUSE;

        //�������
        keyCommand = new KeyCommand();
        pointerCommand = new PointerCommand();

        //��world֮�󴴽����ڳ���֮ǰ����Ϊ������Ҫ֪��canvas�Ĵ�С
        gameCanvas = new GCanvas(this);

        //��Ϊ��Ҫ������Ӱ����������world��
        gameScene = new GScene(this);
        menuScene = new MenuScene(this);
        //��ǰ����
        currentScene = menuScene;
        //currentScene = gameScene;

    }

    /**
     * �½���Ϸ
     */
    public void newGame() {
        status = STATUS_PAUSE;
        taskId = ID_NONE_TASK;
        //�Ѿ�������World,��gameScene������������գ�����Menu�ģ������
        gameScene.resetScene();
        historyRecorder.clearHistory();
    }

    /**
     * ֹͣ��Ϸ
     */
    public void pauseGame() {
        if (status == STATUS_PLAYING) {
            status = STATUS_PAUSE;
        }
    }

    /**
     * ������Ϸ
     */
    public void continueGame() {
        if (status == STATUS_PAUSE) {
            status = STATUS_PLAYING;
        }
    }

    /**
     * �ͷ���Դ
     */
    public void releaseResources() {
        
    }

    /**
     * ������Ϸ
     */
    public void endGame() {
        status = STATUS_OVER;
        //��GMidlet�б���

        try {
            gameMidlet.destroyApp(false);
            gameMidlet.notifyDestroyed();
        } catch (Exception e) {
            Debug.handleException(e);
        }
    }

    /**
     * �¿�ʼ��Ϸ
     */
    public void startGame() {
        gameCanvas.start();
        status = STATUS_PLAYING;
    }

    /**
     * ����Ϸ�����Ͳ˵�����֮���л�
     */
    public void switchScene() {
        if (currentScene == gameScene) {
            switchScene(gameScene, menuScene);
        } else if (currentScene == menuScene) {
            switchScene(menuScene, gameScene);
        }

    }

    /**
     * �л�����
     * @param src Դ����
     * @param dst Ŀ�곡��
     */
    protected void switchScene(IScene src, IScene dst) {
        sceneEngine.startSwitchScene(src, dst);
        //���г����任
        animationEngine.addAnimation(sceneEngine, Common.TIME_SCENE_SWITCH, 0);
        currentScene = sceneEngine;
    }

    /**
     * ������̶���
     * @param spendTime
     */
    public void process(int spendTime) {

        animationEngine.process(spendTime);
        IScene scene = getScene();
        if (scene != null) {
            if (scene.acceptAction()) {
                if (!keyCommand.isEmpty()) //���ȼ����¼�
                {
                    int key = keyCommand.pop();
                    scene.processAction(key);
                } else if (!pointerCommand.isEmpty()) {
                    scene.processEvent(pointerCommand.getPointers());
                }
            }
            scene.process(spendTime);
        }
    }

    /**
     * ���ó���
     * @param scene ����
     */
    public void setScene(IScene scene) {
        //�ṩ��SceneEngine���õ�
        currentScene = scene;
    }

    /**
     * �����Ϸ����
     * @return
     */
    public GScene getGameScene() {
        return gameScene;
    }

    /**
     * ��ò˵�����
     * @return
     */
    public MenuScene getMenuScene() {
        return menuScene;
    }

    /**
     * ������Ϸ
     */
    public void save() {
        gameScene.getCubeEngine().save();
        taskLoader.save();
    }

    /**
     * ��������ID
     * @param id
     */
    public void setTaskId(int id) {
        if (id >= ID_NONE_TASK) {
            taskId = id;
        }
    }

    /**
     * �ж��Ƿ��������
     * @return �Ƿ��������
     */
    public boolean inTask() {
        return taskId != ID_NONE_TASK;
    }

    /**
     * ��õ�ǰ����
     * @return �����ǰ�����񣬷���������󣻷��򷵻�null
     */
    public GTask getGameTask() {
        return taskLoader.getTask(taskId);
    }

    /**
     * ������������
     * @return ���������
     */
    public TaskLoader getTaskLoader() {
        return taskLoader;
    }
}
