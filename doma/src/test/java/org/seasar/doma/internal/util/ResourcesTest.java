package org.seasar.doma.internal.util;

import java.io.InputStream;

import org.seasar.doma.internal.util.Resources;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ResourcesTest extends TestCase {

    public void testGetResourceAsStream() throws Exception {
        String path = getClass().getName().replace(".", "/") + ".txt";
        InputStream inputStream = Resources.getResourceAsStream(path);
        assertNotNull(inputStream);
    }

    public void testGetResourceAsStream_inexistentPath() throws Exception {
        InputStream inputStream = Resources
                .getResourceAsStream("inexistentPath");
        assertNull(inputStream);
    }

    public void testGetResourceAsString() throws Exception {
        String path = getClass().getName().replace(".", "/") + ".txt";
        String value = Resources.getResourceAsString(path);
        assertEquals("aaa", value);
    }
}
