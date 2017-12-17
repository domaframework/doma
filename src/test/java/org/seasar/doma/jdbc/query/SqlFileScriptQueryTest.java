package org.seasar.doma.jdbc.query;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;

/**
 * @author taedium
 * 
 */
public class SqlFileScriptQueryTest extends TestCase {

    private final MockConfig config = new MockConfig();

    public void testPrepare() throws Exception {
        SqlFileScriptQuery query = new SqlFileScriptQuery();
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setScriptFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare.script");
        query.setBlockDelimiter("");
        query.prepare();

        assertEquals(config, query.getConfig());
        assertEquals("aaa", query.getClassName());
        assertEquals("bbb", query.getMethodName());
        assertEquals(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare.script",
                query.getScriptFilePath());
        assertNotNull(query.getScriptFileUrl());
        assertNull(query.getBlockDelimiter());
    }

    public void testPrepare_dbmsSpecific() throws Exception {
        config.dialect = new Mssql2008Dialect();
        SqlFileScriptQuery query = new SqlFileScriptQuery();
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setScriptFilePath(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare_dbmsSpecific.script");
        query.setBlockDelimiter("");
        query.prepare();

        assertEquals(config, query.getConfig());
        assertEquals("aaa", query.getClassName());
        assertEquals("bbb", query.getMethodName());
        assertEquals(
                "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare_dbmsSpecific-mssql.script",
                query.getScriptFilePath());
        assertNotNull(query.getScriptFileUrl());
        assertEquals("GO", query.getBlockDelimiter());
    }

    public void testPrepare_ScriptFileNotFoundException() throws Exception {
        SqlFileScriptQuery query = new SqlFileScriptQuery();
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setScriptFilePath("META-INF/ccc.script");
        query.setBlockDelimiter("ddd");
        try {
            query.prepare();
            fail();
        } catch (ScriptFileNotFoundException expected) {
            System.out.println(expected.getMessage());
        }
    }
}
