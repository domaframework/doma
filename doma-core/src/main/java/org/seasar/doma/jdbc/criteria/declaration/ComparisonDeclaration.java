package org.seasar.doma.jdbc.criteria.declaration;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.context.Criterion;

public abstract class ComparisonDeclaration<CONTEXT extends Context> {

  protected final CONTEXT context;
  protected final DeclarationSupport support;

  protected ComparisonDeclaration(CONTEXT context) {
    Objects.requireNonNull(context);
    this.context = context;
    this.support = new DeclarationSupport();
  }

  public <PROPERTY> void eq(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Eq(support.toProp(left), support.toParam(left, right)));
  }

  public <PROPERTY> void eq(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Eq(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void ne(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Ne(support.toProp(left), support.toParam(left, right)));
  }

  public <PROPERTY> void ne(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ne(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void gt(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Gt(support.toProp(left), support.toParam(left, right)));
  }

  public <PROPERTY> void gt(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Gt(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void ge(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Ge(support.toProp(left), support.toParam(left, right)));
  }

  public <PROPERTY> void ge(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ge(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void lt(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Lt(support.toProp(left), support.toParam(left, right)));
  }

  public <PROPERTY> void lt(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Lt(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void le(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(new Criterion.Le(support.toProp(left), support.toParam(left, right)));
  }

  public <PROPERTY> void le(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Le(support.toProp(left), support.toProp(right)));
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
