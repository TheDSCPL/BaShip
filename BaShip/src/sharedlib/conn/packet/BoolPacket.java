package sharedlib.conn.packet;

public class BoolPacket extends Packet {
    public boolean b;
    
    public BoolPacket(String s) {
        b = s.equals("true");
    }
    
    public BoolPacket(boolean b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return super.toString() + (b ? "true" : "false");
    }
}
