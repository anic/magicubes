/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import magiccube.util.ArrayUtil;

/**
 *
 * @author Administrator
 */
public class GTask {

    public static final int LEVEL_EASY = 0;
    public static final int LEVEL_NORMAL = 1;
    public static final int LEVEL_HARD = 2;
    private boolean bFinish;
    private int level;
    private int target_finish_face;
    private int[] init_array;
    private int id;

    public GTask(int id, int target,int level, int[] init_array) {
        this.id = id;
        this.level = level;
        this.target_finish_face = target;
        this.init_array = new int[init_array.length];
        ArrayUtil.copyArray(init_array, 0, init_array.length, this.init_array, 0);

        bFinish = false;
    }

    public int getId() {
        return this.id;
    }

    public String getDescription() {
        String strFin = (bFinish) ? "已完成" : "未完成";
        return "目标" + this.target_finish_face + "面," + this.getLevelString() + "," + strFin;
    }

    public boolean isFinished() {
        return bFinish;
    }

    public void setFinished(boolean value) {
        this.bFinish = value;
    }

    public void setLevel(int value) {
        this.level = value;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelString() {
        switch (level) {
            case LEVEL_EASY:
                return "简单";
            case LEVEL_NORMAL:
                return "一般";
            case LEVEL_HARD:
                return "困难";
            default:
                return "";
        }
    }

    public boolean checkFinish(int count) {
        return target_finish_face == count;
    }

    public int[] getTaskInitArray() {
        return init_array;
    }
}
