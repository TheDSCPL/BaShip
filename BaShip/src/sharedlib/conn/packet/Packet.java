package sharedlib.conn.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public abstract class Packet {

    protected static final String S1 = "\u001C";
    protected static final String S1R = "[\\x1C]";
    
    protected static final String S2 = "\u001D";
    protected static final String S2R = "[\\x1D]";
    
    protected static final String S3 = "\u001E";
    protected static final String S3R = "[\\x1E]";
    
    //protected static final String S4 = "\u001F";
    //protected static final String S4R = "[\\x1F]";

    /**
     * Unique ID of this packet
     */
    public String id;

    /**
     * Unique ID of the packet that is the request to which this packet is the
     * response
     */
    public String pid;

    protected Packet() {
        id = UUID.randomUUID().toString();
        pid = null;
    }
    
    public static Packet fromString(String string) throws PacketException {
        String[] parts = string.split(S1R);
        
        if (parts.length != 4) {
            throw new PacketException("Invalid packet header size (" + parts.length + "), header: " + string);
        }

        Packet p = null;

        try {
            p = (Packet) Class.forName(parts[0]).getConstructor(String.class).newInstance(parts[3]);
            p.id = parts[1];
            p.pid = parts[2];
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new PacketException("Could not find Packet class", ex);
        }

        return p;
    }

    public String toString() {
        return this.getClass().getName() + S1 + id + S1 + pid + S1;
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

}
