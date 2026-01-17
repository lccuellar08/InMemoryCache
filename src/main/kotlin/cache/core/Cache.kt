package main.kotlin.cache.core

interface Cache<K, V> {
    fun get(key: K): V?
    fun put(key: K, value: V)
    fun remove(key: K): Boolean
    fun clear()
    fun size(): Int
}