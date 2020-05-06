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
import org.seasar.doma.jdbc.criteria.command.MappedObjectStreamHandler;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class NativeSqlSelectCollectable<ELEMENT> extends AbstractStatement<List<ELEMENT>>
    implements Collectable<ELEMENT> {

  private final SelectFromDeclaration declaration;
  private final Function<Row, ELEMENT> rowMapper;

  public NativeSqlSelectCollectable(
      Config config, SelectFromDeclaration declaration, Function<Row, ELEMENT> rowMapper) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    Objects.requireNonNull(rowMapper);
    this.declaration = declaration;
    this.rowMapper = rowMapper;
  }

  public <RESULT> Statement<RESULT> stream(Function<Stream<ELEMENT>, RESULT> streamMapper) {
    List<PropertyDef<?>> propertyDefs = declaration.getContext().allPropertyDefs();
    ResultSetHandler<RESULT> handler =
        new MappedObjectStreamHandler<>(streamMapper, propertyDefs, rowMapper);
    return new NativeSqlSelectTerminal<>(config, declaration, handler);
  }

  public <RESULT> Statement<RESULT> collect(Collector<ELEMENT, ?, RESULT> collector) {
    return stream(s -> s.collect(collector));
  }

  @Override
  protected Command<List<ELEMENT>> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    List<PropertyDef<?>> propertyDefs = declaration.getContext().allPropertyDefs();
    ResultSetHandler<List<ELEMENT>> handler =
        new MappedObjectStreamHandler<>(s -> s.collect(toList()), propertyDefs, rowMapper);
    NativeSqlSelectTerminal<List<ELEMENT>> terminal =
        new NativeSqlSelectTerminal<>(config, declaration, handler);
    return terminal.createCommand(config, commenter, sqlLogType);
  }
}
