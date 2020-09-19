package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GreedyCacheSqlFileRepositoryTest {

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testGetSqlFile() {
    StandardDialect dialect = new StandardDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertNotNull(sqlFile);
    SqlFile sqlFile2 = repository.getSqlFile(method, path, dialect);
    assertSame(sqlFile, sqlFile2);
    assertEquals(path, sqlFile.getPath());
  }

  @Test
  public void testGetSqlFile_oracle() {
    OracleDialect dialect = new OracleDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(
        "META-INF/" + getClass().getName().replace(".", "/") + "-oracle.sql", sqlFile.getPath());
  }

  @Test
  public void testGetSqlFile_postgres() {
    PostgresDialect dialect = new PostgresDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(path, sqlFile.getPath());
  }

  @Test
  public void testClearCache() {
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
