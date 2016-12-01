package pt.up.fe.lpro1613.sharedlib.conn;

import com.google.gson.*;
import java.util.*;
import pt.up.fe.lpro1613.sharedlib.exceptions.PacketException;

/**
 * The unit of information sent between client and server.
 */
public class Packet {

    private static final String SEP_1 = "\u001C";
    private static final String SPLIT_1 = "[\\x1C]";
    private static final String SUB_NL = "\u001F";

    /**
     * Unique ID of this packet.
     */
    String id;

    /**
     * Unique ID of the packet that is the request to which this packet is the
     * response.
     */
    String pid;

    /**
     * Query object that defines what this packet represents and contains in the
     * dialogue between client and server.
     */
    public final Query query;

    /**
     * The information this packet contains.
     */
    public final Object info;

    /**
     * Create a new Packet.
     *
     * @param query The query of this packet
     * @param info The information to be sent. Can be null
     */
    public Packet(Query query, Object info) {
        id = UUID.randomUUID().toString();
        pid = "";
        this.query = query;
        this.info = info;
    }

    /**
     * Create a new Packet with no information.
     *
     * @param query The query of this packet
     */
    public Packet(Query query) {
        this(query, null);
    }

    /**
     * Create a new Packet with an empty query ({@code Query::Empty}) and no information.
     */
    public Packet() {
        this(Query.BEmpty);
    }

    private Packet(String id, String pid, Query query, Object info) {
        this.id = id;
        this.pid = pid;
        this.query = query;
        this.info = info;

        if (query.infoType != null && info != null) {
            if (!query.infoType.getRawType().isInstance(info)) {
                throw new IllegalArgumentException("Packet instantiation failure: class of info object is not compatible with class type defined by query");
            }
        }
    }

    /**
     * Instantiate a {@code Packet} object from its string representation
     * @param string
     * @return 
     * @throws PacketException 
     */
    static Packet fromString(String string) throws PacketException {
        String[] parts = string.split(SPLIT_1);

        String id = decodeString(parts[0]);
        String pid = decodeString(parts[1]);
        
        Query query = Query.valueOf(decodeString(parts[2]));
        if (query == null) {
            throw new PacketException("Invalid query string: " + decodeString(parts[2]));
        }

        Object info;
        if (query.infoType != null) {
            info = getGson().fromJson(decodeString(parts[3]), query.infoType.getType());
        }
        else {
            info = null;
        }

        return new Packet(id, pid, query, info);
    }

    /**
     * @return The string value which represents this packet
     * @throws PacketException
     */
    String getString() throws PacketException {
        String json = info != null ? getGson().toJson(info) : "";
        return encodeString(id) + SEP_1 + encodeString(pid) + SEP_1 + encodeString("" + query) + SEP_1 + encodeString(json);
    }

    private static String encodeString(String s) throws PacketException {
        s = s.replaceAll("\n", SUB_NL);

        if (s.contains(SEP_1)) {
            throw new PacketException("String to be sent in packet contains invalid characters: " + s);
        }

        return s;
    }

    private static String decodeString(String s) {
        s = s.replaceAll(SUB_NL, "\n");
        return s;
    }

    private static Gson getGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    }
}
