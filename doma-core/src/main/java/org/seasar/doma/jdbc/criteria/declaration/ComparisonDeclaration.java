package org.seasar.doma.jdbc.criteria.declaration;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public abstract class ComparisonDeclaration<CONTEXT extends Context> {

  protected final CONTEXT context;

  protected ComparisonDeclaration(CONTEXT context) {
    this.context = Objects.requireNonNull(context);
  }

  public <PROPERTY> void eq(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Eq(new Operand.Prop(left), new Operand.Param(left, right)));
  }

  public <PROPERTY> void eq(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Eq(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void ne(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Ne(new Operand.Prop(left), new Operand.Param(left, right)));
  }

  public <PROPERTY> void ne(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ne(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void gt(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Gt(new Operand.Prop(left), new Operand.Param(left, right)));
  }

  public <PROPERTY> void gt(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Gt(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void ge(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Ge(new Operand.Prop(left), new Operand.Param(left, right)));
  }

  public <PROPERTY> void ge(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ge(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void lt(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Lt(new Operand.Prop(left), new Operand.Param(left, right)));
  }

  public <PROPERTY> void lt(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Lt(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void le(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Le(new Operand.Prop(left), new Operand.Param(left, right)));
  }

  public <PROPERTY> void le(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Le(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public void and(Runnable block) {
    runBlock(block, Criterion.And::new);
  }

  public void or(Runnable block) {
    runBlock(block, Criterion.Or::new);
  }

  public void not(Runnable block) {
    runBlock(block, Criterion.Not::new);
  }

  protected abstract void runBlock(
      Runnable block, Function<List<Criterion>, Criterion> newCriterion);

  protected abstract void add(Criterion criterion);
}