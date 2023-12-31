package prog06;
import java.util.Map;
import java.util.Random;

public class SkipMap<K extends Comparable<K>, V> extends LinkedMap<K, V> {
    // SkipMap containing half the elements chosen at random.
    SkipMap<K, Entry> skip;

    // Coin flipping code.
    Random random = new Random(1);
    /** Flip a coin.
     * @return true if you flip heads.
     */
    boolean heads () {
	    return random.nextInt() % 2 == 0;
    }

    protected void add (Entry nextEntry, Entry newEntry) {

        super.add(nextEntry, newEntry);

        // EXERCISE
        // Flip a coin.  If you flip heads, put newEntry into skip, using
        // its own key as key.  Don't forget to allocate skip if it hasn't
        // been allocated yet.
        ///

        if(heads()){
            if(skip == null){
                skip = new SkipMap<K,Entry>();
            }
            skip.put(newEntry.key, newEntry);
        }

        ///
    }

    protected Entry find (K key) {
        Entry entry = first;
        // EXERCISE
        // Call find for the key in skip.
        // Set entry to the value of that Entry in skip.
        // Check for null so you don't crash.
        ///

        if(skip != null ){
            LinkedMap<K, LinkedMap<K,V>.Entry>.Entry skipEntry = skip.find(key);
            if(skipEntry != null){
                entry = skipEntry.value;
            }
        }

        ///

        // EXERCISE
        // Use the same search as in LinkedMap.find,
        // but use the current value of entry
        // instead of starting at first.
        ///

        for (Entry indexEntry = entry ; indexEntry != null ; indexEntry = indexEntry.next) {
            // Compare entry.key to key.
            int c = indexEntry.key.compareTo(key);
            // return entry if entry.key is equal to key
            if(c == 0) {
                return indexEntry;
            }else if(c > 0){
                return indexEntry.previous;
            }

        }
        ///
        return last;

    }//end of find method

    protected void remove (Entry entry) {
        super.remove(entry);
        // EXERCISE
        // Remove the key of entry from skip.  (Use public remove.)
        // If skip becomes empty, set it to null.
        ///
        if(skip != null) {
            skip.remove(entry.key);
            if(skip.first == null){
                skip = null;
            }
        }

        ///
    }

    public String toString () {
	if (skip == null)
	    return super.toString();
	return skip.toString() + "\n" + super.toString();
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new SkipMap<String, Integer>();
	test(map);
    }
}

