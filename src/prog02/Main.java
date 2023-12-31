package prog02;

/**
 * A program to query and modify the phone directory stored in csc220.txt.
 *
 * @author vjm
 */
public class Main {

    /**
     * Processes user's commands on a phone directory.
     *
     * @param fn The file containing the phone directory.
     * @param ui The UserInterface object to use
     *           to talk to the user.
     * @param pd The PhoneDirectory object to use
     *           to process the phone directory.
     */
    public static void processCommands(String fn, UserInterface ui, PhoneDirectory pd) {
        pd.loadData(fn);
        boolean changed = false;

        String[] commands = {"Add/Change Entry", "Look Up Entry", "Remove Entry", "Save Directory", "Exit"};
        String[] yesOrNo = {"YES", "NO"};

        String name, number, oldNumber;

        while (true) {
            int c = ui.getCommand(commands);
            switch (c) {
                case -1:
                    ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
                    break;
                case 0: // Add or Change Entry
                    name = ui.getInfo("Enter the name ");
                    if (name == null || name.length() == 0){
                        break;
                    }
                    changed = true;
                    number = ui.getInfo("Enter number ");
                    if (number == null){
                        break;
                    }
                    oldNumber = pd.addOrChangeEntry(name, number);

                    if (oldNumber == null){
                        ui.sendMessage("Number for " + name + " was added.\nNew number: " +number);
                    }else {
                        ui.sendMessage("Number for " + name + " was changed. \nOld number: " + oldNumber + "\nNew number: " + number);
                    }
                    pd.addOrChangeEntry(name, number);
                    break;

                case 1: // Look Up Entry
                    name = ui.getInfo("Enter the name ");
                    if (name == null || name.length() == 0){
                        break;
                    }
                    number = pd.lookupEntry(name);

                    if (number == null){
                        ui.sendMessage(name + " is not listed in the directory.");
                    } else {
                        ui.sendMessage(name + " has number " + number);
                    }
                    break;

                case 2: // Remove Entry
                    name = ui.getInfo("Enter the name ");
                    if (name == null || name.length() == 0){
                        break;
                    }

                    number = pd.removeEntry(name);
                    if (number == null){
                        ui.sendMessage(name + " is not listed in the directory.");
                    } else {
                        ui.sendMessage("Removed entry name " + name + " and number " + number);
                        changed = true;
                    }
                    break;
                case 3: // Save Directory
                    pd.save();
                    changed = false;
                    break;
                case 4: // Exit
                    if (!changed){
                        return;
                    } else{
                        ui.sendMessage("The directory has changed. I am going to ask you if you want to exit without saving");

                        int d = ui.getCommand(yesOrNo);
                        if (d == 0 ){
                            return;
                        }else{
                            break;
                        }

                    }

            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fn = "csc220.txt";
        PhoneDirectory pd = new SortedPD();
        UserInterface ui = new GUI("Phone Directory");
        processCommands(fn, ui, pd);
    }
}
