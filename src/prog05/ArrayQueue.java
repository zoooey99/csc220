package prog05;

import java.util.*;

/**
 * Implements the Queue interface using a circular array (ring buffer).
 **/
public class ArrayQueue<E> extends AbstractQueue<E>
    implements Queue<E> {

    // Data Fields
    /** Index of the first element of the queue. */
    public int first;
    /** Index of the last element of the queue or first-1 if it is empty. */
    public int last;
    /** Default capacity of the queue. */
    public static final int DEFAULT_CAPACITY = 5;
    /** Array to hold the elements. */
    public E[] theElements;

    // Constructors
    /**
     * Construct a queue with the default initial capacity.
     */
    public ArrayQueue () {
	this(DEFAULT_CAPACITY);
    }

    /**
     * Construct a queue with the specified initial capacity.
     * @param initCapacity The initial capacity
     */
    @SuppressWarnings("unchecked")
    public ArrayQueue (int initCapacity) {
		theElements = (E[]) new Object[initCapacity];
		first = 0;
		last = -1;
    }

    // Public Methods
    /**
     * Inserts a new element as last.
     * @post element is added as last.
     * @param element The element to add.
     * @return true (always successful)
     */
    @Override
    public boolean offer (E element) {
		if (size() == theElements.length)
			reallocate();

		// Store the new element at the next available index.
		theElements[++last % theElements.length] = element;
		return true;
    }

    /**
     * Returns the first element queue without removing it.
     * @return The first element of the queue (if successful).
     * return null if the queue is empty
     */
    @Override
    public E peek () {
	if (isEmpty())
	    return null;
	return theElements[first % theElements.length];
    }

    /**
     * Removes the entry at the first index of the queue and returns
     * it if the queue is not empty.
     * @post first references element that was second in the queue.
     * @return The element removed if successful or null if not
     */
    @Override
    public E poll () {
	// EXERCISE
		if (isEmpty()){
			return null;
		}
		E item = theElements[first% theElements.length];
		first++;

		return item;
    }

    /**
     * Return the size of the queue
     * @return The number of elements in the queue
     */
    @Override
    public int size () {
	return last - first + 1;
    }

    /**
     * Returns an iterator to the elements in the queue
     * @return an iterator to the elements in the queue
     */
    @Override
    public Iterator<E> iterator () {
	return new Iter();
    }
    
    // Protected Methods
    /** Inner class to implement the Iterator<E> interface. */
    protected class Iter implements Iterator<E> {
	// This is the index of the next element to return.
	int index = first;

	/**
	 * Returns true if there are more elements in the queue to access.
	 * @return true if there are more elements in the queue to access.
	 */
	@Override
	public boolean hasNext () {
	    if(index <= last){
			return true;
		}
		return false;
	}

	/**
	 * Returns the next element in the queue.
	 * @pre index references the next element to access.
	 * @post index and count are incremented.
	 * @return The element with subscript index
	 */
	@Override
	public E next () {
	    if (!hasNext())
		throw new NoSuchElementException();
	    E returnValue = null;

	    // EXERCISE
	    // Set returnValue to the next element that should be returned
	    // (Study offer and poll.)
	    // Appropriately modify the value of index.
	    ///
		returnValue = theElements[index % theElements.length];
		index++;
	    ///

	    return returnValue;
	}

	/**
	 * Remove the element accessed by the Iter object -- not implemented.
	 * @throws UnsupportedOperationException when called
	 */
	@Override
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }

    /**
     * Double the capacity and reallocate the elements.
     * @pre The array is filled to capacity.
     * @post The capacity is doubled and the first half of the
     *       expanded array is filled with elements.
     */
    @SuppressWarnings("unchecked")
    protected void reallocate () {
		int newCapacity = 2 * theElements.length;
		E[] newElements = (E[]) new Object[newCapacity];
		int n = 0;

		// EXERCISE
		// Use the new for-loop to copy all the elements of
		// /**/this/**/ into newElements.  Increment n each time
		// around the loop.
		///

		for( E item : this){
			newElements[n] = item;
			n++;
		}

		///

		theElements = newElements;
		first = 0;
		last = n - 1;
    }

    public static void main (String[] args) {
	ArrayQueue<String> q = new ArrayQueue<String>();

	q.offer("Victor");
	q.offer("Lisa");
	q.offer("Hal");
	q.offer("Zoe");
	q.offer("Wrigley");
	q.offer("Fred");

	System.out.println(q);
    }
}
