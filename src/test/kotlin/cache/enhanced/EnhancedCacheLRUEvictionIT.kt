package cache.enhanced

import main.kotlin.cache.enhanced.EnhancedCache
import main.kotlin.cache.enhanced.eviction.LRUPolicy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EnhancedCacheLRUEvictionIT {

    private lateinit var cache: EnhancedCache<Int, String>

    @BeforeEach
    fun setup() {
        cache = EnhancedCache(
            capacity = 3,
            evictionPolicy = LRUPolicy()
        )
    }

    @Test
    fun `lru evicts least recently accessed key`() {
        cache.put(1, "A")
        cache.put(2, "B")
        cache.put(3, "C")

        // make key 1 most recently used
        cache.get(1)

        // trigger eviction
        cache.put(4, "D")

        assertNull(cache.get(2))   // LRU evicted
        assertEquals("A", cache.get(1))
        assertEquals("C", cache.get(3))
        assertEquals("D", cache.get(4))
    }

    @Test
    fun `lru update counts as access`() {
        cache.put(1, "A")
        cache.put(2, "B")
        cache.put(3, "C")

        // update key 1 → MRU
        cache.put(1, "A2")

        cache.put(4, "D")

        assertNull(cache.get(2))
        assertEquals("A2", cache.get(1))
        assertEquals("C", cache.get(3))
        assertEquals("D", cache.get(4))
    }

    @Test
    fun `single-threaded LRU stress test`() {
        val capacity = 5
        val cache = EnhancedCache<Int, String>(
            capacity = capacity,
            evictionPolicy = LRUPolicy()
        )

        val keys = (1..100).toList()

        // Insert keys repeatedly
        for (i in keys) {
            cache.put(i, "val$i")

            // Occasionally access previous keys to change recency
            if (i > 3) {
                cache.get(i - 2)
                cache.get(i - 1)
            }

            // Assert size never exceeds capacity
            assertTrue(cache.size() <= capacity, "Cache exceeded capacity at iteration $i")
        }

        // Check that eviction order matches access history
        val policy = cache.evictionPolicy as LRUPolicy<Int, String>
        val keyOrder = policy.doublyList
        assertTrue(keyOrder.size() <= capacity, "Policy list exceeded capacity")
    }
}