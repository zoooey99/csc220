package prog10;

import java.io.File;
import java.util.*;

import prog02.UserInterface;
import prog02.GUI;

public class Jumble {
  /**
   * Sort the letters in a word.
   * @param word Input word to be sorted, like "computer".
   * @return Sorted version of word, like "cemoptru".
   */
  public static String sort (String word) {
    char[] sorted = new char[word.length()];
    for (int n = 0; n < word.length(); n++) {
      char c = word.charAt(n);
      int i = n;

      while (i > 0 && c < sorted[i-1]) {
        sorted[i] = sorted[i-1];
        i--;
      }

      sorted[i] = c;
    }

    return new String(sorted, 0, word.length());
  }

  public static void main (String[] args) {
    UserInterface ui = new GUI("Jumble");
    // UserInterface ui = new ConsoleUI();

    // Map<String,String> map = new TreeMap<String,String>();
    // Map<String,String> map = new PDMap();
    // Map<String,String> map = new LinkedMap<String,String>();
    // Map<String,String> map = new SkipMap<String,String>();
    // BST<String,String> map = new BST<String,String>();
    Map<String, List<String>> map = new HashTable<>();

    Scanner in = null;
    do {
      try {
        in = new Scanner(new File(ui.getInfo("Enter word file.")));
      } catch (Exception e) {
        System.out.println(e);
        System.out.println("Try again.");
      }
    } while (in == null);

    int n = 0;
    while (in.hasNextLine()) {
      String word = in.nextLine();
      if (n++ % 1000 == 0) {
        System.out.println(word + " sorted is " + sort(word));
      }

      // EXERCISE: Insert an entry for word into map.
      // What is the key?  What is the value?
      String sortedWord = sort(word);
      if(!map.containsKey(sortedWord)){
        map.put(sortedWord, new ArrayList<String>() );
      }
      map.get(sortedWord).add(word);

    }

    while(true){
      String jumble = ui.getInfo("Enter jumble.");
      if (jumble == null)
        break;

      // EXERCISE:  Look up the jumble in the map.
      // What key do you use?
      String sortedJumble = sort(jumble);
      List words = map.get(sortedJumble);

      if (words == null || words.isEmpty())
        ui.sendMessage("No match for " + jumble);
      else
        ui.sendMessage(jumble + " unjumbled is any of " + words.toString());
    }

    while(true){
      String clueLetters = ui.getInfo("Enter letters from clues.");
      if(clueLetters == null){
        break;
      }
      String sortedLetters = sort(clueLetters);
      int clueLength = Integer.parseInt(ui.getInfo("Enter clue length: "));
      if(clueLength == 0){
        break;
      }

      int key1index = 0;
      for(String key1: map.keySet()){
        if(key1.length()==clueLength){
          String key2 = "";
          key1index = 0;
          for(char letter : sortedLetters.toCharArray()){
            if( key1index >= clueLength ){
              key2 += letter;
            }else if(letter == key1.charAt(key1index)){
              key1index++;
            }else if(letter > key1.charAt(key1index)){
              break;
            }else{
              key2 += letter;
            }

          }

          if( key1index >= clueLength && map.containsKey(key2)){
            ui.sendMessage(map.get(key1)+ "  " + map.get(key2));
          }


        }
      }






    }


  }
}




