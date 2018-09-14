package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    private int index;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        index = 0; 
        size = 10; 
        pairs = makeArrayOfPairs(size);
    }
    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    private int indexOf(K key) {
        for (int i = 0; i < index; i++) {
            if (pairs[i] != null) {
                if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private Pair<K, V>[] copy() {
        size = size * 2;
        Pair<K, V>[] newArray = makeArrayOfPairs(size);
        for (int i = 0; i < index; i++) {
            newArray[i] = new Pair<K, V>(pairs[i].key, pairs[i].value);
        }
        return newArray;
    }
    
    @Override
    public V get(K key) {
        int indexOf = indexOf(key);
        if (indexOf == -1) {
            throw new NoSuchKeyException();
        } else {
            return pairs[indexOf].value;
        }
    }

    @Override
    public void put(K key, V value) {
        
        int indexOf = indexOf(key);
        if (indexOf == -1) {
            if (index == size) {
                pairs = copy();
            }
            pairs[index] = new Pair<K, V>(key, value);
            index++;
        } else {
            pairs[indexOf] = new Pair<K, V>(key, value);
        }
    }

    @Override
    public V remove(K key) {
        int indexOf = indexOf(key);
        if (indexOf == -1) {
            throw new NoSuchKeyException();
        } else {
            V value = pairs[indexOf].value;
            pairs[indexOf] = new Pair<K, V>(pairs[index - 1].key, pairs[index - 1].value);
            index--;
            return value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        return (indexOf(key) != -1);
    }

    @Override
    public int size() {
        return index;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
        
    }
}

