/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game;

import java.util.Vector;
import javax.microedition.rms.RecordStore;
import magiccube.util.Converter;
import magiccube.util.Debug;
import magiccube.util.ReaderUtil;
import magiccube.util.StringUtil;

/**
 *
 * @author Administrator
 */
public class TaskLoader {

    private Vector tasks;
    private int index_task;
    private static final String TASK_STORE = "ST_MAGIC_TASK";
    private static final int RECORD_STORE = 1;
    private static final byte VALUE_UN_FINISH = 0;
    private static final byte VALUE_FINISH = 1;
    private GameEngine game;

    public TaskLoader(GameEngine game) {
        tasks = new Vector();
        index_task = 0;
        this.game = game;
    }

    public void load() {

        //º”‘ÿ»ŒŒÒ
        tasks.removeAllElements();
        ReaderUtil reader = new ReaderUtil();
        String result = reader.loadUTF8Text(Common.FILE_TASK);
        if (result != null) {
            String[] lines = StringUtil.split(result, Common.SYMBOL_SPLIT);
            for (int i = 0; i < lines.length; ++i) {
                GTask task = generateTask(lines[i], i);
                //Debug.println(task.getDescription());
                tasks.addElement(task);
            }
        }
        byte[] data = loadRecord();
        if (data != null) {
            loadRecordValue(data);
        }

    }

    private GTask generateTask(String text, int id) {
        String[] items = StringUtil.split(text, " ");
        //Debug.println("items " + Debug.toString(items));
        int[] intArray = new int[items.length - 2];
        for (int j = 2; j < items.length; ++j) {
            intArray[j - 2] = Integer.valueOf(items[j], 10).intValue();
        }
        return new GTask(id, Integer.valueOf(items[0], 10).intValue(), Integer.valueOf(items[1], 10).intValue(), intArray);

    }

    private byte[] loadRecord() {
        try {
            RecordStore rs = RecordStore.openRecordStore(TASK_STORE, false);
            int size = rs.getNumRecords();

            byte[] result = null;
            if (size > 0) {
                result = rs.getRecord(RECORD_STORE);
            }
            rs.closeRecordStore();
            return result;

        } catch (Exception e) {
            //Debug.handleException(e);
            return null;
        }

    }

 

    public void save() {
        try {
            RecordStore rs = RecordStore.openRecordStore(TASK_STORE, true);
            int num = rs.getNumRecords();
            byte[] data = generateRecord();
            if (num > 0) {
                rs.setRecord(RECORD_STORE, data, 0, data.length);
            } else {
                rs.addRecord(data, 0, 1);
            }
            rs.closeRecordStore();

        } catch (Exception e) {
            Debug.handleException(e);
        }
    }

    private byte[] generateRecord() {
        int taskCount = tasks.size();
        int size = taskCount + Converter.BYTES_PER_INT;
        byte[] result = new byte[size];
        GTask task = game.getGameTask();
        int gameTaskId;
        if (task != null) {
            gameTaskId = task.getId();
        } else {
            gameTaskId = GameEngine.ID_NONE_TASK;
        }
        Converter.intToBytes2(gameTaskId, result, 0);

        for (int i = 0; i < taskCount; ++i) {
            if (((GTask) tasks.elementAt(i)).isFinished()) {
                result[i + Converter.BYTES_PER_INT] = VALUE_FINISH;
            } else {
                result[i + Converter.BYTES_PER_INT] = VALUE_UN_FINISH;
            }
        }

        return result;
    }

    private void loadRecordValue(byte[] record) {
        //Debug.println("load " + Debug.toString(record));

        if (record.length == 1 && record[0] == 0) {
            //Debug.println("id translate 0");
            game.setTaskId(0);
        }

        if (record.length > Converter.BYTES_PER_INT) {
            int id = Converter.byteToInt2(record, 0);
            //Debug.println("id " + id);
            game.setTaskId(id);
        }

        int offset = Converter.BYTES_PER_INT;
        int size = (record.length - Converter.BYTES_PER_INT);
        if (size > tasks.size()) {
            size = tasks.size();
        }

        for (int i = 0; i < size; ++i) {
            GTask task = (GTask) tasks.elementAt(i);
            task.setFinished(record[i + offset] == VALUE_FINISH);
        }
    }

    public GTask getCurrentTask() {
        if (tasks.isEmpty()) {
            return null;
        } else {
            return (GTask) tasks.elementAt(index_task);
        }
    }

    public int getCurrentIndex() {
        return index_task;
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public int getFinishCount() {
        int count = 0;
        for (int i = 0; i < tasks.size(); ++i) {
            if (((GTask) tasks.elementAt(i)).isFinished()) {
                count++;
            }
        }
        return count;
    }

    public GTask getTask(int id) {
        if (id < 0 || id >= tasks.size()) {
            return null;
        } else {
            return (GTask) tasks.elementAt(id);
        }
    }

    public void nextTask(boolean next) {
        int len = tasks.size();
        if (next) {
            this.index_task = (this.index_task + 1) % len;
        } else {
            this.index_task = (this.index_task + len - 1) % len;
        }
    }
}
