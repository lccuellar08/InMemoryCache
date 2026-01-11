package enhancedcache

import main.kotlin.enhancedcache.EnhancedCache
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EnhancedCacheBasicTest {
    private lateinit var cache: EnhancedCache<String>

    @BeforeTest
    fun setup() {
        cache = EnhancedCache()
    }

    @Test
    fun `put and get returns the value`() {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        val returnValue = cache.get(testKey)
        assertEquals(testString, returnValue)
    }

    @Test
    fun `put value twice and return the updated value`() {
        val testKey = 1
        val firstString = "Test String"
        cache.put(testKey, firstString)

        val secondString = "Test String 2"
        cache.put(testKey, secondString)

        val returnValue = cache.get(testKey)
        assertEquals(secondString, returnValue)
    }

    @Test
    fun `get empty cache returns null`() {
        assertNull(cache.get(1))
    }

    @Test
    fun `remove returns true if cache haa value`() {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        val removed = cache.remove(testKey)
        assertTrue(removed)
    }

    @Test
    fun `remove returns false if cache doesn't have value`() {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        val removed = cache.remove(2)
        assertFalse(removed)
    }


    @Test
    fun `put and remove no longer has value`() {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        cache.remove(testKey)

        val returnValue = cache.get(testKey)
        assertNull(returnValue)
    }

    @Test
    fun `size is 0 after clear`() {
        cache.put(1, "Test string 1")
        cache.put(2, "Test string 2")

        cache.clear()
        assertEquals(cache.size(), 0)
    }
}