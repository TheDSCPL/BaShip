package sharedlib.config;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Configuration {

    private final Properties general = new Properties();

    public Configuration(InputStream stream) {
        try {
            general.load(stream);
        }
        catch (Throwable ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not access configuration file -> exiting", ex);
            System.exit(-1);
        }
    }

    /*public Configuration(URL propertiesFile) {
        try {
            this(new FileInputStream(new File(propertiesFile.toURI())));
        }
        catch (Throwable ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not access configuration file -> exiting", ex);
            System.exit(-1);
        }
    }*/

    public String getS(String key) {
        return general.getProperty(key);
    }

    public int getI(String key) {
        return Integer.parseInt(general.getProperty(key));
    }
}
