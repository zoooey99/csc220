package prog07;

import prog02.GUI;
import prog02.UserInterface;
import prog05.LinkedQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NotWordle {
    UserInterface ui; // class variable
    static List<String> allWords = new ArrayList<String>();
    NotWordle(UserInterface ui) { // constructor that takes a UserInterface
        this.ui = ui; // and stores it in a class variable
    }


    public static void main(String[] args) throws FileNotFoundException {

        UserInterface ui = new GUI("Not Wordle");
        NotWordle game = new NotWordle(ui);
        String[] commands = { "Human plays.", "Computer plays." };

        String fileName = ui.getInfo("Enter word file:");
        game.loadWords(fileName);

        String startWord = ui.getInfo("Enter starting word.");
        String targetWord = ui.getInfo("Enter target word.");

        int c = ui.getCommand(commands);
        switch(c) {
            case(0): //human
                game.play(startWord, targetWord);
                break;
            case(1):
                game.solve(startWord, targetWord);
                break;
        }

    } //end of main method
    void play (String start, String target){

        while(true){
            ui.sendMessage("Current word: " + start + "\n Target word: " + target);
            String input = ui.getInfo("What is your next word?");
            if(input == null){
                return;
            }

            while(true){
                if(find(input) >= 0 && oneLetterDifferent(input, start)){
                    break;
                }else if(find(input) < 0){
                    ui.sendMessage(input + " is not in the dictionary.");
                }else if(!oneLetterDifferent(start, input)){
                    ui.sendMessage("Sorry, but " + input + " differs more than one letter from " + start);
                }
                input = ui.getInfo("What is your next word?");
                if(input == null){
                    return;
                }
            }

            start = input;

            //check win
            if (start.equalsIgnoreCase(target)){
                ui.sendMessage("You win!");
                return;
            }
        }


    } //end of play method


    void solve (String startWord, String targetWord){
        /*Inside solve, create a new
        array of int parentIndices whose length equals the number of words
        in allWords.  Set each element to -1.  Create a queue of Integer.
        Find the index of the start word and add it to the queue.  Also
        save it in a variable startIndex since you will need to refer to it
        again later.*/

        Queue<Integer> queue = new LinkedQueue<Integer>();
        Integer[] parentIndices = new Integer[allWords.size()];
        for(int i = 0; i < allWords.size()-1; i++){
            parentIndices[i] = -1;
        }
        int startIndex = allWords.indexOf(startWord);
        queue.offer(startIndex);
        int parentIndex;
        String parentWord;
        int numPoll = 0;

        while(!queue.isEmpty()){
            parentIndex = queue.poll();
            numPoll ++;
            //System.out.println("DEQUEUE " + allWords.get(parentIndex));
            parentWord = allWords.get(parentIndex);

            for(int i = 0; i < allWords.size()-1; i++){

                if( i != startIndex && parentIndices[i] == -1 && oneLetterDifferent(allWords.get(i), parentWord)){
                    parentIndices[i] = parentIndex;
                    queue.add(i);
                    //System.out.println("ENQUEUE " + allWords.get(i));
                    if(allWords.get(i).equals(targetWord)){
                        String p = "";
                        int a = parentIndices[i];
                        while(a != startIndex){
                            p = allWords.get(a) + "\n" + p;
                            a = parentIndices[a];
                        }
                        p = startWord + "\n" + p + targetWord;
                        ui.sendMessage(p);
                        ui.sendMessage("poll was called " + numPoll + " times.");
                        //ui.sendMessage("numDifferent = " + numDifferent(startWord, targetWord));
                        ui.sendMessage(targetWord + " is " + numSteps(parentIndices, parentIndex) + " steps away from " + startWord);
                        return;
                    }

                }

            }

        }//end of while loop



    } //end of solve method
    static boolean oneLetterDifferent(String a, String b){

        boolean same = true;

        if ( a.length() == b.length()){
            for( int i = 0; i < a.length(); i++){
                if( !a.substring(i,i+1).equals( b.substring(i,i+1) ) ){
                    if (!same){
                        return false; //if there was already one different
                    }
                    same = false;
                }

            }
            return true; //if not more than one letter difference
        }else{
            return false; //if not the same size
        }

    }

    void loadWords(String fileName) throws FileNotFoundException {
        File theFile = new File (fileName);
        Scanner in = new Scanner(theFile);

        while(in.hasNext()){
            allWords.add(in.nextLine());
        }

    }
    /*Write a find method that takes a String word and finds that word in
    allWords.  It should return the index of that word or
   -insert_index-1 if not there.  Modify play so it also refuses a
    word not in words.  Test using the file called words.txt in the
    prog05 directory: move it to the csc220 IntelliJ project folder
            (IdeaProjects/csc220).*/
    static int find(String word){

        int low = 0;
        int middle;
        int high = allWords.size() - 1;
        int comparison = 0;

        while(low <= high){
            middle = (low + high) /2;
            comparison = allWords.get(middle).compareTo(word);

            if (comparison > 0){
                high = middle - 1;
            } else if (comparison < 0){
                low = middle + 1;
            } else{
                return middle;
            }
        }
        return -low -1;

    }//end of find method

    static int numDifferent(String start, String target){
        int numDiff = 0;
        for(int i = 0; i < start.length(); i ++){
            if ( start.charAt(i) != target.charAt(i)){
                numDiff++;
            }
        }
        return numDiff;
    }
    static int numSteps (Integer[] parentIndices, int index){
        int count = 1;
        while(parentIndices[index] != -1){
            count ++;
            index = parentIndices[index];
        }
        return count;
    }

//    private class IndexComparator implements Comparator<Integer>{
//        Integer[] parentIndices;
//        String targetWord;
//        IndexComparator(Integer[] parentIndices, String targetWord){
//            this.parentIndices = parentIndices;
//            this.targetWord = targetWord;
//        }
//        public int priorityIndex(){
//
//        }
//        public int compare(int index1, int index2){
//            return sumNums(index2) - sumNums(index1);
//        }
    //}

}
