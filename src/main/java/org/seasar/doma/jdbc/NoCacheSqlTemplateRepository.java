package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.dialect.Dialect;

/** An SQL template repository that does not cache the results of SQL parsing. */
public class NoCacheSqlTemplateRepository extends AbstractSqlTemplateRepository {

  @Override
  protected SqlTemplate getSqlTemplateWithCacheControl(Method method, Dialect dialect) {
    return createSqlTemplate(method, dialect);
  }
}
