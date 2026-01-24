package cache.enhanced

import main.kotlin.cache.enhanced.EnhancedCache
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EnhancedCacheTTLTest {
    private lateinit var cache: EnhancedCache<Int, String>

    @BeforeTest
    fun setup() {
        cache = EnhancedCache(useTTL = true)
    }

    @Test
    fun `put and get returns the value within time period`() {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        val returnValue = cache.get(testKey)
        assertEquals(testString, returnValue)
    }

    @Test
    fun `put and get does not return the value after time period`() {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        Thread.sleep(3000)

        val returnValue = cache.get(testKey)
        assertNull(returnValue)
    }

    @Test
    fun `value removed from cache after time period`() {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        // put another key
        cache.put(2, "Test String 2")

        Thread.sleep(3000)
        val returnValue = cache.get(testKey)
        assertNull(returnValue)

        val size = cache.size()
        assertEquals(1, size)
    }
}