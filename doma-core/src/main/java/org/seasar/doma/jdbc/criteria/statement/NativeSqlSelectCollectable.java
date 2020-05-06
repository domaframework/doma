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
import org.seasar.doma.jdbc.criteria.command.MappedObjectStreamHandler;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class NativeSqlSelectCollectable<ELEMENT>
    extends AbstractStatement<List<ELEMENT>, NativeSqlSelectCollectable<ELEMENT>>
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

  public <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper) {
    List<PropertyDef<?>> propertyDefs = declaration.getContext().allPropertyDefs();
    ResultSetHandler<RESULT> handler =
        new MappedObjectStreamHandler<>(streamMapper, propertyDefs, rowMapper);
    NativeSqlSelectTerminal<RESULT> terminal =
        new NativeSqlSelectTerminal<>(config, declaration, handler);
    return terminal.execute();
  }

  public <RESULT> RESULT collect(Collector<ELEMENT, ?, RESULT> collector) {
    return mapStream(s -> s.collect(collector));
  }

  @Override
  protected Command<List<ELEMENT>> createCommand() {
    List<PropertyDef<?>> propertyDefs = declaration.getContext().allPropertyDefs();
    ResultSetHandler<List<ELEMENT>> handler =
        new MappedObjectStreamHandler<>(s -> s.collect(toList()), propertyDefs, rowMapper);
    NativeSqlSelectTerminal<List<ELEMENT>> terminal =
        new NativeSqlSelectTerminal<>(config, declaration, handler);
    return terminal.createCommand();
  }
}
