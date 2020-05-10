package org.seasar.doma.jdbc.criteria.query;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.DomaException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.AggregateFunction;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.LikeOption;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.StringWrapper;

public class BuilderSupport {
  private final Config config;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;
  private final AliasManager aliasManager;

  public BuilderSupport(
      Config config,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    this.config = Objects.requireNonNull(config);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
    this.aliasManager = Objects.requireNonNull(aliasManager);
  }

  public void table(EntityMetamodel<?> entityMetamodel) {
    EntityType<?> entityType = entityMetamodel.asType();
    buf.appendSql(
        entityType.getQualifiedTableName(
            config.getNaming()::apply, config.getDialect()::applyQuote));
    buf.appendSql(" ");
    String alias = aliasManager.getAlias(entityMetamodel);
    if (alias == null) {
      throw new DomaException(Message.DOMA6003, entityType.getName());
    }
    buf.appendSql(alias);
  }

  public void column(Operand.Prop prop) {
    column(prop.value);
  }

  public void column(PropertyMetamodel<?> propertyMetamodel) {
    Consumer<PropertyMetamodel<?>> appendColumn =
        (p) -> {
          if (p == AggregateFunction.Asterisk) {
            buf.appendSql(AggregateFunction.Asterisk.getName());
          } else {
            String alias = aliasManager.getAlias(p);
            if (alias == null) {
              throw new DomaException(Message.DOMA6004, p.getName());
            }
            buf.appendSql(alias);
            buf.appendSql(".");
            EntityPropertyType<?, ?> propertyType = p.asType();
            buf.appendSql(
                propertyType.getColumnName(
                    config.getNaming()::apply, config.getDialect()::applyQuote));
          }
        };

    if (propertyMetamodel instanceof AggregateFunction) {
      AggregateFunction<?> function = (AggregateFunction<?>) propertyMetamodel;
      buf.appendSql(function.getName());
      buf.appendSql("(");
      appendColumn.accept(function.argument());
      buf.appendSql(")");
    } else {
      appendColumn.accept(propertyMetamodel);
    }
  }

  public void param(Operand.Param param) {
    InParameter<?> parameter = param.createInParameter(config);
    param(parameter);
  }

  private void param(InParameter<?> parameter) {
    buf.appendParameter(parameter);
  }

  public void visitCriterion(int index, Criterion criterion) {
    Criterion.Visitor visitor = new CriterionVisitor(index);
    criterion.accept(visitor);
  }

  private void equality(Operand.Prop left, Operand right, String op) {
    if (isParamNull(right)) {
      if (op.equals("=")) {
        isNull(left);
      } else if (op.equals("<>")) {
        isNotNull(left);
      } else {
        throw new IllegalStateException("The operator is illegal. " + op);
      }
    } else {
      comparison(left, right, op);
    }
  }

  private boolean isParamNull(Operand operand) {
    return operand.accept(
        new Operand.Visitor<Boolean>() {

          @Override
          public Boolean visit(Operand.Param operand) {
            InParameter<?> parameter = operand.createInParameter(config);
            return parameter.getWrapper().get() == null;
          }

          @Override
          public Boolean visit(Operand.Prop operand) {
            return false;
          }
        });
  }

  private void comparison(Operand.Prop left, Operand right, String op) {
    column(left);
    buf.appendSql(" " + op + " ");
    right.accept(
        new Operand.Visitor<Void>() {
          @Override
          public Void visit(Operand.Param operand) {
            param(operand);
            return null;
          }

          @Override
          public Void visit(Operand.Prop operand) {
            column(operand);
            return null;
          }
        });
  }

  private void isNull(Operand.Prop prop) {
    column(prop);
    buf.appendSql(" is null");
  }

  private void isNotNull(Operand.Prop prop) {
    column(prop);
    buf.appendSql(" is not null");
  }

  private void like(Operand.Prop left, Operand right, LikeOption option, boolean not) {
    column(left);
    if (not) {
      buf.appendSql(" not");
    }
    buf.appendSql(" like ");
    right.accept(
        new Operand.Visitor<Void>() {
          @Override
          public Void visit(Operand.Param param) {
            InParameter<?> parameter = param.createInParameter(config);
            Object value = parameter.getWrapper().get();
            if (value == null || option == LikeOption.NONE) {
              param(parameter);
            } else {
              char escapeChar = '$';
              BiFunction<String, Character, String> transformer = getTransformer(option);
              String newValue = transformer.apply(value.toString(), escapeChar);
              param(new BasicInParameter<>(() -> new StringWrapper(newValue)));
              buf.appendSql(" escape '" + escapeChar + "'");
            }
            return null;
          }

          private BiFunction<String, Character, String> getTransformer(LikeOption option) {
            ExpressionFunctions functions = config.getDialect().getExpressionFunctions();
            switch (option) {
              case ESCAPE:
                return functions::escape;
              case PREFIX:
                return functions::prefix;
              case INFIX:
                return functions::infix;
              case SUFFIX:
                return functions::suffix;
              default:
                throw new AssertionError("unreachable. " + option);
            }
          }

          @Override
          public Void visit(Operand.Prop prop) {
            column(prop);
            return null;
          }
        });
  }

  private void between(Operand.Prop prop, Operand.Param begin, Operand.Param end) {
    column(prop);
    buf.appendSql(" between ");
    param(begin);
    buf.appendSql(" and ");
    param(end);
  }

  private void inSingle(Operand.Prop left, List<Operand.Param> right, boolean not) {
    column(left);
    if (not) {
      buf.appendSql(" not");
    }
    buf.appendSql(" in (");
    if (right.isEmpty()) {
      buf.appendSql("null");
    } else {
      for (Operand.Param p : right) {
        param(p);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
    buf.appendSql(")");
  }

  public void inSingleSubQuery(Operand.Prop left, SelectContext right, boolean not) {
    column(left);
    if (not) {
      buf.appendSql(" not");
    }
    buf.appendSql(" in (");
    AliasManager child = new AliasManager(right, aliasManager);
    SelectBuilder builder = new SelectBuilder(config, right, commenter, buf, child);
    builder.interpret();
    buf.appendSql(")");
  }

  private void inPair(
      Tuple2<Operand.Prop, Operand.Prop> left,
      List<Tuple2<Operand.Param, Operand.Param>> right,
      boolean not) {
    buf.appendSql("(");
    column(left.getItem1());
    buf.appendSql(", ");
    column(left.getItem2());
    buf.appendSql(")");
    if (not) {
      buf.appendSql(" not");
    }
    buf.appendSql(" in (");
    if (right.isEmpty()) {
      buf.appendSql("null, null");
    } else {
      right.forEach(
          pair -> {
            buf.appendSql("(");
            param(pair.getItem1());
            buf.appendSql(", ");
            param(pair.getItem2());
            buf.appendSql("), ");
          });
      buf.cutBackSql(2);
    }
    buf.appendSql(")");
  }

  private void inPairSubQuery(
      Tuple2<Operand.Prop, Operand.Prop> left, SelectContext right, boolean not) {
    buf.appendSql("(");
    column(left.getItem1());
    buf.appendSql(", ");
    column(left.getItem2());
    buf.appendSql(")");
    if (not) {
      buf.appendSql(" not");
    }
    buf.appendSql(" in (");
    AliasManager child = new AliasManager(right, aliasManager);
    SelectBuilder builder = new SelectBuilder(config, right, commenter, buf, child);
    builder.interpret();
    buf.appendSql(")");
  }

  public void exists(SelectContext context, boolean not) {
    if (not) {
      buf.appendSql("not ");
    }
    buf.appendSql("exists (");
    AliasManager child = new AliasManager(context, aliasManager);
    SelectBuilder builder = new SelectBuilder(config, context, commenter, buf, child);
    builder.interpret();
    buf.appendSql(")");
  }

  private void and(List<Criterion> criterionList, int index) {
    binaryLogicalOperator(criterionList, index, "and");
  }

  private void or(List<Criterion> criterionList, int index) {
    binaryLogicalOperator(criterionList, index, "or");
  }

  private void binaryLogicalOperator(List<Criterion> criterionList, int index, String operator) {
    if (!criterionList.isEmpty()) {
      if (index > 0) {
        buf.cutBackSql(5);
      }
      if (index != 0) {
        buf.appendSql(" " + operator + " ");
      }
      buf.appendSql("(");
      int i = 0;
      for (Criterion c : criterionList) {
        visitCriterion(i++, c);
        buf.appendSql(" and ");
      }
      buf.cutBackSql(5);
      buf.appendSql(")");
    }
  }

  private void not(List<Criterion> criterionList) {
    if (!criterionList.isEmpty()) {
      buf.appendSql("not ");
      buf.appendSql("(");
      int index = 0;
      for (Criterion c : criterionList) {
        visitCriterion(index++, c);
        buf.appendSql(" and ");
      }
      buf.cutBackSql(5);
      buf.appendSql(")");
    }
  }

  private class CriterionVisitor implements Criterion.Visitor {

    private final int index;

    public CriterionVisitor(int index) {
      this.index = index;
    }

    @Override
    public void visit(Criterion.Eq c) {
      equality(c.left, c.right, "=");
    }

    @Override
    public void visit(Criterion.Ne c) {
      equality(c.left, c.right, "<>");
    }

    @Override
    public void visit(Criterion.Gt c) {
      comparison(c.left, c.right, ">");
    }

    @Override
    public void visit(Criterion.Ge c) {
      comparison(c.left, c.right, ">=");
    }

    @Override
    public void visit(Criterion.Lt c) {
      comparison(c.left, c.right, "<");
    }

    @Override
    public void visit(Criterion.Le c) {
      comparison(c.left, c.right, "<=");
    }

    @Override
    public void visit(Criterion.IsNull c) {
      isNull(c.prop);
    }

    @Override
    public void visit(Criterion.IsNotNull c) {
      isNotNull(c.prop);
    }

    @Override
    public void visit(Criterion.Like c) {
      like(c.left, c.right, c.option, false);
    }

    @Override
    public void visit(Criterion.NotLike c) {
      like(c.left, c.right, c.option, true);
    }

    @Override
    public void visit(Criterion.Between c) {
      between(c.prop, c.start, c.end);
    }

    @Override
    public void visit(Criterion.In c) {
      inSingle(c.left, c.right, false);
    }

    @Override
    public void visit(Criterion.NotIn c) {
      inSingle(c.left, c.right, true);
    }

    @Override
    public void visit(Criterion.InSubQuery c) {
      inSingleSubQuery(c.left, c.right, false);
    }

    @Override
    public void visit(Criterion.NotInSubQuery c) {
      inSingleSubQuery(c.left, c.right, true);
    }

    @Override
    public void visit(Criterion.InTuple2 c) {
      inPair(c.left, c.right, false);
    }

    @Override
    public void visit(Criterion.NotInTuple2 c) {
      inPair(c.left, c.right, true);
    }

    @Override
    public void visit(Criterion.InTuple2SubQuery c) {
      inPairSubQuery(c.left, c.right, false);
    }

    @Override
    public void visit(Criterion.NotInTuple2SubQuery c) {
      inPairSubQuery(c.left, c.right, true);
    }

    @Override
    public void visit(Criterion.Exists c) {
      exists(c.context, false);
    }

    @Override
    public void visit(Criterion.NotExists c) {
      exists(c.context, true);
    }

    @Override
    public void visit(Criterion.And criterion) {
      and(criterion.criterionList, index);
    }

    @Override
    public void visit(Criterion.Or criterion) {
      or(criterion.criterionList, index);
    }

    @Override
    public void visit(Criterion.Not criterion) {
      not(criterion.criterionList);
    }
  }
}
