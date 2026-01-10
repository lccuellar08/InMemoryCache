package main.kotlin.enhancedcache

import main.kotlin.utils.DoublyLinkedList
import main.kotlin.utils.DoublyNode

class LRUPolicy<T> : EvictionPolicy<T> {
    val keyMap: MutableMap<Int, DoublyNode<Int>> = mutableMapOf()
    val doublyList = DoublyLinkedList<Int>()

    override fun selectEvictionCandidate(): Int {
        // Return head
        val candidate = doublyList.first()
        if(candidate == null)
            throw IllegalStateException("No items in policy")
        return candidate
    }

    override fun onPut(key: Int, value: T) {
        val newNode = doublyList.add(key)
        keyMap[key] = newNode
    }

    override fun onGet(key: Int, isMiss: Boolean) {
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

    override fun onRemove(key: Int) {
        val nodeToRemove = keyMap[key]
        keyMap.remove(key)
        if(nodeToRemove != null)
            doublyList.removeNode(nodeToRemove)
    }

    override fun onUpdate(key: Int, value: T) {
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