package org.seasar.doma.internal.util;

import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ResourceUtilTest extends TestCase {

    public void testGetResourceAsStream() throws Exception {
        String path = getClass().getName().replace(".", "/") + ".txt";
        InputStream inputStream = ResourceUtil.getResourceAsStream(path);
        assertNotNull(inputStream);
    }

    public void testGetResourceAsStream_nonexistentPath() throws Exception {
        InputStream inputStream = ResourceUtil.getResourceAsStream("nonexistentPath");
        assertNull(inputStream);
    }

    public void testGetResourceAsString() throws Exception {
        String path = getClass().getName().replace(".", "/") + ".txt";
        String value = ResourceUtil.getResourceAsString(path);
        assertEquals("aaa", value);
    }
}
