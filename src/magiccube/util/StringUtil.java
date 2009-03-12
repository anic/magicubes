/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.util;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class StringUtil {

    public static String[] splitByNumbers(String content, int words_per_line) {
        Vector result = new Vector();
        int startIndex = 0;
        int endIndex = (startIndex + words_per_line < content.length()) ? startIndex + words_per_line : content.length();

        while (startIndex < content.length()) {
            result.addElement(content.substring(startIndex, endIndex));
            startIndex = endIndex;
            endIndex = (startIndex + words_per_line < content.length()) ? startIndex + words_per_line : content.length();
        }

        if (result.isEmpty()) {
            return new String[]{};
        } else {
            String[] strResult = new String[result.size()];
            for (int i = 0; i < strResult.length; ++i) {
                strResult[i] = (String) result.elementAt(i);
            }
            return strResult;
        }
    }

    //Split操作
    public static String[] split(String content, String splitItem) {
        Vector result = new Vector();
        int cur = 0;
        int index = content.indexOf(splitItem, cur);

        while (index != -1) {
            String s = content.substring(cur, index);
            if (s.length() != 0) //空的不输入
            {
                result.addElement(s);
            }
            cur = index + splitItem.length();
            index = content.indexOf(splitItem, cur);
        }

        String s = content.substring(cur);
        if (s.length() != 0) //空的不输入
        {
            result.addElement(s);
        }

        String[] strResult = new String[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            strResult[i] = (String) result.elementAt(i);
        }
        return strResult;

    }
}
