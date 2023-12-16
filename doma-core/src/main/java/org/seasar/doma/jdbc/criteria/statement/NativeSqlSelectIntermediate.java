package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.criteria.command.MappedResultStreamHandler;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.query.AliasManager;
import org.seasar.doma.jdbc.criteria.query.SelectBuilder;
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
  public void appendQuery(
      Config config,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    AliasManager childAliasManager = new AliasManager(declaration.getContext(), aliasManager);
    SelectBuilder builder =
        new SelectBuilder(config, declaration.getContext(), commenter, buf, childAliasManager);
    builder.build();
  }

  @Override
  public <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper) {
    Objects.requireNonNull(streamMapper);
    NativeSqlSelectTerminal<RESULT> terminal = createNativeSqlSelectTerminal(streamMapper);
    return terminal.execute();
  }

  @Override
  protected Command<List<ELEMENT>> createCommand() {
    NativeSqlSelectTerminal<List<ELEMENT>> terminal =
        createNativeSqlSelectTerminal(stream -> stream.collect(toList()));
    return terminal.createCommand();
  }

  private <RESULT> NativeSqlSelectTerminal<RESULT> createNativeSqlSelectTerminal(
      Function<Stream<ELEMENT>, RESULT> streamMapper) {
    ResultSetHandler<RESULT> handler =
        new MappedResultStreamHandler<>(streamMapper, objectProviderFactory);
    return new NativeSqlSelectTerminal<>(config, declaration, handler);
  }
}
