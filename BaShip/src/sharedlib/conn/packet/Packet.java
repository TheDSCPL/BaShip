package sharedlib.conn.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import sharedlib.conn.Query;
import static sharedlib.conn.packet.Packet.decodeString;
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
    public String id;

    /**
     * Unique ID of the packet that is the request to which this packet is the
     * response
     */
    public String pid;

    /**
     * Contains a str string or a str that identifies this packet functionally
     */
    public Query query;

    public Packet() {
        id = UUID.randomUUID().toString();
        pid = "";
        query = Query.Empty;
    }

    public static Packet fromString(String string) throws PacketException {
        String[] parts = string.split(SPLIT_1);

        Packet p = null;
        String packetType = decodeString(parts[0]);

        if (packetType.equals(Packet.class.getName())) {
            if (parts.length != 4) {
                throw new PacketException("Invalid packet header size (" + parts.length + "), header: " + string);
            }
            
            p = new Packet();
        }
        else {
            if (parts.length != 5) {
                throw new PacketException("Invalid packet header size (" + parts.length + "), header: " + string);
            }
            
            try {
                p = (Packet) Class.forName(decodeString(parts[0])).getConstructor(String.class).newInstance(parts[4]);
            }
            catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new PacketException("Could not find instantiate correct Packet subclass", ex);
            }
        }

        p.id = decodeString(parts[1]);
        p.pid = decodeString(parts[2]);
        p.query = Query.fromString(decodeString(parts[3]));

        if (p.query == null) {
            throw new PacketException("Invalid query string: " + decodeString(parts[3]));
        }

        return p;
    }

    public String getString() throws PacketException {
        return this.getClass().getName() + SEP_1 + encodeString(id) + SEP_1 + encodeString(pid) + SEP_1 + encodeString(query.str) + SEP_1;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Packet)) {
            return false;
        }
        return ((Packet) obj).id.equals(id);
    }

    protected static String encodeString(String s) throws PacketException {
        s = s.replaceAll("\n", SUB_NL);

        if (s.contains(SEP_1) || s.contains(SEP_L) || s.contains(SEP_M)) {
            throw new PacketException("String to be sent in packet contains invalid characters: " + s);
        }

        return s;
    }

    protected static String decodeString(String s) {
        s = s.replaceAll(SUB_NL, "\n");
        return s;
    }
}
