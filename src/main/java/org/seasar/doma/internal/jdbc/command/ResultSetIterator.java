package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.query.SelectQuery;

/** @author nakamura-to */
public class ResultSetIterator<TARGET> implements Iterator<TARGET> {

  protected final ResultSet resultSet;

  protected final SelectQuery query;

  protected final Consumer<ResultSetState> stateChecker;

  protected final ObjectProvider<TARGET> provider;

  protected boolean next;

  protected ResultSetState resultSetState;

  public ResultSetIterator(
      ResultSet resultSet,
      SelectQuery query,
      Consumer<ResultSetState> stateChecker,
      ObjectProvider<TARGET> provider)
      throws SQLException {
    assertNotNull(resultSet, query, stateChecker, provider);
    this.resultSet = resultSet;
    this.query = query;
    this.stateChecker = stateChecker;
    this.provider = provider;
    this.next = resultSet.next();
    resultSetState = ResultSetState.UNKNOWN.apply(next);
    stateChecker.accept(resultSetState);
  }

  @Override
  public boolean hasNext() {
    return next;
  }

  @Override
  public TARGET next() {
    TARGET result;
    try {
      result = provider.get(resultSet);
      next = resultSet.next();
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
    resultSetState = resultSetState.apply(next);
    stateChecker.accept(resultSetState);
    return result;
  }

  public List<TARGET> toList() {
    List<TARGET> result = new ArrayList<>();
    this.forEachRemaining(result::add);
    return result;
  }

  @SuppressWarnings("serial")
  protected static class SQLRuntimeException extends RuntimeException {

    protected SQLRuntimeException(SQLException cause) {
      super(cause);
    }

    @Override
    public SQLException getCause() {
      return (SQLException) super.getCause();
    }
  }
}
