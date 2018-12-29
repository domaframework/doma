package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * SQLの解析結果をキャッシュしない {@link SqlFileRepository} の実装です。
 *
 * @author taedium
 */
public class NoCacheSqlFileRepository extends AbstractSqlFileRepository {

  @Override
  protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
    return createSqlFile(path, dialect);
  }
}
