package main.kotlin.enhancedcache

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

private data class EnhancedEntry<T>(val value: T, val timeToLive: TimeSource.Monotonic.ValueTimeMark?)

class EnhancedCache<T>(val capacity: Int = 10, val evictionPolicy: EvictionPolicy<T>? = null) {
    private val data: MutableMap<Int, EnhancedEntry<T>> = mutableMapOf()
    private val timeDelta: Duration = 3.seconds
    private val MAXIMUM_CAPACITY = capacity

    fun put(key: Int, value: T, useTTL: Boolean = false) {
        val currentTime = TimeSource.Monotonic.markNow()
        val newEntry = EnhancedEntry(value, if(useTTL) currentTime + timeDelta else null)

        // Check if there is a new value in the key
        val currentValue = data[key]

        // If currentKey is null, insert normally
        if(currentValue == null) {

            // When adding a new key, let's check for eviction policy
            if(data.size == MAXIMUM_CAPACITY) {
                if(evictionPolicy == null)
                    throw IllegalStateException("Cache is at max capacity; attempted to insert $key: $value")

                // Evict an element based on the eviction policy
                val evictionCandidate = evictionPolicy.selectEvictionCandidate()
                remove(evictionCandidate)
            }

            data[key] = newEntry
            evictionPolicy?.onPut(key, value)
        }
        // If we have a hit, let's check if it's different from what's being passed
        else if(currentValue != value){
            data[key] = newEntry
            evictionPolicy?.onUpdate(key, value)
        }
        // We have a hit, but value is the same, policy is not updated
        else {
            // Count as access
            evictionPolicy?.onGet(key, false)
        }
    }

    fun get(key: Int): T? {
        val entry = data[key] ?: return null

        if(entry.timeToLive != null && entry.timeToLive.hasPassedNow()) {
            remove(key)
            evictionPolicy?.onGet(key, isMiss = true)
            return null
        }

        evictionPolicy?.onGet(key, isMiss = false)
        return entry.value
    }

    fun remove(key: Int): Boolean {
        val removedValue = data.remove(key) ?: return false
        evictionPolicy?.onRemove(key)
        return true
    }

    fun clear() {
        data.clear()
        evictionPolicy?.onClear()
    }

    fun printCache() {
        data.forEach { (key, entry) ->
            println("$key -> $entry")
        }
        evictionPolicy?.print()
    }

    fun size(): Int {
        return data.size
    }
}