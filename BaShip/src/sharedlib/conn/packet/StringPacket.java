package sharedlib.conn.packet;

import sharedlib.conn.Packet;
import sharedlib.exceptions.PacketException;

public class StringPacket extends Packet {
    public String str;
    
    public StringPacket(String s) {
        str = decodeString(s);
    }

    @Override
    public String getString() throws PacketException {
        return super.getString() + encodeString(str);
    }
}
