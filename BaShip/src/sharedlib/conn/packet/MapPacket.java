package sharedlib.conn.packet;

import java.util.HashMap;
import java.util.Map;
import sharedlib.exceptions.PacketException;

public class MapPacket extends Packet {
    
    public Map<String, String> map;

    public MapPacket(String s) throws PacketException {
        map = decodeMap(s);
    }

    public MapPacket() {
        this(new HashMap<>());
    }

    public MapPacket(Map<String, String> m) {
        map = m;
    }

    @Override
    public String getString() throws PacketException {        
        return super.getString() + encodeMap(map);
    }
    
    public static String encodeMap(Map<String, String> map) throws PacketException {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(encodeString(entry.getKey()))
              .append(SEP_M)
              .append(encodeString(entry.getValue()))
              .append(SEP_M);
        }
        
        return sb.toString();
    }
    
    public static Map<String, String> decodeMap(String s) throws PacketException {
        Map<String, String> map = new HashMap<>();
        String[] parts = s.split(SPLIT_M);

        if (parts.length % 2 != 0) {
            throw new PacketException("Invalid number of values in MapPacket, length should be an even number");
        }

        for (int i = 0; i < parts.length; i += 2) {
            map.put(decodeString(parts[i]), decodeString(parts[i + 1]));
        }
        
        return map;
    }
}
