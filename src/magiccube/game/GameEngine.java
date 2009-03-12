package magiccube.game;

import javax.microedition.midlet.MIDlet;
import magiccube.game.scene.SwitcherScene;
import magiccube.game.animation.AnimationEngine;
import magiccube.game.scene.MenuScene;
import magiccube.game.scene.IScene;
import magiccube.game.scene.GScene;
import magiccube.util.Debug;

/**
 * 游戏引擎
 * @author Anic
 */
public class GameEngine {

    /**
     * Midlet对象
     */
    private GMIDlet gameMidlet;
    /**
     * 画布对象
     */
    private GCanvas gameCanvas;


    /**
     * 按键 
     */
    private KeyCommand keyCommand;
    /**
     * 手写笔轨迹
     */
    private PointerCommand pointerCommand;
    /**
     * 游戏大小，即魔方阶数，默认为3
     */
    private int gameSize = 3;
    /**
     * 游戏状态常量，进行游戏
     */
    public static final int STATUS_PLAYING = 1;
    /**
     * 游戏状态常量，暂停游戏
     */
    public static final int STATUS_PAUSE = 2;
    /**
     * 游戏状态常量，结束游戏
     */
    public static final int STATUS_OVER = 3;
    /**
     * 状态变量
     */
    private int status;
    /**
     * 当前场景
     */
    private IScene currentScene;
    /**
     * 场景转换控制
     */
    private SwitcherScene sceneEngine;
    /**
     * 游戏场景
     */
    private GScene gameScene;
    /**
     * 菜单场景
     */
    private MenuScene menuScene;
    /**
     * 动画引擎
     */
    private AnimationEngine animationEngine;
    /**
     * 资源加载
     */
    private ResourceLoader resourceLoader;
    /**
     * 历史记录器
     */
    private HistoryRecorder historyRecorder;
    /**
     * 任务加载器 
     */
    private TaskLoader taskLoader;
    /**
     * 游戏没有加载任务时候，任务的ID号
     */
    public static final int ID_NONE_TASK = -1;
    private int taskId; //当前游戏的任务的id

    /**
     * 创建游戏引擎
     * @param myMidlet Midlet对象
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
     * 获得资源加载类
     * @return 资源加载类
     */
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * 获得全局的动画引擎
     * @return 动画引擎
     */
    public AnimationEngine getAnimationEngine() {
        return animationEngine;
    }

    /**
     * 获得按键
     * @return 按键
     */
    public KeyCommand getCommand() {
        return this.keyCommand;
    }

    public MIDlet getMIDlet()
    {
        return this.gameMidlet;
    }

    /**
     * 获得手写笔命令
     * @return 手写笔命令
     */
    public PointerCommand getPointerCommand() {
        return this.pointerCommand;
    }

    

    /**
     * 获得游戏画布
     * @return 游戏画布
     */
    public GCanvas getCanvas() {
        return gameCanvas;
    }

    /**
     * 获得当前场景
     * @return 当前场景
     */
    public IScene getScene() {
        return currentScene;
    }

    /**
     * 获得历史记录器
     * @return
     */
    public HistoryRecorder getHistoryRecorder() {
        return historyRecorder;
    }

    /**
     * 设置魔方阶数
     * @param size 阶数
     */
    public void setGameSize(int size) {
        gameSize = size;

    }

    /**
     * 获得游戏中的魔方阶数
     * @return 魔方阶数
     */
    public int getGameSize() {
        return gameSize;
    }

    /**
     * 判断是否正在进行游戏
     * @return 是否正在进行游戏
     */
    public boolean isPlaying() {
        return this.status != STATUS_OVER;
    }

    /**
     * 获得游戏当前状态，从STATUS_*的常量中取得
     * @return 当前状态
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * 初始化游戏
     */
    public void initGame() {

        resourceLoader.Init();

        //没有计划
        taskId = ID_NONE_TASK;
        taskLoader.load();

        status = STATUS_PAUSE;

        //命令队列
        keyCommand = new KeyCommand();
        pointerCommand = new PointerCommand();

        //在world之后创建，在场景之前，因为场景需要知道canvas的大小
        gameCanvas = new GCanvas(this);

        //因为需要创建摄影机，并加入world中
        gameScene = new GScene(this);
        menuScene = new MenuScene(this);
        //当前场景
        currentScene = menuScene;
        //currentScene = gameScene;

    }

    /**
     * 新建游戏
     */
    public void newGame() {
        status = STATUS_PAUSE;
        taskId = ID_NONE_TASK;
        //已经创建了World,将gameScene的所有内容清空，对于Menu的，不清除
        gameScene.resetScene();
        historyRecorder.clearHistory();
    }

    /**
     * 停止游戏
     */
    public void pauseGame() {
        if (status == STATUS_PLAYING) {
            status = STATUS_PAUSE;
        }
    }

    /**
     * 继续游戏
     */
    public void continueGame() {
        if (status == STATUS_PAUSE) {
            status = STATUS_PLAYING;
        }
    }

    /**
     * 释放资源
     */
    public void releaseResources() {
        
    }

    /**
     * 结束游戏
     */
    public void endGame() {
        status = STATUS_OVER;
        //在GMidlet中保存

        try {
            gameMidlet.destroyApp(false);
            gameMidlet.notifyDestroyed();
        } catch (Exception e) {
            Debug.handleException(e);
        }
    }

    /**
     * 新开始游戏
     */
    public void startGame() {
        gameCanvas.start();
        status = STATUS_PLAYING;
    }

    /**
     * 在游戏场景和菜单场景之间切换
     */
    public void switchScene() {
        if (currentScene == gameScene) {
            switchScene(gameScene, menuScene);
        } else if (currentScene == menuScene) {
            switchScene(menuScene, gameScene);
        }

    }

    /**
     * 切换场景
     * @param src 源场景
     * @param dst 目标场景
     */
    protected void switchScene(IScene src, IScene dst) {
        sceneEngine.startSwitchScene(src, dst);
        //进行场景变换
        animationEngine.addAnimation(sceneEngine, Common.TIME_SCENE_SWITCH, 0);
        currentScene = sceneEngine;
    }

    /**
     * 处理键盘动作
     * @param spendTime
     */
    public void process(int spendTime) {

        animationEngine.process(spendTime);
        IScene scene = getScene();
        if (scene != null) {
            if (scene.acceptAction()) {
                if (!keyCommand.isEmpty()) //优先键盘事件
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
     * 设置场景
     * @param scene 场景
     */
    public void setScene(IScene scene) {
        //提供给SceneEngine调用的
        currentScene = scene;
    }

    /**
     * 获得游戏场景
     * @return
     */
    public GScene getGameScene() {
        return gameScene;
    }

    /**
     * 获得菜单场景
     * @return
     */
    public MenuScene getMenuScene() {
        return menuScene;
    }

    /**
     * 保存游戏
     */
    public void save() {
        gameScene.getCubeEngine().save();
        taskLoader.save();
    }

    /**
     * 设置任务ID
     * @param id
     */
    public void setTaskId(int id) {
        if (id >= ID_NONE_TASK) {
            taskId = id;
        }
    }

    /**
     * 判断是否进行任务
     * @return 是否进行任务
     */
    public boolean inTask() {
        return taskId != ID_NONE_TASK;
    }

    /**
     * 获得当前任务
     * @return 如果当前有任务，返回任务对象；否则返回null
     */
    public GTask getGameTask() {
        return taskLoader.getTask(taskId);
    }

    /**
     * 获得任务加载器
     * @return 任务加载器
     */
    public TaskLoader getTaskLoader() {
        return taskLoader;
    }
}
