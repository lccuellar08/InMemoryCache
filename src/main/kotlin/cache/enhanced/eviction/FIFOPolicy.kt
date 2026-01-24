package main.kotlin.cache.enhanced.eviction

class FIFOPolicy<K,V> : EvictionPolicy<K,V> {
    private val valueQueue: MutableList<K> = mutableListOf()

    override fun selectEvictionCandidate(): K {
        return valueQueue.first()
    }

    override fun onPut(key: K, value: V) {
        valueQueue.add(key)
    }

    override fun onGet(key: K, isMiss: Boolean) {
        // Do nothing
    }

    override fun onRemove(key: K) {
        val indexToRemove = valueQueue.indexOf(key)
        if(indexToRemove != -1)
            valueQueue.removeAt(indexToRemove)
    }

    override fun onUpdate(key: K, value: V) {
        // Do nothing
    }

    override fun onClear() {
        valueQueue.clear()
    }

    override fun print() {
        println(valueQueue)
    }

    override fun size(): Int {
        return valueQueue.size
    }

    override fun keys(): Set<K> {
        return valueQueue.toSet()
    }
}