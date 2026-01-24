package cache.enhanced.eviction

import main.kotlin.cache.enhanced.eviction.FIFOPolicy
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class FIFOPolicyTest {

    private lateinit var policy: FIFOPolicy<Int, String>

    @BeforeTest
    fun setup() {
        policy = FIFOPolicy()
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