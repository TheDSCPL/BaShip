package server.other;

import sharedlib.utils.Preferences;

/**
 * Contains the keys to the various server-side persistence values
 *
 * @author Alex
 */
public enum PrefsKey implements Preferences.Key {

    /**
     * Port of server to connect
     */
    ServerPort("ServerPort", 4413),

    /**
     * URL of the database to connect
     */
    DatabaseURL("DatabaseURL", "jdbc:postgresql://dbm.fe.up.pt/lpro1613"),

    /**
     * Username of database login
     */
    DatabaseUsername("DatabaseUsername", "lpro1613"),

    /**
     * Password of database login
     */
    DatabasePassword("DatabasePassword", "X!5493jbo");
    /*DatabaseURL("DatabaseURL", "jdbc:postgresql://localhost/Alex"),
    DatabaseUsername("DatabaseUsername", "Alex"),
    DatabasePassword("DatabasePassword", "");*/
    /*DatabaseURL("DatabaseURL", "jdbc:postgresql://localhost/BaShip"),
    DatabaseUsername("DatabaseUsername", "postgres"),
    DatabasePassword("DatabasePassword", "olaola");*/

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
