/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Anic
 */
public class TaskCanvas extends GameCanvas {

    /**
     * 运行任务
     * @param name 任务名称
     * @param tasks 任务列表
     * @param midlet midlet
     * @param canvas 恢复的画布
     */
    public static void run(String name, Runnable[] tasks, MIDlet midlet, Canvas canvas) {
        new TaskCanvas(name, tasks, midlet, canvas, null);
    }

    /**
     * 运行任务
     * @param name 任务名称
     * @param tasks 任务列表
     * @param midlet midlet
     * @param callback 回调函数
     */
    public static void run(String name, Runnable[] tasks, MIDlet midlet, IEventListener callback) {
        new TaskCanvas(name, tasks, midlet, null, callback);
    }

    /**
     * 事件监听
     */
    public interface IEventListener {

        /**
         * 任务执行完
         * @param src 执行者
         */
        public void onFinishing(Object src);
    }

    /**
     * 执行任务的线程
     */
    public class TaskThread implements Runnable {

        private Object parent;

        public TaskThread(Object parent) {
            this.parent = parent;
        }

        public void run() {
            for (int i = 0; i < tasks.length; ++i) {
                tasks[i].run();
            }
            //完成任务后，timer取消
            timer.cancel();
            
            if (canvas != null) {
                Display.getDisplay(midlet).setCurrent(canvas);
            } else if (callback != null) {
                callback.onFinishing(parent);
            }
        }
    }

    /**
     * 用于绘制的进程，用计时器实现
     */
    public class DrawingThread extends TimerTask {

        public void run() {
            draw(getGraphics());
        }
    }

    public TaskCanvas(String name, Runnable[] tasks, MIDlet midlet, Canvas canvas, IEventListener callback) {
        super(false);
        //设置全屏模式
        setFullScreenMode(true);
        Display.getDisplay(midlet).setCurrent(this);

        this.tasks = tasks;
        this.midlet = midlet;
        this.callback = callback;
        this.canvas = canvas;
        this.title = name;

        //绘制
        timer = new Timer();
        timer.schedule(new DrawingThread(), 0, 1000 / 60);

        //执行任务
        Thread taskThread = new Thread(new TaskThread(this));
        taskThread.start();

    }
    private Runnable[] tasks;
    private MIDlet midlet;
    private IEventListener callback;
    private Canvas canvas;
    private Timer timer;

    /**
     * 绘制加载条
     * @param g
     */
    private void drawLoading(Graphics g) {
        g.setColor(0);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(0x00ffffff);

        int barWidth = this.getWidth() - 1;
        int blockY = this.getHeight() / 2 - BLOCK_HEIGHT / 2;
        int blockX = barWidth * current / MAX;
        int blockWidth = (blockX + BLOCK_WIDTH > barWidth) ? barWidth - blockX : BLOCK_WIDTH;

        g.drawRect(0, blockY, barWidth, BLOCK_HEIGHT);
        g.fillRect(blockX, blockY, blockWidth, BLOCK_HEIGHT);

        int textWidth = Common.FONT_DEFAULT.stringWidth(title);
        int textX = (this.getWidth() - textWidth) / 2;
        int textY = blockY - Common.FONT_DEFAULT.getHeight() - 10;
        g.drawString(title, textX, textY, 0);
    }
    private int defaultProcess = MAX / 10;
    private int current = -defaultProcess;
    private static final int MAX = 100;
    private static final int BLOCK_WIDTH = 30;
    private static final int BLOCK_HEIGHT = 10;
    private String title; //= "加载中...";

    /**
     * 处理
     * @param spend
     */
    private void process(int process) {
        current = (current + process) % MAX;
    }

    private void draw(Graphics g) {
        // update the world if the player has moved
        process(defaultProcess);
        // draw the Scene
        drawLoading(g);
        // flush to the screen
        flushGraphics();
    }
}
