package sharedlib.coms;

import java.io.*;
import java.util.*;

public class Package implements Serializable {

    public final UUID id;
    public Map<String, Object> contents = new HashMap<>();

    public Package question;

    public Package() {
        id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Package)) {
            return false;
        }

        return id.equals(((Package) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
