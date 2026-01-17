package cache.ehanced

import main.kotlin.cache.enhanced.EnhancedCache
import main.kotlin.cache.enhanced.eviction.FIFOPolicy
import kotlin.test.*

class EnhancedCacheFIFOEvictionIT {
    private lateinit var cache: EnhancedCache<Int, String>

    @BeforeTest
    fun setup() {
        cache = EnhancedCache(
            capacity = 3,
            evictionPolicy = FIFOPolicy()
        )
    }

    @Test
    fun `fifo evicts oldest inserted key`() {
        cache.put(1, "A")
        cache.put(2, "B")
        cache.put(3, "C")

        // triggers eviction
        cache.put(4, "D")

        assertNull(cache.get(1))   // evicted
        assertEquals("B", cache.get(2))
        assertEquals("C", cache.get(3))
    }

    @Test
    fun `fifo does not evict on update`() {
        cache.put(1, "A")
        cache.put(2, "B")
        cache.put(3, "C")

        // update existing key
        cache.put(1, "A2")

        // still full, but no eviction
        assertEquals("A2", cache.get(1))
        assertEquals("B", cache.get(2))
        assertEquals("C", cache.get(3))
    }
}