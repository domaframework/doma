package org.seasar.doma.jdbc.criteria;

import java.util.Objects;

public final class Tuple2<A, B> {
  private final A first;
  private final B second;

  public Tuple2(A first, B second) {
    this.first = first;
    this.second = second;
  }

  public A first() {
    return first;
  }

  public B second() {
    return second;
  }

  public A component1() {
    return first;
  }

  public B component2() {
    return second;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tuple2)) return false;
    Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
    return Objects.equals(first, tuple2.first) && Objects.equals(second, tuple2.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public String toString() {
    return "Tuple2{" + "first=" + first + ", second=" + second + '}';
  }
}
