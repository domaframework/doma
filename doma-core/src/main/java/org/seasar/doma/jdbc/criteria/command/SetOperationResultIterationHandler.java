package org.seasar.doma.jdbc.criteria.command;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.criteria.statement.Row;
import org.seasar.doma.jdbc.query.SelectQuery;

public class SetOperationResultIterationHandler<ELEMENT>
    extends AbstractIterationHandler<ELEMENT, List<ELEMENT>> {
  private final Function<Row, ELEMENT> mapper;

  public SetOperationResultIterationHandler(Function<Row, ELEMENT> mapper) {
    super(new ResultListCallback<>());
    Objects.requireNonNull(mapper);
    this.mapper = mapper;
  }

  @Override
  protected SetOperationResultProvider<ELEMENT> createObjectProvider(SelectQuery query) {
    return new SetOperationResultProvider<>(query, mapper);
  }
}
