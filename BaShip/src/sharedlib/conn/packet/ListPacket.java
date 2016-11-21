package sharedlib.conn.packet;

import sharedlib.conn.Packet;
import java.util.ArrayList;
import java.util.List;
import sharedlib.exceptions.PacketException;

public class ListPacket extends Packet {

    public List<String> list;

    public ListPacket(String s) throws PacketException {
        list = decodeList(s);
    }

    public ListPacket(List<String> l) {
        list = l;
    }

    public ListPacket() {
        this(new ArrayList<>());
    }

    @Override
    public String getString() throws PacketException {
        return super.getString() + encodeList(list);
    }
    
    public static String encodeList(List<String> list) throws PacketException {
        StringBuilder sb = new StringBuilder();

        for (String str : list) {
            sb.append(str).append(SEP_L);
        }
        
        return sb.toString();
    }
    
    public static List<String> decodeList(String str) throws PacketException {
        List<String> list = new ArrayList<>();
        
        String[] sl = str.split(SPLIT_L);
        for (String s : sl) {
            list.add(decodeString(s));
        }
        
        return list;
    }
}
