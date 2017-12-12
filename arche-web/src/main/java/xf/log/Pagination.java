package xf.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pagination implements Serializable {
    
	private static final long serialVersionUID = 1L;
	public static final String DIRECTION_DESC = "DESC";
    public static final String DIRECTION_ASC = "ASC";
    private int start;
    private int limit = 50;
    private String sort;
    private String dir;
    private boolean needCount;
    private int totalCount;

    private List resultList = new ArrayList();

    public Pagination() {
    }

    public List getResultList() {
        return this.resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    public final int getTotalCount() {
        return this.totalCount;
    }

    public final void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public final void calStart() {
        if (this.start >= this.totalCount) {
            this.start = (this.totalCount - 1) / this.limit * this.limit;
        }

    }

    public final void setPgNumber(int pgNumber) {
        if (pgNumber < 1) {
            pgNumber = 1;
        }

        this.start = (pgNumber - 1) * this.limit;
    }

    public final int getPgNumber() {
        return this.start / this.limit + 1;
    }

    public final int getEnd() {
        return this.start + this.limit;
    }

    public final int getStart() {
        return this.start;
    }

    public final void setStart(int start) {
        this.start = start;
    }

    public final int getLimit() {
        return this.limit;
    }

    public final void setLimit(int limit) {
        this.limit = limit;
    }

    public final boolean isNeedCount() {
        return this.needCount;
    }

    public final void setNeedCount(boolean needCount) {
        this.needCount = needCount;
    }

    public final String getSort() {
        return this.sort;
    }

    public final void setSort(String sort) {
        this.sort = sort;
    }

    public final String getDir() {
        return this.dir;
    }

    public final void setDir(String dir) {
        this.dir = dir;
    }
}

