package prog02;

import java.io.*;

/**
 * This is an implementation of PhoneDirectory that uses a sorted
 * array to store the entries.
 *
 * @author 
 */
public class SortedPD extends ArrayBasedPD {
    /**
     * Find an entry in the directory.
     *
     * @param name The name to be found
     * @return The index of the entry with that name or, if it is not
     * there, (-insert_index - 1), where insert_index is the index
     * where should be added.
     */
    protected int find(String name) {
        int low = 0;
        int high = size -1;
        int middle;
        int comparison = 0;

        while ( low <= high ){
            middle = ( low + high ) / 2;
            String middle_name = theDirectory[middle].getName();
            comparison = middle_name.compareTo(name);

            if (comparison < 0 ){
                low = middle + 1;
            }else if( comparison > 0 ){
                high = middle - 1;
            } else {
                return middle;
            }
        }

        if(theDirectory[low] != null && theDirectory[low].getName().equals(name)){
            return low;
        }else{
            return -(low +1);
        }
    }

    /**
     * Add an entry to the directory.
     *
     * @param index    The index at which to add the entry to theDirectory.
     * @param newEntry The new entry to add.
     * @return The DirectoryEntry that was just added.
     */
    protected DirectoryEntry add(int index, DirectoryEntry newEntry) {
        if (size == theDirectory.length)
            reallocate();
        for(int i = size -1; i >= index; i--){
            theDirectory[i+1] = theDirectory[i];
        }

        theDirectory[index] = newEntry;
        size++;
        return newEntry;
    }

    /**
     * Remove an entry from the directory.
     *
     * @param index The index in theDirectory of the entry to remove.
     * @return The DirectoryEntry that was just removed.
     */
    protected DirectoryEntry remove(int index) {
        DirectoryEntry entry = theDirectory[index];
        for(int i = index; i < size; i++){
            theDirectory[i] = theDirectory[i+1];
        }
        size--;
        return entry;

    }



}
