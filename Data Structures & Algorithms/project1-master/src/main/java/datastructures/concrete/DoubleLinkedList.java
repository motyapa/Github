package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (front == null) {
            front = new Node<T>(item);
            back = front;
        } else {
            back = new Node<T>(back, item, null);
            back.prev.next = back;
        }
        size++;
    }

    @Override
    public T remove() {
        if (front == null) {
            throw new EmptyContainerException();
        }
        T temp = this.back.data;
        if (size == 1) {
            back = null;
            front = null;
        } else {
            back = back.prev;
            back.next = null;         
        }
        size--;
        return temp;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = traversal(index);
        return temp.data;
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        // size = 1 case
        if (size == 1) {
            remove();
            add(item);
        // front case
        } else if (index == 0) {
            front = new Node<T>(null, item, front.next);
            front.next.prev = front;
        // back case
        } else if (index == size() - 1) {
            back = new Node<T>(back.prev, item, null);
            back.prev.next = back;
        } else {
            Node<T> temp = traversal(index);
            temp = new Node<T>(temp.prev, item, temp.next);
            temp.prev.next = temp;
            temp.next.prev = temp;
        }
    }
    
    //private helper method for traversing through the double
    //list, starts from the back if index is closer to back and
    //front if the index is closer to the front.
    private Node<T> traversal(int index) {
        Node<T> temp = null;
        if (size / 2 <= index) {
            temp = this.back;
            for (int i = 0; i < size - index - 1; i++) {
                temp = temp.prev;
            }
        } else {
            temp = this.front;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        }
        return temp;
    }
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        
        //front case
        if (index == 0) {
            if (size == 0) {
                add(item);
            } else {
                front = new Node<T>(null, item, front);
                front.next.prev = front;
                size++;
            }
        // back case    
        } else if (index == size) {
            add(item);
        } else {
            Node<T> temp = traversal(index - 1);
            temp.next = new Node<T>(temp, item, temp.next);
            temp.next.next.prev = temp.next;
            size++;
        }
 
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        T item = null;
        if (index == size - 1) {
            item = remove();
        } else if (index == 0) {
            item = front.data;
            front = front.next;
            front.prev = null;          
            size--;
        } else {
            Node<T> temp = traversal(index - 1);
            item = temp.next.data;
            temp.next = temp.next.next;
            temp.next.prev = temp;
            size--;
        }
        return item;
    }

    @Override
    public int indexOf(T item) {       
        if (front != null) {
            Node<T> temp = front;
            int index = 0;
            while (temp != null) {
                if (temp.data == item || (temp.data != null && temp.data.equals(item))) {
                    return index;
                }
                index++;
                temp = temp.next;           
            }
        }
        return -1;
    }    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        return (indexOf(other) != -1);
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return (current != null);
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (hasNext()) {
                Node<T> temp = current;
                current = current.next;
                return temp.data;
            } else if (current != null && current.next == null) {
                return current.data;
            }
            throw new NoSuchElementException();
        }
    }
}
