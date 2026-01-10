package main.kotlin.simplecache

class SimpleCache<T> {
    private val data: MutableMap<Int, T> = mutableMapOf()

    fun put(key: Int, value: T) {
        data[key] = value
    }

    fun get(key: Int): T? {
        return data[key]
    }

    fun remove(key: Int): Boolean {
        val removedValue = data.remove(key) ?: return false
        return true
    }

    fun clear() {
        data.clear()
    }

    fun size(): Int {
        return data.size
    }
}