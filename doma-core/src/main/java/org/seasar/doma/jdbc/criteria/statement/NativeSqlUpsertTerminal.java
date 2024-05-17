package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
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
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssemblerContextBuilder;
import org.seasar.doma.jdbc.query.UpsertSetValue;

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
    List<EntityPropertyType<?, ?>> keys = toEntityPropertyTypes(context.onDuplicateContext.keys);
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        insertValues(context.values);
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        setValues(context.onDuplicateContext.setValues);

    PreparedSqlBuilder sql = assembleQuery(settings, context, keys, insertValues, setValues);
    return sql.build(createCommenter(settings.getComment()));
  }

  private List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues(
      Map<Operand.Prop, Operand.Param> insertContextValue) {
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> list = new ArrayList<>();
    for (Map.Entry<Operand.Prop, Operand.Param> entry : insertContextValue.entrySet()) {
      Operand.Prop prop = entry.getKey();
      Operand.Param param = entry.getValue();
      list.add(new Tuple2<>(prop.value.asType(), param.createInParameter(config)));
    }
    return list;
  }

  private List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues(
      Map<Operand.Prop, Operand> upsertSetValues) {
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> list = new ArrayList<>();
    for (Map.Entry<Operand.Prop, Operand> upsertSetValue : upsertSetValues.entrySet()) {
      Operand.Prop prop = upsertSetValue.getKey();
      Operand operand = upsertSetValue.getValue();
      UpsertSetValue jdbcQueryUpsertSetValue = operand.accept(new OperandVisitor());
      list.add(new Tuple2<>(prop.value.asType(), jdbcQueryUpsertSetValue));
    }
    return list;
  }

  private static List<EntityPropertyType<?, ?>> toEntityPropertyTypes(
      List<PropertyMetamodel<?>> propertyMetamodels) {
    return propertyMetamodels.stream().map(PropertyMetamodel::asType).collect(toList());
  }

  private PreparedSqlBuilder assembleQuery(
      InsertSettings settings,
      InsertContext context,
      List<EntityPropertyType<?, ?>> keys,
      List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues,
      List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {
    PreparedSqlBuilder sql =
        new PreparedSqlBuilder(config, SqlKind.INSERT, settings.getSqlLogType());
    UpsertAssemblerContext upsertAssemblerContext =
        UpsertAssemblerContextBuilder.build(
            sql,
            context.entityMetamodel.asType(),
            context.onDuplicateContext.duplicateKeyType,
            config.getNaming(),
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    UpsertAssembler upsertQuery = config.getDialect().getUpsertAssembler(upsertAssemblerContext);
    upsertQuery.assemble();
    return sql;
  }

  /** A visitor for converting {@link Operand} to {@link UpsertSetValue}. */
  private class OperandVisitor implements Operand.Visitor<UpsertSetValue> {
    @Override
    public UpsertSetValue visit(Operand.Param param) {
      return new UpsertSetValue.Param(param.createInParameter(config));
    }

    @Override
    public UpsertSetValue visit(Operand.Prop prop) {
      return new UpsertSetValue.Prop(prop.value.asType());
    }
  }
}
