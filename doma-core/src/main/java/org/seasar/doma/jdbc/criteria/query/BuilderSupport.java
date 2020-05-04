package org.seasar.doma.jdbc.criteria.query;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.DomaException;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.Tuple2;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.AggregateFunction;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;

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
    Objects.requireNonNull(config);
    Objects.requireNonNull(commenter);
    Objects.requireNonNull(buf);
    this.config = config;
    this.commenter = commenter;
    this.buf = buf;
    this.aliasManager = aliasManager;
  }

  public void table(EntityDef<?> entityDef) {
    EntityType<?> entityType = entityDef.asType();
    buf.appendSql(
        entityType.getQualifiedTableName(
            config.getNaming()::apply, config.getDialect()::applyQuote));
    if (aliasManager != null) {
      buf.appendSql(" ");
      String alias = aliasManager.getAlias(entityDef);
      if (alias == null) {
        throw new DomaException(Message.DOMA6003, entityType.getName());
      }
      buf.appendSql(alias);
    }
  }

  public void column(Operand.Prop prop) {
    column(prop.value);
  }

  public void column(PropertyDef<?> propertyDef) {
    Consumer<PropertyDef<?>> appendColumn =
        (p) -> {
          if (p == AggregateFunction.Asterisk) {
            buf.appendSql(AggregateFunction.Asterisk.getName());
          } else {
            if (aliasManager != null) {
              String alias = aliasManager.getAlias(p);
              if (alias == null) {
                throw new DomaException(Message.DOMA6004, p.getName());
              }
              buf.appendSql(alias);
              buf.appendSql(".");
            }
            EntityPropertyType<?, ?> propertyType = p.asType();
            buf.appendSql(
                propertyType.getColumnName(
                    config.getNaming()::apply, config.getDialect()::applyQuote));
          }
        };

    if (propertyDef instanceof AggregateFunction) {
      AggregateFunction<?> function = (AggregateFunction<?>) propertyDef;
      buf.appendSql(function.getName());
      buf.appendSql("(");
      appendColumn.accept(function.argument());
      buf.appendSql(")");
    } else {
      appendColumn.accept(propertyDef);
    }
  }

  public void param(Operand.Param param) {
    InParameter<?> parameter = param.value.apply(config.getClassHelper());
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
            InParameter<?> parameter = operand.value.apply(config.getClassHelper());
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

  private void like(Operand.Prop left, Operand right, boolean not) {
    column(left);
    if (not) {
      buf.appendSql(" not");
    }
    buf.appendSql(" like ");
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
    column(left.first());
    buf.appendSql(", ");
    column(left.second());
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
            param(pair.first());
            buf.appendSql(", ");
            param(pair.second());
            buf.appendSql("), ");
          });
      buf.cutBackSql(2);
    }
    buf.appendSql(")");
  }

  private void inPairSubQuery(
      Tuple2<Operand.Prop, Operand.Prop> left, SelectContext right, boolean not) {
    buf.appendSql("(");
    column(left.first());
    buf.appendSql(", ");
    column(left.second());
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
      like(c.left, c.right, false);
    }

    @Override
    public void visit(Criterion.NotLike c) {
      like(c.left, c.right, true);
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
