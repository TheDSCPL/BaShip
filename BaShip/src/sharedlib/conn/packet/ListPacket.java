package sharedlib.conn.packet;

import java.util.Arrays;
import java.util.List;

public class ListPacket extends Packet {
    public List<String> l;
    
    public ListPacket(String s) {
        l = Arrays.asList(s.split(S2R));
    }
    
    public ListPacket(List<String> l) {
        this.l = l;
    }

    @Override
    public String toString() {
        return super.toString() + String.join(S2, l);
    }
}
