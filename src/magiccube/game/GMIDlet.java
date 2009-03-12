package magiccube.game;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class GMIDlet extends MIDlet {

    /**
     * ��Ϸ����
     */
    private GameEngine myGame;

    public class LoadingThread implements Runnable, TaskCanvas.IEventListener {

        public void onFinishing(Object src) {
            Display.getDisplay(midlet).setCurrent(myGame.getCanvas());
        }
        private GMIDlet midlet;

        public LoadingThread(GMIDlet midlet) {
            this.midlet = midlet;
        }

        public void run() {
            myGame = new GameEngine(midlet);
            myGame.initGame();
            myGame.startGame();
        }
    }

    /**
     * ��������
     */
    public GMIDlet() {
    }

    /**
     *	This initializes the game state, and generates a M3G world programmatically.
     * @throws MIDletStateChangeException
     */
    public void startApp() throws MIDletStateChangeException {
        // Catch excpetions here before they go too far.
        LoadingThread task = new LoadingThread(this);
        TaskCanvas.run("������", new Runnable[]{task}, this, task);
    }

    /**
     * On exit, simply shut everything down
     * @param unconditional
     * @throws MIDletStateChangeException 
     */
    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {

        // Release resources.
        if (myGame.getAnimationEngine().isMoving()) {
            myGame.getAnimationEngine().process(10000);//��10s���¼�
        }
        //���û��ֶ�����
        //myGame.save();
        myGame.releaseResources();
    }

    protected void pauseApp() {
    }
}
    
    
