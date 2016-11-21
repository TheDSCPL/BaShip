package sharedlib.conn.packet;

import sharedlib.conn.Packet;
import sharedlib.exceptions.PacketException;

public class BoolPacket extends Packet {
    public boolean b;
    
    public BoolPacket(String s) {
        b = s.equals("true");
    }
    
    public BoolPacket(boolean b) {
        this.b = b;
    }

    @Override
    public String getString() throws PacketException {
        return super.getString() + (b ? "true" : "false");
    }
}
