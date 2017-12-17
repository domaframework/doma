package org.seasar.doma.jdbc;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BatchOptimisticLockExceptionTest extends TestCase {

    public void test() throws Exception {
        BatchOptimisticLockException e = new BatchOptimisticLockException(SqlLogType.FORMATTED,
                SqlKind.UPDATE, "aaa", "bbb");
        System.out.println(e.getMessage());
        assertSame(SqlKind.UPDATE, e.getKind());
        assertEquals("aaa", e.getRawSql());
        assertEquals("bbb", e.getSqlFilePath());
        assertNull(e.getFormattedSql());
    }
}
