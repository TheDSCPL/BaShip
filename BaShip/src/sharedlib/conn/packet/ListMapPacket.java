package sharedlib.conn.packet;

import sharedlib.conn.Packet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sharedlib.exceptions.PacketException;

public class ListMapPacket extends Packet {

    public List<Map<String, String>> listmap;

    public ListMapPacket(String str) throws PacketException {

        List<String> list = ListPacket.decodeList(str);
        listmap = new ArrayList<>();

        for (String s : list) {
            listmap.add(MapPacket.decodeMap(s));
        }
    }

    public ListMapPacket(List<Map<String, String>> lm) {
        this.listmap = lm;
    }

    public ListMapPacket() {
        this(new ArrayList<>());
    }

    @Override
    public String getString() throws PacketException {
        List<String> list = new ArrayList<>();

        for (Map<String, String> map : listmap) {
            list.add(MapPacket.encodeMap(map));
        }

        return super.getString() + ListPacket.encodeList(list);
    }

}
