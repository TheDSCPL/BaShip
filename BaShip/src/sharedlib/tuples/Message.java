package sharedlib.tuples;

import java.util.Date;

public class Message {

    public final Long id;
    public final Long userID;
    public final Date timestamp;
    public final String text;

    public Message(Long id, Long userID, Date timestamp, String text) {
        this.id = id;
        this.userID = userID;
        this.timestamp = timestamp;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", userID=" + userID + ", timestamp=" + timestamp + ", text=" + text + '}';
    }
    
}
