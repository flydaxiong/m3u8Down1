package com.xq.m3u8down.service.m3u8;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: m3u8解密信息
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/20 12:41
 */
public class M3u8Info {
    private String method;
    private byte[] key;
    private byte[] iv;
    private String relativeUrl;

    private Map<Integer, URI> uriMap;

    @Override
    public String toString() {
        return "M3u8Info{" +
                "method='" + method + '\'' +
                ", key=" + Arrays.toString(key) +
                ", iv=" + Arrays.toString(iv) +
                ", baseUrl='" + relativeUrl + '\'' +
                '}';
    }

    public M3u8Info() {
        uriMap = new HashMap<>();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public void addUri(int index, URI uri) {
        this.uriMap.put(index, uri);
    }

    public Map<Integer, URI> getUriMap() {
        return uriMap;
    }

    public String getMethod() {
        return method;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getIv() {
        return iv;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }
}
