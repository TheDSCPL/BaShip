package server.other;

import sharedlib.utils.Preferences;

public enum PrefsKey implements Preferences.Key {
    DatabaseURL("DatabaseURL", "jdbc:postgresql://dbm.fe.up.pt/lpro1613"),
    ServerPort("ServerPort", 4413),
    DatabaseUsername("DatabaseUsername", "lpro1613"),
    DatabasePassword("DatabasePassword", "X!5493jbo");

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
