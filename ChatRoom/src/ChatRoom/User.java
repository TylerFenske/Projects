/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Users are initialized with a username, user ID, and passed a reference of the
 * tuple space. Users can call the methods:
 *
 * logoff() - sets their Tuple container's active status to false.
 * printActiveUsers() - prints all users with active status to console.
 * printAllUsers() - prints all users to console.
 * sendMessage() - creates a tuple (and adds it to the tuple space) for a
 * message in the following format:
 * [(String)UserName, (String)TimeStamp, (String)Message, (int)MessageID]
 * printMessageLog() - prints the most recent 10 messages sent by all users
 * in chronological order.
 */

package ChatRoom;
import TupleSpace.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class User {

    private String userName;
    private int userID;
    private TupleSpace tupleSpace;
    private static int messageID = 0;

    private final String WILD = "*";
    private final int USER_REF = 0;
    private final int USER_NAME = 1;
    private final int USER_ID = 2;
    private final int ACTIVE_STATUS = 3;

    private final int MSG_USER_NAME = 0;
    private final int MSG_TIME_STAMP = 1;
    private final int MSG_MESSAGE = 2;
    private final int MSG_MESSAGE_ID = 3;

    /**
     * Constructor for User
     * @param userName
     * @param userID
     * @param tupleSpace
     */
    public User(String userName, int userID, TupleSpace tupleSpace){
        this.userName = userName;
        this.userID = userID;
        this.tupleSpace = tupleSpace;
    }

    /**
     * Removes user tuple with "true" active status, and adds a new
     * tuple with all the same information (with "false" active status instead.)
     */
    public void logOff(){
        tupleSpace.remove(this, userName, userID, WILD);

        tupleSpace.add(new Tuple(Arrays.asList(this, userName, userID, false)));

        System.out.println(userName + " has gone offline.\n");
    }

    /**
     * Loops over all user IDs until it finds a null tuple. Each user ID it
     * finds with an active status = true, prints the users name to console.
     */
    public void printActiveUsers(){
        int userID = 0;
        Tuple user = tupleSpace.read(WILD, WILD, userID, WILD);

        System.out.println("ACTIVE USERS:");
        while(user != null){
            if((boolean) user.get(ACTIVE_STATUS)){
                System.out.println(user.get(USER_NAME));
            }
            userID++;
            user = tupleSpace.read(WILD, WILD, userID, WILD);
        }
        System.out.println();
    }

    /**
     * Loops over all user IDs until it finds a null tuple. Each user ID it
     * finds, prints the users name to console.
     */
    public void printAllUsers(){
        int userID = 0;
        Tuple user = tupleSpace.read(WILD, WILD, userID, WILD);

        System.out.println("ALL USERS:");
        while(user != null){
            System.out.println(user.get(USER_NAME));
            userID++;
            user = tupleSpace.read(WILD, WILD, userID, WILD);
        }
        System.out.println();
    }

    /**
     * Creates a new 4-tuple with the following format:
     * [(String)UserName, (String)TimeStamp, (String)Message, (int)MessageID]
     * then adds it to the tuple space.
     *
     * Message ID is static and incremented each time a new message is created.
     * @param message
     */
    public void sendMessage(String message){
        String timeStamp = new SimpleDateFormat("HH:mm").
                format(Calendar.getInstance().getTime());

        Tuple tuple = new Tuple(Arrays.asList(userName, timeStamp,
                message, messageID));

        tupleSpace.add(tuple);

        messageID++;
    }

    /**
     * Prints the 10 most recently added messages to the console in the
     * following format:
     *
     * TimeStamp UserName: Message
     */
    public void printMessageLog(){
        int messageID = this.messageID;
        int count = 10;

        if(messageID > 10){
            messageID -= 10;
        }else{
            messageID = 0;
        }

        Tuple message = tupleSpace.read(WILD, WILD, WILD, messageID);

        System.out.println("\n******************MESSAGE LOG******************");
        while(message != null && count > 0){
            System.out.println(message.get(MSG_TIME_STAMP) + " " +
                    message.get(MSG_USER_NAME) + ": " +
                    message.get(MSG_MESSAGE));

            messageID++;
            count--;
            message = tupleSpace.read(WILD, WILD, WILD, messageID);
        }
        System.out.println("***********************************************\n");
    }
}
