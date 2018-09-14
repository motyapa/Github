package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int bigArraySize;
    private int arrays;
    private int items;


    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
       arrays = 0;
       bigArraySize = 10;
       chains = makeArrayOfChains(bigArraySize);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    public IDictionary<K, V>[] resize(IDictionary<K, V>[] current) {
        bigArraySize = 2 * bigArraySize;
        arrays = 0;
        IDictionary<K, V>[] temp = makeArrayOfChains(bigArraySize);
        for (int i = 0; i < bigArraySize / 2; i++) {
            if (chains[i] != null) {
                IDictionary<K, V> curr = chains[i];
                for (KVPair<K, V> pair : curr) {
                    if (temp[Math.abs(pair.getKey().hashCode()) % bigArraySize] == null) {
                        temp[Math.abs(pair.getKey().hashCode()) % bigArraySize] = new ArrayDictionary<>();
                        arrays++;
                    }
                    temp[Math.abs(pair.getKey().hashCode()) % bigArraySize].put(pair.getKey(), pair.getValue());
                }
            }
        }
        return temp;
    }
    
    @Override
    public V get(K key) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % bigArraySize;
        }
        if (chains[index] != null) {
            if (chains[index].containsKey(key)) {
                return chains[index].get(key);
            }
        }
        throw new NoSuchKeyException();
        
    }
    
    @Override
    public void put(K key, V value) {
        if (items/bigArraySize >= 1) {
            chains = resize(chains);
        }
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % bigArraySize;
        }
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<>();
            arrays++;
        }
        if (!chains[index].containsKey(key)) {
            items++;
        }
        chains[index].put(key, value);
    }

    @Override
    public V remove(K key) {
        int code = 0;
        if (key != null) {
            code = Math.abs(key.hashCode()) % bigArraySize;
        }
        if (chains[code] != null && chains[code].containsKey(key)) {
            V temp = chains[code].get(key);
            chains[code].remove(key);
            items--;
            if (chains[code].size() == 0) {
                arrays--;
            }
            return temp;
        }
        throw new NoSuchKeyException();
    }

    @Override
    public boolean containsKey(K key) {
        int code = 0;
        if (key != null) {
            code = Math.abs(key.hashCode()) % bigArraySize;
        }
        if (chains[code] != null) {
            return chains[code].containsKey(key);
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return items;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains, arrays);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int index;
        private int maxArrays;
        private int currArrays;
        private Iterator<KVPair<K, V>> itr;

        public ChainedIterator(IDictionary<K, V>[] chains, int max) {
            this.chains = chains;
            index = 0;
            currArrays = 0;
            maxArrays = max;
            if (maxArrays > 0) {
                while (chains[index] == null) {
                    index++;
                }
                itr = chains[index].iterator();
                currArrays++;
            }
        }

        @Override
        public boolean hasNext() {
            if (maxArrays == 0) {
                return false;
            } else if (itr.hasNext()) {
                return true;
            } else if (currArrays < maxArrays) {
                return true;
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {    
            if (hasNext()) {
                if (itr.hasNext()) {
                    return itr.next();
                } else if (currArrays < maxArrays) {
                    index++;
                    while (chains[index] == null) {
                        index++;
                    }
                    itr = chains[index].iterator();
                    currArrays++;
                    return next();
                }
            }
            throw new NoSuchElementException();
        }  
    }
}
