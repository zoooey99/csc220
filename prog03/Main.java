package prog03;

import prog02.UserInterface;
import prog02.GUI;

import java.util.InputMismatchException;

/**
 * @author vjm
 */
public class Main {
    /**
     * Use this variable to store the result of each call to fib.
     */
    public static double fibn;

    /**
     * Determine the time in microseconds it takes to calculate the
     * n'th Fibonacci number.
     *
     * @param fib an object that implements the Fib interface
     * @param n   the index of the Fibonacci number to calculate
     * @return the time for the call to fib(n)
     */
    public static double time(Fib fib, int n) {
        // Get the current time in nanoseconds.
        long start = System.nanoTime();
        // Calculate the n'th Fibonacci number.  Store the
        // result in fibn.

        fibn = fib.fib(n); // fix this

        // Get the current time in nanoseconds.
        long end = System.nanoTime(); // fix this

        // Return the difference between the end time and the
        // start time divided by 1000.0 to convert to microseconds.
        return ( end - start ) / 1000.0; // fix this
    }

    /**
     * Determine the average time in microseconds it takes to calculate
     * the n'th Fibonacci number.
     *
     * @param fib    an object that implements the Fib interface
     * @param n      the index of the Fibonacci number to calculate
     * @param ncalls the number of calls to average over
     * @return the average time per call
     */
    public static double averageTime(Fib fib, int n, int ncalls) {
        double totalTime = 0;

        // Add up the total call time for ncalls calls to time (above).
        for(int i = 0; i < ncalls; i++){
            totalTime += time(fib,n);
        }

        // Return the average time.
        return totalTime / ncalls;
    }

    /**
     * Determine the time in microseconds it takes to to calculate the
     * n'th Fibonacci number.  Average over enough calls for a total
     * time of at least one second.
     *
     * @param fib an object that implements the Fib interface
     * @param n   the index of the Fibonacci number to calculate
     * @return the time it it takes to compute the n'th Fibonacci number
     */
    public static double accurateTime(Fib fib, int n) {
        // Get the time in microseconds for one call.
        double t = time(fib,n); // fix this

        // If the time is (equivalent to) more than a second, return it.
        if (t > 1000000){
            return t;
        }

        // Estimate the number of calls that would add up to one second.
        // Use   (int)(YOUR EXPESSION)   so you can save it into an int variable.
        int numcalls = (int) (1000000.0 / t); // fix this

        System.out.println("numcalls " + numcalls);

        // Get the average time using averageTime above with that many
        // calls and return it.
        return averageTime(fib, n, numcalls); // fix this
    }

    //private static UserInterface ui = new TestUI("Fibonacci experiments");
    private static UserInterface ui = new GUI("Fibonacci experiments");

    /**
     * Get a non-negative integer from the using using ui.
     * If the user enters a negative integer, like -2, say
     * "-2 is negative...invalid"
     * If the user enters a non-integer, like abc, say
     * "abc is not an integer"
     * If the user clicks cancel, return -1.
     *
     * @return the non-negative integer entered by the user or -1 for cancel.
     */
    static int getInteger() {
        String s = ui.getInfo("Enter n");
        int n;

        if (s == null)
            return -1; // user clicked cancel

        try{
            n = Integer.parseInt(s);
            if (n<0){
                ui.sendMessage(n + " is negative...invalid.");
            }
        }
        catch(NumberFormatException ex) {
            ui.sendMessage( s + " is not an integer.");
            return -1;
        }
        return n;
    }

    public static void doExperiments(Fib fib) {
        boolean isFirst = true;
        String fibName = fib.getClass().getName().substring(7);
        System.out.println("doExperiments " + fib);

        while(true) {
            int n1 = getInteger();
            if (n1 < 0) {
                return;
            } else {
                double time1acc;

                if (isFirst) {
                    time1acc = accurateTime(fib, n1);
                    ui.sendMessage(fibName + "(" + n1 + ") = " + fib.fib(n1) + " in " + time1acc + " microseconds. ");
                    fib.saveConstant(n1, time1acc);
                }else{
                    double time1est = fib.estimateTime(n1);
                    ui.sendMessage("Estimated time on input " + n1 + " is " + time1est + " microseconds.");
                    double oneHour = 3.6 * Math.pow(10, 9);

                    if(time1est > oneHour){
                        ui.sendMessage("Estimated wait time is more than an hour. \nI'm going to ask you if you really want to run it.");
                        String[] yesOrNo = {"YES", "NO"};
                        int d = ui.getCommand(yesOrNo);
                        if (d == 1 ){
                            doExperiments();
                        }
                    }
                    time1acc = accurateTime(fib, n1);
                    fib.saveConstant(n1, time1acc);
                    double percentErr = ((time1est - time1acc) / time1acc) * 100.0;
                    ui.sendMessage(fibName + "(" + n1 + ") = " + fib.fib(n1) + " in " + time1acc + " microseconds. " + percentErr + "% error.");
                }
                isFirst = false;
            }
        }
    }

    public static void doExperiments() {
        // Give the user a choice instead, in a loop, with the option to exit.
        String[] commands = {"ExponentialFib", "LinearFib", "LogFib", "ConstantFib", "MysteryFib","EXIT"};

        while(true){
            int choice = ui.getCommand(commands);
            System.out.println("choice" + choice);
            switch(choice){
                case 0: //ExponentialFib
                    doExperiments(new ExponentialFib());
                    break;
                case 1: //LinearFib
                    doExperiments(new LinearFib());
                    break;
                case 2: //LogFib
                    doExperiments(new LogFib());
                    break;
                case 3: //ConstantFib
                    doExperiments(new ConstantFib());
                    break;
                case 4: //MysteryFib
                    doExperiments(new MysteryFib());
                    break;
                case 5: //Exit
                    return;
            }
        }
    }

    static void labExperiments() {
        // Create (Exponential time) Fib object and test it.
        Fib efib = new ConstantFib();
        System.out.println(efib);
        for (int i = 0; i < 11; i++)
            System.out.println(i + " " + efib.fib(i));

        // Determine running time for n1 = 20 and print it out.
        int n1 = 20;
        double time1 = accurateTime(efib, n1);
        System.out.println("n1 " + n1 + " time1 " + time1);

        // Calculate constant:  time = constant times O(n).
        double c = time1 / efib.O(n1);
        System.out.println("c " + c);

        // Estimate running time for n2=30.
        int n2 = 30;
        double time2est = c * efib.O(n2);
        System.out.println("n2 " + n2 + " estimated time " + time2est);

        // Calculate actual running time for n2=30.
        double time2 = accurateTime(efib, n2);
        System.out.println("n2 " + n2 + " actual time " + time2);

        // Estimate how long ExponentialFib.fib(100) would take.
        int n3 = 100;
        double time3est = ( c * efib.O(n3) ) / ( 3.17098 * Math.pow(10, 14) );
        System.out.println("n3 " + n3 + " estimated time " + time3est + " years");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        labExperiments();
        doExperiments();
    }
}
