package main.kotlin.utils

data class DoublyNode<T>(val value: T, var prev: DoublyNode<T>?, var next: DoublyNode<T>?) {
    override fun toString(): String {
        return "$value"
    }
}

class DoublyLinkedList<T> {
    private var head: DoublyNode<T>? = null
    private var tail: DoublyNode<T>? = null
    private var size: Int = 0

    fun add(value: T): DoublyNode<T> {
        if(size == 0) {
            head = DoublyNode(value, null, null)
            tail = head
            size += 1
        } else {
            val newDoublyNode = DoublyNode(value, tail, null)
            tail?.next = newDoublyNode
            tail = newDoublyNode
            size += 1
        }

        return tail!!
    }

    private fun addNode(doublyNode: DoublyNode<T>) {
        if(size == 0) {
            head = doublyNode
            tail = head
            size += 1
        } else {
            tail?.next = doublyNode
            doublyNode.prev = tail
            tail = doublyNode
            size += 1
        }
    }

    fun removeNode(doublyNode: DoublyNode<T>) {
        if(doublyNode == head) {
            head = head?.next
        } else if(doublyNode == tail){
            tail = tail?.prev
            tail?.next = null
        } else {
//            doublyNode.prev?.next = doublyNode.next
            val prevNode = doublyNode.prev
            val nextNode = doublyNode.next
            prevNode?.next = nextNode
            nextNode?.prev = prevNode
        }

        doublyNode.prev = null
        doublyNode.next = null
        size -= 1
    }

    fun remove(value: T): Boolean {
        // Iterate until we find value
        var tempNode = head
        while(tempNode != null) {
            if(tempNode.value == value) {
                removeNode(tempNode)
                return true
            }
            tempNode = tempNode.next
        }
        return false
    }

    fun removeAt(index: Int): Boolean {
        if(index >= size)
            throw IndexOutOfBoundsException("Index $index greater than size of list")

        var tempNode = head
        for(i in 0..<index) {
            tempNode = tempNode?.next
        }
        if(tempNode == null)
            return false

        removeNode(tempNode)
        return true
    }

    fun getAt(index: Int): T? {
        if(index >= size)
            throw IndexOutOfBoundsException("Index $index greater than size of list")

        var tempNode = head
        for(i in 0..<index) {
            tempNode = tempNode?.next
        }
        if(tempNode == null)
            return null

        return tempNode.value
    }

    fun getNodeFromValue(value: T): DoublyNode<T>? {
        var tempNode = head
        while(tempNode != null) {
            if(tempNode.value == value)
                return tempNode
            tempNode = tempNode.next
        }
        return null
    }

    fun moveNodeToEnd(doublyNode: DoublyNode<T>) {
        removeNode(doublyNode)
        addNode(doublyNode)
    }

    fun first(): T? {
        return head?.value
    }

    fun last(): T? {
        return tail?.value
    }

    fun clear() {
        head = null
        tail = null
        size = 0
    }

    override fun toString(): String {
        var outStr = "Size: $size : "
        var tempNode = head
        while(tempNode != null) {
            outStr += "${tempNode.value} -> "
            tempNode = tempNode.next
        }
        return outStr
    }

    fun size(): Int {
        return size
    }
}