package org.seasar.doma.jdbc.criteria.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public class CaseExpression<RESULT> implements PropertyMetamodel<RESULT> {

  public final PropertyMetamodel<RESULT> otherwise;
  public final List<Pair<Criterion, Operand>> criterionList = new ArrayList<>();

  public CaseExpression(PropertyMetamodel<RESULT> otherwise) {
    this.otherwise = Objects.requireNonNull(otherwise);
  }

  @Override
  public Class<?> asClass() {
    return otherwise.asClass();
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return otherwise.asType();
  }

  @Override
  public String getName() {
    return otherwise.getName();
  }

  @Override
  public void accept(PropertyMetamodel.Visitor visitor) {
    if (visitor instanceof CaseExpression.Visitor) {
      CaseExpression.Visitor v = (CaseExpression.Visitor) visitor;
      v.visit(this);
    }
  }

  public class Declaration {

    public <PROPERTY> void eq(
        PropertyMetamodel<PROPERTY> left,
        PropertyMetamodel<PROPERTY> right,
        PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(right, "right");
      Objects.requireNonNull(then, "then");
      add(Criterion.Eq::new, left, right, then);
    }

    public <PROPERTY> void eq(
        PropertyMetamodel<PROPERTY> left, PROPERTY right, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(then, "then");
      if (right != null) {
        add(Criterion.Eq::new, left, right, then);
      }
    }

    public <PROPERTY> void ne(
        PropertyMetamodel<PROPERTY> left,
        PropertyMetamodel<PROPERTY> right,
        PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(right, "right");
      Objects.requireNonNull(then, "then");
      add(Criterion.Ne::new, left, right, then);
    }

    public <PROPERTY> void ne(
        PropertyMetamodel<PROPERTY> left, PROPERTY right, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(then, "then");
      if (right != null) {
        add(Criterion.Ne::new, left, right, then);
      }
    }

    public <PROPERTY> void ge(
        PropertyMetamodel<PROPERTY> left,
        PropertyMetamodel<PROPERTY> right,
        PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(right, "right");
      Objects.requireNonNull(then, "then");
      add(Criterion.Ge::new, left, right, then);
    }

    public <PROPERTY> void ge(
        PropertyMetamodel<PROPERTY> left, PROPERTY right, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(then, "then");
      if (right != null) {
        add(Criterion.Ge::new, left, right, then);
      }
    }

    public <PROPERTY> void gt(
        PropertyMetamodel<PROPERTY> left,
        PropertyMetamodel<PROPERTY> right,
        PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(right, "right");
      Objects.requireNonNull(then, "then");
      add(Criterion.Gt::new, left, right, then);
    }

    public <PROPERTY> void gt(
        PropertyMetamodel<PROPERTY> left, PROPERTY right, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(then, "then");
      if (right != null) {
        add(Criterion.Gt::new, left, right, then);
      }
    }

    public <PROPERTY> void le(
        PropertyMetamodel<PROPERTY> left,
        PropertyMetamodel<PROPERTY> right,
        PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(right, "right");
      Objects.requireNonNull(then, "then");
      add(Criterion.Le::new, left, right, then);
    }

    public <PROPERTY> void le(
        PropertyMetamodel<PROPERTY> left, PROPERTY right, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(then, "then");
      if (right != null) {
        add(Criterion.Le::new, left, right, then);
      }
    }

    public <PROPERTY> void lt(
        PropertyMetamodel<PROPERTY> left,
        PropertyMetamodel<PROPERTY> right,
        PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(right, "right");
      Objects.requireNonNull(then, "then");
      add(Criterion.Lt::new, left, right, then);
    }

    public <PROPERTY> void lt(
        PropertyMetamodel<PROPERTY> left, PROPERTY right, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(left, "left");
      Objects.requireNonNull(then, "then");
      if (right != null) {
        add(Criterion.Lt::new, left, right, then);
      }
    }

    public <PROPERTY> void isNull(
        PropertyMetamodel<PROPERTY> propertyMetamodel, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(propertyMetamodel, "propertyMetamodel");
      Objects.requireNonNull(then, "then");
      add(Criterion.IsNull::new, propertyMetamodel, then);
    }

    public <PROPERTY> void isNotNull(
        PropertyMetamodel<PROPERTY> propertyMetamodel, PropertyMetamodel<RESULT> then) {
      Objects.requireNonNull(propertyMetamodel, "propertyMetamodel");
      Objects.requireNonNull(then, "then");
      add(Criterion.IsNotNull::new, propertyMetamodel, then);
    }

    private <PROPERTY> void add(
        BiFunction<Operand.Prop, Operand.Prop, Criterion> operator,
        PropertyMetamodel<PROPERTY> left,
        PropertyMetamodel<PROPERTY> right,
        PropertyMetamodel<RESULT> then) {
      Criterion criterion = operator.apply(new Operand.Prop(left), new Operand.Prop(right));
      criterionList.add(new Pair<>(criterion, new Operand.Prop(then)));
    }

    private <PROPERTY> void add(
        BiFunction<Operand.Prop, Operand.Param, Criterion> operator,
        PropertyMetamodel<PROPERTY> left,
        PROPERTY right,
        PropertyMetamodel<RESULT> then) {
      Criterion criterion = operator.apply(new Operand.Prop(left), new Operand.Param(left, right));
      criterionList.add(new Pair<>(criterion, new Operand.Prop(then)));
    }

    private <PROPERTY> void add(
        Function<Operand.Prop, Criterion> operator,
        PropertyMetamodel<PROPERTY> propertyMetamodel,
        PropertyMetamodel<RESULT> then) {
      Criterion criterion = operator.apply(new Operand.Prop(propertyMetamodel));
      criterionList.add(new Pair<>(criterion, new Operand.Prop(then)));
    }
  }

  public interface Visitor {
    void visit(CaseExpression<?> expression);
  }
}
