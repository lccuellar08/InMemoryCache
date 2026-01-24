package utils

import main.kotlin.utils.DoublyLinkedList
import main.kotlin.utils.DoublyNode
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class DoublyLinkedListTest {
    private lateinit var list: DoublyLinkedList<Int>

    @BeforeTest
    fun setup() {
        list = DoublyLinkedList()
    }

    @Test
    fun `add elements`() {
        list.add(1)
        list.add(2)
        list.add(3)
        assertEquals(3, list.size())
        assertEquals(1, list.first())
        assertEquals(3, list.last())
    }

    @Test
    fun `remove elements by value`() {
        list.add(1)
        list.add(2)
        list.add(3)
        list.remove(2)
        assertEquals(2, list.size())
        assertFalse(list.remove(4)) // removing non-existent
    }

    @Test
    fun `remove first node`() {
        val node1 = list.add(1)
        val node2 = list.add(2)
        val node3 = list.add(3)

        list.removeNode(node1)

        // Size should be 2
        assertEquals(2, list.size())

        // Head should be node2
        assertEquals(node2.value, list.first())

        // Tail should be node3
        assertEquals(node3.value, list.last())

        // List should not contain node1
        assertNull(list.getNodeFromValue(node1.value))
    }

    @Test
    fun `remove middle node`() {
        val node1 = list.add(1)
        val node2 = list.add(2)
        val node3 = list.add(3)

        list.removeNode(node2)

        // Size should be 2
        assertEquals(2, list.size())

        // Head should be node1
        assertEquals(node1.value, list.first())

        // Tail should be node3
        assertEquals(node3.value, list.last())

        // List should not contain node2
        assertNull(list.getNodeFromValue(node2.value))
    }

    @Test
    fun `remove last node`() {
        val node1 = list.add(1)
        val node2 = list.add(2)
        val node3 = list.add(3)

        list.removeNode(node3)

        // Size should be 2
        assertEquals(2, list.size())

        // Head should be node1
        assertEquals(node1.value, list.first())

        // Tail should be node2
        assertEquals(node2.value, list.last())

        // List should not contain node2
        assertNull(list.getNodeFromValue(node3.value),
            list.toString())
    }

    @Test
    fun `move node to end`() {
        val n1 = list.add(1)
        val n2 = list.add(2)
        val n3 = list.add(3)

        list.moveNodeToEnd(n2)
        assertEquals(1, list.first())
        assertEquals(2, list.last())
        assertEquals(3, list.size())
    }

    @Test
    fun `clear list`() {
        list.add(1)
        list.add(2)
        list.clear()
        assertEquals(0, list.size())
        assertNull(list.first())
        assertNull(list.last())
    }
}
