package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.StandardSqlFileRepository;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class StandardSqlNodeRepositoryTest extends TestCase {

    public void testGetSqlNode() throws Exception {
        StandardDialect dialect = new StandardDialect();
        String path = getClass().getName().replace(".", "/") + ".sql";
        StandardSqlFileRepository repository = new StandardSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertNotNull(sqlFile);
        SqlFile sqlFile2 = repository.getSqlFile(path, dialect);
        assertSame(sqlFile, sqlFile2);
        assertEquals(path, sqlFile.getRealPath());
    }

    public void testGetSqlNode_oracle() throws Exception {
        OracleDialect dialect = new OracleDialect();
        String path = getClass().getName().replace(".", "/") + ".sql";
        StandardSqlFileRepository repository = new StandardSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertEquals(getClass().getName().replace(".", "/") + "_oracle.sql", sqlFile
                .getRealPath());
    }

    public void testGetSqlNode_postgres() throws Exception {
        PostgresDialect dialect = new PostgresDialect();
        String path = getClass().getName().replace(".", "/") + ".sql";
        StandardSqlFileRepository repository = new StandardSqlFileRepository();
        SqlFile sqlFile = repository.getSqlFile(path, dialect);
        assertEquals(path, sqlFile.getRealPath());
    }
}
