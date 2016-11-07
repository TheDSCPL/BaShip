package sharedlib.conn.packet;

/**
 *
 * @author Alex
 */
public class BoolPacket extends Packet {
    public Boolean b;
    public BoolPacket(Boolean b) { this.b = b; }
}
