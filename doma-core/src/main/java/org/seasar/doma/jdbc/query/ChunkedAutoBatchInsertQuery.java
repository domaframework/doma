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

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * An {@link AutoBatchInsertQuery} that defers SQL generation until each chunk is executed.
 *
 * <p>The standard {@link AutoBatchInsertQuery} builds {@link PreparedSql} for every entity in the
 * batch up front. For very large entity lists this can exhaust the heap. This subclass instead
 * prepares only the first entity during {@link #prepare()} (so the {@link
 * org.seasar.doma.jdbc.command.BatchInsertCommand} can obtain a representative SQL for {@code
 * PreparedStatement} creation) and produces the SQLs for the remaining entities in chunks via
 * {@link #buildSqls(int, int)}, which is invoked from {@code BatchInsertCommand} when it detects
 * the {@link ChunkedBatchInsertQuery} interface.
 *
 * <p>Users who need this behavior should instantiate this class from a custom {@link
 * org.seasar.doma.jdbc.QueryImplementors} implementation.
 *
 * @param <ENTITY> the entity type
 */
public class ChunkedAutoBatchInsertQuery<ENTITY> extends AutoBatchInsertQuery<ENTITY>
    implements ChunkedBatchInsertQuery {

  /**
   * Whether the SQL for the first entity has already been built and is still cached in {@code
   * sqls}.
   */
  private boolean firstEntitySqlPending;

  /**
   * Constructs an instance.
   *
   * @param entityType the entity type
   */
  public ChunkedAutoBatchInsertQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * Defers SQL generation for entities beyond the first to {@link #buildSqls(int, int)}.
   *
   * <p>After this method returns, {@code sqls} contains exactly one entry — the SQL for entity 0,
   * built by {@link #prepare()} — which is needed so that {@link #getSql()} returns a
   * representative {@link PreparedSql} when the command creates the {@code PreparedStatement}.
   */
  @Override
  protected void prepareSqlsForRemainingEntities() {
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
   * <p>This implementation reuses the SQL built in {@link #prepare()} for entity 0 the first time
   * it is requested, then runs pre-insert hooks and SQL assembly for each remaining entity in the
   * range. The internal {@code sqls} buffer is cleared after every chunk so previously built SQLs
   * become eligible for garbage collection.
   */
  @Override
  public List<PreparedSql> buildSqls(int from, int to) {
    int size = entities.size();
    if (from < 0 || to > size || from > to) {
      throw new DomaIllegalArgumentException(
          "from/to", "from = " + from + ", to = " + to + ", entities.size() = " + size);
    }
    List<PreparedSql> chunkSqls = new ArrayList<>(to - from);
    int start = from;
    if (from == 0 && firstEntitySqlPending && !sqls.isEmpty()) {
      chunkSqls.add(sqls.get(0));
      start = 1;
    }
    sqls.clear();
    for (int i = start; i < to; i++) {
      currentEntity = entities.get(i);
      preInsert();
      prepareIdValue();
      prepareVersionValue();
      prepareSql();
      entities.set(i, currentEntity);
    }
    currentEntity = null;
    chunkSqls.addAll(sqls);
    sqls.clear();
    firstEntitySqlPending = false;
    return chunkSqls;
  }
}
