package main.kotlin.enhancedcache

class FIFOPolicy<T> : EvictionPolicy<T>{
    private val valueQueue: MutableList<Int> = mutableListOf()

    override fun selectEvictionCandidate(): Int {
        return valueQueue.first()
    }

    override fun onPut(key: Int, value: T) {
        valueQueue.add(key)
    }

    override fun onGet(key: Int, isMiss: Boolean) {
        // Do nothing
    }

    override fun onRemove(key: Int) {
        val indexToRemove = valueQueue.indexOf(key)
        if(indexToRemove != -1)
            valueQueue.removeAt(indexToRemove)
    }

    override fun onUpdate(key: Int, value: T) {
        // Do nothing
    }

    override fun onClear() {
        valueQueue.clear()
    }

    override fun print() {
        println(valueQueue)
    }
}