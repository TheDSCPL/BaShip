package client.other;

import pt.up.fe.lpro1613.sharedlib.utils.Preferences;

/**
 * Contains the keys to the various client-side persistence values
 * @author Alex
 */
public enum PrefsKey implements Preferences.Key {
    ServerIP("ServerIP", "localhost"),
    ServerPort("ServerPort", 4413),
    DarkTheme("DarkTheme", false),
    Sound("Sound", true);

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
