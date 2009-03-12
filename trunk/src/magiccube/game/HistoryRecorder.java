/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import java.util.Stack;

/**
 *
 * @author Administrator
 */
public class HistoryRecorder {

    //private LoopStack_Int stack;
    private Stack stack;
    private GameEngine game;

    public HistoryRecorder(GameEngine game) {
        //stack = new LoopStack_Int(20);
        stack = new Stack();
        this.game = game;
    }

    public void addHistory(int mode, int mode_index, int clockwise) {
        //编码
        int historyItem = clockwise * game.getGameSize() * game.getGameSize() + mode * game.getGameSize() + mode_index;
        //stack.push(historyItem);
        stack.push(new Integer(historyItem));
    }

    //如果没有历史，返回null;如果有，返回int数组，对应为{mode,mode_index,clockwise}
    public int[] popHistory() {
        if (stack.empty()) {
            return null;
        }
        int historyItem = ((Integer) stack.pop()).intValue();
        int[] result = new int[3];
        //解码
        result[2] = historyItem / game.getGameSize() / game.getGameSize();
        result[0] = historyItem / game.getGameSize() % game.getGameSize();
        result[1] = historyItem % game.getGameSize();
        return result;
    }

    public boolean hasHistory() {
        return !stack.empty();
    }

    public void clearHistory() {
        //stack.clear();
        stack.removeAllElements();

    }
}
