package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

/** @author taedium */
public class GreedyCacheSqlFileRepositoryTest extends TestCase {

  private Method method;

  @Override
  protected void setUp() throws Exception {
    method = getClass().getMethod(getName());
  }

  public void testGetSqlFile() throws Exception {
    StandardDialect dialect = new StandardDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertNotNull(sqlFile);
    SqlFile sqlFile2 = repository.getSqlFile(method, path, dialect);
    assertSame(sqlFile, sqlFile2);
    assertEquals(path, sqlFile.getPath());
  }

  public void testGetSqlFile_oracle() throws Exception {
    OracleDialect dialect = new OracleDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(
        "META-INF/" + getClass().getName().replace(".", "/") + "-oracle.sql", sqlFile.getPath());
  }

  public void testGetSqlFile_postgres() throws Exception {
    PostgresDialect dialect = new PostgresDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(path, sqlFile.getPath());
  }

  public void testClearCache() throws Exception {
    StandardDialect dialect = new StandardDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertNotNull(sqlFile);
    repository.clearCache();
    SqlFile sqlFile2 = repository.getSqlFile(method, path, dialect);
    assertNotSame(sqlFile, sqlFile2);
    assertEquals(path, sqlFile.getPath());
  }
}
