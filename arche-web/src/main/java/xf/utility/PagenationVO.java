package xf.utility;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装分页信息
 */
public class PagenationVO implements Serializable {

    private List result = new ArrayList();

    /**
     * 每页显示的行数,默认为1
     */
    private int rownumperpage = 1;

    /**
     * 目前是哪一页 默认为第一页
     */
    private int currentPage = 1;

    /**
     * 总记录数
     */
    private int totalRownum;

    /**
     * 总页数
     */
    private int pageNums;

    /**
     * 页面显示的第一页(新的分页外观需求) 页面需要显示多个页码数
     */
    private int startPage = 1;

    /**
     * 页面显示的翻页的最后个页码
     */
    private int endPage = 1;

    /**
     * startPage - endPage 的距离 计算翻页时候用
     */
    private int pageStep = 9;

    public int getPageNums() {
        calcPages();
        if (pageNums <= 0) {
            pageNums = 1;
        }
        return pageNums;
    }

    public int getTotalRownum() {
        return totalRownum;

    }

    public void setTotalRownum(int totalRownum) {
        this.totalRownum = totalRownum;
        calcPages();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage <= 0) {
            this.currentPage = 1;
        } else {
            this.currentPage = currentPage;
        }
    }

    /**
     * 计算总页数
     */
    private void calcPages() {
        if (totalRownum <= 0) {
            totalRownum = 0;
            pageNums = 0;
            currentPage = 1;
        } else {
            if (totalRownum % rownumperpage == 0) {
                pageNums = totalRownum / rownumperpage;
            } else {
                pageNums = totalRownum / rownumperpage + 1;
            }
            if (currentPage > pageNums) {
                currentPage = pageNums;
            }
            if (pageNums <= pageStep) {
                startPage = 1;
            } else {
                startPage = currentPage - (pageStep / 2);
                if (startPage <= 0) {
                    startPage = 1;
                }
            }
            endPage = startPage + pageStep - 1;
            if (endPage > pageNums) {
                endPage = pageNums;
            }
        }
    }

    public int getStartPage() {
        return startPage;
    }

    public int getRownumperpage() {
        return rownumperpage;
    }

    public void setRownumperpage(int rownumperpage) {
        this.rownumperpage = rownumperpage;
    }

    /**
     * 获取当前页的第一条记录的位置
     */
    public int getFirstResult() {
        return (currentPage - 1) * rownumperpage;
    }

    public void setCurrentResult(int cur) {
        int k = cur / rownumperpage + 1;
        this.setCurrentPage(k);
    }

    public int getEndPage() {
        calcPages();
        return endPage;
    }

    public void setPageStep(int pageStep) {
        if ((pageStep < 10) && (pageStep > 1)) {
            this.pageStep = pageStep;
        }
    }

    public int getReverseEndResult() {
        int firstResut = getTotalRownum()
                - (this.getEndPage() - this.getCurrentPage())
                * this.getRownumperpage();
        return firstResut <= 0 ? 0 : firstResut;
    }

    public int getReverseFirstResult() {
        int endResult = this.getReverseEndResult() - this.getRownumperpage();
        return endResult <= 0 ? 0 : endResult;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public int getEndRowNum() {
        return getStartRowNum() + rownumperpage - 1;
    }

    public int getStartRowNum() {
        calcPages();
        return (currentPage - 1) * rownumperpage + 1;
    }

    public void setPageNums(int pageNums) {
        this.pageNums = pageNums;
    }

}

