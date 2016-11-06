package sharedlib.coms.packet;

import java.io.*;
import java.util.*;

public class Packet implements Serializable {

    /**
     * Unique ID of this packet
     */
    public final UUID id;
    
    /**
     * The Packet object this packet is a response to
     */
    public Packet request;

    public Packet() {
        id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Packet)) {
            return false;
        }

        return id.equals(((Packet) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
