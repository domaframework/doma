package org.seasar.doma.jdbc.criteria.declaration;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.LikeOption;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;

/** The where declaration. */
public class WhereDeclaration extends ComparisonDeclaration {

  public WhereDeclaration(SelectContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(DeleteContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(UpdateContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  /**
   * Adds a {@code LIKE} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @throws NullPointerException if {@code left} is null
   */
  public void like(PropertyMetamodel<?> left, CharSequence right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Like(new Operand.Prop(left), right, LikeOption.none()));
    }
  }

  /**
   * Adds a {@code LIKE} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @param option the option
   * @throws NullPointerException if {@code left} is null
   */
  public void like(PropertyMetamodel<?> left, CharSequence right, LikeOption option) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Like(new Operand.Prop(left), right, option));
    }
  }

  /**
   * Adds a {@code NOT LIKE} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @throws NullPointerException if {@code left} is null
   */
  public void notLike(PropertyMetamodel<?> left, CharSequence right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.NotLike(new Operand.Prop(left), right, LikeOption.none()));
    }
  }

  /**
   * Adds a {@code NOT LIKE} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @param option the option
   * @throws NullPointerException if {@code left} is null
   */
  public void notLike(PropertyMetamodel<?> left, CharSequence right, LikeOption option) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.NotLike(new Operand.Prop(left), right, option));
    }
  }

  /**
   * Adds a {@code BETWEEN} operator.
   *
   * <p>If either of the {@code start} parameter or the {@code end} parameter is null, the query
   * condition doesn't include the operator.
   *
   * @param propertyMetamodel the left hand operand
   * @param start the first argument of the right hand operand
   * @param end the second argument of the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code propertyMetamodel} is null
   */
  public <PROPERTY> void between(
      PropertyMetamodel<PROPERTY> propertyMetamodel, PROPERTY start, PROPERTY end) {
    Objects.requireNonNull(propertyMetamodel);
    if (start != null && end != null) {
      add(
          new Criterion.Between(
              new Operand.Prop(propertyMetamodel),
              new Operand.Param(propertyMetamodel, start),
              new Operand.Param(propertyMetamodel, end)));
    }
  }

  /**
   * Adds a {@code IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void in(PropertyMetamodel<PROPERTY> left, List<PROPERTY> right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(
          new Criterion.In(
              new Operand.Prop(left),
              right.stream().map(p -> new Operand.Param(left, p)).collect(toList())));
    }
  }

  /**
   * Adds a {@code NOT IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void notIn(PropertyMetamodel<PROPERTY> left, List<PROPERTY> right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(
          new Criterion.NotIn(
              new Operand.Prop(left),
              right.stream().map(p -> new Operand.Param(left, p)).collect(toList())));
    }
  }

  /**
   * Adds a {@code IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void in(
      PropertyMetamodel<PROPERTY> left, SubSelectContext<PropertyMetamodel<PROPERTY>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.InSubQuery(new Operand.Prop(left), right.get()));
  }

  /**
   * Adds a {@code NOT IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void notIn(
      PropertyMetamodel<PROPERTY> left, SubSelectContext<PropertyMetamodel<PROPERTY>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.NotInSubQuery(new Operand.Prop(left), right.get()));
  }

  /**
   * Adds a {@code IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @param <PROPERTY1> the first property type
   * @param <PROPERTY2> the second property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY1, PROPERTY2> void in(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      List<Tuple2<PROPERTY1, PROPERTY2>> right) {
    Objects.requireNonNull(left);
    if (right != null) {
      Operand.Prop prop1 = new Operand.Prop(left.getItem1());
      Operand.Prop prop2 = new Operand.Prop(left.getItem2());
      List<Tuple2<Operand.Param, Operand.Param>> params =
          right.stream()
              .map(
                  pair -> {
                    Operand.Param param1 = new Operand.Param(left.getItem1(), pair.getItem1());
                    Operand.Param param2 = new Operand.Param(left.getItem2(), pair.getItem2());
                    return new Tuple2<>(param1, param2);
                  })
              .collect(toList());
      add(new Criterion.InTuple2(new Tuple2<>(prop1, prop2), params));
    }
  }

  /**
   * Adds a {@code NOT IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @param <PROPERTY1> the first property type
   * @param <PROPERTY2> the second property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY1, PROPERTY2> void notIn(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      List<Tuple2<PROPERTY1, PROPERTY2>> right) {
    Objects.requireNonNull(left);
    if (right != null) {
      Operand.Prop prop1 = new Operand.Prop(left.getItem1());
      Operand.Prop prop2 = new Operand.Prop(left.getItem2());
      List<Tuple2<Operand.Param, Operand.Param>> params =
          right.stream()
              .map(
                  pair -> {
                    Operand.Param param1 = new Operand.Param(left.getItem1(), pair.getItem1());
                    Operand.Param param2 = new Operand.Param(left.getItem2(), pair.getItem2());
                    return new Tuple2<>(param1, param2);
                  })
              .collect(toList());
      add(new Criterion.NotInTuple2(new Tuple2<>(prop1, prop2), params));
    }
  }

  /**
   * Adds a {@code IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY1> the first property type
   * @param <PROPERTY2> the second property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY1, PROPERTY2> void in(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    add(new Criterion.InTuple2SubQuery(new Tuple2<>(prop1, prop2), right.get()));
  }

  /**
   * Adds a {@code NOT IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY1> the first property type
   * @param <PROPERTY2> the second property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY1, PROPERTY2> void notIn(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    add(new Criterion.NotInTuple2SubQuery(new Tuple2<>(prop1, prop2), right.get()));
  }

  /**
   * Adds a {@code EXISTS} operator.
   *
   * @param subSelectContext the sub-select context
   * @throws NullPointerException if {@code subSelectContext} is null
   */
  public void exists(SubSelectContext<?> subSelectContext) {
    Objects.requireNonNull(subSelectContext);
    add(new Criterion.Exists(subSelectContext.get()));
  }

  /**
   * Adds a {@code NOT EXISTS} operator.
   *
   * @param subSelectContext the sub-select context
   * @throws NullPointerException if {@code subSelectContext} is null
   */
  public void notExists(SubSelectContext<?> subSelectContext) {
    Objects.requireNonNull(subSelectContext);
    add(new Criterion.NotExists(subSelectContext.get()));
  }

  /**
   * Creates a sub-select context.
   *
   * @param entityMetamodel the entity model
   * @param <ENTITY> the entity type
   * @return the sub-select context
   * @throws NullPointerException if {@code entityMetamodel} is null
   */
  public <ENTITY> SubSelectFromDeclaration<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return new SubSelectFromDeclaration<>(entityMetamodel);
  }
}
