package main.kotlin.cache.concurrent

import main.kotlin.cache.enhanced.eviction.EvictionPolicy
import main.kotlin.cache.core.Cache
import main.kotlin.cache.enhanced.EnhancedCacheEntry
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class AtomicCache<K,V> (val capacity: Int = 10, val useTTL: Boolean = false,  val evictionPolicy: EvictionPolicy<K,V>?) : Cache<K,V> {
    private val data: MutableMap<K, EnhancedCacheEntry<V>> = mutableMapOf()
    private val timeDelta: Duration = 3.seconds

    private val cacheLock = Any()

    private fun assertInvariants() {
        check(data.size <= capacity)

        evictionPolicy?.let { policy ->
            check(data.size == policy.size())
            check(data.keys == policy.keys())
        }
    }

    /*
       Get: Retrieves value from cache. If TTL has passed, the element is removed from cache
     */
    override fun get(key: K): V? {
        synchronized(cacheLock) {
            // 1 - Retrieve value from map
            val entry = data[key]

            // 1.a - Notify policy if key was a miss. Return null
            if(entry == null) {
                evictionPolicy?.onGet(key, isMiss = true)
                return null
            }

            // 2 - Check if TTL has passed
            if(entry.timeToLive?.hasPassedNow() == true) {

                // 2.a - Remove Key. Handle here explicitly
                data.remove(key)
                evictionPolicy?.onRemove(key)

                // 2.b - Notify policy that key is a miss
                evictionPolicy?.onGet(key, isMiss = true)
                return null
            }

            // 3 - Notify policy key was not a miss. Return value
            evictionPolicy?.onGet(key, isMiss = false)

            // 4 - Debug Only: Check invariants
            assertInvariants()
            return entry.value
        }
    }

    /*
        Put: Add value to cache. If at capacity, get a key to remove from policy and remove it.
     */
    override fun put(key: K, value: V) {
        synchronized(cacheLock) {
            val currentTime = TimeSource.Monotonic.markNow()
            val newEntry = EnhancedCacheEntry(value, if(useTTL) currentTime + timeDelta else null)

            // 1. Check if there is a current value with this key
            val currentValue = data[key]

            if(currentValue != null){
                // 1.a Update value, refresh TTL
                data[key] = newEntry
                evictionPolicy?.onUpdate(key, value)
                assertInvariants()
                return
            }

            // 2. Before adding - Check for eviction policy
            if(data.size == capacity) {
                if(evictionPolicy == null)
                    throw IllegalStateException("Cache is at max capacity; attempted to insert $key: $value")

                // 2.1 Get Eviction candidate from policy
                val evictionCandidate = evictionPolicy.selectEvictionCandidate()

                // 2.2 - Remove Candidate. Handle here explicitly
                data.remove(evictionCandidate)
                evictionPolicy.onRemove(evictionCandidate)
            }

            // 3. Add key and value to cache and policy
            data[key] = newEntry
            evictionPolicy?.onPut(key, value)
            assertInvariants()
        }
    }

    override fun remove(key: K): Boolean {
        synchronized(cacheLock) {
            val removed = data.remove(key) != null
            if(removed)
                evictionPolicy?.onRemove(key)

            assertInvariants()
            return removed
        }
    }

    fun printCache() {
        data.forEach { (key, entry) ->
            println("$key -> $entry")
        }
        evictionPolicy?.print()
    }

    override fun clear() {
        synchronized(cacheLock) {
            data.clear()
            evictionPolicy?.onClear()
        }
    }

    override fun size(): Int {
        synchronized(cacheLock) {
            return data.size
        }
    }
}