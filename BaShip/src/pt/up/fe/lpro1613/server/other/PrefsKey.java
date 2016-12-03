package pt.up.fe.lpro1613.server.other;

import pt.up.fe.lpro1613.sharedlib.utils.Preferences;

/**
 * Contains the keys to the various server-side persistence values
 *
 * @author Alex
 */
public enum PrefsKey implements Preferences.Key {
    ServerPort("ServerPort", 4413),
    DatabaseURL("DatabaseURL", "jdbc:postgresql://dbm.fe.up.pt/lpro1613"),
    DatabaseUsername("DatabaseUsername", "lpro1613"),
    DatabasePassword("DatabasePassword", "X!5493jbo");/*
    DatabaseURL("DatabaseURL", "jdbc:postgresql://localhost/Alex"),
    DatabaseUsername("DatabaseUsername", "Alex"),
    DatabasePassword("DatabasePassword", "");*/

    private final String key;
    private final Object defaultValue;

    private PrefsKey(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
