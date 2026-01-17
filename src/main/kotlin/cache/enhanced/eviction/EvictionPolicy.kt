package main.kotlin.cache.enhanced.eviction

interface EvictionPolicy<K,V> {
    fun selectEvictionCandidate(): K
    fun onPut(key: K, value: V)
    fun onGet(key: K, isMiss: Boolean)
    fun onRemove(key: K)
    fun onUpdate(key: K, value: V)
    fun onClear()
    fun print()
}