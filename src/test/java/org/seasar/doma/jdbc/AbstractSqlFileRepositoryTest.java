package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.experimental.Sql;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class AbstractSqlFileRepositoryTest extends TestCase {

  public void testGetSqlFile() throws Exception {
    SqlFileRepository repository = new MySqlFileRepository();
    Method method = getClass().getMethod(getName());
    String path = String.format("META-INF/%s.sql", getClass().getName().replace(".", "/"));
    Dialect dialect = new StandardDialect();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals("select * from employee", sqlFile.getSql());
  }

  public void testGetSqlFile_illegalPath() throws Exception {
    SqlFileRepository repository = new MySqlFileRepository();
    Method method = getClass().getMethod(getName());
    String path = getName();
    Dialect dialect = new StandardDialect();
    try {
      repository.getSqlFile(method, path, dialect);
      fail();
    } catch (DomaIllegalArgumentException ignored) {
    }
  }

  @Sql("select * from address")
  public void testGetSqlFile_SqlAnnotation() throws Exception {
    SqlFileRepository repository = new MySqlFileRepository();
    Method method = getClass().getMethod(getName());
    String path = getName();
    Dialect dialect = new StandardDialect();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals("select * from address", sqlFile.getSql());
  }

  private static class MySqlFileRepository extends AbstractSqlFileRepository {

    @Override
    protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
      return createSqlFile(method, path, dialect);
    }
  }
}
