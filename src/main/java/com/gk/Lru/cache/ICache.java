package com.gk.Lru.cache;

import java.util.UUID;

/**
 * Created by gkang on 1/16/17.
 */
public interface ICache<T> {
    public void put(T key, Object value);
    public Object get(T key);
}
