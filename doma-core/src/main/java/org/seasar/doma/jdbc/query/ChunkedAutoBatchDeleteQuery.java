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
package org.seasar.doma.jdbc.query;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * An {@link AutoBatchDeleteQuery} that defers SQL generation until each entity is executed.
 *
 * <p>The standard {@link AutoBatchDeleteQuery} builds {@link PreparedSql} for every entity in the
 * batch up front. For very large entity lists this can exhaust the heap. This subclass instead
 * prepares only the first entity during {@link #prepare()} so the command can obtain a
 * representative SQL for {@code PreparedStatement} creation, and produces the SQL for each
 * remaining entity on demand via {@link #buildSql(int)}.
 *
 * <p>Users who need this behavior should instantiate this class from a custom {@link
 * org.seasar.doma.jdbc.QueryImplementors} implementation.
 *
 * @param <ENTITY> the entity type
 */
public class ChunkedAutoBatchDeleteQuery<ENTITY> extends AutoBatchDeleteQuery<ENTITY>
    implements ChunkedBatchDeleteQuery {

  /**
   * Whether the SQL for the first entity has already been built by {@link #prepare()} and is still
   * cached in {@code sqls}.
   */
  private boolean firstEntitySqlPending;

  /**
   * Constructs an instance.
   *
   * @param entityType the entity type
   */
  public ChunkedAutoBatchDeleteQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /** Defers SQL generation for entities beyond the first to {@link #buildSql(int)}. */
  @Override
  protected void prepareSqlsForRemainingEntities(int size) {
    firstEntitySqlPending = !sqls.isEmpty();
    currentEntity = null;
  }

  /** {@inheritDoc} */
  @Override
  public int getEntityCount() {
    return entities == null ? 0 : entities.size();
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns the SQL built in {@link #prepare()} for entity 0 on the first
   * call, then runs pre-delete hooks and SQL assembly for each subsequent entity. The internal
   * {@code sqls} buffer is cleared after every call so the previously built {@link PreparedSql}
   * becomes eligible for garbage collection as soon as the command finishes binding it.
   */
  @Override
  public PreparedSql buildSql(int index) {
    int size = entities.size();
    if (index < 0 || index >= size) {
      throw new DomaIllegalArgumentException(
          "index", "index = " + index + ", entities.size() = " + size);
    }
    if (index == 0 && firstEntitySqlPending && !sqls.isEmpty()) {
      PreparedSql sql = sqls.get(0);
      sqls.clear();
      firstEntitySqlPending = false;
      return sql;
    }
    sqls.clear();
    currentEntity = entities.get(index);
    preDelete();
    prepareSql();
    entities.set(index, currentEntity);
    currentEntity = null;
    PreparedSql sql = sqls.get(0);
    sqls.clear();
    firstEntitySqlPending = false;
    return sql;
  }
}
