package client.other;

import sharedlib.utils.Preferences;

/**
 * Contains the keys to the various client-side persistence values
 * @author Alex
 */
public enum PrefsKey implements Preferences.Key {

    /**
     * Server IP of the connection
     */
    ServerIP("ServerIP", "localhost"),

    /**
     * Port of the server connection
     */
    ServerPort("ServerPort", 4413),

    /**
     * UI colour theme
     */
    DarkTheme("DarkTheme", false),

    /**
     * Background music
     */
    Sound("Sound", true);

    private final String key;
    private final Object defaultValue;

    private PrefsKey(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     *
     * @return Key
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     *
     * @return Default Value
     */
    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
