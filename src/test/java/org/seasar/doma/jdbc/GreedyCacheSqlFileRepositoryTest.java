package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class GreedyCacheSqlFileRepositoryTest extends TestCase {

  private Method method;

  @Override
  protected void setUp() throws Exception {
    method = getClass().getMethod(getName());
  }

  public void testGetSqlFile() throws Exception {
    var dialect = new StandardDialect();
    var path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    var repository = new GreedyCacheSqlFileRepository();
    var sqlFile = repository.getSqlFile(method, path, dialect);
    assertNotNull(sqlFile);
    var sqlFile2 = repository.getSqlFile(method, path, dialect);
    assertSame(sqlFile, sqlFile2);
    assertEquals(path, sqlFile.getPath());
  }

  public void testGetSqlFile_oracle() throws Exception {
    var dialect = new OracleDialect();
    var path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    var repository = new GreedyCacheSqlFileRepository();
    var sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(
        "META-INF/" + getClass().getName().replace(".", "/") + "-oracle.sql", sqlFile.getPath());
  }

  public void testGetSqlFile_postgres() throws Exception {
    var dialect = new PostgresDialect();
    var path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    var repository = new GreedyCacheSqlFileRepository();
    var sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(path, sqlFile.getPath());
  }

  public void testClearCache() throws Exception {
    var dialect = new StandardDialect();
    var path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    var repository = new GreedyCacheSqlFileRepository();
    var sqlFile = repository.getSqlFile(method, path, dialect);
    assertNotNull(sqlFile);
    repository.clearCache();
    var sqlFile2 = repository.getSqlFile(method, path, dialect);
    assertNotSame(sqlFile, sqlFile2);
    assertEquals(path, sqlFile.getPath());
  }
}
