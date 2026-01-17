package main.kotlin.cache.enhanced

import main.kotlin.cache.enhanced.EnhancedCacheEntry
import main.kotlin.cache.enhanced.eviction.EvictionPolicy
import main.kotlin.cache.core.Cache
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class EnhancedCache<K,V>(val capacity: Int = 10, val useTTL: Boolean = false, val evictionPolicy: EvictionPolicy<K,V>? = null) : Cache<K,V>{
    private val data: MutableMap<K, EnhancedCacheEntry<V>> = mutableMapOf()
    private val timeDelta: Duration = 3.seconds
    private val MAXIMUM_CAPACITY = capacity

    override fun put(key: K, value: V) {
        val currentTime = TimeSource.Monotonic.markNow()
        val newEntry = EnhancedCacheEntry(value, if(useTTL) currentTime + timeDelta else null)

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

    override fun get(key: K): V? {
        val entry = data[key] ?: return null

        if(entry.timeToLive != null && entry.timeToLive.hasPassedNow()) {
            remove(key)
            evictionPolicy?.onGet(key, isMiss = true)
            return null
        }

        evictionPolicy?.onGet(key, isMiss = false)
        return entry.value
    }

    override fun remove(key: K): Boolean {
        data.remove(key) ?: return false
        evictionPolicy?.onRemove(key)
        return true
    }

    override fun clear() {
        data.clear()
        evictionPolicy?.onClear()
    }

    fun printCache() {
        data.forEach { (key, entry) ->
            println("$key -> $entry")
        }
        evictionPolicy?.print()
    }

    override fun size(): Int {
        return data.size
    }
}