package sharedlib.conn.packet;

public class IntPacket extends Packet {
    public int i;
    
    public IntPacket(String s) {
        i = new Integer(s);
    }
    
    public IntPacket(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return super.toString() + i;
    }
}