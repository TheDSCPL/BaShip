package sharedlib.conn.packet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListMapPacket extends Packet {

    public List<Map<String, String>> lm;

    public ListMapPacket(String s) throws PacketException {
        try {
            lm = Arrays.asList(s.split(S2R))
                .stream()
                .map((ms) -> {
                    Map<String, String> m = new HashMap<>();
                    
                    String[] parts = ms.split(S3R);
                    for(int i = 0; i < parts.length; i += 2) {
                        m.put(parts[i], parts[i + 1]);
                    }
                    
                    return m;
                })
                .collect(Collectors.toList());
        }
        catch (Throwable ex) {
            throw new PacketException(ex);
        }
    }

    public ListMapPacket(List<Map<String, String>> lm) {
        this.lm = lm;
    }

    @Override
    public String toString() {
        return super.toString()
               + String
                .join(S2, lm
                      .stream()
                      .map((m) -> {
                          StringBuilder sb = new StringBuilder();
                          m.forEach((k, v) -> sb.append(k).append(S3).append(v).append(S3));
                          return sb.toString();
                      })
                      .collect(Collectors.toList()));
    }

}
