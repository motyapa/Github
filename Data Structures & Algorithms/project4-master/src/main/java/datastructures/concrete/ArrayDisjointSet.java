package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    int nextSpot;
    private IDictionary<T, Integer> values;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        this.pointers = new int[10];
        nextSpot = 0;
        values = new ChainedHashDictionary<T, Integer>();
    }

    @Override
    public void makeSet(T item) {
        if (values.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        if (nextSpot == pointers.length) {
            pointers = resize();
        }
        pointers[nextSpot] = -1;
        values.put(item, nextSpot);
        nextSpot++;
    }
    
    private int[] resize() {
        int[] temp = new int[pointers.length * 2];
        for (int i = 0; i < pointers.length; i++) {
            temp[i] = pointers[i];
        }
        return temp;
    }

    @Override
    public int findSet(T item) {
        if (!values.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        return compress(values.get(item));
    }

    private int compress(int spot) {
        if (pointers[spot] > -1) {
            pointers[spot] = compress(pointers[spot]);
            return pointers[spot];
        } else {
            return spot;
        }
    }
    
    @Override
    public void union(T item1, T item2) {
        if (!values.containsKey(item1) || !values.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int root1 = findSet(item1);
        int root2 = findSet(item2);
        if (root1 == root2) {
            throw new IllegalArgumentException();
        }
        if (pointers[root1] < pointers[root2]) {
            pointers[root2] = root1;
        } else {
            if (pointers[root1] == pointers[root2]) {
                pointers[root2]--;
            }
            pointers[root1] = root2;
        }
    }
}
