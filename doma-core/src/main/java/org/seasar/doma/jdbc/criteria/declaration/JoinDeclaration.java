package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Join;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class JoinDeclaration {

  private final Join join;

  public JoinDeclaration(Join join) {
    this.join = Objects.requireNonNull(join);
  }

  private void add(Criterion criterion) {
    join.on.add(criterion);
  }

  public <PROPERTY> void eq(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Eq(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void ne(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ne(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void ge(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ge(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void gt(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Gt(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void le(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Le(new Operand.Prop(left), new Operand.Prop(right)));
  }

  public <PROPERTY> void lt(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Lt(new Operand.Prop(left), new Operand.Prop(right)));
  }

  /**
   * Adds a {@code IS NULL} operator.
   *
   * @param propertyMetamodel the operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code propertyMetamodel} is null
   */
  public <PROPERTY> void isNull(PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    add(new Criterion.IsNull(new Operand.Prop(propertyMetamodel)));
  }

  /**
   * Adds a {@code IS NOT NULL} operator.
   *
   * @param propertyMetamodel the operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code propertyMetamodel} is null
   */
  public <PROPERTY> void isNotNull(PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    add(new Criterion.IsNotNull(new Operand.Prop(propertyMetamodel)));
  }
}
