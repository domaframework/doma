package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.criteria.command.SetOperationResultStreamHandler;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class NativeSqlSetCollectable<ELEMENT>
    extends AbstractStatement<List<ELEMENT>, NativeSqlSetCollectable<ELEMENT>>
    implements Collectable<ELEMENT> {

  private final SetOperationContext<ELEMENT> context;
  private final Function<Row, ELEMENT> mapper;

  public NativeSqlSetCollectable(
      Config config, SetOperationContext<ELEMENT> context, Function<Row, ELEMENT> mapper) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(context);
    Objects.requireNonNull(mapper);
    this.context = context;
    this.mapper = mapper;
  }

  public <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper) {
    ResultSetHandler<RESULT> handler = new SetOperationResultStreamHandler<>(streamMapper, mapper);
    NativeSqlSetTerminal<RESULT> terminal = new NativeSqlSetTerminal<>(config, context, handler);
    return terminal.execute();
  }

  public <RESULT> RESULT collect(Collector<ELEMENT, ?, RESULT> collector) {
    return mapStream(s -> s.collect(collector));
  }

  @Override
  protected Command<List<ELEMENT>> createCommand() {
    ResultSetHandler<List<ELEMENT>> handler =
        new SetOperationResultStreamHandler<>(s -> s.collect(toList()), mapper);
    NativeSqlSetTerminal<List<ELEMENT>> terminal =
        new NativeSqlSetTerminal<>(config, context, handler);
    return terminal.createCommand();
  }
}
