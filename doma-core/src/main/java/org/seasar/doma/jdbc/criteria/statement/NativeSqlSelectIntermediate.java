package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.criteria.command.MappedResultStreamHandler;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.query.SelectQuery;

public class NativeSqlSelectIntermediate<ELEMENT>
    extends AbstractSetOperand<NativeSqlSelectIntermediate<ELEMENT>, ELEMENT> {

  private final SelectFromDeclaration declaration;

  public NativeSqlSelectIntermediate(
      Config config,
      SelectFromDeclaration declaration,
      Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory) {
    super(Objects.requireNonNull(config), objectProviderFactory);
    this.declaration = Objects.requireNonNull(declaration);
  }

  @Override
  public SetOperationContext<ELEMENT> getContext() {
    return new SetOperationContext.Select<>(declaration.getContext());
  }

  @Override
  public Stream<ELEMENT> openStream() {
    NativeSqlSelectTerminal<Stream<ELEMENT>> terminal =
        createNativeSqlSelectTerminal(Function.identity(), true);
    return terminal.execute();
  }

  @Override
  public <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper) {
    Objects.requireNonNull(streamMapper);
    NativeSqlSelectTerminal<RESULT> terminal = createNativeSqlSelectTerminal(streamMapper, false);
    return terminal.execute();
  }

  @Override
  protected Command<List<ELEMENT>> createCommand() {
    NativeSqlSelectTerminal<List<ELEMENT>> terminal =
        createNativeSqlSelectTerminal(stream -> stream.collect(toList()), false);
    return terminal.createCommand();
  }

  private <RESULT> NativeSqlSelectTerminal<RESULT> createNativeSqlSelectTerminal(
      Function<Stream<ELEMENT>, RESULT> streamMapper, boolean returnsStream) {
    ResultSetHandler<RESULT> handler =
        new MappedResultStreamHandler<>(streamMapper, objectProviderFactory);
    return new NativeSqlSelectTerminal<>(config, declaration, handler, returnsStream);
  }
}
