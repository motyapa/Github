package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {

    @Test(timeout=5*SECOND)
    public void insertPeakRemoveMany() {
    	IPriorityQueue<Integer> heap = new ArrayHeap<>();
    	for (int i = 0; i < 100000; i++) {
    		heap.insert(i);
    	}
    	assertEquals(100000, heap.size());
    	
    	for (int i = 0; i < 100000; i++) {
    		assertEquals(i, heap.peekMin());		
    		assertEquals(i, heap.removeMin());
    	}
    	assertEquals(0, heap.size());
    }
    
    @Test(timeout=5*SECOND)
    public void topSortSmallAmountOnLargeList() {
        IList<Integer> input = new DoubleLinkedList<>();
        for (int i = 0; i < 100000; i++) {
            input.add(i);
        }
        assertEquals(100000, input.size());
        
        IList<Integer> top = Searcher.topKSort(100, input);
        assertEquals(100, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(99900 + i, top.get(i));
        }
    }

    @Test(timeout=10*SECOND)
    public void topSortLargeKEqualToInput() {
        IList<Integer> input = new DoubleLinkedList<>();
        for (int i = 0; i < 100000; i++) {
            input.add(i);
        }
        assertEquals(100000, input.size());
        
        IList<Integer> top = Searcher.topKSort(100000, input);
        assertEquals(100000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=10*SECOND)
    public void topSortLargeKGreaterThanInput() {
        IList<Integer> input = new DoubleLinkedList<>();
        for (int i = 0; i < 100000; i++) {
            input.add(i);
        }
        assertEquals(100000, input.size());
        
        IList<Integer> top = Searcher.topKSort(110000, input);
        assertEquals(100000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }      
    }
    
    @Test(timeout=10*SECOND)
    public void topSortLargeKSmallerThanInput() {
        IList<Integer> input = new DoubleLinkedList<>();
        for (int i = 0; i < 100000; i++) {
            input.add(i);
        }
        assertEquals(100000, input.size());
        
        IList<Integer> top = Searcher.topKSort(80000, input);
        assertEquals(80000, top.size());
        
        for (int i = 0; i < top.size(); i++) {
            assertEquals(20000 + i, top.get(i));
        }
    }
    
}

