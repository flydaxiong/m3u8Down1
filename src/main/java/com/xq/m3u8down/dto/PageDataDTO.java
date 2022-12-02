package com.xq.m3u8down.dto;

import java.util.List;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/21 0:23
 */
public class PageDataDTO<T> {
    // 总记录数
    private Long total;
    // 当前页记录数
    private Long perPage;
    // 当前页
    private Long currentPage;
    // 总共多少页
    private Long lastPage;
    private List<T> data;

    public String toString(boolean printData) {
        return "PageDataDTO{" +
                "total=" + total +
                ", perPage=" + perPage +
                ", currentPage=" + currentPage +
                ", lastPage=" + lastPage +
                '}';
    }

    @Override
    public String toString() {
        return "PageDataDTO{" +
                "total=" + total +
                ", perPage=" + perPage +
                ", currentPage=" + currentPage +
                ", lastPage=" + lastPage +
                '}';
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPerPage() {
        return perPage;
    }

    public void setPerPage(Long perPage) {
        this.perPage = perPage;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getLastPage() {
        return lastPage;
    }

    public void setLastPage(Long lastPage) {
        this.lastPage = lastPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
