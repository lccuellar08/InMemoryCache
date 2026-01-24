package main.kotlin.cache.concurrent

import main.kotlin.cache.core.Cache

/**
 * Coarse-grained synchronization ensures thread safety,
 * but does not guarantee correctness of eviction policies
 * under concurrent access.
 */
class SynchronizedCache<K,V> (private val delegate: Cache<K,V>): Cache<K,V> {
    private val cacheLock = Any()

    override fun get(key: K): V? {
        synchronized(cacheLock) {
            return delegate.get(key)
        }
    }

    override fun put(key: K, value: V) {
        synchronized(cacheLock) {
            delegate.put(key, value)
        }
    }

    override fun remove(key: K): Boolean {
        synchronized(cacheLock) {
            return delegate.remove(key)
        }
    }

    override fun clear() {
        synchronized(cacheLock) {
            delegate.clear()
        }
    }

    override fun size(): Int {
        synchronized(cacheLock) {
            return delegate.size()
        }
    }
}