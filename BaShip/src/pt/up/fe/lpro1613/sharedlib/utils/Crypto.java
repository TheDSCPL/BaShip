package pt.up.fe.lpro1613.sharedlib.utils;

import java.nio.*;
import java.nio.charset.*;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crypto {
    
    /**
     * Encode a string using SHA1.
     * @param str The string to process.
     * @return The result SHA1 string.
     */
    public static String SHA1(char[] str) {        
        try {
            // Convert char[] to byte[]
            ByteBuffer bb = Charset.forName("UTF-8").encode(CharBuffer.wrap(str));
            byte[] bytes = new byte[bb.remaining()];
            bb.get(bytes);
            
            //blankOutByteArray(bb.array());
            
            // Calculate SHA-1 hash
            byte[] result = MessageDigest.getInstance("SHA1").digest(bytes);
            
            // Get String representation
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            // Return result
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypto.class.getName()).log(Level.SEVERE, null, ex);
            return "INVALID HASH!";
        }
    }
}
