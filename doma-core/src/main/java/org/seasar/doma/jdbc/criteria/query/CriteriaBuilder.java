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
package org.seasar.doma.jdbc.criteria.query;

import java.util.function.Consumer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

/**
 * A builder interface for database-specific SQL criteria operations.
 *
 * <p>This interface provides methods to build SQL fragments that are specific to
 * each database dialect, such as pagination, string concatenation, and locking operations.
 * Implementations are provided by each database dialect.
 *
 * @see org.seasar.doma.jdbc.dialect.Dialect#getCriteriaBuilder()
 */
public interface CriteriaBuilder {

  void concat(PreparedSqlBuilder buf, Runnable leftOperand, Runnable rightOperand);

  void offsetAndFetch(PreparedSqlBuilder buf, int offset, int limit);

  void lockWithTableHint(
      PreparedSqlBuilder buf, ForUpdateOption option, Consumer<PropertyMetamodel<?>> column);

  void forUpdate(
      PreparedSqlBuilder buf,
      ForUpdateOption option,
      Consumer<PropertyMetamodel<?>> column,
      AliasManager aliasManager);
}
