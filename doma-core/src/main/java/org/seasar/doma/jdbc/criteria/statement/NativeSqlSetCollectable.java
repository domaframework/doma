package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.criteria.command.SetOperationResultStreamHandler;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class NativeSqlSetCollectable<ELEMENT> extends AbstractStatement<List<ELEMENT>>
    implements Collectable<ELEMENT> {

  private final SetOperationContext<ELEMENT> context;
  private final Function<Row, ELEMENT> mapper;

  public NativeSqlSetCollectable(
      SetOperationContext<ELEMENT> context, Function<Row, ELEMENT> mapper) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(mapper);
    this.context = context;
    this.mapper = mapper;
  }

  public <RESULT> Statement<RESULT> stream(Function<Stream<ELEMENT>, RESULT> streamMapper) {
    ResultSetHandler<RESULT> handler = new SetOperationResultStreamHandler<>(streamMapper, mapper);
    return new NativeSqlSetTerminal<>(context, handler);
  }

  public <RESULT> Statement<RESULT> collect(Collector<ELEMENT, ?, RESULT> collector) {
    return stream(s -> s.collect(collector));
  }

  @Override
  protected Command<List<ELEMENT>> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    ResultSetHandler<List<ELEMENT>> handler =
        new SetOperationResultStreamHandler<>(s -> s.collect(toList()), mapper);
    NativeSqlSetTerminal<List<ELEMENT>> terminal = new NativeSqlSetTerminal<>(context, handler);
    return terminal.createCommand(config, commenter, sqlLogType);
  }
}
