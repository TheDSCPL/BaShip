package sharedlib.conn.packet;

import sharedlib.conn.Packet;
import sharedlib.exceptions.PacketException;

public class IntPacket extends Packet {
    public int i;
    
    public IntPacket(String s) {
        i = new Integer(s);
    }
    
    public IntPacket(int i) {
        this.i = i;
    }

    @Override
    public String getString() throws PacketException {
        return super.getString() + i;
    }
}