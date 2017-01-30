package com.gk.Lru.linkedList;

import com.gk.Lru.cache.CacheEntity;

/**
 * Created by gkang on 1/18/17.
 */
public class DoublyLinkedList<T> {
    private ListNode<T> head;
    private ListNode<T> tail;
    private int size;

    public DoublyLinkedList() {
    }

    public void insertFirst(ListNode<T> node) {
        node.prev = null;
        node.next = head;
        if (head == null) {
            head = tail = node;
            return;
        }

        head.prev = node;
        head = node;
        size++;
    }

    public void insertLast(ListNode<T> newNode) {
        newNode.next = null;
        if (tail == null) {
            head = tail = newNode;
            head.prev = null;
            return;
        }

        newNode.prev = tail;
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    public void delete(ListNode<T> node) {
        if (node.prev == null) {
            if (node != head) {
                throw new RuntimeException("Not a valid node");
            }
            deleteFirstNode();
            return;
        }

        if (node.next == null) {
            if (node != tail) {
                throw new RuntimeException("Not a valid node");
            }
            deleteLastNode();
            return;
        }

        node.next.prev = node.prev;
        node.prev.next = node.next;
        node.next = null;
        node.prev = null;
        size--;
    }

    public ListNode<T> deleteFirstNode() {
        if (head == null) {
            return null;
        }

        ListNode<T> deletedNode = head;
        head = head.next;
        if (head == null) {
            return deletedNode;
        }

        head.prev.next = null;
        head.prev = null;
        size--;
        return deletedNode;
    }

    public ListNode<T> deleteLastNode() {
        if (tail == null) {
            return null;
        }

        ListNode<T> deletedNode = tail;
        tail = deletedNode.prev;
        tail.next = null;
        deletedNode.prev = null;
        size--;
        return deletedNode;
    }

    public int getSize() {
        return size;
    }
}
