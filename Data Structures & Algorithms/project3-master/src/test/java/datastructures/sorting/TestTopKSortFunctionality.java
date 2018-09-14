package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testSpecialK() {
        // list.size = 0; k = 0
        IList<Integer> list1 = new DoubleLinkedList<>();
        IList<Integer> top1 = Searcher.topKSort(0, list1);
        assertEquals(0, top1.size());
        
        
        //k = list.size
        IList<Integer> list2 = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list2.add(i);
        }
        
        IList<Integer> top2 = Searcher.topKSort(20, list2);
        assertEquals(20, top2.size());
        
        for (int i = 0; i < 20; i++) {
            assertEquals(i, top2.get(i));
        }
        
        
        //k > list.size
        IList<Integer> list3 = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list3.add(i);
        }
        
        IList<Integer> top3 = Searcher.topKSort(30, list2);
        assertEquals(20, top3.size());
        
        for (int i = 0; i < 20; i++) {
            assertEquals(i, top3.get(i));
        }
        
        //list.size != 0; k = 0
        IList<Integer> list4 = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list4.add(i);
        }
        IList<Integer> top4 = Searcher.topKSort(0, list2);
        assertEquals(0, top4.size());
        
    } 
    
    @Test(timeout=SECOND)
    public void testErrorK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
}
