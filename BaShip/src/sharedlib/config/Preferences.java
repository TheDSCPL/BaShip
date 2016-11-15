package sharedlib.config;

public class Preferences {

    private final java.util.prefs.Preferences prefs;

    public interface Key {

        public String getKey();

        public Object getDefaultValue();
    }

    public Preferences(Class c) {
        prefs = java.util.prefs.Preferences.userNodeForPackage(c);
    }

    public void setB(Key k, boolean v) {
        prefs.putBoolean(k.getKey(), v);
    }

    public void setI(Key k, int v) {
        prefs.putInt(k.getKey(), v);
    }

    public void setS(Key k, String v) {
        prefs.put(k.getKey(), v);
    }

    public boolean getB(Key k) {
        return prefs.getBoolean(k.getKey(), (boolean) k.getDefaultValue());
    }

    public int getI(Key k) {
        return prefs.getInt(k.getKey(), (int) k.getDefaultValue());
    }

    public String getS(Key k) {
        return prefs.get(k.getKey(), (String) k.getDefaultValue());
    }
}
