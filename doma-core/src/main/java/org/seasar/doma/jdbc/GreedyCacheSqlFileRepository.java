package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.seasar.doma.jdbc.dialect.Dialect;

/** An SQL file repository that caches the results of SQL parsing without limit. */
public class GreedyCacheSqlFileRepository extends AbstractSqlFileRepository {

  protected final ConcurrentMap<CacheKey, SqlFile> sqlFileMap = new ConcurrentHashMap<>(200);

  public GreedyCacheSqlFileRepository() {}

  public GreedyCacheSqlFileRepository(SqlParserConfig sqlParserConfig) {
    super(sqlParserConfig);
  }

  @Override
  protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
    CacheKey key = new CacheKey(method, path);
    SqlFile file = sqlFileMap.get(key);
    if (file != null) {
      return file;
    }
    file = createSqlFile(method, path, dialect);
    SqlFile current = sqlFileMap.putIfAbsent(key, file);
    return current != null ? current : file;
  }

  @Override
  public void clearCache() {
    sqlFileMap.clear();
  }
}
