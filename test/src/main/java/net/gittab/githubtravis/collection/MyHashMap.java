package net.gittab.githubtravis.collection;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MyHashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {

    private static final long serialVersionUID = 362498820763181265L;

    /**
     * default initial capacity 16.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * maximum capacity.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * the default load factor used.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * bins are converted to trees when adding an element to a bin with at least this many
     * nodes. 当桶(bucket)上的结点数大于这个值时会转成红黑树.
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * 当桶(bucket)上的结点数小于这个值时树转链表.
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * 桶中结构转化为红黑树对应的table的最小大小.
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    static class Node<K, V> implements Map.Entry<K, V> {

        final int hash;

        final K key;

        V value;

        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey())
                        && Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }

    }

    static final int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 存储元素的数组，总是2的幂次倍.
     */
    transient Node<K, V>[] table;

    /**
     * 存放具体元素的集.
     */
    transient Set<Map.Entry<K, V>> entrySet;

    /**
     * 存放元素的个数，注意这个不等于数组的长度.
     */
    transient int size;

    /**
     * 每次扩容和更改map结构的计数器.
     */
    transient int modCount;

    /**
     * 临界值 当实际大小(容量*填充因子)超过临界值时，会进行扩容.
     */
    int threshold;

    /**
     * 加载因子.
     */
    final float loadFactor;

    static final int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
        HashMap hashMap = new HashMap(16);

        System.out.println(tableSizeFor1(10000));
        System.out.println(tableSizeFor(5));
        MyHashMap<Integer, Integer> myHashMap = new MyHashMap<>(5, 0.75f);
        System.out.println(myHashMap.threshold);
        System.out.println(Arrays.toString(myHashMap.table));
        myHashMap.put(1, 1);
        myHashMap.put(2, 2);
        myHashMap.put(3, 3);
        myHashMap.put(4, 4);
        System.out.println(myHashMap.threshold);
        myHashMap.put(5, 5);
        System.out.println(myHashMap.threshold);
        myHashMap.put(6, 6);
    }

    static class TestMap<K, V> extends HashMap<K, V>{

        public void printThread(){

        }
    }

    static final int tableSizeFor1(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                    "Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public MyHashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }

    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            if (this.table == null) {
                float ft = ((float) s / loadFactor) + 1.0f;
                int t = ((ft < (float) MAXIMUM_CAPACITY) ? (int) ft : MAXIMUM_CAPACITY);
                if (t > threshold) {
                    threshold = tableSizeFor(t);
                }
            } else if (s > this.threshold) {
                resize();
            }
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if ((p = tab[i = (n - 1) & hash]) == null) {
            //tab[i] = newNode(hash, key, value, null);
        }
        return null;
    }

    final Node<K, V>[] resize() {
        MyHashMap.Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
                // double threshold
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            // initial capacity was placed in threshold
            newCap = oldThr;
        } else { // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        MyHashMap.Node<K, V>[] newTab = (MyHashMap.Node<K, V>[]) new MyHashMap.Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                MyHashMap.Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
//                    } else if (e instanceof TreeNode) {
//                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
//                    } else { // preserve order
                        MyHashMap.Node<K, V> loHead = null, loTail = null;
                        MyHashMap.Node<K, V> hiHead = null, hiTail = null;
                        MyHashMap.Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    static final class TreeNode<K, V> {

    }

}
