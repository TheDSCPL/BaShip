package sharedlib.coms.packet;

/**
 *
 * @author Alex
 */
public class ErrorPacket extends Packet {
    public String error;
    public ErrorPacket(String error) { this.error = error; }
}