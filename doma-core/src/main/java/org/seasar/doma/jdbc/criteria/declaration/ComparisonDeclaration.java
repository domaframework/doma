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

  /**
   * Adds a {@code =} operator.
   *
   * @param left the left hand operand
   * @param right the right hand operand. If this value is null, the query condition does'nt include
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
   * @param right the right hand operand. If this value is null, the query condition does'nt include
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
   * @param right the right hand operand. If this value is null, the query condition does'nt include
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
   * @param right the right hand operand. If this value is null, the query condition does'nt include
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
   * @param right the right hand operand. If this value is null, the query condition does'nt include
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
   * @param right the right hand operand. If this value is null, the query condition does'nt include
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
