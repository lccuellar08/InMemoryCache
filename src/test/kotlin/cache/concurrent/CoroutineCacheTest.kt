package cache.concurrent

import main.kotlin.cache.concurrent.CoroutineCache
import main.kotlin.cache.enhanced.eviction.LRUPolicy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CoroutineCacheTest {
    private lateinit var cache: CoroutineCache<Int, String>

    @BeforeTest
    fun setup() {
        cache = CoroutineCache(10, false, LRUPolicy<Int, String>())
    }

    @Test
    fun `put and get returns the value`() = runTest {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        val returnValue = cache.get(testKey)
        assertEquals(testString, returnValue)
    }

    @Test
    fun `put value twice and return the updated value`() = runTest {
        val testKey = 1
        val firstString = "Test String"
        cache.put(testKey, firstString)

        val secondString = "Test String 2"
        cache.put(testKey, secondString)

        val returnValue = cache.get(testKey)
        assertEquals(secondString, returnValue)
    }

    @Test
    fun `get empty cache returns null`() = runTest {
        assertNull(cache.get(1))
    }

    @Test
    fun `remove returns true if cache haa value`() = runTest {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        val removed = cache.remove(testKey)
        assertTrue(removed)
    }

    @Test
    fun `remove returns false if cache doesn't have value`() = runTest {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        val removed = cache.remove(2)
        assertFalse(removed)
    }


    @Test
    fun `put and remove no longer has value`() = runTest {
        val testKey = 1
        val testString = "Test String"
        cache.put(testKey, testString)

        cache.remove(testKey)

        val returnValue = cache.get(testKey)
        assertNull(returnValue)
    }

    @Test
    fun `size is 0 after clear`() = runTest {
        cache.put(1, "Test string 1")
        cache.put(2, "Test string 2")

        cache.clear()
        assertEquals(cache.size(), 0)
    }
}