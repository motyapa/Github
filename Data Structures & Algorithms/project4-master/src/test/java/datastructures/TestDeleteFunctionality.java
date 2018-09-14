package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }

    @Test(timeout=SECOND)
    public void deleteFrontBackMiddleAndLastElement() {
        IList<String> list = makeBasicList();
        list.add("d");
        list.add("e");
        list.add("f");
        
        this.assertListMatches(new String[] {"a", "b", "c", "d", "e", "f"}, list);
        
        //front case
        String removeA = list.delete(0);
        assertEquals(removeA, "a");
        this.assertListMatches(new String[] {"b", "c", "d", "e", "f"}, list);
        
        //back case
        String removeF = list.delete(list.size() - 1);
        assertEquals(removeF, "f");
        this.assertListMatches(new String[] {"b", "c", "d", "e"}, list);
        
        //middle case
        String removeD = list.delete(2);
        assertEquals(removeD, "d");
        this.assertListMatches(new String[] {"b", "c", "e"}, list);
        
        list.delete(0);
        list.delete(0);
        
        //last in the list
        assertEquals(list.delete(0), "e");
        this.assertListMatches(new String[] {}, list); 
    }
    
    @Test(timeout=SECOND)
    public void testDeleteOnEmptyListThrowsException() {
        IList<String> list = this.makeBasicList();
        try {
            list.delete(3);
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
        try {
            list.delete(-1);
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=5 * SECOND)
    public void testAddAndDeleteFromEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        for (int i = 0; i < 10000; i++) {
            list.add(-1);
            list.delete(10000);
        }
    }
    
}
