package main.kotlin.cache.enhanced.eviction

import main.kotlin.utils.DoublyLinkedList
import main.kotlin.utils.DoublyNode

class LRUPolicy<K,V> : EvictionPolicy<K,V> {
    val keyMap: MutableMap<K, DoublyNode<K>> = mutableMapOf()
    val doublyList = DoublyLinkedList<K>()

    override fun selectEvictionCandidate(): K {
        // Return head
        val candidate = doublyList.first()
        if(candidate == null)
            throw IllegalStateException("No items in policy")
        return candidate
    }

    override fun onPut(key: K, value: V) {
        val newNode = doublyList.add(key)
        keyMap[key] = newNode
    }

    override fun onGet(key: K, isMiss: Boolean) {
//        println("Accessed $key")
        if(isMiss)
        // Do nothing if it's a miss
            return

        val thisNode = keyMap[key]
        if(thisNode != null) {
//            println("Moving $thisNode to end")
            doublyList.moveNodeToEnd(thisNode)
        }
    }

    override fun onRemove(key: K) {
        val nodeToRemove = keyMap[key]
        keyMap.remove(key)
        if(nodeToRemove != null)
            doublyList.removeNode(nodeToRemove)
    }

    override fun onUpdate(key: K, value: V) {
        val thisNode = keyMap[key]
        if(thisNode != null)
            doublyList.moveNodeToEnd(thisNode)
    }

    override fun onClear() {
        keyMap.clear()
        doublyList.clear()
    }

    override fun print() {
        println(keyMap)
        println(doublyList)
    }
}