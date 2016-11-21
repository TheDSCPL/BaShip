package sharedlib.conn;

import com.google.gson.Gson;
import java.util.UUID;
import sharedlib.exceptions.PacketException;

public class Packet {

    protected static final String SEP_1 = "\u001C";
    protected static final String SPLIT_1 = "[\\x1C]";
    protected static final String SEP_L = "\u001D";
    protected static final String SPLIT_L = "[\\x1D]";
    protected static final String SEP_M = "\u001E";
    protected static final String SPLIT_M = "[\\x1E]";
    protected static final String SUB_NL = "\u001F";

    /**
     * Unique ID of this packet
     */
    String id;

    /**
     * Unique ID of the packet that is the request to which this packet is the
     * response
     */
    String pid;

    /**
     * Contains a str string or a str that identifies this packet functionally
     */
    public final Query query;

    /**
     * The object to be sent
     */
    public final Object info;

    public Packet(Query query, Object info) {
        id = UUID.randomUUID().toString();
        pid = "";
        this.query = query;
        this.info = info;
    }
    
    public Packet(Object info) {
        this(Query.Empty, info);
    }
    
    public Packet(Query query) {
        this(query, "");
    }
    
    public Packet() {
        this(Query.Empty);
    }

    private Packet(String id, String pid, Query query, Object info) {
        this.id = id;
        this.pid = pid;
        this.query = query;
        this.info = info;
    }

    static Packet fromString(String string) throws PacketException {
        String[] parts = string.split(SPLIT_1);
        
        String id = decodeString(parts[0]);
        String pid = decodeString(parts[1]);
        Query query = Query.fromString(decodeString(parts[2]));
        Object info;
        
        try {
            info = new Gson().fromJson(decodeString(parts[4]), Class.forName(decodeString(parts[3])));
        }
        catch (ClassNotFoundException | SecurityException | IllegalArgumentException ex) {
            throw new PacketException("Could not find info class", ex);
        }
        
        if (query == null) {
            throw new PacketException("Invalid query string: " + decodeString(parts[2]));
        }

        return new Packet(id, pid, query, info);
    }

    String getString() throws PacketException {
        String json = new Gson().toJson(info);
        return encodeString(id) + SEP_1 + encodeString(pid) + SEP_1 + encodeString("" + query) + SEP_1 + encodeString(info.getClass().getName()) + SEP_1 + encodeString(json);
    }

    private static String encodeString(String s) throws PacketException {
        s = s.replaceAll("\n", SUB_NL);

        if (s.contains(SEP_1) || s.contains(SEP_L) || s.contains(SEP_M)) {
            throw new PacketException("String to be sent in packet contains invalid characters: " + s);
        }

        return s;
    }

    private static String decodeString(String s) {
        s = s.replaceAll(SUB_NL, "\n");
        return s;
    }
}
