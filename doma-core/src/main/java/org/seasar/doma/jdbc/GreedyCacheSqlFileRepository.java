/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.seasar.doma.jdbc.dialect.Dialect;

/** An SQL file repository that caches the results of SQL parsing without limit. */
public class GreedyCacheSqlFileRepository extends AbstractSqlFileRepository {

  protected final ConcurrentMap<CacheKey, SqlFile> sqlFileMap = new ConcurrentHashMap<>(200);

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
