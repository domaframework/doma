package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.criteria.ForUpdateOption;
import org.seasar.doma.jdbc.criteria.declaration.HavingDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class NativeSqlSelectStarting<ENTITY> extends AbstractStatement<List<ENTITY>> {

  private final SelectFromDeclaration declaration;
  private final EntityDef<ENTITY> entityDef;

  public NativeSqlSelectStarting(
      Config config, SelectFromDeclaration declaration, EntityDef<ENTITY> entityDef) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    Objects.requireNonNull(entityDef);
    this.declaration = declaration;
    this.entityDef = entityDef;
  }

  public NativeSqlSelectStarting<ENTITY> innerJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityDef, block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> leftJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityDef, block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> groupBy(PropertyDef<?>... propertyDefs) {
    Objects.requireNonNull(propertyDefs);
    declaration.groupBy(propertyDefs);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.having(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> orderBy(Consumer<OrderByDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.orderBy(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> limit(Integer limit) {
    declaration.limit(limit);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> offset(Integer offset) {
    declaration.offset(offset);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> forUpdate() {
    declaration.forUpdate(ForUpdateOption.WAIT);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> forUpdate(ForUpdateOption option) {
    declaration.forUpdate(option);
    return this;
  }

  public <RESULT> Mappable<RESULT> select(PropertyDef<?>... propertyDefs) {
    declaration.select(propertyDefs);
    return new NativeSqlSelectMappable<>(config, declaration);
  }

  public <RESULT> Statement<RESULT> stream(Function<Stream<ENTITY>, RESULT> streamMapper) {
    ResultSetHandler<RESULT> handler = new EntityStreamHandler<>(entityDef.asType(), streamMapper);
    return new NativeSqlSelectTerminal<>(config, declaration, handler);
  }

  public <RESULT> Statement<RESULT> collect(Collector<ENTITY, ?, RESULT> collector) {
    return stream(s -> s.collect(collector));
  }

  @Override
  protected Command<List<ENTITY>> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    ResultSetHandler<List<ENTITY>> handler =
        new EntityStreamHandler<>(entityDef.asType(), s -> s.collect(toList()));
    NativeSqlSelectTerminal<List<ENTITY>> terminal =
        new NativeSqlSelectTerminal<>(config, declaration, handler);
    return terminal.createCommand(config, commenter, sqlLogType);
  }
}
