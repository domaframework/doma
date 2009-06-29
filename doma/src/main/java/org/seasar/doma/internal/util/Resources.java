package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.Assertions.*;

import java.io.InputStream;

import org.seasar.doma.internal.WrapException;


/**
 * @author taedium
 * 
 */
public final class Resources {

    public static InputStream getResourceAsStream(String path) {
        assertNotNull(path);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            return null;
        }
        return loader.getResourceAsStream(path);
    }

    public static String getResourceAsString(String path) throws WrapException {
        assertNotNull(path);
        assertTrue(path.length() > 0);
        InputStream inputStream = getResourceAsStream(path);
        if (inputStream == null) {
            return null;
        }
        return IOs.readAsString(inputStream);
    }
}
