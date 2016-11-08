package sharedlib.conn.packet;

public class StringPacket extends Packet {
    public String s;
    
    public StringPacket(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return super.toString() + s;
    }
}
