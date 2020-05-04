package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.command.MappedObjectIterationHandler;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SelectBuilder;
import org.seasar.doma.jdbc.entity.EntityType;

public class NativeSqlSelectTerminate<ELEMENT> extends AbstractStatement<List<ELEMENT>>
    implements SelectStatement<ELEMENT> {

  private final SelectFromDeclaration declaration;

  public NativeSqlSelectTerminate(SelectFromDeclaration declaration) {
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  @Override
  protected Command<List<ELEMENT>> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    SelectContext context = declaration.getContext();
    SelectBuilder builder = new SelectBuilder(config, context, commenter, sqlLogType);
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), executeMethodName);
    ResultSetHandler<List<ELEMENT>> handler = createResultSetHandler(context);
    return new SelectCommand<>(query, handler);
  }

  @SuppressWarnings("unchecked")
  private ResultSetHandler<List<ELEMENT>> createResultSetHandler(SelectContext context) {
    if (context.mapper == null) {
      EntityType<ELEMENT> entityType = (EntityType<ELEMENT>) context.entityDef.asType();
      return new EntityResultListHandler<>(entityType);
    } else {
      List<PropertyDef<?>> propertyDefs = context.allPropertyDefs();
      Function<Row, ELEMENT> mapper = (Function<Row, ELEMENT>) context.mapper;
      return new MappedObjectIterationHandler<>(propertyDefs, mapper);
    }
  }
}
