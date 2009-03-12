package magiccube.game;
/*
 * KeyCommand.java
 *
 * Created on 2006年9月9日, 下午2:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

/**
 *
 * @author cya
 */
public class KeyCommand {

    private int content = EMPTY;
    private static final int EMPTY = -1;

    /**
     * Creates a new instance of KeyCommand
     */
    public KeyCommand() {
    }

    public void push(int item) {
        content = item;
    }

    public boolean isEmpty() {
        return content == EMPTY;
    }

    public int pop() {
        if (!isEmpty()) {
            int result = content;
            content = EMPTY;
            return result;
        }
        return EMPTY;
    }
}
