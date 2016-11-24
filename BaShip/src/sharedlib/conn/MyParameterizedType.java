/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sharedlib.conn;

import java.lang.reflect.*;
import java.util.*;

public class MyParameterizedType implements ParameterizedType{

    private final Type[] actualTypeArguments;
    private final Class<?> rawType;
    private final Type ownerType;
    
    public static Type factory(Class<?> rawType, Type[] actualTypeArguments, Type ownerType)
    {
        if(actualTypeArguments == null)
            return rawType;
        if(actualTypeArguments.length == 0)
            return rawType;
        return new MyParameterizedType(rawType, actualTypeArguments, ownerType);
    }
    
    private MyParameterizedType(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
        this.ownerType = (ownerType == null) ? rawType.getDeclaringClass() : ownerType;
        validateConstructorArguments();
    }
    
    private void validateConstructorArguments() {
        TypeVariable<?>[] formals = rawType.getTypeParameters();
        // check correct arity of actual type args
        if (formals.length != actualTypeArguments.length){
            throw new MalformedParameterizedTypeException();
        }
        for (int i = 0; i < actualTypeArguments.length; i++) {
            // check actuals against formals' bounds
        }
    }
    
    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ParameterizedType))
            return false;
        ParameterizedType that = (ParameterizedType) o;

        if (this == that) {
            return true;
        }

        Type thatOwner = that.getOwnerType();
        Type thatRawType = that.getRawType();

        return Objects.equals(ownerType, thatOwner)
                && Objects.equals(rawType, thatRawType)
                && Arrays.equals(actualTypeArguments,
                                 that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments)
                ^ Objects.hashCode(ownerType)
                ^ Objects.hashCode(rawType);
    }
    
}
