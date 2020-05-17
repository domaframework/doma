package org.seasar.doma.jdbc.criteria.declaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public abstract class ComparisonDeclaration {

  private final Supplier<List<Criterion>> getter;
  private final Consumer<List<Criterion>> setter;

  protected ComparisonDeclaration(
      Supplier<List<Criterion>> getter, Consumer<List<Criterion>> setter) {
    this.getter = Objects.requireNonNull(getter);
    this.setter = Objects.requireNonNull(setter);
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
    Objects.requireNonNull(block);
    runBlock(block, Criterion.And::new);
  }

  public void or(Runnable block) {
    Objects.requireNonNull(block);
    runBlock(block, Criterion.Or::new);
  }

  public void not(Runnable block) {
    Objects.requireNonNull(block);
    runBlock(block, Criterion.Not::new);
  }

  private void runBlock(Runnable block, Function<List<Criterion>, Criterion> factory) {
    List<Criterion> preservedList = getter.get();
    List<Criterion> newList = new ArrayList<>();
    setter.accept(newList);
    block.run();
    setter.accept(preservedList);
    if (!newList.isEmpty()) {
      add(factory.apply(newList));
    }
  }

  protected void add(Criterion criterion) {
    getter.get().add(criterion);
  }
}
