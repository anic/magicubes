/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.util;

import java.io.InputStream;

/**
 *
 * @author Administrator
 */
public class ReaderUtil {

    public static final int MAX_LENGTH = 20 * 1024;

    //读取本地Unicode big endian编码文本文件
    public String loadUnicodeText(String name) {
        String strReturn = "";
        InputStream in = null;
        byte[] word_utf = new byte[MAX_LENGTH];
        try {
            in = getClass().getResourceAsStream(name);
            in.read(word_utf);
            in.close();
            strReturn = new String(word_utf, "UTF-16BE");//后一个参数改为"UTF-8"即可读取UTF-8编码的
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            in = null;
        }
        return strReturn;
    }

    //读取本地UTF－8编码的文本文件(没问题)
    public String loadUTF8Text(String name) {
        String strReturn = "";
        InputStream in = null;
        byte[] word_utf = new byte[MAX_LENGTH];
        try {
            in = getClass().getResourceAsStream(name);
            int len = in.read(word_utf);
            in.close();
            strReturn = new String(word_utf, 0, len, "UTF-8");
            if (len == MAX_LENGTH) {
                System.out.println("输入数据量达到读取上限。");
            }

        } catch (Exception e) {
            System.out.println("读取本地UTF－8编码的文本出错:" + e.toString());
        } finally {
            in = null;
        }
        strReturn = strReturn.substring(1);
        return strReturn;
    }
}
