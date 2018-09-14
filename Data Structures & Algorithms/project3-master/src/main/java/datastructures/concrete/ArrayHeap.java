package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private int size;
    private int index; //index of next open spot

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        size = 10;
        heap = makeArrayOfT(size);
        index = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        if (index == 0) {
            throw new EmptyContainerException();
        } else {
            T value = heap[0];
            heap[0] = heap[index - 1];
            index--;
            percolateDown(heap, 0);
            return value;
        }
    }
    
    private void percolateDown(T[] tempheap, int curr) {
    		int pos = findSmallest(tempheap, curr);
    		if (pos != 0) {
    			T temp = tempheap[curr * 4 + pos];
    			tempheap[curr * 4 + pos] = tempheap[curr];
    			tempheap[curr] = temp;
    			curr = curr * 4 + pos;
    			percolateDown(tempheap, curr);
    		}
    }
    
    private int findSmallest(T[] tempheap, int curr) {// parent's index, find smallest among children
    		T min = tempheap[curr];
    		int pos = 0;
    		for (int i = 1; i <= 4; i++) {
    			if (curr * 4 + i < index && min.compareTo(heap[curr * 4 + i]) > 0) {
    				pos = i;
    				min = tempheap[curr * 4 + i];
    			}
    		}
    		return pos;
    }

    @Override
    public T peekMin() {
        if (index == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (index == size) {
            heap = resizeArray();
        }
        heap[index] = item;
        percolateUp();
        index++;
    }
        
        
    private T[] resizeArray() {
        T[] newHeap = makeArrayOfT(size * 2);
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        size = size * 2;
        return newHeap;
    }
    @Override
    public int size() {
        return index;
    }
    
    
    private void percolateUp() {
        int currIndex = index;
        while (heap[currIndex].compareTo(heap[(currIndex - 1) / 4]) < 0) {
            T temp = heap[(currIndex - 1) / 4];
            heap[(currIndex - 1) / 4] = heap[currIndex];
            heap[currIndex] = temp;
            currIndex = (currIndex - 1) / 4;
        }
    }
}
