package sharedlib.coms;

import java.io.*;
import java.util.*;

public class ConnectionObject implements Serializable {

    public final String command;
    public Map<String, Object> contents = new HashMap<>();

    public ConnectionObject(String command) {
        this.command = command;
    }
}
