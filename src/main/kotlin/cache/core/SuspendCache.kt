package main.kotlin.cache.core

interface SuspendCache<K, V> {
    suspend fun get(key: K): V?
    suspend fun put(key: K, value: V)
    suspend fun remove(key: K): Boolean
    suspend fun clear()
    suspend fun size(): Int
}