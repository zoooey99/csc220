package prog06;
import java.util.*;
import java.util.Map.Entry;

public class LinkedMap <K extends Comparable<K>, V>
    extends AbstractMap<K, V> {

    protected class Entry implements Map.Entry<K, V> {
	K key;
	V value;
	Entry previous, next;
    
	Entry (K key, V value) {
	    this.key = key;
	    this.value = value;
	}
    
	public K getKey () { return key; }
	public V getValue () { return value; }
	public V setValue (V newValue) {
	    V oldValue = value;
	    value = newValue;
	    return oldValue;
	}

	public String toString () {
	    return "{" + key + "=" + value + "}";
	}
    }
  
    protected Entry first, last;
  
    /**
     * Find the Entry with key or the previous key.
     * @param key The Key to be found.
     * @return The Entry e with e.key.equals(key)
     * or the last Entry with e.key < key
     * or null
     */
    protected Entry find (K key) {
	// EXERCISE
	// Look at size() method.
	///
	for (Entry entry = first ; entry != null ; entry = entry.next) {
	    // Compare entry.key to key.
		int c = entry.key.compareTo(key);
	    // return entry if entry.key is equal to key
		if(c == 0) {
			return entry;
		}else if(c > 0){
			return entry.previous;
		}

	}
	///
	return last; // Did not find the entry, so last must be previous.
    }    
  
    /**
     * Determine if the Entry returned by find is the one we are looking
     * for.
     * @param entry The Entry returned by find.
     * @param key The Key to be found.
     * @return true if find found the entry with that key
     * or false otherwise
     */
    protected boolean found (Entry entry, K key) {
		// EXERCISE
		return entry != null && entry.key.equals(key);
		///
    }

    public boolean containsKey (Object keyAsObject) {
		K key = (K) keyAsObject;
		Entry entry = find(key);
		return found(entry, key);
    }
  
    public V get (Object keyAsObject) {
		// EXERCISE
		// Look at containsKey.
		// If Entry with key was found, return its value.
		///

		K key = (K) keyAsObject;
		Entry entry = find(key);
		if(entry != null && entry.key.equals(key)){
			return entry.value;
		}


		///
		return null;
    }
  
    /**
     * Add newEntry just after previousEntry or as first Entry if
     * previousEntry is null.
     * @param previousEntry Entry before newEntry or null if there isn't one.
     * @param newEntry The new Entry to be inserted after previousEntry.
     */
    protected void add (Entry previousEntry, Entry newEntry) {
		// EXERCISE
		Entry nextEntry = null;
		///
		// Set nextEntry.  Two cases.
		if(previousEntry == null ) {
			nextEntry = first;
		}else {
			nextEntry = previousEntry.next;
		}
		// Set previousEntry.next or first to newEntry.
		if(previousEntry != null){
			previousEntry.next = newEntry;
		}else{
			first = newEntry;
		}
		// Set nextEntry.previous or last to newEntry.
		if( nextEntry != null){
			nextEntry.previous = newEntry;
		}else{
			last = newEntry;
		}
		// Set newEntry.previous and newEntry.next.
		newEntry.previous = previousEntry;
		newEntry.next = nextEntry;


	///
    }

    public V put (K key, V value) {
		Entry entry = find(key);

		// EXERCISE
		///
		// Handle the case that the key is already there.
		// Save yourself typing:  setValue returns the old value!
		if (entry == null) {
			add(null, new Entry(key, value) );
			return null;
		}else if (entry.key.equals(key)){
			V oldValue = entry.value;
			entry.value = value;
			return oldValue;
		} else {
			add(entry, new Entry(key, value));
			return null;
		}

		///

    }      
  
    /**
     * Remove Entry entry from list.
     * @param entry The entry to remove.
     */
    protected void remove (Entry entry) {
	// EXERCISE
	///
		if(found(entry,entry.key)){
			Entry previousEntry = entry.previous;
			Entry nextEntry = entry.next;
			if(entry != first){
				previousEntry.next = nextEntry;
			}else{
				first = nextEntry;
			}
			if(entry != last){
				nextEntry.previous = previousEntry;
			}else{
				last = previousEntry;
			}
		}

	///
    }

    public V remove (Object keyAsObject) {
		// EXERCISE
		// Use find, but make sure you got the right Entry!
		// Look at containsKey.
		// If you do, then remove it and return its value.
		///

		K key = (K) keyAsObject;
		Entry entry = find(key);
		if ( found(entry, key)){
			remove(entry);
			return entry.value;
		}

		///
		return null;
    }      

    protected class Iter implements Iterator<Map.Entry<K, V>> {
	// EXERCISE
	// Set entry to the first entry.
	///
	Entry entry = first;
	///
    
	public boolean hasNext () { 
	    // EXERCISE
	    ///
	    return entry != null;
	    ///
	}
    
	public Map.Entry<K, V> next () {
	    Entry ret = entry;
	    // EXERCISE
	    // Set entry to the next value of entry.
	    ///
		if (!hasNext())
			throw new NoSuchElementException();

		entry = entry.next;

	    ///
	    return ret;

	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    public int size () {
	int count = 0;
	for (Entry entry = first; entry != null; entry = entry.next)
	    count++;
	return count;
    }

    protected class Setter extends AbstractSet<Map.Entry<K, V>> {
	public Iterator<Map.Entry<K, V>> iterator () {
	    return new Iter();
	}
    
	public int size () { return LinkedMap.this.size(); }
    }
  
    public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }
  
    static void test (Map<String, Integer> map) {
	if (false) {
	    map.put("Victor", 50);
	    map.put("Irina", 45);
	    map.put("Lisa", 47);
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
    
	    System.out.println(map.put("Irina", 55));

	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());

	    System.out.println(map.remove("Irina"));
	    System.out.println(map.remove("Irina"));
	    System.out.println(map.get("Irina"));
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
	}
	else {
	    String[] keys = { "Vic", "Ira", "Sue", "Zoe", "Bob", "Ann", "Moe" };
	    for (int i = 0; i < keys.length; i++) {
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + -i + ") = ");
		System.out.println(map.put(keys[i], -i));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	    for (int i = keys.length; --i >= 0;) {
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	}
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new LinkedMap<String, Integer>();
	test(map);
    }
}
