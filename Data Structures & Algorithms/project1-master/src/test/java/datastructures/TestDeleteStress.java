package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;


import static org.junit.Assert.assertTrue;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }
    
    @Test(timeout=2 * SECOND)
    public void testAddAndDeleteWorksForManyNumbers() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
        for (int i = 0; i < cap; i++) {
            int value = list.delete(0);
            assertEquals(i* 2, value);
        }
        assertEquals(0, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testAlternatingAddAndDelete() {
        int iterators = 1000;

        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < iterators; i++) {
            String entry = "" + i;
            list.add(entry);
            assertEquals(1, list.size());

            String out = list.delete(0);
            assertEquals(entry, out);
            assertEquals(0, list.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testNullDelete() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.insert(2, null);
        assertListMatches(new Integer[]{1, 2, null, 3, 4}, list);

        assertEquals(null, list.delete(2));
    }
    
}
