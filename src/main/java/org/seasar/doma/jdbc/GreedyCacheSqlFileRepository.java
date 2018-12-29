package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * SQLの解析結果をメモリが許す限り最大限にキャッシュする {@link SqlFileRepository} の実装です。
 *
 * @author taedium
 */
public class GreedyCacheSqlFileRepository extends AbstractSqlFileRepository {

  /** SQLのパスをキー、SQLファイルを値とするマップです。 */
  protected final ConcurrentMap<String, SqlFile> sqlFileMap =
      new ConcurrentHashMap<String, SqlFile>(200);

  @Override
  protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
    SqlFile file = sqlFileMap.get(path);
    if (file != null) {
      return file;
    }
    file = createSqlFile(path, dialect);
    SqlFile current = sqlFileMap.putIfAbsent(path, file);
    return current != null ? current : file;
  }

  @Override
  public void clearCache() {
    sqlFileMap.clear();
  }
}
