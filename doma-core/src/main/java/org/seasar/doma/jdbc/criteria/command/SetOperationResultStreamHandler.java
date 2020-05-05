package org.seasar.doma.jdbc.criteria.command;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.command.AbstractStreamHandler;
import org.seasar.doma.jdbc.criteria.statement.Row;
import org.seasar.doma.jdbc.query.SelectQuery;

public class SetOperationResultStreamHandler<ELEMENT, RESULT>
    extends AbstractStreamHandler<ELEMENT, RESULT> {
  private final Function<Row, ELEMENT> mapper;

  public SetOperationResultStreamHandler(
      Function<Stream<ELEMENT>, RESULT> streamMapper, Function<Row, ELEMENT> mapper) {
    super(Objects.requireNonNull(streamMapper));
    this.mapper = Objects.requireNonNull(mapper);
  }

  @Override
  protected SetOperationResultProvider<ELEMENT> createObjectProvider(SelectQuery query) {
    return new SetOperationResultProvider<>(query, mapper);
  }
}
