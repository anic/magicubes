/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.engine;

import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.Node;
import javax.microedition.m3g.World;
import javax.microedition.rms.RecordEnumeration;
import magiccube.game.*;
import magiccube.game.model.Cube;
import magiccube.util.Debug;
import javax.microedition.rms.RecordStore;
import magiccube.game.animation.AnimationEngine;
import magiccube.game.model.CoreCube;
import magiccube.util.ArrayUtil;
import magiccube.util.DrawingUtil;

/**
 * ����Cube��ħ������
 * @author Anic
 */
public class CubeBasedEngine extends MagicCubeEngine {

    public Camera getCamera() {
        return this.camera;
    }
    /**
     * �洢����
     */
    private static final String STORE_NAME = "ST_CUBES_" + Common.VERSION;
    /**
     * ��������Cube������
     */
    private Cube[] cubes;
    /**
     * ��¼ѡ�е�����
     */
    private Cube[] selected_cubes_array;
    private int[] selected_cubes_index;
    //private Cube[] selected_cubes_array_8;
    private FaceEngine faceEngine;
    /**
     * ��¼��ɫ������
     */
    private float angle;
    private int[] axis; //ת���ᣬ��Ϊ���Ǳ�׼�ᣬ����int�㹻
    private GameEngine game;
    private int totalTime; //������ʱ��
    private int[][] cube_ref;
    private int[][] rotate_ref;
    private static final byte ID_CUBE = (byte) 127;
    private static final byte ID_FACE = (byte) 126;
    private static final byte ID_VERSION = (byte) 125;
    private World world;
    private ArrowSelector arrow;
    private Camera camera;

    /**
     * ��������Cube��ħ������
     * @param game ��Ϸ����
     * @param localEngine
     */
    public CubeBasedEngine(GameEngine game, AnimationEngine localEngine) {
        super(game.getGameSize(), null);

        //�޸���Ӧ����
        arrow = new ArrowSelector(game.getGameSize(),
                game.getAnimationEngine(),
                localEngine);



        super.eventListener = arrow;
        this.game = game;

        //��ʼ����������Size�йصĳ�ʼ������init�������
        axis = new int[3];
        angle = 0f;
    }

    /**
     * ����ѡ�е�Cube,ע�����ڿյ�Cube
     * @return ѡ�е�Cube
     */
    private Cube[] getSelectedCube() {
        if (getMode() == FACE_NONE) {
            return null;
        } else {
            return selected_cubes_array;
        }

    }

    protected void beforeModifyCubeByMode(int mode, int index, int clockwise) {
        //index�ķ�Χֻ��0��1��2
        switch (mode) {
            case FACE_AXIS_X:
                int[] indexOfArrayX = createCubeIndex(mode);
                //new int[]{0, 9, 18, 3, 12, 21, 6, 15, 24};
                for (int i = 0; i < indexOfArrayX.length; ++i) {
                    selected_cubes_index[i] = indexOfArrayX[i] + index;
                    selected_cubes_array[i] = this.cubes[selected_cubes_index[i]];
                    
                }

                break;
            case FACE_AXIS_Y:
                //int[] indexOfArrayY = new int[]{18, 19, 20, 9, 10, 11, 0, 1, 2};
                int[] indexOfArrayY = createCubeIndex(mode);
                for (int i = 0; i < indexOfArrayY.length; ++i) {
                    selected_cubes_index[i] = indexOfArrayY[i] + size * (size - 1 - index);
                    selected_cubes_array[i] = this.cubes[selected_cubes_index[i]];
                }
                break;
            case FACE_AXIS_Z:
                //int[]{0,1,2,3,4,5,6,7,8}
                int[] indexOfArrayZ = createCubeIndex(mode);
                for (int i = 0; i < size * size; ++i) {
                    selected_cubes_index[i] = (size - 1 - index) * size * size + indexOfArrayZ[i];
                    selected_cubes_array[i] = cubes[selected_cubes_index[i]];
                }
                break;
        }
    }

    public boolean isFaceSameColor(int face_index) {
        //ûʵ��
        return false;
    }

//    public Node[] getAllObject() {
//        Vector result = new Vector();
//        for (int i = 0; i < cubes.length; ++i) {
//            if (cubes[i] != null) {
//                result.addElement(cubes[i].getMesh());
//            }
//        }
//        result.addElement(core.getMesh());
//
//        int len = result.size();
//        Node[] result3D = new Node[len];
//        for (int i = 0; i < len; ++i) {
//            result3D[i] = (Node) result.elementAt(i);
//        }
//        return result3D;
//    }
    public void init(boolean load) {

        int aSize = 0;
        if (load) {
            aSize = this.getSavedSize();
        }

        if (aSize < 2) {
            aSize = game.getGameSize();
            load = false;
        }

        //��ʼ��size
        size = aSize;
        cubes = new Cube[size * size * size];
        selected_cubes_array = new Cube[size * size];
        selected_cubes_index = new int[size * size];

        rotate_ref = new int[][]{
                    {0, size * (size - 1), size * size - 1, size - 1}, //startId
                    {size, 1, -size, -1},//dx
                    {1, -size, -1, size}//dy
                };
        //�����������
        cube_ref = new int[][]{
                    //startId,dx,dy
                    {0, size * size, size},
                    {size * size * (size - 1), 1, -size * size},
                    {0, 1, size}
                };

        if (load) {
            loadCubes();
        } else {
            createCubes();
        }

        //���ö�Ӧ
        setMode(getMode(), getModeIndex(), getClockwise());


        //��������
        this.world = new World();

        //��Ӽ�ͷ
        Node[] arrowObj = arrow.getObject3D();
        for (int i = 0; i < arrowObj.length; ++i) {
            world.addChild(arrowObj[i]);
        }

        //��Ӻ���
        CoreCube core = new CoreCube((byte) 128, (byte) 128, (byte) 128, this.size);
        world.addChild(core.getMesh());

        //��ӷ���
        for (int i = 0; i < cubes.length; ++i) {
            if (cubes[i] != null) {
                world.addChild(cubes[i].getMesh());
            }
        }

        camera = new Camera();
        world.addChild(camera);
        world.setActiveCamera(camera);
    }

    private void createCubes() {
        for (int i = 0; i < size * size * size; ++i) {
            int[] temp_cube_faces = new int[6];
            int count = 0;
            float[] pos = getCord(i);
            for (int j = 0; j < 6; ++j) {
                if (isInFace(i, j)) {
                    temp_cube_faces[count++] = j;
                }
            }
            if (count == 0) {
                continue;
            }

            int[] cube_faces = new int[count];
            ArrayUtil.copyArray(temp_cube_faces, 0, count, cube_faces, 0);

            cubes[i] = new Cube(i, cube_faces);
            cubes[i].setPosition(pos[0], pos[1], pos[2]);
        }

        faceEngine = new FaceEngine(size);
    }

    private int[] createCubeIndex(int mode) {
        int[] result = new int[size * size];
        int id = cube_ref[mode][0];
        int dx = cube_ref[mode][1];
        int dy = cube_ref[mode][2];
        for (int i = 0; i < size; ++i) {
            int startId = id;
            for (int j = 0; j < size; ++j) {
                result[i * size + j] = (id + j * dx);
            }
            id = startId + dy;
        }
        return result;
    }

    //�����ж��ٸ�����ɫ��ͬ
    public int countSameColorFaces() {
        int count = 0;
        for (int i = 0; i < 6; ++i) {
            int[] face = faceEngine.getFace(i);
            int start = face[0];
            boolean same = true;
            for (int j = 1; j < face.length; ++j) {
                if (face[j] != start) {
                    same = false;
                    break;
                }
            }

            if (same) {
                count++;
            }
        }
        return count;
    }

    private void deleteRecord() {
        try {
            RecordStore.deleteRecordStore(STORE_NAME);
        } catch (javax.microedition.rms.RecordStoreNotFoundException e) {
        } catch (Exception e) {
            Debug.handleException(e);
        }
    }

    public void save() {

        try {
            deleteRecord();
            RecordStore rs = RecordStore.openRecordStore(STORE_NAME, true);
            rs.addRecord(new byte[]{ID_VERSION, (byte) size}, 0, 2);
            for (int i = 0; i < cubes.length; ++i) {
                if (cubes[i] != null) {
                    byte[] buffer = cubes[i].toBytes();
                    byte[] id = new byte[buffer.length + 2];
                    id[0] = ID_CUBE;
                    id[1] = (byte) i;
                    ArrayUtil.copyArray(buffer, 0, buffer.length, id, 2);
                    rs.addRecord(id, 0, id.length);

                }
            }
            byte[] buffer = faceEngine.toByte();
            byte[] bufFace = new byte[buffer.length + 1];
            bufFace[0] = ID_FACE;
            ArrayUtil.copyArray(buffer, 0, buffer.length, bufFace, 1);
            rs.addRecord(bufFace, 0, bufFace.length);

            rs.closeRecordStore();

        } catch (Exception e) {
            Debug.handleException(e);
        }

    }

    private int getSavedSize() {
        try {
            RecordStore rs = RecordStore.openRecordStore(STORE_NAME, false);
            RecordEnumeration en = rs.enumerateRecords(null, null, false);
            while (en.hasNextElement()) {
                byte[] record = en.nextRecord();
                if (record[0] == ID_VERSION) {
                    rs.closeRecordStore();
                    return (int) record[1];
                }
            }
            rs.closeRecordStore();
            return 0;
        } catch (Exception ex) {
            return 0;
        }
    }

    private void loadCubes() {
        try {
            RecordStore rs = RecordStore.openRecordStore(STORE_NAME, false);
            RecordEnumeration en = rs.enumerateRecords(null, null, false);
            while (en.hasNextElement()) {
                byte[] record = en.nextRecord();
                switch (record[0]) {
                    case ID_CUBE:
                        performLoadCube(record);
                        break;
                    case ID_FACE:
                        performLoadFace(record);
                        break;
                    case ID_VERSION:
                        break;
                    default:
                        break;
                }
            }
            rs.closeRecordStore();
        } catch (Exception e) {
            Debug.handleException(e);
        }

    }

    private void performLoadCube(byte[] src) {
        int index = (int) src[1];
        cubes[index] = Cube.fromBytes(src, 2, src.length - 2);
    }

    private void performLoadFace(byte[] src) {
        faceEngine = FaceEngine.fromBytes(src, 1, src.length - 1);
    }

    public void before_end() {
        //��cubes�Ķ�������޸�

        float dif = (totalAngle > 0) ? totalAngle - 90f : totalAngle + 90f;

        Cube[] selected = getSelectedCube();
        if (selected != null) {
            for (int i = 0; i <
                    selected.length; ++i) {
                //���ԣ������ۻ�����
                if (selected[i] != null) {
                    selected[i].rotate(-dif, axis[0], axis[1], axis[2]);
                }

            }
        }
        swapFace(this.selected_cubes_index, getClockwise());
        faceEngine.rotate(this.getMode(), this.getModeIndex(), this.getClockwise());

    }
    private float totalAngle = 0f; //�ۻ��Ƕ�

    public void process(int spendTime, int currentTime) {

        Cube[] selected = getSelectedCube();
        if (selected != null) {
            for (int i = 0; i <
                    selected.length; ++i) {
                if (selected[i] != null) {
                    selected[i].rotate(angle * spendTime / totalTime, axis[0], axis[1], axis[2]);
                }

            }
            totalAngle += angle * spendTime / totalTime;
        }

    }

    public void before_start(int totalTime) {
        Debug.println("mode:" + this.getMode() + " index:" + this.getModeIndex() + " clockwise:" + this.getClockwise());
        this.totalTime = totalTime;
        angle = (getClockwise() == MODE_CLOCKWISE) ? 90f : -90f;
        //ת����
        this.getAxis(this.axis);
        totalAngle =0f;

    }

    private boolean isInFace(int indexOfCube, int faceIndex) {
        switch (faceIndex) {
            case Cube.FACE_FONT:
                return indexOfCube / (size * size) == 0;
            case Cube.FACE_BACK:
                return indexOfCube / (size * size) == size - 1;
            case Cube.FACE_LEFT:
                return indexOfCube % size == 0;
            case Cube.FACE_RIGHT:
                return indexOfCube % size == size - 1;
            case Cube.FACE_TOP:
                return indexOfCube % (size * size) / size == 0;
            case Cube.FACE_BOTTOM:
                return indexOfCube % (size * size) / size == size - 1;
            default:
                return false;
        }

    }
    private static final float CUBE_WIDTH = 1f;

    private float[] getCord(int index) {
        float pos[] = new float[3];
        int i, j, k;
        i =
                index % size;
        pos[0] = -(float) size / 2 + CUBE_WIDTH / 2 + CUBE_WIDTH * i;
        j =
                (index / size) % size;
        pos[1] = (float) size / 2 - CUBE_WIDTH / 2 - CUBE_WIDTH * j;
        k =
                index / (size * size);
        pos[2] = (float) size / 2 - CUBE_WIDTH / 2 - CUBE_WIDTH * k;
        return pos;
    }

    private void swapFace(int[] selectedId, int clockwise) {
        int[] id = new int[4];
        ArrayUtil.copyArray(rotate_ref[0], 0, rotate_ref[0].length, id, 0);
        int[] stepX = rotate_ref[1];
        int[] stepY = rotate_ref[2];
        for (int k = 0; k <
                size / 2; k++) {
            int[] tempId = new int[id.length];
            ArrayUtil.copyArray(id, 0, id.length, tempId, 0);
            for (int i = 0; i <
                    (size + 1) / 2; ++i) {
                cycleShift(new int[]{selectedId[id[0]],
                            selectedId[id[1]],
                            selectedId[id[2]],
                            selectedId[id[3]]}, clockwise);
                setNextStep(id, stepX);
            }

            id = tempId;
            setNextStep(id, stepY);
        }

    }

    private void cycleShift(int[] id, int clockwise) {

        Cube valueOf0 = this.cubes[id[0]];
        if (clockwise == MagicCubeEngine.MODE_CLOCKWISE) {
            this.cubes[id[0]] = this.cubes[id[3]];
            this.cubes[id[3]] = this.cubes[id[2]];
            this.cubes[id[2]] = this.cubes[id[1]];
            this.cubes[id[1]] = valueOf0;
        } else {
            this.cubes[id[0]] = this.cubes[id[1]];
            this.cubes[id[1]] = this.cubes[id[2]];
            this.cubes[id[2]] = this.cubes[id[3]];
            this.cubes[id[3]] = valueOf0;
        }

    }

    private void setNextStep(int[] id, int step[]) {
        for (int i = 0; i <
                id.length; ++i) {
            id[i] = id[i] + step[i];
        }

    }

    public void setSelection(boolean highlight) {
        Cube[] oper = highlight ? this.selected_cubes_array : cubes;

        for (int i = 0; i <
                oper.length; ++i) {
            if (oper[i] != null) {
                setCubeHighLight(oper[i], highlight);
            }

        }

    }

    private void setCubeHighLight(Cube cube, boolean highlight) {
        if (highlight) {
            cube.setAlpha(0.7f);
        } else {
            cube.setAlpha(1f);
        }

    }

    public void draw(Graphics g, int width, int height) {
        DrawingUtil.draw3DByWorld(g, world);
    }

    public FaceEngine getFaceEngine() {
        return this.faceEngine;
    }
}
