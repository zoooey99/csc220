package prog04;

import java.util.Stack;

import prog02.UserInterface;
import prog02.ConsoleUI;
import prog02.GUI;

public class Game {
    //static UserInterface ui = new ConsoleUI();
    static UserInterface ui = new GUI("Tower of Hanoi");

    static public void main(String[] args) {
        int n = getInt("How many disks?");
        if (n <= 0) return;
        Game tower = new Game(n);

        String[] commands = {"Human plays.", "Computer plays."};
        int c = ui.getCommand(commands);
        if (c == -1) return;
        if (c == 0) tower.play();
        else tower.solve();
    }

    /**
     * Get an integer from the user using prompt as the request.
     * Return 0 if user cancels.
     */
    static int getInt(String prompt) {
        while (true) {
            String number = ui.getInfo(prompt);
            if (number == null) return 0;
            try {
                return Integer.parseInt(number);
            } catch (Exception e) {
                ui.sendMessage(number + " is not a number.  Try again.");
            }
        }
    }

    int nDisks;
    StackInterface<Integer>[] pegs = (StackInterface<Integer>[]) new ArrayStack[3];

    Game(int nDisks) {
        this.nDisks = nDisks;
        for (int i = 0; i < pegs.length; i++) {
            pegs[i] = new ArrayStack<Integer>();
        }
        for(int i = nDisks; i > 0; i--){
            pegs[0].push(i);
        }
        // EXERCISE: Initialize game with pile of nDisks disks on peg 'a' (pegs[0]).


    }//end of Game

    void play() {
        String[] moves = {"ab", "ac", "ba", "bc", "ca", "cb"};
        int[] froms = {0, 0, 1, 1, 2, 2};
        int[] tos = {1, 2, 0, 2, 0, 1};

        boolean fixthis = true;

        while (!pegs[0].empty() || !pegs[1].empty()) {
            displayPegs();
            int imove = ui.getCommand(moves);
            if (imove == -1) return;
            int from = froms[imove];
            int to = tos[imove];
            move(from, to);
        }

        displayPegs();
        ui.sendMessage("You win!");
    }

    String stackToString(StackInterface<Integer> peg) {
        StackInterface<Integer> helper = new ArrayStack<Integer>();

        // String to append items to.
        String s = "";

        // EXERCISE:  append the items in peg to s from bottom to top.
        while (!peg.empty()) {
            helper.push(peg.pop());
        }
        while(!helper.empty()){
            s = s + " " + helper.peek();
            peg.push(helper.pop());
        }

        return s;
    }

    void displayPegs() {
        String s = "";
        for (int i = 0; i < pegs.length; i++) {
            char abc = (char) ('a' + i);
            s = s + abc + stackToString(pegs[i]);
            if (i < pegs.length - 1) s = s + "\n";
        }
        ui.sendMessage(s);
    }

    void move(int from, int to) {

        if (pegs[from].empty()) {
            System.out.println("Cannot move a disk from empty stack.");
            return;
        }

        // Check if the move is legal: peg 'to' is empty or top disk of peg 'from' is smaller than top disk of peg 'to'
        if (!pegs[to].empty() && pegs[from].peek().intValue() >= pegs[to].peek().intValue()) {
            System.out.println("Cannot put disk " + pegs[from].peek() + " on top of disk " + pegs[to].peek());
            return;
        }
        pegs[to].push(pegs[from].pop());

        //check to make sure the bigger is not on top of smaller
        // EXERCISE:  move one disk from pegs[from] to pegs[to].
        // Don't allow illegal moves:  send a warning message instead.
        // For example "Cannot place disk 2 on top of disk 1."
        // Use ui.sendMessage() to send messages.
    }



    public class Goal{
        int fromPeg, toPeg,nMoves;

        Goal(int from, int to, int n){
            fromPeg = from;
            toPeg = to;
            nMoves = n;
        }
        public String toString(){
            char[] peg = {'a', 'b', 'c'};

            if(nMoves>1){
                return("Move " + nMoves + " disks from peg " + peg[fromPeg] + " to peg " + peg[toPeg]);
            }
            else{
                return("Move " + nMoves + " disk from peg " + peg[fromPeg] + " to peg " + peg[toPeg]);
            }
        }
    }// end of Goal class

    void displayGoals(StackInterface<Goal> goals) {
        StackInterface<Goal> helperStack = new ArrayStack<Goal>();
        String message = "";

        while (!goals.empty()) {
            helperStack.push(goals.pop());
        }

        // Pushing back the goals to the original stack
        while (!helperStack.empty()) {
            message = goals.push(helperStack.pop()).toString() + "\n" + message;
        }

        ui.sendMessage(message);
    }

    void solve() {

        StackInterface<Goal> goalStack = new ArrayStack<Goal>();
        boolean firstTime = true;

        // Initial goal: move all disks from peg 0 to peg 2
        Goal initialGoal = new Goal(0, 2, nDisks);
        goalStack.push(initialGoal);

        displayPegs();

        while (!goalStack.empty()) {
            // Display current goal stack
            displayGoals(goalStack);

            Goal currentGoal = goalStack.pop();

            if (currentGoal.nMoves == 1) {
                // Directly move the disk if only one move is needed
                move(currentGoal.fromPeg, currentGoal.toPeg);
                displayPegs();
            } else {
                // Decrement the number of disks by 1 for recursive strategy
                // First step: Move n-1 disks from fromPeg to auxPeg using toPeg
                // Second step: Move nth disk from fromPeg to toPeg
                // Third step: Move n-1 disks from auxPeg to toPeg using fromPeg

                int auxPeg = 3 - currentGoal.fromPeg - currentGoal.toPeg; // Determines the helper peg
                goalStack.push(new Goal(auxPeg, currentGoal.toPeg, currentGoal.nMoves - 1));
                goalStack.push(new Goal(currentGoal.fromPeg, currentGoal.toPeg, 1));
                goalStack.push(new Goal(currentGoal.fromPeg, auxPeg, currentGoal.nMoves - 1));

            }
        }

        ui.sendMessage("Solution complete!");

    }



}//end of Game class
