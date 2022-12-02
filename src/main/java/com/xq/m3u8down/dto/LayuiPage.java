package com.xq.m3u8down.dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/21 23:16
 */
public class LayuiPage<T> {
    private int code = 0;
    private long count = 0;
    private List<T> data;

    public LayuiPage() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public LayuiPage(List<T> data) {
        this.code = 0;
        this.data = data;
    }

    public static <T> LayuiPage<T> convert(Page<T> page) {
        LayuiPage<T> layuiPage = new LayuiPage<>(page.toList());
        layuiPage.setCount(page.getTotalElements());
        return layuiPage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
