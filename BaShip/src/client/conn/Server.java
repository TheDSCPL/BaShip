package client.conn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import sharedlib.conn.Connection;
import sharedlib.conn.ConnectionException;
import sharedlib.conn.packet.ListMapPacket;
import sharedlib.conn.packet.ListPacket;
import sharedlib.conn.packet.MapPacket;
import sharedlib.conn.packet.Packet;
import sharedlib.conn.packet.QueryPacket;
import sharedlib.conn.packet.StringPacket;

public class Server implements Connection.Delegate {

    private final Connection conn;

    public Server(Connection conn) {
        this.conn = conn;
        this.conn.delegate = this;
    }

    @Override
    public Packet handle(Packet packet) {
        return null;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected to server on " + connection.address());
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Disconnected from server on " + connection.address());
        
    }

    public Delegate delegate;

    public interface Delegate {

        public void receiveGameMessage();

        public void receiveGlobalMessage();
        // ...
    }

    public void doLogin() {

    }

    public boolean getUsernameAvailable(String username) throws ConnectionException {
        //QueryPacket request = new QueryPacket("UsernameAvailable");
        //request.m.put("username", username);

        //BoolPacket response = (BoolPacket) conn.sendAndReceive(request);
        //return response.b;
        return false;
    }

    public String stringTest() throws ConnectionException {
        StringPacket request = new StringPacket("REQUEST");
        return ((StringPacket) conn.sendAndReceive(request)).s;
    }

    public List<String> listTest() throws ConnectionException {
        ListPacket request = new ListPacket(Arrays.asList("list1", "list2", "list3"));
        return ((ListPacket) conn.sendAndReceive(request)).l;
    }

    public void listMapTest() throws ConnectionException {
        List<Map<String, String>> l = new ArrayList<>();

        Map<String, String> m1 = new HashMap();
        m1.put("A", "a");
        m1.put("B", "b");
        m1.put("C", "c");

        Map<String, String> m2 = new HashMap();
        m2.put("1", "one");
        m2.put("2", "two");
        m2.put("3", "three");

        l.add(m1);
        l.add(m2);

        String s = String
                .join("XXX", l
                      .stream()
                      .map((m) -> {
                          StringBuilder sb = new StringBuilder();
                          m.forEach((k, v) -> sb.append(k).append("YYY").append(v).append("YYY"));
                          return sb.toString();
                      })
                      .collect(Collectors.toList()));

        conn.sendOnly(new ListMapPacket(l));
    }

    public void queryTest() throws ConnectionException {
        Map<String, String> m1 = new HashMap();
        m1.put("A", "a");
        m1.put("B", "b");
        m1.put("C", "c");

        conn.sendOnly(new QueryPacket("QUERY", m1));
    }

    public void mapTest() throws ConnectionException {
        Map<String, String> m1 = new HashMap();
        m1.put("A", "a");
        m1.put("B", "b");
        m1.put("C", "c");

        conn.sendOnly(new MapPacket(m1));
    }
}
