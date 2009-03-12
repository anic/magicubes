/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magiccube.util;

/**
 *
 * @author Administrator
 */
public class ArrayUtil {
    
    public static void copyArray(Object source,int start,int count,Object destination,int dstart)
    {
        System.arraycopy(source, start, destination, dstart, count);
    }

    //数组是否包含某个元素
    public static boolean contain(int[] array, int item)
    {
        for(int i=0;i<array.length;++i)
            if (item == array[i])
                return true;
        return false;
    }

    public static int sum(int[] array)
    {
        int result = 0;
        for(int i=0;i<array.length;++i)
            result += array[i];

        return result;
    }
}
