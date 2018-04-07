package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class GreedyCacheSqlTemplateRepositoryTest extends TestCase {

  private Method method;

  @Override
  protected void setUp() throws Exception {
    method = getClass().getMethod(getName());
  }

  public void testGetSqlFile() throws Exception {
    var dialect = new StandardDialect();
    var path = "META-INF/" + getClass().getName().replace(".", "/") + "/" + getName() + ".sql";
    var repository = new GreedyCacheSqlTemplateRepository();
    var sqlTemplate = repository.getSqlTemplate(method, dialect);
    assertNotNull(sqlTemplate);
    var sqlTemplate2 = repository.getSqlTemplate(method, dialect);
    assertSame(sqlTemplate, sqlTemplate2);
    assertEquals(path, sqlTemplate.getPath());
  }

  public void testGetSqlFile_oracle() throws Exception {
    var dialect = new OracleDialect();
    var repository = new GreedyCacheSqlTemplateRepository();
    var sqlTemplate = repository.getSqlTemplate(method, dialect);
    assertEquals(
        "META-INF/" + getClass().getName().replace(".", "/") + "/" + getName() + "-oracle.sql",
        sqlTemplate.getPath());
  }

  public void testGetSqlFile_postgres() throws Exception {
    var dialect = new PostgresDialect();
    var path = "META-INF/" + getClass().getName().replace(".", "/") + "/" + getName() + ".sql";
    var repository = new GreedyCacheSqlTemplateRepository();
    var sqlTemplate = repository.getSqlTemplate(method, dialect);
    assertEquals(path, sqlTemplate.getPath());
  }

  public void testClearCache() throws Exception {
    var dialect = new StandardDialect();
    var path = "META-INF/" + getClass().getName().replace(".", "/") + "/" + getName() + ".sql";
    var repository = new GreedyCacheSqlTemplateRepository();
    var sqlTemplate = repository.getSqlTemplate(method, dialect);
    assertNotNull(sqlTemplate);
    repository.clearCache();
    var sqlFile2 = repository.getSqlTemplate(method, dialect);
    assertNotSame(sqlTemplate, sqlFile2);
    assertEquals(path, sqlTemplate.getPath());
  }
}
