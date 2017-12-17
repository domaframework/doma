package org.seasar.doma.jdbc;

import java.lang.reflect.Method;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.MapKeyNamingType;

/**
 * A naming convention controller for the keys contained in a
 * {@code Map<String, Object>} object.
 */
public interface MapKeyNaming {

    /**
     * Applies the naming convention.
     * 
     * @param method
     *            the DAO method or {@code null} if the SQL is built with the
     *            query builder
     * @param mapKeyNamingType
     *            the naming convention
     * @param text
     *            the text
     * @return the converted text
     * @throws DomaNullPointerException
     *             if {@code mapKeyNamingType} or {@code text} is {@code null}
     */
    default String apply(Method method, MapKeyNamingType mapKeyNamingType, String text) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (text == null) {
            throw new DomaNullPointerException("text");
        }
        return mapKeyNamingType.apply(text);
    }
}
