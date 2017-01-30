package com.gk.Lru.cache;

/**
 * Created by gkang on 1/22/17.
 */
public class CacheEntity<T> {
    private final Object value;
    private final T key;
    private boolean inCache = false;

    public CacheEntity(T key, Object value) {
        this.value = value;
        this.key = key;
    }

    public void setInCache(boolean inCache) {
        this.inCache = inCache;
    }

    public boolean getInCache() {
        return inCache;
    }

    public Object getValue() {
        return value;
    }

    public T getKey() {
        return key;
    }
}
