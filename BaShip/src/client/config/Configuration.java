package client.config;

import java.io.*;
import java.util.*;

/**
 *
 * @author Alex
 */
public class Configuration {
    private final Properties general = new Properties();
    private static final String PropertiesFileName = "src/client/config/config.properties";
    
    public Configuration() {
        try { general.load(new FileInputStream(new File(PropertiesFileName))); }
        catch (Exception ex) { throw new Error("Could not access config file: " + ex); }
    }
    
    public String getS(String key) {
        return general.getProperty(key);
    }
    
    public int getI(String key) {
        return Integer.parseInt(general.getProperty(key));
    }
}
