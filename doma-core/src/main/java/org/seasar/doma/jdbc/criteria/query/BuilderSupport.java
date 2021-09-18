package org.seasar.doma.jdbc.criteria.query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.DomaException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.ConvertToLogFormatFunction;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.expression.AggregateFunction;
import org.seasar.doma.jdbc.criteria.expression.ArithmeticExpression;
import org.seasar.doma.jdbc.criteria.expression.CaseExpression;
import org.seasar.doma.jdbc.criteria.expression.LiteralExpression;
import org.seasar.doma.jdbc.criteria.expression.SelectExpression;
import org.seasar.doma.jdbc.criteria.expression.StringExpression;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.LikeOption;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

public class BuilderSupport {
  private final Config config;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;
  private final AliasManager aliasManager;
  private final Operand.Visitor<Void> operandVisitor;
  private final PropertyMetamodelVisitor propertyMetamodelVisitor;

  public BuilderSupport(
      Config config,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    this.config = Objects.requireNonNull(config);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
    this.aliasManager = Objects.requireNonNull(aliasManager);
    this.operandVisitor = new OperandVisitor();
    this.propertyMetamodelVisitor = new PropertyMetamodelVisitor();
  }

  public void table(EntityMetamodel<?> entityMetamodel) {
    EntityType<?> entityType = entityMetamodel.asType();
    buf.appendSql(
        entityType.getQualifiedTableName(
            config.getNaming()::apply, config.getDialect()::applyQuote));
    buf.appendSql(" ");
    String alias = getAlias(entityMetamodel);
    buf.appendSql(alias);
  }

  public void alias(EntityMetamodel<?> entityMetamodel) {
    String alias = getAlias(entityMetamodel);
    buf.appendSql(alias);
  }

  public String getAlias(EntityMetamodel<?> entityMetamodel) {
    EntityType<?> entityType = entityMetamodel.asType();
    String alias = aliasManager.getAlias(entityMetamodel);
    if (alias == null) {
      throw new DomaException(Message.DOMA6003, entityType.getName());
    }
    return alias;
  }

  public void operand(Operand operand) {
    operand.accept(operandVisitor);
  }

  public void column(Operand.Prop prop) {
    column(prop.value);
  }

  public void column(PropertyMetamodel<?> propertyMetamodel) {
    propertyMetamodel.accept(propertyMetamodelVisitor);
  }

  public void columnWithoutAlias(Operand.Prop prop) {
    columnWithoutAlias(prop.value);
  }

  public void columnWithoutAlias(PropertyMetamodel<?> propertyMetamodel) {
    propertyMetamodel.accept(
        new PropertyMetamodelVisitor() {
          @Override
          protected Optional<String> getAlias(PropertyMetamodel<?> propertyMetamodel) {
            return Optional.empty();
          }
        });
  }

  public void param(Operand.Param param) {
    InParameter<?> parameter = param.createInParameter(config);
    param(parameter);
  }

  private void param(InParameter<?> parameter) {
    buf.appendParameter(parameter);
  }

  public void visitCriterion(int index, Criterion criterion) {
    criterion.accept(new CriterionVisitor(index));
  }

  private class OperandVisitor implements Operand.Visitor<Void> {
    @Override
    public Void visit(Operand.Param param) {
      param(param);
      return null;
    }

    @Override
    public Void visit(Operand.Prop prop) {
      column(prop);
      return null;
    }
  }

  private class CriterionVisitor implements Criterion.Visitor {
    private final int index;

    public CriterionVisitor(int index) {
      this.index = index;
    }

    @Override
    public void visit(Criterion.Eq c) {
      comparison(c.left, c.right, "=");
    }

    @Override
    public void visit(Criterion.Ne c) {
      comparison(c.left, c.right, "<>");
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

    private void comparison(Operand.Prop left, Operand right, String op) {
      column(left);
      buf.appendSql(" " + op + " ");
      right.accept(operandVisitor);
    }

    private void isNull(Operand.Prop prop) {
      column(prop);
      buf.appendSql(" is null");
    }

    private void isNotNull(Operand.Prop prop) {
      column(prop);
      buf.appendSql(" is not null");
    }

    private void like(Operand.Prop left, CharSequence right, LikeOption option, boolean not) {
      column(left);
      if (not) {
        buf.appendSql(" not");
      }
      buf.appendSql(" like ");
      String value = right == null ? null : right.toString();
      ExpressionFunctions functions = config.getDialect().getExpressionFunctions();
      option.accept(
          new LikeOption.Visitor() {
            @Override
            public void visit(LikeOption.None none) {
              addParam(value);
            }

            @Override
            public void visit(LikeOption.Escape escape) {
              appendNewValue(functions::escape, escape.escapeChar);
            }

            @Override
            public void visit(LikeOption.Prefix prefix) {
              appendNewValue(functions::prefix, prefix.escapeChar);
            }

            @Override
            public void visit(LikeOption.Infix infix) {
              appendNewValue(functions::infix, infix.escapeChar);
            }

            @Override
            public void visit(LikeOption.Suffix suffix) {
              appendNewValue(functions::suffix, suffix.escapeChar);
            }

            private void appendNewValue(
                BiFunction<String, Character, String> function, char escapeChar) {
              String newValue = function.apply(value, escapeChar);
              addParam(newValue);
              buf.appendSql(" escape '" + escapeChar + "'");
            }

            private void addParam(String value) {
              param(new BasicInParameter<>(() -> new StringWrapper(value)));
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
  }

  class PropertyMetamodelVisitor
      implements PropertyMetamodel.Visitor,
          ArithmeticExpression.Visitor,
          StringExpression.Visitor,
          LiteralExpression.Visitor,
          AggregateFunction.Visitor,
          CaseExpression.Visitor,
          SelectExpression.Visitor {

    @Override
    public void visit(PropertyMetamodel<?> propertyMetamodel) {
      Optional<String> alias = getAlias(propertyMetamodel);
      alias.ifPresent(
          it -> {
            buf.appendSql(it);
            buf.appendSql(".");
          });
      EntityPropertyType<?, ?> propertyType = propertyMetamodel.asType();
      buf.appendSql(
          propertyType.getColumnName(config.getNaming()::apply, config.getDialect()::applyQuote));
    }

    protected Optional<String> getAlias(PropertyMetamodel<?> propertyMetamodel) {
      String alias = aliasManager.getAlias(propertyMetamodel);
      if (alias == null) {
        throw new DomaException(Message.DOMA6004, propertyMetamodel.getName());
      }
      return Optional.of(alias);
    }

    @Override
    public void visit(ArithmeticExpression.Add<?> add) {
      binaryOperator(add.getName(), add.left, add.right);
    }

    @Override
    public void visit(ArithmeticExpression.Sub<?> sub) {
      binaryOperator(sub.getName(), sub.left, sub.right);
    }

    @Override
    public void visit(ArithmeticExpression.Mul<?> mul) {
      binaryOperator(mul.getName(), mul.left, mul.right);
    }

    @Override
    public void visit(ArithmeticExpression.Div<?> div) {
      binaryOperator(div.getName(), div.left, div.right);
    }

    @Override
    public void visit(ArithmeticExpression.Mod<?> mod) {
      if (config.getDialect().supportsModOperator()) {
        binaryOperator(mod.getName(), mod.left, mod.right);
      } else {
        buf.appendSql("mod(");
        mod.left.accept(operandVisitor);
        buf.appendSql(", ");
        mod.right.accept(operandVisitor);
        buf.appendSql(")");
      }
    }

    @Override
    public void visit(StringExpression.Concat<?> concat) {
      CriteriaBuilder criteriaBuilder = config.getDialect().getCriteriaBuilder();
      criteriaBuilder.concat(
          buf,
          () -> concat.first.accept(operandVisitor),
          () -> concat.second.accept(operandVisitor));
    }

    @Override
    public void visit(StringExpression.Lower<?> lower) {
      oneArgumentFunction(lower.getName(), lower.argument);
    }

    @Override
    public void visit(StringExpression.Ltrim<?> ltrim) {
      oneArgumentFunction(ltrim.getName(), ltrim.argument);
    }

    @Override
    public void visit(StringExpression.Rtrim<?> rtrim) {
      oneArgumentFunction(rtrim.getName(), rtrim.argument);
    }

    @Override
    public void visit(StringExpression.Trim<?> trim) {
      oneArgumentFunction(trim.getName(), trim.argument);
    }

    @Override
    public void visit(StringExpression.Upper<?> upper) {
      oneArgumentFunction(upper.getName(), upper.argument);
    }

    @Override
    public void visit(LiteralExpression<?> expression) {
      Wrapper<?> wrapper = expression.asType().createProperty().getWrapper();
      String text =
          wrapper.accept(
              config.getDialect().getSqlLogFormattingVisitor(),
              new ConvertToLogFormatFunction(),
              null);
      buf.appendSql(text);
    }

    @Override
    public void visit(AggregateFunction.Avg<?> avg) {
      oneArgumentFunction(avg.getName(), avg.argument());
    }

    @Override
    public void visit(AggregateFunction.AvgAsDouble avg) {
      oneArgumentFunction(avg.getName(), avg.argument());
    }

    @Override
    public void visit(AggregateFunction.Count count) {
      buf.appendSql(count.getName());
      buf.appendSql("(");
      if (count.distinct) {
        buf.appendSql("distinct ");
      }
      count.argument().accept(this);
      buf.appendSql(")");
    }

    @Override
    public void visit(AggregateFunction.Max<?> max) {
      oneArgumentFunction(max.getName(), max.argument());
    }

    @Override
    public void visit(AggregateFunction.Min<?> min) {
      oneArgumentFunction(min.getName(), min.argument());
    }

    @Override
    public void visit(AggregateFunction.Sum<?> sum) {
      oneArgumentFunction(sum.getName(), sum.argument());
    }

    @Override
    public void visit(AggregateFunction.Asterisk asterisk) {
      buf.appendSql(asterisk.getName());
    }

    @Override
    public void visit(CaseExpression<?> expression) {
      if (expression.criterionList.isEmpty()) {
        expression.otherwise.accept(this);
      } else {
        buf.appendSql("case");
        expression.criterionList.forEach(
            pair -> {
              buf.appendSql(" when ");
              Criterion criterion = pair.fst;
              Operand operand = pair.snd;
              criterion.accept(new CriterionVisitor(0));
              buf.appendSql(" then ");
              operand.accept(operandVisitor);
            });
        buf.appendSql(" else ");
        expression.otherwise.accept(this);
        buf.appendSql(" end");
      }
    }

    @Override
    public void visit(SelectExpression<?> expression) {
      buf.appendSql("(");
      AliasManager child = new AliasManager(expression.context, aliasManager);
      SelectBuilder builder = new SelectBuilder(config, expression.context, commenter, buf, child);
      builder.interpret();
      buf.appendSql(")");
    }

    private void binaryOperator(String operator, Operand left, Operand right) {
      buf.appendSql("(");
      left.accept(operandVisitor);
      buf.appendSql(" " + operator + " ");
      right.accept(operandVisitor);
      buf.appendSql(")");
    }

    private void oneArgumentFunction(String name, PropertyMetamodel<?> argument) {
      buf.appendSql(name);
      buf.appendSql("(");
      argument.accept(this);
      buf.appendSql(")");
    }
  }
}
