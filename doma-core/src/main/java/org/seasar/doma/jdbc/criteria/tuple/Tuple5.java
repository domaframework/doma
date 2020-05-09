package org.seasar.doma.jdbc.criteria.tuple;

import java.util.Objects;

public final class Tuple5<T1, T2, T3, T4, T5> {
  private final T1 item1;
  private final T2 item2;
  private final T3 item3;
  private final T4 item4;
  private final T5 item5;

  public Tuple5(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5) {
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
    this.item4 = item4;
    this.item5 = item5;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tuple5)) return false;
    Tuple5<?, ?, ?, ?, ?> tuple5 = (Tuple5<?, ?, ?, ?, ?>) o;
    return Objects.equals(item1, tuple5.item1)
        && Objects.equals(item2, tuple5.item2)
        && Objects.equals(item3, tuple5.item3)
        && Objects.equals(item4, tuple5.item4)
        && Objects.equals(item5, tuple5.item5);
  }

  @Override
  public int hashCode() {
    return Objects.hash(item1, item2, item3, item4, item5);
  }

  @Override
  public String toString() {
    return "Tuple5{"
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
        + '}';
  }
}
