package sharedlib.structs;

import java.util.Date;

/**
 * A class representing a message sent by a user at a certain time with a
 * certain text.
 *
 * @author Alex
 */
public class Message {

    /**
     * ID of the message
     */
    public final Long id;

    /**
     * ID of the user who sent the message
     */
    public final Long userID;

    /**
     * Username of the user who sent the message
     */
    public final String username;

    /**
     * Info abou time and date of the message
     */
    public final Date timestamp;

    /**
     * Message text
     */
    public final String text;

    /**
     * The constructor of the Message class
     * @param id ID of message
     * @param userID ID of user that sends the message
     * @param username Username of usar that sends the message
     * @param timestamp Date and Time of the message
     * @param text Text of the message
     */
    public Message(Long id, Long userID, String username, Date timestamp, String text) {
        this.id = id;
        this.userID = userID;
        this.username = username;
        this.timestamp = timestamp;
        this.text = text;
    }

}
