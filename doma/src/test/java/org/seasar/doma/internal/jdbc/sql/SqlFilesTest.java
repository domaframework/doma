package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.internal.jdbc.sql.SqlFiles;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class SqlFilesTest extends TestCase {

    public void testBuildPath() throws Exception {
        String path = SqlFiles.buildPath("aaa.bbb.Ccc", "ddd");
        assertEquals("META-INF/aaa/bbb/Ccc/ddd.sql", path);
    }

    public void testBuildPath_defaultPackage() throws Exception {
        String path = SqlFiles.buildPath("Ccc", "ddd");
        assertEquals("META-INF/Ccc/ddd.sql", path);
    }

}
