package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.ForUpdateOption;
import org.seasar.doma.jdbc.criteria.declaration.HavingDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class NativeSqlSelectStarting<ELEMENT> extends AbstractStatement<List<ELEMENT>>
    implements SelectStatement<ELEMENT> {

  private final SelectFromDeclaration declaration;

  public NativeSqlSelectStarting(SelectFromDeclaration declaration) {
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  public NativeSqlSelectStarting<ELEMENT> innerJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityDef, block);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> leftJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityDef, block);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> groupBy(PropertyDef<?>... propertyDefs) {
    Objects.requireNonNull(propertyDefs);
    declaration.groupBy(propertyDefs);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.having(block);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> orderBy(Consumer<OrderByDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.orderBy(block);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> limit(Integer limit) {
    declaration.limit(limit);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> offset(Integer offset) {
    declaration.offset(offset);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> forUpdate() {
    declaration.forUpdate(ForUpdateOption.WAIT);
    return this;
  }

  public NativeSqlSelectStarting<ELEMENT> forUpdate(ForUpdateOption option) {
    declaration.forUpdate(option);
    return this;
  }

  public <RESULT> NativeSqlSelectIntermediate<RESULT> select(PropertyDef<?>... propertyDefs) {
    declaration.select(propertyDefs);
    return new NativeSqlSelectIntermediate<>(declaration);
  }

  @Override
  protected Command<List<ELEMENT>> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    NativeSqlSelectTerminate<ELEMENT> statement = new NativeSqlSelectTerminate<>(declaration);
    return statement.createCommand(config, commenter, sqlLogType);
  }
}
