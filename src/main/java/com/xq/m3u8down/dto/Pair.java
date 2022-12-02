package com.xq.m3u8down.dto;

import java.util.Map;
import java.util.function.Function;

/**
 * Description:
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/20 16:11
 */
public class Pair<K,V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K,V> Pair<K,V> of(K k, V v) {
        return new Pair<>(k, v);
    }

    public static <K,V> Pair<K,V> of(Map.Entry<K, V> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    public static <K, R, T> Pair<K,T> map(Pair<K,R> pair, Function<R, T> function) {
        return new Pair<>(pair.getKey(), function.apply(pair.getValue()));
    }

    public static <K, R, T> Function<Pair<K,R>, Pair<K,T>> valueMap(Function<R, T> function) {
        return pair -> Pair.of(pair.getKey(), function.apply(pair.getValue()));
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
