package main.kotlin.cache.enhanced

import main.kotlin.cache.core.CacheEntry
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class EnhancedCacheEntry<T> (
    value: T,
    val timeToLive: TimeSource.Monotonic.ValueTimeMark?
) : CacheEntry<T>(value)