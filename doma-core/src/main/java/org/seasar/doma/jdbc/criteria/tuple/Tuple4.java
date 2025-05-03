/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.criteria.tuple;

import java.util.Objects;

/**
 * A tuple class that holds four elements of potentially different types.
 *
 * <p>This class is used in the criteria API to represent quadruplets of values returned from
 * database queries, allowing for type-safe access to multiple columns in query results.
 *
 * @param <T1> the type of the first element
 * @param <T2> the type of the second element
 * @param <T3> the type of the third element
 * @param <T4> the type of the fourth element
 */
public final class Tuple4<T1, T2, T3, T4> {
  private final T1 item1;
  private final T2 item2;
  private final T3 item3;
  private final T4 item4;

  /**
   * Constructs a new tuple with the specified elements.
   *
   * @param item1 the first element
   * @param item2 the second element
   * @param item3 the third element
   * @param item4 the fourth element
   */
  public Tuple4(T1 item1, T2 item2, T3 item3, T4 item4) {
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
    this.item4 = item4;
  }

  /**
   * Returns the first element of this tuple.
   *
   * @return the first element
   */
  public T1 getItem1() {
    return item1;
  }

  /**
   * Returns the second element of this tuple.
   *
   * @return the second element
   */
  public T2 getItem2() {
    return item2;
  }

  /**
   * Returns the third element of this tuple.
   *
   * @return the third element
   */
  public T3 getItem3() {
    return item3;
  }

  /**
   * Returns the fourth element of this tuple.
   *
   * @return the fourth element
   */
  public T4 getItem4() {
    return item4;
  }

  /**
   * Returns the first element of this tuple.
   * 
   * <p>This method provides Kotlin-style component access.
   *
   * @return the first element
   */
  public T1 component1() {
    return item1;
  }

  /**
   * Returns the second element of this tuple.
   * 
   * <p>This method provides Kotlin-style component access.
   *
   * @return the second element
   */
  public T2 component2() {
    return item2;
  }

  /**
   * Returns the third element of this tuple.
   * 
   * <p>This method provides Kotlin-style component access.
   *
   * @return the third element
   */
  public T3 component3() {
    return item3;
  }

  /**
   * Returns the fourth element of this tuple.
   * 
   * <p>This method provides Kotlin-style component access.
   *
   * @return the fourth element
   */
  public T4 component4() {
    return item4;
  }

  /**
   * Compares this tuple with the specified object for equality.
   *
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tuple4)) return false;
    Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) o;
    return Objects.equals(item1, tuple4.item1)
        && Objects.equals(item2, tuple4.item2)
        && Objects.equals(item3, tuple4.item3)
        && Objects.equals(item4, tuple4.item4);
  }

  /**
   * Returns the hash code value for this tuple.
   *
   * @return the hash code value
   */
  @Override
  public int hashCode() {
    return Objects.hash(item1, item2, item3, item4);
  }

  /**
   * Returns a string representation of this tuple.
   *
   * @return a string representation
   */
  @Override
  public String toString() {
    return "Tuple4{"
        + "item1="
        + item1
        + ", item2="
        + item2
        + ", item3="
        + item3
        + ", item4="
        + item4
        + '}';
  }
}
