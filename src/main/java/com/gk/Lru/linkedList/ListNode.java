package com.gk.Lru.linkedList;

import java.util.List;

/**
 * Created by gkang on 1/18/17.
 */
public class ListNode<T> {
    public final T value;
    public ListNode<T> prev;
    public ListNode<T> next;

    public ListNode(T value, ListNode<T> prev, ListNode<T> next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }
}
