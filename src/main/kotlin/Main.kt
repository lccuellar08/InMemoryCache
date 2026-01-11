package main.kotlin

import main.kotlin.enhancedcache.EnhancedCache
import main.kotlin.enhancedcache.FIFOPolicy
import main.kotlin.enhancedcache.LRUPolicy

fun runFIFO() {
    val cache = EnhancedCache<String>(evictionPolicy =  FIFOPolicy())
    for(i in 0..20) {
        cache.put(i, "Test string: $i")
//        cache.printCache()
    }
    cache.printCache()
}

fun runLRU() {
    val cache = EnhancedCache<String>(evictionPolicy = LRUPolicy())
    for(i in 0..10) {
        cache.put(i, "Test string: $i")
//        cache.printCache()
    }
    cache.printCache()
    // Access 2, 4, 6, 8, 10
    cache.get(2)
    cache.get(4)
    cache.get(6)
    cache.get(8)
    cache.get(10)
    cache.printCache()

    // Now add 3 more values
    cache.put(11, "Test string: 11")
    cache.put(12, "Test string: 12")
    cache.put(13, "Test string: 13")
    cache.printCache()
}

fun runMaxCapacity() {
    val cache = EnhancedCache<String>()
    for(i in 0..10) {
        cache.put(i, "Test string: $i")
    }
}

fun runTTL() {
    val cache = EnhancedCache<String>()
    cache.put(1, "Hello")
    cache.put(2, "World!", true)

    cache.printCache()

    println("Reading key 2: ${cache.get(2)}")
    Thread.sleep(3000)

    println("Reading key 2: ${cache.get(2)}")
    cache.printCache()
}

fun main() {
    runLRU()
}