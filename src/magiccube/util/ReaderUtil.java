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

    //��ȡ����Unicode big endian�����ı��ļ�
    public String loadUnicodeText(String name) {
        String strReturn = "";
        InputStream in = null;
        byte[] word_utf = new byte[MAX_LENGTH];
        try {
            in = getClass().getResourceAsStream(name);
            in.read(word_utf);
            in.close();
            strReturn = new String(word_utf, "UTF-16BE");//��һ��������Ϊ"UTF-8"���ɶ�ȡUTF-8�����
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            in = null;
        }
        return strReturn;
    }

    //��ȡ����UTF��8������ı��ļ�(û����)
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
                System.out.println("�����������ﵽ��ȡ���ޡ�");
            }

        } catch (Exception e) {
            System.out.println("��ȡ����UTF��8������ı�����:" + e.toString());
        } finally {
            in = null;
        }
        strReturn = strReturn.substring(1);
        return strReturn;
    }
}
