package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.ResultSetIterator.SQLRuntimeException;
import org.seasar.doma.internal.util.IteratorUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * @param <RESULT> 結果
 * @param <TARGET> 反復処理対象
 */
public abstract class AbstractIterationHandler<TARGET, RESULT> implements ResultSetHandler<RESULT> {

  protected final IterationCallback<TARGET, RESULT> iterationCallback;

  public AbstractIterationHandler(IterationCallback<TARGET, RESULT> iterationCallback) {
    assertNotNull(iterationCallback);
    this.iterationCallback = iterationCallback;
  }

  @Override
  public Supplier<RESULT> handle(
      ResultSet resultSet, SelectQuery query, Consumer<ResultSetState> stateChecker)
      throws SQLException {
    ObjectProvider<TARGET> provider = createObjectProvider(query);
    Iterator<TARGET> iterator = new ResultSetIterator<>(resultSet, query, stateChecker, provider);
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
