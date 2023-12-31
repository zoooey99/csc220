
package prog11;

import java.util.*;

import prog05.ArrayQueue;
import prog11.HardDisk;

public class NotGPT implements SearchEngine {

    HardDisk pageDisk = new HardDisk();
    Map<String, Long> urlToIndex = new TreeMap<>();

    HardDisk wordDisk = new HardDisk();
    Map<String, Long> wordToIndex = new HashMap<>();

    void rankSlow(double defaultInfluence){
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            double influencePerIndex = file.influence/ (double)file.indices.size();
            for(Long pageIndex: file.indices){
                InfoFile indexFile = pageDisk.get(pageIndex);
                indexFile.influenceTemp += influencePerIndex;
            }
        }
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.influence = file.influenceTemp + defaultInfluence;
            file.influenceTemp = 0.0;

        }

    }
    void rankFast(double defaultInfluence){
        List<Vote> voteList = new ArrayList<>();

        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();

            for(Long indexOnPage: file.indices){
                voteList.add(new Vote(indexOnPage, file.influence / file.indices.size()));
            }
        }
        Collections.sort(voteList);
        Iterator<Vote> iterator = voteList.iterator();
        Vote vote = null;
        if(iterator.hasNext()){
            vote = iterator.next();
        }

        for(Map.Entry<Long, InfoFile> entry: pageDisk.entrySet()) //FOR EACH PAGE, add  priority of votes with index of page
        {
            long index = entry.getKey();
            InfoFile file = entry.getValue();

            file.influence = defaultInfluence;

            while (vote != null && vote.index == index) {
                file.influence += vote.vote;

                if (iterator.hasNext()) {
                    vote = iterator.next();
                } else vote = null;


            }
        }

    }

    public long indexWord(String word) {
        Long index = wordDisk.newFile();
        InfoFile file = new InfoFile(word);

        wordDisk.put(index, file);
        wordToIndex.put(word,index);

        System.out.println("Indexing word " + index +  " " + file );

        return index;
    } //end of indexWord method

    // collect method
    @Override
    public void collect(Browser browser, List<String> startingURLs) {
        Queue<Long> indexQueue = new LinkedList<>();

        for(String s : startingURLs) {
            if(!urlToIndex.containsKey(s)) {
                Long index = indexPage(s);
                indexQueue.offer(index);
            }
        }

        while(!indexQueue.isEmpty()){
            long index = indexQueue.poll();

            InfoFile url = pageDisk.get(index);


            if(browser.loadPage(url.data)) {
                Set<String> seen = new HashSet<String>();

                Set<String> wordSeen= new HashSet<String>();

                for (String url2 : browser.getURLs()) {
                    if (urlToIndex.get(url2) == null) {
                        //System.out.println("Sorry there is no page with that index!")
                        indexQueue.offer(indexPage(url2));
                    }
                    if(!seen.contains(url2)) {
                        seen.add(url2);
                        url.indices.add(urlToIndex.get(url2));

                    }
                }

                for(String word : browser.getWords()) {
                    Long wordIndex = wordToIndex.get(word);

                    if(wordIndex == null) {
                        wordIndex = indexWord(word);
                    }
                    if(!wordSeen.contains(word)) {
                        wordSeen.add(word);
                        InfoFile words = wordDisk.get(wordIndex);
                        words.indices.add(index);

                    }

                }

            }

        }

    }//end of collect method

    // Empty rank method
    @Override
    public void rank(boolean fast) {
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.influence = 1.0;
            file.influenceTemp = 0.0;
        }
        int count = 0;
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();

            if(file.indices.isEmpty()){
                count++;
            }
        }
        double defaultInfluence = 1.0 * (double)count / pageDisk.size();

        if(fast){
            for(int i = 0; i < 20; i++){
                rankFast(defaultInfluence);
            }
        }else{
            for(int i = 0; i < 20; i++){
                rankSlow(defaultInfluence);
            }
        }

    } // end of rank method



    //Empty search String method
    @Override
    public String[] search(List<String> searchWords, int numResults) {

        Iterator<Long>[] wordPageIndexIterators = new Iterator[searchWords.size()];
        //long[] currentPageIndex = new long[searchWords.size()];
        long[] currentPageIndex;

        for(int i = 0; i < wordPageIndexIterators.length; i++){

            Long wordIndex = wordToIndex.get(searchWords.get(i));
            if(wordIndex == null){
                searchWords.remove(searchWords.get(i));
                continue;
            }
            InfoFile wordFile = wordDisk.get(wordIndex);
            wordPageIndexIterators[i] = wordFile.indices.iterator();

        }

        currentPageIndex = new long[searchWords.size()];



        PageComparator pageComparator = new PageComparator();

        // Matching pages with the least popular page on the top of the
        // queue.

        PriorityQueue<Long> bestPageIndices = new PriorityQueue<Long>(numResults, new PageComparator());


        while(getNextPageIndices(currentPageIndex, wordPageIndexIterators)){

            if(currentPageIndex.length == 0){
                break;
            }
            if(allEqual(currentPageIndex)){
                Long pageIndex = currentPageIndex[0];
                System.out.println(pageDisk.get(pageIndex).data);

                /*If the priority queue is not "full" (has numResults elements), just
                offer the matching page index.*/
                if(bestPageIndices.size() < numResults){
                    bestPageIndices.offer(pageIndex);
                }else{
                    if(pageComparator.compare(pageIndex, bestPageIndices.peek()) > 0){ //could be the opposite way
                        bestPageIndices.poll();
                        bestPageIndices.offer(pageIndex);
                    }
                }
            }

        }
        int arraySize = bestPageIndices.size();
        String[] output = new String[arraySize];
        ArrayDeque<Long> holder = new ArrayDeque<>();
        for(int i = 0; i < arraySize; i++){
            holder.push(bestPageIndices.poll());
        }

        //System.out.println("Order of Influence:"); //debug
        for(int i = 0; i < arraySize; i++){
            Long holderIndex = holder.pop();
            output[i] = pageDisk.get(holderIndex).data;

            //System.out.println("    " + pageDisk.get(holderIndex).influence); //debug
        }

        return output;
    } //end of search method

    /** Check if all elements in an array of long are equal.
     @param array an array of numbers
     @return true if all are equal, false otherwise
     */
    private boolean allEqual(long[] array){
        for(int i = 0; i < array.length - 1; i++){
            if(array[i] != array[i+1]) {
                 return false;
            }
        }
        return true;
    }//end allEqual Method

    /** Get the largest element of an array of long.
     @param array an array of numbers
     @return largest element
     */
    private long getLargest(long[] array){
        long largest = 0;
        for(int i = 0; i < array.length ; i++){
            if(array[i] > largest) {
                largest = array[i];
            }
        }
        return largest;
    }//end getLargest method

    /** If all the elements of currentPageIndex are equal,
     set each one to the next() of its Iterator,
     but if any Iterator hasNext() is false, just return false.

     Otherwise, do that for every element not equal to the largest element.

     Return true.

     @param currentPageIndex array of current page indices
     @param wordPageIndexIterators array of iterators with next page indices
     @return true if all page indices are updated, false otherwise
     */
    private boolean getNextPageIndices(long[] currentPageIndex, Iterator<Long>[] wordPageIndexIterators) {
        if(allEqual(currentPageIndex)){
            for(int i = 0; i < currentPageIndex.length; i++){
                if(!wordPageIndexIterators[i].hasNext()){
                    return false;
                }
                currentPageIndex[i] = wordPageIndexIterators[i].next();
            }
        }

        long largest = getLargest(currentPageIndex);
        for(int i = 0; i < currentPageIndex.length; i++){
            if(currentPageIndex[i] < largest){
                if(!wordPageIndexIterators[i].hasNext()){
                    return false;
                }
                currentPageIndex[i] = wordPageIndexIterators[i].next();
            }
        }
        return true;

    }//end getNextPageIndices method
    public long indexPage(String url) {
        long index = pageDisk.newFile(); // Get the index of a new file from pageDisk
        InfoFile infoFile = new InfoFile(url); // Create a new InfoFile
        pageDisk.put(index, infoFile); // Store the InfoFile in pageDisk
        urlToIndex.put(url, index); // Map URL to the index
        System.out.println("Indexed: " + url + " at " + index); // Print statement as instructed
        return index;


    }//end of indexPage

    public class Vote implements Comparable<Vote> {

        Long index;
        double vote;
        public Vote (Long index, double vote) {

            this.index = index;
            this.vote = vote;
        }

        @Override
        public int compareTo(Vote o) {
            long c = this.index - o.index;
            if(c!=0) return (int)c;
            return Double.compare(pageDisk.get(this.index).influence, pageDisk.get(o.index).influence);

        }
    }

    public class PageComparator implements Comparator<Long>{
//        Long pageIndex1;
//        Long pageIndex2;
//        public PageComparator(Long pageIndex1, Long pageIndex2){
//            this.pageIndex1 = pageIndex1;
//            this.pageIndex2 = pageIndex2;
//        }
        public int compare(Long pageIndex1, Long pageIndex2 ){
            return Double.compare(pageDisk.get(pageIndex1).influence, pageDisk.get(pageIndex2).influence);
        }

    }// end of PageComparator class




}//end of NotGPT class
