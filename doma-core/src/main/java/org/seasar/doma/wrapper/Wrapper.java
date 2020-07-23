package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * A wrapper for a basic type.
 *
 * <p>The implementation instance is not required to be thread safe.
 *
 * @param <BASIC> The basic type
 */
public interface Wrapper<BASIC> {

  /**
   * Returns the value.
   *
   * <p>The value may be {@code null}.
   *
   * @return the value.
   */
  BASIC get();

  /**
   * Sets the value.
   *
   * @param value the value
   */
  void set(BASIC value);

  /**
   * Return a copy of the value.
   *
   * @return a copy of the value
   */
  BASIC getCopy();

  /**
   * Returns the default value.
   *
   * @return the boxed default value of a primitive type if this is a wrapper for a boxed type, else
   *     {@code null}
   */
  BASIC getDefault();

  /**
   * Whether this object has an equal value to the other one.
   *
   * @param other the other object
   * @return {@code true} if this object has an equal value to the other one.
   */
  boolean hasEqualValue(Object other);

  /**
   * Returns the class of the basic type.
   *
   * @return the class of the basic type
   */
  Class<BASIC> getBasicClass();

  /**
   * Whether this object is a wrapper for a primitive type.
   *
   * @return {@code true} if this object is a wrapper for a primitive type.
   */
  default boolean isPrimitiveWrapper() {
    return false;
  }

  /**
   * Accept a visitor.
   *
   * @param <R> The result
   * @param <P> The first parameter type
   * @param <Q> The second parameter type
   * @param <TH> The error or exception type
   * @param visitor the visitor
   * @param p the first parameter
   * @param q the second parameter
   * @return the result
   * @throws TH the error or exception type
   * @throws DomaNullPointerException if {@code visitor} is {@code null}
   */
  <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q) throws TH;
}
