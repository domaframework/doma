package org.seasar.doma.jdbc.criteria.declaration;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.LikeOption;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.criteria.tuple.Tuple3;

public abstract class ComparisonDeclaration {

  private final Supplier<List<Criterion>> getter;
  private final Consumer<List<Criterion>> setter;

  protected ComparisonDeclaration(
      Supplier<List<Criterion>> getter, Consumer<List<Criterion>> setter) {
    this.getter = Objects.requireNonNull(getter);
    this.setter = Objects.requireNonNull(setter);
  }

  /**
   * Adds a {@code =} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operand.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void eq(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Eq(new Operand.Prop(left), new Operand.Param(left, right)));
    }
  }

  /**
   * Adds a {@code =} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void eq(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Eq(new Operand.Prop(left), new Operand.Prop(right)));
  }

  /**
   * Adds a {@code <>} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operand.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void ne(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Ne(new Operand.Prop(left), new Operand.Param(left, right)));
    }
  }

  /**
   * Adds a {@code <>} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void ne(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ne(new Operand.Prop(left), new Operand.Prop(right)));
  }

  /**
   * Adds a {@code >} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operand.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void gt(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Gt(new Operand.Prop(left), new Operand.Param(left, right)));
    }
  }

  /**
   * Adds a {@code >} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void gt(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Gt(new Operand.Prop(left), new Operand.Prop(right)));
  }

  /**
   * Adds a {@code >=} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operand.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void ge(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Ge(new Operand.Prop(left), new Operand.Param(left, right)));
    }
  }

  /**
   * Adds a {@code >=} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void ge(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Ge(new Operand.Prop(left), new Operand.Prop(right)));
  }

  /**
   * Adds a {@code <} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operand.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void lt(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Lt(new Operand.Prop(left), new Operand.Param(left, right)));
    }
  }

  /**
   * Adds a {@code <} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void lt(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Lt(new Operand.Prop(left), new Operand.Prop(right)));
  }

  /**
   * Adds a {@code <=} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operand.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void le(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right != null) {
      add(new Criterion.Le(new Operand.Prop(left), new Operand.Param(left, right)));
    }
  }

  /**
   * Adds a {@code <=} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY> void le(PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.Le(new Operand.Prop(left), new Operand.Prop(right)));
  }

  /**
   * Adds a {@code IS NULL} operator.
   *
   * @param propertyMetamodel the left hand operand
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
   * @param propertyMetamodel the left hand operand
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code propertyMetamodel} is null
   */
  public <PROPERTY> void isNotNull(PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    add(new Criterion.IsNotNull(new Operand.Prop(propertyMetamodel)));
  }

  /**
   * Adds a {@code =} operator or a {@code IS NULL} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition includes the
   *     {@code IS NULL} operator.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void eqOrIsNull(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right == null) {
      add(new Criterion.IsNull(new Operand.Prop(left)));
    } else {
      add(new Criterion.Eq(new Operand.Prop(left), new Operand.Param(left, right)));
    }
  }

  /**
   * Adds a {@code <>} operator or a {@code IS NOT NULL} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition includes the
   *     {@code IS NOT NULL} operator.
   * @param <PROPERTY> the property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY> void neOrIsNotNull(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    if (right == null) {
      add(new Criterion.IsNotNull(new Operand.Prop(left)));
    } else {
      add(new Criterion.Ne(new Operand.Prop(left), new Operand.Param(left, right)));
    }
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
   * Adds a {@code IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition doesn't include
   *     the operator.
   * @param <PROPERTY1> the first property type
   * @param <PROPERTY2> the second property type
   * @param <PROPERTY3> the third property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY1, PROPERTY2, PROPERTY3> void in(
      Tuple3<
              PropertyMetamodel<PROPERTY1>,
              PropertyMetamodel<PROPERTY2>,
              PropertyMetamodel<PROPERTY3>>
          left,
      List<Tuple3<PROPERTY1, PROPERTY2, PROPERTY3>> right) {
    Objects.requireNonNull(left);
    if (right != null) {
      Operand.Prop prop1 = new Operand.Prop(left.getItem1());
      Operand.Prop prop2 = new Operand.Prop(left.getItem2());
      Operand.Prop prop3 = new Operand.Prop(left.getItem3());
      List<Tuple3<Operand.Param, Operand.Param, Operand.Param>> params =
          right.stream()
              .map(
                  triple -> {
                    Operand.Param param1 = new Operand.Param(left.getItem1(), triple.getItem1());
                    Operand.Param param2 = new Operand.Param(left.getItem2(), triple.getItem2());
                    Operand.Param param3 = new Operand.Param(left.getItem3(), triple.getItem3());
                    return new Tuple3<>(param1, param2, param3);
                  })
              .collect(toList());
      add(new Criterion.InTuple3(new Tuple3<>(prop1, prop2, prop3), params));
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
   * @param <PROPERTY3> the third property type
   * @throws NullPointerException if {@code left} is null
   */
  public <PROPERTY1, PROPERTY2, PROPERTY3> void notIn(
      Tuple3<
              PropertyMetamodel<PROPERTY1>,
              PropertyMetamodel<PROPERTY2>,
              PropertyMetamodel<PROPERTY3>>
          left,
      List<Tuple3<PROPERTY1, PROPERTY2, PROPERTY3>> right) {
    Objects.requireNonNull(left);
    if (right != null) {
      Operand.Prop prop1 = new Operand.Prop(left.getItem1());
      Operand.Prop prop2 = new Operand.Prop(left.getItem2());
      Operand.Prop prop3 = new Operand.Prop(left.getItem3());
      List<Tuple3<Operand.Param, Operand.Param, Operand.Param>> params =
          right.stream()
              .map(
                  triple -> {
                    Operand.Param param1 = new Operand.Param(left.getItem1(), triple.getItem1());
                    Operand.Param param2 = new Operand.Param(left.getItem2(), triple.getItem2());
                    Operand.Param param3 = new Operand.Param(left.getItem3(), triple.getItem3());
                    return new Tuple3<>(param1, param2, param3);
                  })
              .collect(toList());
      add(new Criterion.NotInTuple3(new Tuple3<>(prop1, prop2, prop3), params));
    }
  }

  /**
   * Adds a {@code IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY1> the first property type
   * @param <PROPERTY2> the second property type
   * @param <PROPERTY3> the third property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY1, PROPERTY2, PROPERTY3> void in(
      Tuple3<
              PropertyMetamodel<PROPERTY1>,
              PropertyMetamodel<PROPERTY2>,
              PropertyMetamodel<PROPERTY3>>
          left,
      SubSelectContext<
              Tuple3<
                  PropertyMetamodel<PROPERTY1>,
                  PropertyMetamodel<PROPERTY2>,
                  PropertyMetamodel<PROPERTY3>>>
          right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    Operand.Prop prop3 = new Operand.Prop(left.getItem3());
    add(new Criterion.InTuple3SubQuery(new Tuple3<>(prop1, prop2, prop3), right.get()));
  }

  /**
   * Adds a {@code NOT IN} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand
   * @param <PROPERTY1> the first property type
   * @param <PROPERTY2> the second property type
   * @param <PROPERTY3> the third property type
   * @throws NullPointerException if {@code left} or {@code right} is null
   */
  public <PROPERTY1, PROPERTY2, PROPERTY3> void notIn(
      Tuple3<
              PropertyMetamodel<PROPERTY1>,
              PropertyMetamodel<PROPERTY2>,
              PropertyMetamodel<PROPERTY3>>
          left,
      SubSelectContext<
              Tuple3<
                  PropertyMetamodel<PROPERTY1>,
                  PropertyMetamodel<PROPERTY2>,
                  PropertyMetamodel<PROPERTY3>>>
          right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    Operand.Prop prop3 = new Operand.Prop(left.getItem3());
    add(new Criterion.NotInTuple3SubQuery(new Tuple3<>(prop1, prop2, prop3), right.get()));
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

  /**
   * Add a {@code AND} operator.
   *
   * @param block the right hand operand
   * @throws NullPointerException if {@code block} is null
   */
  public void and(Runnable block) {
    Objects.requireNonNull(block);
    runBlock(block, Criterion.And::new);
  }

  /**
   * Add a {@code OR} operator.
   *
   * @param block the right hand operand
   * @throws NullPointerException if {@code block} is null
   */
  public void or(Runnable block) {
    Objects.requireNonNull(block);
    runBlock(block, Criterion.Or::new);
  }

  /**
   * Add a {@code NOT} operator.
   *
   * @param block the right hand operand
   * @throws NullPointerException if {@code block} is null
   */
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
