package org.seasar.doma.jdbc.criteria.statement;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.GenerationType;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
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
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.query.QueryOperand;
import org.seasar.doma.jdbc.query.QueryOperandPair;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssemblerContextBuilder;

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

    if (config.getDialect().supportsUpsertEmulationWithMergeStatement()
        && isIdentityKeyIncludedInDuplicateKeys(context)) {
      // fallback to INSERT statement from MERGE statement
      NativeSqlInsertTerminal terminal = new NativeSqlInsertTerminal(config, declaration);
      return terminal.createCommand();
    }

    InsertSettings settings = context.getSettings();
    PreparedSql sql = createPreparedSql(settings, context);
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    return new InsertCommand(query);
  }

  static boolean isIdentityKeyIncludedInDuplicateKeys(InsertContext context) {
    GeneratedIdPropertyType<?, ?, ?> generatedIdPropertyType =
        context.entityMetamodel.asType().getGeneratedIdPropertyType();
    if (generatedIdPropertyType == null) {
      return false;
    }
    if (generatedIdPropertyType.getGenerationType() != GenerationType.IDENTITY) {
      return false;
    }
    List<PropertyMetamodel<?>> keys = context.onDuplicateContext.keys;
    if (keys.isEmpty()) {
      return true;
    }
    return keys.stream().anyMatch(p -> p.getName().equals(generatedIdPropertyType.getName()));
  }

  private PreparedSql createPreparedSql(InsertSettings settings, InsertContext context) {
    List<EntityPropertyType<?, ?>> keys = prepareKeys(context.onDuplicateContext.keys);
    List<QueryOperandPair> insertValues = prepareInsertValues(context.values);
    List<QueryOperandPair> setValues = prepareSetValues(context.onDuplicateContext.setValues);

    PreparedSqlBuilder sqlBuilder = assembleQuery(settings, context, keys, insertValues, setValues);
    return sqlBuilder.build(createCommenter(settings.getComment()));
  }

  private List<EntityPropertyType<?, ?>> prepareKeys(
      List<PropertyMetamodel<?>> propertyMetamodels) {
    return propertyMetamodels.stream().map(PropertyMetamodel::asType).collect(toList());
  }

  private List<QueryOperandPair> prepareInsertValues(
      Map<Operand.Prop, Operand.Param> insertContextValue) {
    List<QueryOperandPair> list = new ArrayList<>(insertContextValue.size());
    for (Map.Entry<Operand.Prop, Operand.Param> entry : insertContextValue.entrySet()) {
      Operand.Prop prop = entry.getKey();
      Operand.Param param = entry.getValue();
      QueryOperand left = new QueryOperand.Prop(prop.getPropertyMetamodel().asType());
      QueryOperand right =
          new QueryOperand.Param(
              param.getPropertyMetamodel().asType(), param.createInParameter(config));
      QueryOperandPair pair = new QueryOperandPair(left, right);
      list.add(pair);
    }
    return list;
  }

  private List<QueryOperandPair> prepareSetValues(Map<Operand.Prop, Operand> upsertSetValues) {
    List<QueryOperandPair> list = new ArrayList<>(upsertSetValues.size());
    for (Map.Entry<Operand.Prop, Operand> upsertSetValue : upsertSetValues.entrySet()) {
      Operand.Prop prop = upsertSetValue.getKey();
      Operand operand = upsertSetValue.getValue();
      QueryOperand left = new QueryOperand.Prop(prop.getPropertyMetamodel().asType());
      QueryOperand right = operand.accept(new OperandVisitor());
      QueryOperandPair pair = new QueryOperandPair(left, right);
      list.add(pair);
    }
    return list;
  }

  private PreparedSqlBuilder assembleQuery(
      InsertSettings settings,
      InsertContext context,
      List<EntityPropertyType<?, ?>> keys,
      List<QueryOperandPair> insertValues,
      List<QueryOperandPair> setValues) {
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

  /** A visitor for converting {@link Operand} to {@link QueryOperand}. */
  private class OperandVisitor implements Operand.Visitor<QueryOperand> {
    @Override
    public QueryOperand visit(Operand.Param param) {
      return new QueryOperand.Param(
          param.getPropertyMetamodel().asType(), param.createInParameter(config));
    }

    @Override
    public QueryOperand visit(Operand.Prop prop) {
      return new QueryOperand.Prop(prop.value.asType());
    }
  }
}
