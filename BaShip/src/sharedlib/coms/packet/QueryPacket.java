package sharedlib.coms.packet;

public class QueryPacket extends MapPacket {
    public String query;
    public QueryPacket(String query) { this.query = query; }
}
