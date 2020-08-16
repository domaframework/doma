package org.seasar.doma.jdbc.criteria.option;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

/** Represents the option that decides whether to append the FOR UPDATE clause or not. */
public interface ForUpdateOption {

  /**
   * Indicates that the option does nothing.
   *
   * @return the option
   */
  static ForUpdateOption none() {
    return None.INSTANCE;
  }

  /**
   * Indicate that the option append the FOR UPDATE clause.
   *
   * @param propertyMetamodels properties to be locked
   * @return the option
   */
  static ForUpdateOption basic(PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    return new Basic(Arrays.asList(propertyMetamodels));
  }

  /**
   * Indicate that the option append the FOR UPDATE ... NO WAIT clause.
   *
   * @param propertyMetamodels properties to be locked
   * @return the option
   */
  static ForUpdateOption noWait(PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    return new NoWait(Arrays.asList(propertyMetamodels));
  }

  /**
   * Indicate that the option append the FOR UPDATE ... WAIT clause.
   *
   * @param seconds wait time in seconds
   * @param propertyMetamodels properties to be locked
   * @return the option
   */
  static ForUpdateOption wait(int seconds, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    return new Wait(seconds, Arrays.asList(propertyMetamodels));
  }

  /**
   * Accepts a visitor.
   *
   * @param visitor a visitor.
   */
  void accept(Visitor visitor);

  class None implements ForUpdateOption {
    private static final None INSTANCE = new None();

    private None() {}

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Basic implements ForUpdateOption {
    public final List<PropertyMetamodel<?>> propertyMetamodels;

    public Basic(List<PropertyMetamodel<?>> propertyMetamodels) {
      Objects.requireNonNull(propertyMetamodels);
      this.propertyMetamodels = Collections.unmodifiableList(propertyMetamodels);
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class NoWait implements ForUpdateOption {
    public final List<PropertyMetamodel<?>> propertyMetamodels;

    public NoWait(List<PropertyMetamodel<?>> propertyMetamodels) {
      Objects.requireNonNull(propertyMetamodels);
      this.propertyMetamodels = Collections.unmodifiableList(propertyMetamodels);
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Wait implements ForUpdateOption {
    public final int second;
    public final List<PropertyMetamodel<?>> propertyMetamodels;

    public Wait(int second, List<PropertyMetamodel<?>> propertyMetamodels) {
      Objects.requireNonNull(propertyMetamodels);
      this.second = second;
      this.propertyMetamodels = Collections.unmodifiableList(propertyMetamodels);
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  interface Visitor {
    default void visit(None none) {}

    void visit(Basic basic);

    void visit(NoWait wait);

    void visit(Wait noWait);
  }
}
