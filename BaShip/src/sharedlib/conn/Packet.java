package sharedlib.conn;

import com.google.gson.Gson;
import java.util.UUID;
import sharedlib.exceptions.PacketException;

/**
 * The unit of information sent between client and server
 */
public class Packet {

    private static final String SEP_1 = "\u001C";
    private static final String SPLIT_1 = "[\\x1C]";
    private static final String SUB_NL = "\u001F";

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
     * Query object that defines what this packet represents and contains in the
     * dialogue between client and server
     */
    public final Query query;

    /**
     * The information this packet contains
     */
    public final Object info;

    /**
     * Create a new Packet
     *
     * @param query The query of this packet
     * @param info The information to be sent
     */
    public Packet(Query query, Object info) {
        id = UUID.randomUUID().toString();
        pid = "";
        this.query = query;
        this.info = info;
    }

    /**
     * Create a new Packet with no information
     *
     * @param query The query of this packet
     */
    public Packet(Query query) {
        this(query, null);
    }

    /**
     * Create a new Packet with an empty query and no information
     */
    public Packet() {
        this(Query.BEmpty);
    }

    private Packet(String id, String pid, Query query, Object info) {
        this.id = id;
        this.pid = pid;
        this.query = query;
        this.info = info;
        
        // TODO: verify if query.infoType matches info.class
    }

    static Packet fromString(String string) throws PacketException {
        String[] parts = string.split(SPLIT_1);

        String id = decodeString(parts[0]);
        String pid = decodeString(parts[1]);
        Query query = Query.fromString(decodeString(parts[2]));
        Object info;

        if (query == null) {
            throw new PacketException("Invalid query string: " + decodeString(parts[2]));
        }

        if (query.infoType != null) {
            try {
                /*//java.util.ArrayList
            Class<?> rawClass = Class.forName(decodeString(parts[3]));
            
            //Type listGenericType = Class.forName(decodeString(parts[4])); //TODO: trocar a linha de baixo por esta quando se adicionar este valor à string de Json
            Type listGenericType = Class.forName("sharedlib.tuples.UserInfo");
            
            Type[] genericTypes = null;
            if(rawClass == ArrayList.class)
                genericTypes = new Type[]{listGenericType};
            
            Type type = MyParameterizedType.factory(rawClass, genericTypes, null);
            
            //info = new Gson().fromJson(decodeString(parts[5]), type); //TODO: trocar a linha de baixo por esta quando se adicionar este valor à string de Json
            info = new Gson().fromJson(decodeString(parts[4]), type);
            //info = new Gson().fromJson(decodeString(parts[4]), new TypeToken<ArrayList<UserInfo>>(){}.getType()); //Deprecated. This way only lets you have a hardcoded generic type
            
                 *//*else
                info = new Gson().fromJson(decodeString(parts[4]), rawClass);*/

                info = new Gson().fromJson(decodeString(parts[3]), /*Class.forName(decodeString(parts[3]))*/ query.infoType.getType());
            }
            catch (SecurityException | IllegalArgumentException ex) {
                throw new PacketException("Could not find info class", ex);
            }
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
        String json = info != null ? new Gson().toJson(info) : "";
        return encodeString(id) + SEP_1 + encodeString(pid) + SEP_1 + encodeString("" + query) + SEP_1 + /*encodeString(info.getClass().getName()) + SEP_1 +*/ encodeString(json);
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
}
