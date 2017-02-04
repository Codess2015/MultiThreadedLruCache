package com.gk.Lru.cache;

import com.gk.Lru.linkedList.ListNode;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gkang on 1/16/17.
 */
public class LRUCacheMultiThreaded<T> implements ICache<T> {

    int cacheSize;
    int pendingQueueSize;
    AtomicBoolean isInterrupted = new AtomicBoolean(false);

    BlockingQueue<ListNode<CacheEntity<T>>> cachePurgeTaskQueue = new ArrayBlockingQueue<>(pendingQueueSize);
    PurgeCacheNotThreadSafe purgeCache;
    ConcurrentHashMap<T, ListNode<CacheEntity<T>>> map = new ConcurrentHashMap<>();
    ExecutorService purgeCacheExecutorService = Executors.newSingleThreadExecutor();

    public LRUCacheMultiThreaded(int size, int pendingQueueSize) {
        this.cacheSize = size;
        this.pendingQueueSize = pendingQueueSize;
        purgeCache = new PurgeCacheNotThreadSafe(cachePurgeTaskQueue, map, size);
    }

    public void put(T key, Object value){

        if (isInterrupted.get()) {
            throw new LRUCacheMultiThreadedCacheException("Cache unavailable because the thread was interrupted");
        }

        final CacheEntity<T> entity = new CacheEntity<>(key, value);
        ListNode<CacheEntity<T>> node = new ListNode<>(entity, null, null);

        try {
            // Only one thread will be able to insert.
            if (map.putIfAbsent(key, node) == null) {
                cachePurgeTaskQueue.put(node);
                // TODO: could change this to use FutureTask
                purgeCacheExecutorService.execute(purgeCache);
            }
        } catch (InterruptedException ex) {
            destroy();
            Thread.currentThread().interrupt();
        }
    }

    public Object get(T key) {
        if (isInterrupted.get()) {
            throw new LRUCacheMultiThreadedCacheException("Cache unavailable because the thread was interrupted");
        }

        ListNode<CacheEntity<T>> entity = map.get(key);
        if (entity != null) {
            try {
                cachePurgeTaskQueue.put(entity);
                // TODO: could change this to use FutureTask
                purgeCacheExecutorService.execute(purgeCache);
            } catch (InterruptedException ex) {
                destroy();
                Thread.currentThread().interrupt();
            }
        }

        return entity.value.getValue();
    }

    public void destroy() {
        purgeCacheExecutorService.shutdown();
        isInterrupted.set(true);
    }
}
