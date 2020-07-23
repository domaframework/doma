package org.seasar.doma.jdbc.criteria.tuple;

import java.util.Objects;

public final class Tuple6<T1, T2, T3, T4, T5, T6> {
  private final T1 item1;
  private final T2 item2;
  private final T3 item3;
  private final T4 item4;
  private final T5 item5;
  private final T6 item6;

  public Tuple6(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6) {
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
    this.item4 = item4;
    this.item5 = item5;
    this.item6 = item6;
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

  public T5 getItem5() {
    return item5;
  }

  public T6 getItem6() {
    return item6;
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

  public T5 component5() {
    return item5;
  }

  public T6 component6() {
    return item6;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tuple6)) return false;
    Tuple6<?, ?, ?, ?, ?, ?> tuple6 = (Tuple6<?, ?, ?, ?, ?, ?>) o;
    return Objects.equals(item1, tuple6.item1)
        && Objects.equals(item2, tuple6.item2)
        && Objects.equals(item3, tuple6.item3)
        && Objects.equals(item4, tuple6.item4)
        && Objects.equals(item5, tuple6.item5)
        && Objects.equals(item6, tuple6.item6);
  }

  @Override
  public int hashCode() {
    return Objects.hash(item1, item2, item3, item4, item5, item6);
  }

  @Override
  public String toString() {
    return "Tuple6{"
        + "item1="
        + item1
        + ", item2="
        + item2
        + ", item3="
        + item3
        + ", item4="
        + item4
        + ", item5="
        + item5
        + ", item6="
        + item6
        + '}';
  }
}
