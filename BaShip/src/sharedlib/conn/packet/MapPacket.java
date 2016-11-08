package sharedlib.conn.packet;

import java.util.HashMap;
import java.util.Map;

public class MapPacket extends Packet {

    public Map<String, String> m;

    public MapPacket(String s) throws PacketException {
        String[] parts = s.split(S3R);
        m = new HashMap<>();

        try {
            for (int i = 0; i < parts.length; i += 2) {
                m.put(parts[i], parts[i + 1]);
            }
        }
        catch (Throwable ex) {
            throw new PacketException(ex);
        }
    }

    public MapPacket(Map<String, String> m) {
        this.m = m;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        m.forEach((k, v) -> sb.append(k).append(S3).append(v).append(S3));
        return super.toString() + sb.toString();
    }
}
