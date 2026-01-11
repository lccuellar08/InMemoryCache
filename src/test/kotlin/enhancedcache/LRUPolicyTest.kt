package enhancedcache

import main.kotlin.enhancedcache.EnhancedCache
import main.kotlin.enhancedcache.LRUPolicy
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LRUPolicyTest {

    private lateinit var policy: LRUPolicy<String>

    @BeforeTest
    fun setup() {
        policy = LRUPolicy()
    }

    @Test
    fun `put multiple and eviction candidate is first`() {
        val firstKey = 1
        val firstValue = "Test String 1"

        policy.onPut(firstKey, firstValue)
        policy.onPut(2, "Test String 2")
        policy.onPut(3, "Test String 3")

        val candidate = policy.selectEvictionCandidate()
        assertEquals(firstKey, candidate)
    }

    @Test
    fun `put multiple, access first, eviction candidate is second`() {
        val firstKey = 1
        val firstValue = "Test String 1"

        val secondKey = 2
        val secondValue = "Test String 2"

        policy.onPut(firstKey, firstValue)
        policy.onPut(secondKey, secondValue)
        policy.onPut(3, "Test String 3")

        policy.onGet(firstKey, isMiss = false)

        val candidate = policy.selectEvictionCandidate()
        assertEquals(secondKey, candidate)
    }

    @Test
    fun `put multiple, update first, eviction candidate is second`() {
        val firstKey = 1
        val firstValue = "Test String 1"

        val secondKey = 2
        val secondValue = "Test String 2"

        policy.onPut(firstKey, firstValue)
        policy.onPut(secondKey, secondValue)
        policy.onPut(3, "Test String 3")

        policy.onUpdate(firstKey, "New Test String 1")

        val candidate = policy.selectEvictionCandidate()
        assertEquals(secondKey, candidate)
    }

    @Test
    fun `put and remove first no longer has first as eviction candidate`() {
        val firstKey = 1
        val firstValue = "Test String 1"

        policy.onPut(firstKey, firstValue)
        policy.onPut(2, "Test String 2")
        policy.onPut(3, "Test String 3")

        policy.onRemove(firstKey)

        val candidate = policy.selectEvictionCandidate()

        assertNotEquals(candidate, firstKey)
    }
}