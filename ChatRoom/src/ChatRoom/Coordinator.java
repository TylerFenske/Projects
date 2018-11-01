/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Coordinator for chat room program. Creates a tuple space and systemUser
 * (which creates users on command). The program starts by giving the
 * systemUser a turn. While it's the systemUser's turn, commands can be entered
 * in the command line as follows:
 *
 * "/create"	- Create new user (and log them in).
 * "/logon"	    - Log user onto the chat room.
 * "/pass"		- Pass control to first user (or return control to user that
 *                called "/system").
 * "/kill"		- Kill chat room program.
 *
 * When /pass is called, the program will then pass control to the first active
 * user, which can execute the following commands:
 *
 * "/printactive"	- Print all active users to console.
 * "/printall"		- Print all users to console.
 * "/logoff"		- Log off this user.
 * "/send"			- Send message to all users.
 * "/pass"			- Pass control to next user.
 * "/system"		- Pass control to System.
 *
 * A user may printactive, printall, or call system as many times as they like
 * during their turn. Once they send a message, log off, or pass, their turn is
 * over and will pass control to the next user (or back to themselves if there
 * is only one active user).
 *
 * When the user's turn is ended, the chat room's message log will be printed
 * to the console.
 *
 * This cycle continues until systemUser "/kill" command is executed.
 *
 * If all users become inactive at any point, systemUser will re-gain control.
 */

package ChatRoom;
import TupleSpace.*;
import java.util.Scanner;

public class Coordinator {

    private static final int MAX_TUPLE_SIZE = 4;
    private static final String CREATE_USER = "/create";
    private static final String LOG_ON= "/logon";
    private static final String LOG_OFF = "/logoff";
    private static final String PASS = "/pass";
    private static final String PRINT_ACTIVE = "/printactive";
    private static final String PRINT_ALL = "/printall";
    private static final String HELP = "/help";
    private static final String KILL = "/kill";
    private static final String WILD = "*";
    private static final String SEND = "/send";
    private static final String SYSTEM = "/system";
    private static final int USER_REF = 0;
    private static final int USER_NAME = 1;
    private static final int USER_ID = 2;
    private static final int ACTIVE_STATUS = 3;

    private static boolean kill = false;


    /**
     * Main method for chat room program. No command line args are used.
     * @param args
     */
    public static void main(String[] args){
        TupleSpace tupleSpace = new TrieTupleSpace(MAX_TUPLE_SIZE);
        SystemUser systemUser = new SystemUser(tupleSpace);


        while(!kill){

            int userID = 0;
            Tuple user = tupleSpace.read(WILD, WILD, userID, WILD);
            Tuple activeUsers = tupleSpace.read(WILD, WILD, WILD, true);

            while((user == null || activeUsers == null) && !kill){
                systemControl(systemUser);
                user = tupleSpace.read(WILD, WILD, userID, WILD);
                activeUsers = tupleSpace.read(WILD, WILD, WILD, true);
            }

            while(user != null && !kill){
                if((boolean) user.get(ACTIVE_STATUS)){
                    userControl(user, systemUser);
                    userID++;
                    user = tupleSpace.read(WILD, WILD, userID, WILD);
                    //systemControl(systemUser);
                }else{
                    userID++;
                    user = tupleSpace.read(WILD, WILD, userID, WILD);
                }
            }
        }
    }



    private static void systemControl(SystemUser sys){
        boolean pass = false;
        Scanner scanner = new Scanner(System.in);
        String input;

        while(!pass){
            System.out.println("*****SYSTEM MESSAGE: Please enter a system " +
                    "command.*****");
            input = scanner.nextLine().toLowerCase().trim();

            switch(input){
                case CREATE_USER:
                    System.out.println("Please enter the username you'd like" +
                            " to create.");
                    String userName = scanner.nextLine().trim();
                    if(sys.createNewUser(userName)){
                        sys.logOnUser(userName);
                    }
                    break;
                case LOG_ON:
                    System.out.println("Please enter the username you'd like" +
                            " to log in.");
                    sys.logOnUser(scanner.nextLine().trim());
                    break;
                case PASS:
                    pass = true;
                    System.out.println();
                    break;
                case KILL:
                    kill = true;
                    pass = true;
                    break;
                case HELP:
                    System.out.println("VALID SYSTEM COMMANDS:\n" +
                            "\"/create\"\t- Create new user " +
                            "(and log them in).\n" +
                            "\"/logon\"\t- Log user onto the chat room.\n" +
                            "\"/pass\"\t\t- Pass control to first user " +
                            "(or return control to user that called " +
                            "\"/system\").\n" +
                            "\"/kill\"\t\t- Kill chat room program.\n");
                    break;
                default:
                    System.out.println("Not a valid system command. Please " +
                            "try again. Type \"/help\" for a list of valid " +
                            "commands.\n");
            }
        }
    }


    private static void userControl(Tuple userTup, SystemUser sys){
        User user = (User) userTup.get(USER_REF);
        boolean turnMade = false;
        Scanner scanner = new Scanner(System.in);
        String input;

        while(!turnMade && !kill){
            System.out.print(userTup.get(USER_NAME) + ": ");
            input = scanner.nextLine().toLowerCase().trim();

            switch(input){
                case PRINT_ACTIVE:
                    user.printActiveUsers();
                    break;
                case PRINT_ALL:
                    user.printAllUsers();
                    break;
                case LOG_OFF:
                    user.logOff();
                    turnMade = true;
                    break;
                case SEND:
                    System.out.println("Please enter message you would like to"+
                            " send, " + userTup.get(USER_NAME) + ".");
                    user.sendMessage(scanner.nextLine().trim());
                    turnMade = true;
                    break;
                case SYSTEM:
                    systemControl(sys);
                    break;
                case PASS:
                    turnMade = true;
                    break;
                case HELP:
                    System.out.println("VALID USER COMMANDS:\n" +
                            "\"/printactive\"\t\t- Print all active users to " +
                            "console.\n" +
                            "\"/printall\"\t\t- Print all users to console.\n" +
                            "\"/logoff\"\t\t- Log off this user.\n" +
                            "\"/send\"\t\t\t- Send message to all users.\n" +
                            "\"/pass\"\t\t\t- Pass control to next user.\n" +
                            "\"/system\"\t\t- Pass control to System.\n");
                    break;
                default:
                    System.out.println("Not a valid user command. Please " +
                            "try again. Type \"/help\" for a list of valid " +
                            "commands.\n");

            }
        }
        user.printMessageLog();
    }
}
