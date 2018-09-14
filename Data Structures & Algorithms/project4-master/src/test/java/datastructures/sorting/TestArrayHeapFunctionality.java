package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test
    public void getFirst() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(3);
    	assertEquals(3, heap.peekMin());
    	assertEquals(1, heap.size());
    	assertEquals(3, heap.removeMin());
    	assertTrue(heap.isEmpty());
    }
    
    @Test
    public void testNullEntry() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test
    public void testEmptyRemove() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    } 
    
    @Test
    public void testEmptyPeek() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    } 
    
    @Test
    public void testPercolate() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(8);
    	heap.insert(3);
    	heap.insert(12);
    	heap.insert(6);
    	heap.insert(1);
    	heap.insert(0);
    	assertEquals(6, heap.size());
    	
    	assertEquals(0, heap.removeMin());
    	assertEquals(1, heap.removeMin());
    	assertEquals(3, heap.removeMin());
    	assertEquals(6, heap.removeMin());
    	assertEquals(8, heap.removeMin());
    	assertEquals(12, heap.removeMin());  
    	assertEquals(0, heap.size());
    }
    
    @Test
    public void testPercolateUp() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	for (int i = 21; i > 0; i--) {
    		heap.insert(i);
    	}
    	
    	assertEquals(21, heap.size());
    	assertEquals(1, heap.peekMin());
    	
    	for (int i = 1; i <= 21; i++) {
    	    assertEquals(i, heap.removeMin());
    	}
    }
    
    
    
}
