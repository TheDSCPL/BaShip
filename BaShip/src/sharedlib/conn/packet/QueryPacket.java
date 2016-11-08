package sharedlib.conn.packet;

import java.util.HashMap;
import java.util.Map;

public class QueryPacket extends Packet {

    public String query;
    public Map<String, String> info;

    public QueryPacket(String s) throws PacketException {

        String[] parts1 = s.split(S2R);
        if (parts1.length != 2) {
            throw new PacketException("Invalid query packet size " + parts1.length);
        }
        
        query = parts1[0];
        info = new HashMap<>();

        String[] parts2 = parts1[1].split(S3R);
        try {
            for (int i = 0; i < parts2.length; i += 2) {
                info.put(parts2[i], parts2[i + 1]);
            }
        }
        catch (Throwable ex) {
            throw new PacketException(ex);
        }
    }

    public QueryPacket(String query, Map<String, String> info) {
        this.info = info;
        this.query = query;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        info.forEach((k, v) -> sb.append(k).append(S3).append(v).append(S3));
        return super.toString() + query + S2 + sb.toString();
    }
}
