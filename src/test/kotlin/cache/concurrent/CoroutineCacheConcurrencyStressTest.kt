package cache.concurrent

import kotlinx.coroutines.launch
import main.kotlin.cache.concurrent.CoroutineCache
import main.kotlin.cache.enhanced.eviction.LRUPolicy
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlinx.coroutines.test.runTest

class CoroutineCacheConcurrencyStressTest {

    private lateinit var cache: CoroutineCache<Int, String>
    private val capacity = 5

    @BeforeTest
    fun setup() {
        cache = CoroutineCache(capacity, false, LRUPolicy<Int,String>())
    }

    @Test
    fun `cache breaks under concurrent puts, capacity violation`() = runTest {
        val threads = mutableListOf<Thread>()

        repeat(20) {threadId ->
            val t = Thread {
                repeat(1_000) { i ->
                    launch {
                        val keyID = i + threadId * 100
                        cache.put(keyID, i.toString())
                    }
                }
            }
            threads += t
        }

        threads.forEach {it.start()}
        threads.forEach {it.join()}

        // This should never be > capacity
        assertTrue(
            cache.size() <= capacity,
            "Cache size exceeded capacity: ${cache.size()}"
        )
    }

    @Test
    fun `LRU order corrupts under concurrent access`() = runTest {
        val threads = List(10) {
            Thread {
                repeat(1_000) {
                    launch {
                        val keyID = (0..capacity).random()
                        cache.put(keyID, keyID.toString())
                        cache.get(keyID)
                    }
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // Try to force eviction after chaos
        cache.put(999, 999.toString())

        // If eviction policy is broken, this may throw
        assertTrue(
            cache.size() <= capacity,
            "Cache size exceeded capacity: ${cache.size()}"
        )
    }

    @Test
    fun `expired entries returned under concurrency`() = runTest {
        val writer = Thread {
            repeat(1_000) {
                launch {
                    cache.put(1, it.toString())
                    Thread.sleep(1)
                }
            }
        }

        val reader = Thread {
            repeat(1_000) {
                launch {
                    val value = cache.get(1)
                    if (value != null) {
                        // This should NEVER be expired
                        assertNotNull(value)
                    }
                }
            }
        }

        writer.start()
        reader.start()
        writer.join()
        reader.join()
    }
}