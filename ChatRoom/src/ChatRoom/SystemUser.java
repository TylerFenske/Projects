/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * System user is created only one time. System user gets the first turn,
 * and a turn each time a user types "/system". System user also gets a turn
 * if all users become inactive.
 *
 * System User has the following public methods:
 *
 * createNewUser(String userName) - creates a new user by initializing a new
 * User object, then creates a user tuple and adds it to the tuple space in the
 * following format:
 * [(User)UserReference, (String)UserName, (int)UserID, (boolean)ActiveStatus]
 * logOnUser(String userName) - changes a user's active status to true.
 */

package ChatRoom;
import TupleSpace.*;
import java.util.Arrays;

public class SystemUser {

    private TupleSpace tupleSpace;
    private int userID = 0;

    private final String WILD = "*";
    private final int USER_REF = 0;
    private final int USER_NAME = 1;
    private final int USER_ID = 2;
    private final int ACTIVE_STATUS = 3;


    /**
     * Constructor for SystemUser
     * @param tupleSpace
     */
    public SystemUser(TupleSpace tupleSpace){
        this.tupleSpace = tupleSpace;
    }

    /**
     * Creates a new user as long as the user name isn't already in use.
     * Creates a new tuple with the following format:
     * [(User)UserReference,(String)UserName,(int)UserID,(boolean)ActiveStatus]
     * and adds it to the tuple space.
     *
     * User IDs are kept track of and incremented in the SystemUser class.
     * @param userName
     */
    public boolean createNewUser(String userName){
        if(tupleSpace.read(WILD, userName, WILD, WILD) != null){
            System.out.println("Username already in use, please try again " +
                    "with a different name.\n");
            return false;
        }else{
            User user = new User(userName, userID, tupleSpace);
            tupleSpace.add(new Tuple(Arrays.asList(user, userName,
                    userID, false)));
            userID++;
            System.out.println("\nWelcome new user, " + userName + "!\n");
            return true;
        }
    }

    /**
     * As long as user exists and isn't already "logged in", changes the users
     * active status to true. This is done by removing the tuple found with
     * the passed username, then recreates a copy of it, with the active status
     * field changed to true, then added back to the tuple space.
     * @param userName
     */
    public void logOnUser(String userName){
        if(tupleSpace.read(WILD, userName, WILD, WILD) == null){
            System.out.println("User doesn't exist, please try again.\n");
        }else if((boolean) tupleSpace.read(WILD, userName, WILD, WILD).
                get(ACTIVE_STATUS)){
            System.out.println(userName + " is already logged in.\n");
        }else{
            Tuple tuple = tupleSpace.remove(WILD, userName, WILD, WILD);
            Object[] objects = tuple.convertToObjArr();
            objects[ACTIVE_STATUS] = true;

            tupleSpace.add(new Tuple(Arrays.asList(objects[USER_REF],
                    objects[USER_NAME], objects[USER_ID],
                    objects[ACTIVE_STATUS])));

            System.out.println(userName + " has come online.\n");
        }
    }
}
