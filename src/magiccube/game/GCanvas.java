package magiccube.game;

/*
 * GCanvas.java
 *
 * Created on 2006��9��6��, ����11:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import magiccube.game.scene.IScene;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import magiccube.util.Debug;

/**
 *
 * @author cya
 */
public class GCanvas extends GameCanvas implements Runnable {

    private float fps = 0;
    private Thread mainThread;
    GameEngine game = null;
    public static final int KEY_LEFT_MENU = -6;
    public static final int KEY_RIGHT_MENU = -7;
    private static final int MAX_FPS = 60;
    private static final int TIME_THREAD_SLEEP = 30;

    /**
     * Construct a new canvas
     * @param g ��Ϸ����
     */
    public GCanvas(GameEngine g) {
        super(false);
        game = g;

        //����ȫ��ģʽ
        setFullScreenMode(true);
    }

    public void start() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    /**
     * Cleanup and destroy.
     */
    void destroy() {
    }

    private void drawScene(Graphics g) {

        if (!Common.DEBUG_INFO.equals("")) {
            g.setColor(0x0);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(0x00ffffff);
            g.drawString(Common.DEBUG_INFO, 0, 0, 0);
            return;
        }


        IScene scene = this.game.getScene();
        if (scene != null) {
            scene.draw(g, this.getWidth(), this.getHeight());
        }

        // text color
        g.setColor(45, 235, 45);

        // draw FPS on the bottom left
        g.drawString("FPS: " + fps,
                0,
                getHeight(),
                Graphics.BOTTOM | Graphics.LEFT);

    }

    public void run() {
        Graphics g = getGraphics();
        long start;
        long end; //��һ������
        long lastStart = System.currentTimeMillis();
        int timeStep = 1000 / MAX_FPS; //60fps
        int drawingTime;
        while (game.isPlaying()) {
            try {
                if (isShown()) {
                    start = System.currentTimeMillis();
                    // update the world if the player has moved
                    game.process((int) (start - lastStart));
                    lastStart = start;


                    // draw the Scene
                    drawScene(g);

                    // flush to the screen
                    flushGraphics();

                    //��ʾ�����ϴε�fps
                    end = System.currentTimeMillis();
                    drawingTime = (int) (end - start);
                    // calculate fps

                    // wait for a little and give the other threads
                    // the chance to run
                    if (drawingTime < timeStep) {
                        //���������MAX_FPS��
                        Thread.sleep(timeStep - drawingTime + TIME_THREAD_SLEEP);
                    } else {
                        //��ʱ�䴦����̡��������¼�
                        Thread.sleep(TIME_THREAD_SLEEP);
                    }

                    //��ʾ��FPS��ˢ�µ�FPS��ͬ
                    fps = 1000 / drawingTime;
                } else {
                    //������Ƶ�ʱ��
                    Thread.sleep(timeStep);
                }
            } catch (Exception e) {
                // ignore. Not much to but we want to keep the loop running
                Debug.handleException(e);
            }
        }

    }

    protected void keyReleased(int keyCode) {
        game.getCommand().push(translate(keyCode));
    }

    //ת�밴��
    public static int translate(int key) {
        switch (key) {
            case -1:
            case GameCanvas.KEY_NUM2:
                return GameCanvas.UP;
            case -2:
            case GameCanvas.KEY_NUM8:
                return GameCanvas.DOWN;
            case -4:        //right��Code����֪������������
            case GameCanvas.KEY_NUM6:
                return GameCanvas.RIGHT;
            case -3:
            case GameCanvas.KEY_NUM4:
                return GameCanvas.LEFT;
            case -5:
            case GameCanvas.KEY_NUM5:
                return GameCanvas.FIRE;
//            case GameCanvas.KEY_NUM3:
//                return GCanvas.KEY_RIGHT_MENU;
            case GameCanvas.KEY_NUM1:
                return GCanvas.KEY_LEFT_MENU;
            default:
                return key;
        }
    }

    protected void pointerPressed(int x, int y) {
        game.getPointerCommand().save(Common.EVENT_PRESSED, x, y);
    }

    protected void pointerReleased(int x, int y) {
        game.getPointerCommand().save(Common.EVENT_RELEASED, x, y);
    }
}
