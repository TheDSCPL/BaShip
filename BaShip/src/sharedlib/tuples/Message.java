package sharedlib.tuples;

import java.util.Date;

/**
 * A class representing a message sent by a user at a certain time with a
 * certain text.
 *
 * @author Alex
 */
public class Message {

    public final Long id;
    public final Long userID;
    public final String username;
    public final Date timestamp;
    public final String text;

    public Message(Long id, Long userID, String username, Date timestamp, String text) {
        this.id = id;
        this.userID = userID;
        this.username = username;
        this.timestamp = timestamp;
        this.text = text;
    }

}
