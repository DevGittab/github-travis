package net.gittab.test.collection;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * MyLinkedList.
 *
 * @param <E> e
 * @author xiaohua zhou
 **/
public class MyLinkedList<E> extends AbstractSequentialList<E>
		implements List<E>, Deque<E>, Cloneable, Serializable {

	transient int size = 0;

	transient Node<E> first;

	transient Node<E> last;

	public MyLinkedList() {
	}

	public MyLinkedList(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	private void linkFirst(E e) {
		final Node<E> f = this.first;
		final Node<E> newNode = new Node<>(null, e, f);
		this.first = newNode;
		if (f == null) {
			this.last = newNode;
		}
		else {
			f.prev = newNode;
		}
		this.size++;
		this.modCount++;
	}

	void linkLast(E e) {
		final Node<E> l = this.last;
		final Node<E> newNode = new Node<>(l, e, null);
		this.last = newNode;
		if (l == null) {
			this.first = newNode;
		}
		else {
			l.next = newNode;
		}
		this.size++;
		this.modCount++;
	}

	void linkBefore(E e, Node<E> succ) {
		final Node<E> p = succ.prev;
		final Node<E> newNode = new Node<>(p, e, succ);
		succ.prev = newNode;
		if (p == null) {
			this.first = newNode;
		}
		else {
			p.next = newNode;
		}
		this.size++;
		this.modCount++;
	}

	void linkAfter(E e, Node<E> succ) {
		final Node<E> n = succ.next;
		final Node<E> newNode = new Node<>(succ, e, n);
		succ.next = newNode;
		if (n == null) {
			this.last = newNode;
		}
		else {
			n.prev = newNode;
		}
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return null;
	}

	@Override
	public void addFirst(E e) {

	}

	@Override
	public void addLast(E e) {

	}

	@Override
	public boolean offerFirst(E e) {
		return false;
	}

	@Override
	public boolean offerLast(E e) {
		return false;
	}

	@Override
	public E removeFirst() {
		return null;
	}

	@Override
	public E removeLast() {
		return null;
	}

	@Override
	public E pollFirst() {
		return null;
	}

	@Override
	public E pollLast() {
		return null;
	}

	@Override
	public E getFirst() {
		return null;
	}

	@Override
	public E getLast() {
		return null;
	}

	@Override
	public E peekFirst() {
		return null;
	}

	@Override
	public E peekLast() {
		return null;
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		return false;
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		return false;
	}

	@Override
	public boolean offer(E e) {
		return false;
	}

	@Override
	public E remove() {
		return null;
	}

	@Override
	public E poll() {
		return null;
	}

	@Override
	public E element() {
		return null;
	}

	@Override
	public E peek() {
		return null;
	}

	@Override
	public void push(E e) {

	}

	@Override
	public E pop() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Iterator<E> descendingIterator() {
		return null;
	}

	private static class Node<E> {

		E item;

		Node<E> next;

		Node<E> prev;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.prev = prev;
			this.next = next;
		}

	}

}
