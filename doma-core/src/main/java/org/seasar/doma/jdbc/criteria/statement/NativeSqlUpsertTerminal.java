package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.query.UpsertBuilder;
import org.seasar.doma.jdbc.query.UpsertContext;

public class NativeSqlUpsertTerminal extends AbstractStatement<NativeSqlUpsertTerminal, Integer> {

  private final InsertDeclaration declaration;

  public NativeSqlUpsertTerminal(Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  /**
   * {@inheritDoc}
   *
   * @throws org.seasar.doma.jdbc.UniqueConstraintException if an unique constraint is violated
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public Integer execute() {
    return super.execute();
  }

  @Override
  protected Command<Integer> createCommand() {
    InsertContext context = declaration.getContext();
    InsertSettings settings = context.getSettings();
    PreparedSql sql = getPreparedSql(settings, context);
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    return new InsertCommand(query);
  }

  private PreparedSql getPreparedSql(InsertSettings settings, InsertContext context) {
    List<EntityPropertyType<?, ?>> insertPropertyTypes =
        toEntityPropertyTypes(context.entityMetamodel.allPropertyMetamodels());
    List<EntityPropertyType<?, ?>> keysPropertyTypes = toEntityPropertyTypes(context.upsertKeys);
    List<EntityPropertyType<?, ?>> updatePropertyTypes =
        toEntityPropertyTypes(context.upsertSetPropertyMetamodels);

    Map<EntityPropertyType<?, ?>, InParameter<?>> propertyValuePairs =
        toPropertyValuePairs(context.values);

    PreparedSqlBuilder sql =
        buildQuery(
            settings,
            context,
            keysPropertyTypes,
            insertPropertyTypes,
            updatePropertyTypes,
            propertyValuePairs);

    return sql.build(createCommenter(settings.getComment()));
  }

  private static List<EntityPropertyType<?, ?>> toEntityPropertyTypes(
      List<PropertyMetamodel<?>> propertyMetamodels) {
    return propertyMetamodels.stream().map(PropertyMetamodel::asType).collect(toList());
  }

  private PreparedSqlBuilder buildQuery(
      InsertSettings settings,
      InsertContext context,
      List<EntityPropertyType<?, ?>> keys,
      List<EntityPropertyType<?, ?>> insertPropertyTypes,
      List<EntityPropertyType<?, ?>> updatePropertyTypes,
      Map<EntityPropertyType<?, ?>, InParameter<?>> propertyValuePairs) {
    PreparedSqlBuilder sql =
        new PreparedSqlBuilder(config, SqlKind.UPSERT, settings.getSqlLogType());
    UpsertContext upsertContext =
        new UpsertContext(
            sql,
            context.entityMetamodel.asType(),
            context.duplicateKeyType.get(),
            config.getNaming(),
            config.getDialect(),
            keys,
            insertPropertyTypes,
            updatePropertyTypes,
            propertyValuePairs);
    UpsertBuilder upsertQuery = config.getDialect().getUpsertBuilder(upsertContext);
    upsertQuery.build();
    return sql;
  }

  private Map<EntityPropertyType<?, ?>, InParameter<?>> toPropertyValuePairs(
      Map<Operand.Prop, Operand.Param> insertContextValue) {
    Map<EntityPropertyType<?, ?>, InParameter<?>> map = new HashMap<>();
    for (Map.Entry<Operand.Prop, Operand.Param> entry : insertContextValue.entrySet()) {
      Operand.Prop prop = entry.getKey();
      Operand.Param param = entry.getValue();
      map.put(prop.value.asType(), param.createInParameter(config));
    }
    return map;
  }
}
