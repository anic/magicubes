/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magiccube.game.reader;

import magiccube.game.*;
import java.util.Vector;
import magiccube.util.ReaderUtil;
import magiccube.util.StringUtil;

/**
 *
 * @author Administrator
 */
public class GReader {

    private Vector pages = new Vector();
    private int pageIndex = 0;


    public GReader() {
    }

    public void load(String name, int width, int height) {
        pages.removeAllElements();

        //GPage page = new GPage(); //先建立一个默认的Page

        ReaderUtil util = new ReaderUtil();
        String text = util.loadUTF8Text(name);
        String[] strSegments = StringUtil.split(text, Common.SYMBOL_SPLIT);

        int offset = 0;
        int len = 0;
        for (int i = 0; i < strSegments.length; ++i) {
            len++;
            if (strSegments[i].startsWith(Common.PREFIX_NEW_PAGE)) {
                //将页面加入页面列表
                GPage page = new GPage();
                page.set(strSegments, offset, len - 1);
                pages.addElement(page);
                //Debug.println("offset " + offset + " len " + len);

                //重置offset 和 len
                offset = i + 1;
                len = 0;
            }
        }
        //最后一页
        //Debug.println("offset " + offset + " len " + len);
        GPage page = new GPage();
        page.set(strSegments, offset, len);
        pages.addElement(page);
    }

    public GPage getCurrentPage() {
        if (pages.isEmpty()) {
            return null;
        } else {
            GPage page = (GPage) pages.elementAt(pageIndex);
            if (!page.isLoaded()) {
                //Debug.println("load page");
                page.load();
            }
            return page;
        }

    }

    public void setPageIndex(int index) {
        if (index < pages.size() && index >= 0) {
            this.pageIndex = index;
        }
    }

    public void nextPage() {
        this.pageIndex = (this.pageIndex == pages.size() - 1) ? this.pageIndex : this.pageIndex + 1;
    }

    public void previousPage() {
        this.pageIndex = (this.pageIndex == 0) ? 0 : this.pageIndex - 1;
    }

    public int getCurrentIndex() {
        return this.pageIndex;
    }

    public int getPageCount() {
        return this.pages.size();
    }
}
