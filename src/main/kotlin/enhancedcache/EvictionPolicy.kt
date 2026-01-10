package main.kotlin.enhancedcache

interface EvictionPolicy<T> {
    fun selectEvictionCandidate(): Int
    fun onPut(key: Int, value: T)
    fun onGet(key: Int, isMiss: Boolean)
    fun onRemove(key: Int)
    fun onUpdate(key: Int, value: T)
    fun onClear()
    fun print()
}