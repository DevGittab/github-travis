package net.gittab.githubtravis.collection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/**
 * MyArrayList.
 *
 * @param <E> e
 * @author xiaohua zhou
 **/
public class MyArrayList<E> extends AbstractList<E>
		implements List<E>, Cloneable, RandomAccess, Serializable {

	/**
	 * Default initial capacity.
	 */
	private static final int DEFAULT_CAPACITY = 10;

	/**
	 * empty array instance.
	 */
	private static final Object[] EMPTY_ELEMENT_DATA = {};

	/**
	 * default empty array instance.
	 */
	private static final Object[] DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA = {};

	/**
	 * the array buffer.
	 */
	transient Object[] elementData;

	private int size;

	public MyArrayList() {
		this.elementData = DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA;
	}

	public MyArrayList(int initialCapacity) {
		if (initialCapacity > 0) {
			this.elementData = new Object[initialCapacity];
		}
		else if (initialCapacity == 0) {
			this.elementData = EMPTY_ELEMENT_DATA;
		}
		else {
			throw new IllegalArgumentException("illegal argument exception");
		}
	}

	public MyArrayList(Collection<? extends E> collection) {
		this.elementData = collection.toArray();
		this.size = this.elementData.length;
		if (this.size != 0) {
			if (this.elementData.getClass() != Object[].class) {
				this.elementData = Arrays.copyOf(this.elementData, this.size,
						Object[].class);
			}
		}
		else {
			this.elementData = EMPTY_ELEMENT_DATA;
		}
	}

	public void tripToSize() {
		this.modCount++;
		if (this.size < this.elementData.length) {
			this.elementData = (this.size == 0) ? EMPTY_ELEMENT_DATA
					: Arrays.copyOf(this.elementData, this.size);
		}
	}

	public void ensureCapacity(int minCapacity) {
		if (minCapacity > this.elementData.length
				&& !(this.elementData == DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA
						&& minCapacity <= DEFAULT_CAPACITY)) {
			modCount++;
			grow(minCapacity);
		}
	}

	private Object[] grow(int minCapacity) {
		this.elementData = Arrays.copyOf(this.elementData, newCapacity(minCapacity));
		return this.elementData;
	}

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	private int newCapacity(int minCapacity) {
		int oldCapacity = this.elementData.length;
		System.out.println(oldCapacity);
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		System.out.println(newCapacity);
		if (newCapacity - minCapacity <= 0) {
			if (this.elementData == DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA) {
				return Math.max(DEFAULT_CAPACITY, minCapacity);
			}
			if (minCapacity < 0) {
				throw new OutOfMemoryError();
			}
			return minCapacity;
		}
		return (newCapacity - MAX_ARRAY_SIZE <= 0) ? newCapacity
				: hugeCapacity(minCapacity);

	}

	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) {
			throw new OutOfMemoryError();
		}
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}

	public static void main(String[] args) {
		MyArrayList<String> myArrayList = new MyArrayList<>(10);
		for (int i = 0; i < myArrayList.elementData.length; i++) {
			System.out.println(myArrayList.elementData[i]);
		}
		System.out.println(myArrayList.size());
		System.out.println(myArrayList.elementData.length);
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public int indexOf(Object o) {
		return indexOfRange(o, 0, this.size);
	}

	int indexOfRange(Object o, int start, int end) {
		Object[] es = this.elementData;
		if (o == null) {
			for (int i = start; i < end; i++) {
				if (es[i] == null) {
					return i;
				}
			}
		}
		else {
			for (int i = start; i < end; i++) {
				if (o.equals(es[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		return lastIndexOfRange(o, 0, this.size);
	}

	int lastIndexOfRange(Object o, int start, int end) {
		Object[] es = this.elementData;
		if (o == null) {
			for (int i = end - 1; i >= start; i--) {
				if (es[i] == null) {
					return i;
				}
			}
		}
		else {
			for (int i = end - 1; i >= start; i--) {
				if (o.equals(es[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= this.size) {
			throw new IllegalArgumentException("IndexOutOfBoundsException");
		}
		return this.elementData(index);
	}

	@SuppressWarnings("unchecked")
	E elementData(int index) {
		return (E) this.elementData[index];
	}

	@Override
	public E set(int index, E element) {
		if (index < 0 || index >= this.size) {
			throw new IllegalArgumentException("IndexOutOfBoundsException");
		}
		E oldValue = this.elementData(index);
		this.elementData[index] = element;
		return oldValue;
	}

	@Override
	public int size() {
		return this.size;
	}

	private Object[] grow() {
		return grow(this.size + 1);
	}

	private void add(E e, Object[] elementData, int s) {
		if (s == this.elementData.length) {
			this.elementData = grow();
		}
		this.elementData[s] = e;
		this.size = s + 1;
	}

	@Override
	public boolean add(E e) {
		this.modCount++;
		add(e, this.elementData, this.size);
		return false;
	}

	@Override
	public void add(int index, Object element) {
		if (index > this.size || index < 0) {
			throw new IllegalArgumentException("IndexOutOfBoundsException");
		}
		this.modCount++;
		final int s = this.size;
		Object[] elementData = this.elementData;
		if (s == elementData.length) {
			elementData = grow();
		}
		System.arraycopy(elementData, index, elementData, index + 1, s - index);
		elementData[index] = element;
		this.size = s + 1;
	}

	@Override
	public boolean addAll(Collection c) {
		return false;
	}

	@Override
	public boolean addAll(int index, Collection c) {
		return false;
	}

	@Override
	public Object[] toArray(Object[] a) {
		return Arrays.copyOf(this.elementData, this.size);
	}

}
