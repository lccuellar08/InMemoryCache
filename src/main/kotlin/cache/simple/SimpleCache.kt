package main.kotlin.cache.simple

import main.kotlin.cache.core.Cache
import main.kotlin.cache.core.CacheEntry

class SimpleCache<K,V> : Cache<K,V>{
    private val data: MutableMap<K, CacheEntry<V>> = mutableMapOf()

    override fun get(key: K): V? {
        return data[key]?.value
    }

    override fun put(key: K, value: V) {
        data[key] = CacheEntry(value)
    }

    override fun remove(key: K): Boolean {
        data.remove(key) ?: return false
        return true
    }

    override fun clear() {
        data.clear()
    }

    override fun size(): Int {
        return data.size
    }

}