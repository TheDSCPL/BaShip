package sharedlib.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Alex
 */
public final class Configuration {

    private final Properties props = new Properties();
    private File file;


    /*public Configuration(InputStream stream) {
        try {
            props.load(stream);
        }
        catch (Throwable ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not access configuration file -> exiting", ex);
            System.exit(-1);
        }
    }*/
    public Configuration(URL propertiesFile) {
        try {
            System.out.println(propertiesFile);
            file = new File(propertiesFile.toURI());
            load();
        }
        catch (URISyntaxException | IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not access configuration file -> exiting", ex);
            System.exit(-1);
        }
    }

    public void load() throws IOException {
        InputStream in = new FileInputStream(file);
        props.load(in);
    }
    
    public void save() throws IOException {
        OutputStream out = new FileOutputStream(file);
        props.store(out, "This is an optional header comment string");
    }
    
    public void setB(String key, boolean value) {
        props.setProperty(key, value ? "true" : "false");
    }
    
    public void setS(String key, String value) {
        props.setProperty(key, value);
    }

    public String getS(String key) {
        return props.getProperty(key);
    }

    public int getI(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
}
