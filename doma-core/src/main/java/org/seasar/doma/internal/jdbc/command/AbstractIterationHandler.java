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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Supplier;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.ResultSetIterator.SQLRuntimeException;
import org.seasar.doma.internal.util.IteratorUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.ResultSetRowIndexConsumer;
import org.seasar.doma.jdbc.query.SelectQuery;

public abstract class AbstractIterationHandler<TARGET, RESULT> implements ResultSetHandler<RESULT> {

  protected final IterationCallback<TARGET, RESULT> iterationCallback;

  public AbstractIterationHandler(IterationCallback<TARGET, RESULT> iterationCallback) {
    assertNotNull(iterationCallback);
    this.iterationCallback = iterationCallback;
  }

  @Override
  public Supplier<RESULT> handle(
      ResultSet resultSet, SelectQuery query, ResultSetRowIndexConsumer consumer)
      throws SQLException {
    ObjectProvider<TARGET> provider = createObjectProvider(query);
    Iterator<TARGET> iterator = new ResultSetIterator<>(resultSet, query, consumer, provider);
    try {
      if (query.getFetchType() == FetchType.EAGER) {
        // consume ResultSet
        Iterator<TARGET> it = IteratorUtil.copy(iterator);
        return () -> iterate(it);
      } else {
        RESULT result = iterate(iterator);
        return () -> result;
      }
    } catch (SQLRuntimeException e) {
      throw e.getCause();
    }
  }

  protected RESULT iterate(Iterator<TARGET> iterator) {
    IterationContext context = new IterationContext();
    RESULT candidate = iterationCallback.defaultResult();
    while (!context.isExited() && iterator.hasNext()) {
      TARGET target = iterator.next();
      candidate = iterationCallback.iterate(target, context);
    }
    return iterationCallback.postIterate(candidate, context);
  }

  protected abstract ObjectProvider<TARGET> createObjectProvider(SelectQuery query);
}
