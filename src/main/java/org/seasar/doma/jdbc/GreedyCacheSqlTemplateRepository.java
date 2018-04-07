package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.seasar.doma.jdbc.dialect.Dialect;

/** An SQL template repository that caches the results of SQL parsing without limit. */
public class GreedyCacheSqlTemplateRepository extends AbstractSqlTemplateRepository {

  protected final ConcurrentMap<Method, SqlTemplate> sqlFileMap = new ConcurrentHashMap<>(200);

  @Override
  protected SqlTemplate getSqlTemplateWithCacheControl(Method method, Dialect dialect) {
    var file = sqlFileMap.get(method);
    if (file != null) {
      return file;
    }
    file = createSqlTemplate(method, dialect);
    var current = sqlFileMap.putIfAbsent(method, file);
    return current != null ? current : file;
  }

  @Override
  public void clearCache() {
    sqlFileMap.clear();
  }
}
