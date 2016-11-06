package sharedlib.config;

import java.io.*;
import java.util.*;

/**
 *
 * @author Alex
 */
public class Configuration {
    private final Properties general = new Properties();
    
    public Configuration(String propertiesFile) {
        try { general.load(new FileInputStream(new File(propertiesFile))); }
        catch (Throwable ex) { throw new Error("Could not access config file: " + ex); }
    }
    
    public String getS(String key) {
        return general.getProperty(key);
    }
    
    public int getI(String key) {
        return Integer.parseInt(general.getProperty(key));
    }
}
