package org.seasar.doma.internal.util;

import java.lang.reflect.Constructor;

import org.seasar.doma.internal.util.Constructors;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ReflectionsTest extends TestCase {

    public void testToSignature() throws Exception {
        Constructor<String> constructor = String.class
                .getConstructor(char[].class, int.class, int.class);
        assertEquals("java.lang.String(char[], int, int)", Constructors
                .toSignature(constructor));
    }
}
