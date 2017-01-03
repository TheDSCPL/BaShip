package sharedlib.utils;

/**
 * Empty class used to represent {@code null} values in collections which do not support {@code null} values.
 * @author Alex
 */
public class Null {
    public Null() {
        
    }

    @Override
    public int hashCode() {
        return 77;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return getClass() == obj.getClass();
    }
    
}
