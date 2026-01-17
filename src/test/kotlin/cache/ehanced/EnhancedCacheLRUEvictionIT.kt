package cache.ehanced

import main.kotlin.cache.enhanced.EnhancedCache
import main.kotlin.cache.enhanced.eviction.LRUPolicy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
}