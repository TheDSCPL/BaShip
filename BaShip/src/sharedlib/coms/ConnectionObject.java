package sharedlib.coms;

import java.io.*;
import java.util.*;

public class ConnectionObject implements Serializable {

    public final String command;
    public Map<String, Object> contents = new HashMap<>();

    public ConnectionObject(String command) {
        this.command = command;
    }

    public final String serialize() throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(/*new GZIPOutputStream(*/arrayOutputStream);//);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();

        return Base64.getEncoder().encodeToString(arrayOutputStream.toByteArray());
    }

    public static final ConnectionObject deserialize(String string) throws IOException, ClassNotFoundException {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(string));
        //GZIPInputStream gzipInputStream = new GZIPInputStream(arrayInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);
        
        Object object = objectInputStream.readObject();
        if (object instanceof ConnectionObject) {
            return (ConnectionObject) objectInputStream.readObject();
        }

        throw new ClassNotFoundException();
    }
}
