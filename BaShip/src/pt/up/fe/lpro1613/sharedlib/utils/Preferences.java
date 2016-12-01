package pt.up.fe.lpro1613.sharedlib.utils;

/**
 * Wrapper around {@code java.util.prefs.Preferences} that supports the use of
 * keys with type-safety and default values
 *
 * @author Alex
 */
public class Preferences {

    private final java.util.prefs.Preferences prefs;

    /**
     * A Key used by this Preferences class. Must contain a String value (the
     * key) and a default value for when no object for that key is present on
     * disk.
     */
    public interface Key {

        public String getKey();

        public Object getDefaultValue();
    }

    /**
     * Create a preferences object for the specified class. Calls
     * {@code Preferences::userNodeForPackage}
     *
     * @param c
     */
    public Preferences(Class c) {
        prefs = java.util.prefs.Preferences.userNodeForPackage(c);
    }

    /**
     * Set boolean value for key
     *
     * @param k The key
     * @param v The value
     */
    public void setB(Key k, boolean v) {
        prefs.putBoolean(k.getKey(), v);
    }

    /**
     * Set int value for key
     *
     * @param k The key
     * @param v The value
     */
    public void setI(Key k, int v) {
        prefs.putInt(k.getKey(), v);
    }

    /**
     * Set String value for key
     *
     * @param k The key
     * @param v The value
     */
    public void setS(Key k, String v) {
        prefs.put(k.getKey(), v);
    }

    /**
     * Get boolean value from key, or default if none exists
     *
     * @param k The key
     * @return The value, or default
     */
    public boolean getB(Key k) {
        return prefs.getBoolean(k.getKey(), (boolean) k.getDefaultValue());
    }

    /**
     * Get int value from key, or default if none exists
     *
     * @param k The key
     * @return The value, or default
     */
    public int getI(Key k) {
        return prefs.getInt(k.getKey(), (int) k.getDefaultValue());
    }

    /**
     * Get String value from key, or default if none exists
     *
     * @param k The key
     * @return The value, or default
     */
    public String getS(Key k) {
        return prefs.get(k.getKey(), (String) k.getDefaultValue());
    }
}
