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

public final class Tuple4<T1, T2, T3, T4> {
  private final T1 item1;
  private final T2 item2;
  private final T3 item3;
  private final T4 item4;

  public Tuple4(T1 item1, T2 item2, T3 item3, T4 item4) {
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
    this.item4 = item4;
  }

  public T1 getItem1() {
    return item1;
  }

  public T2 getItem2() {
    return item2;
  }

  public T3 getItem3() {
    return item3;
  }

  public T4 getItem4() {
    return item4;
  }

  public T1 component1() {
    return item1;
  }

  public T2 component2() {
    return item2;
  }

  public T3 component3() {
    return item3;
  }

  public T4 component4() {
    return item4;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(item1, item2, item3, item4);
  }

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
