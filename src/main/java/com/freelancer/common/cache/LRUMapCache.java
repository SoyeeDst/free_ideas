package com.freelancer.common.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Soyee.Deng on 2016/3/8.
 * <p/>
 * The purpose of this class is to define one cache conceptually based on LRU mechanism.
 * <p/>
 * The eldest element will be evicted if there is no enough space for new element.
 * As the side effect, the auto expansion capacity will be turned off automatically
 * <p/>
 * Note that this Cache is not thread safe
 */
public class LRUMapCache<K, V> {

    /**
     * Underlying cache, delegation
     */
    private LinkedHashMap<K, V> itsCache;

    public LRUMapCache(final int maxSize) {
        itsCache = new LinkedHashMap<K, V>(maxSize, 0.75f) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                if (itsCache.size() > maxSize) {
                    return true;
                }
                return false;
            }
        };
    }

    public V get(K key) {
        return itsCache.get(key);
    }

    public void put(K key, V value) {
        itsCache.put(key, value);
    }

    public boolean remove(K key) {
        return itsCache.remove(key) != null;
    }

    public long size() {
        return itsCache.size();
    }

}
