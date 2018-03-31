package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.ResultSetIterator.SQLRuntimeException;
import org.seasar.doma.internal.util.IteratorUtil;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * @param <TARGET> 処理対象
 * @param <RESULT> 結果
 */
public abstract class AbstractStreamHandler<TARGET, RESULT> implements ResultSetHandler<RESULT> {

  protected final Function<Stream<TARGET>, RESULT> mapper;

  public AbstractStreamHandler(Function<Stream<TARGET>, RESULT> mapper) {
    assertNotNull(mapper);
    this.mapper = mapper;
  }

  @Override
  public Supplier<RESULT> handle(
      ResultSet resultSet, SelectQuery query, Consumer<ResultSetState> stateChecker)
      throws SQLException {
    var provider = createObjectProvider(query);
    Iterator<TARGET> iterator = new ResultSetIterator<>(resultSet, query, stateChecker, provider);
    try {
      if (query.getFetchType() == FetchType.EAGER) {
        // consume ResultSet
        var list = IteratorUtil.toList(iterator);
        return () -> mapper.apply(list.stream());
      } else {
        var spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
        var stream = StreamSupport.stream(spliterator, false);
        var result = mapper.apply(stream);
        return () -> result;
      }
    } catch (SQLRuntimeException e) {
      throw e.getCause();
    }
  }

  protected abstract ObjectProvider<TARGET> createObjectProvider(SelectQuery query);
}
