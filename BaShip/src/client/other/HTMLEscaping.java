package client.other;

import java.util.regex.Pattern;

public class HTMLEscaping {
    private static final Pattern[] patterns = new Pattern[] {
        Pattern.compile("&"),
        Pattern.compile("<"),
        Pattern.compile(">"),
        Pattern.compile("\""),
        Pattern.compile("\'")
    };
    private static final String[] replacements = new String[] {
        "&amp;",
        "&lt;",
        "&gt;",
        "&quot;",
        "&#39;"
    };
    
    /**
     * Return string with especial characters in HTML replaced by their escaped versions.
     * @param input
     * @return 
     */
    public static String escapeHTML(String input) {
        for(int i = 0 ;i < patterns.length ; i++)
            input = patterns[i].matcher(input).replaceAll(replacements[i]);
        return input;
    }
}
