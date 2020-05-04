package org.seasar.doma.jdbc.criteria;

import java.util.Objects;

public final class Tuple3<A, B, C> {
  private final A first;
  private final B second;
  private final C third;

  public Tuple3(A first, B second, C third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public A first() {
    return first;
  }

  public B second() {
    return second;
  }

  public C third() {
    return third;
  }

  public A component1() {
    return first;
  }

  public B component2() {
    return second;
  }

  public C component3() {
    return third;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tuple3)) return false;
    Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
    return Objects.equals(first, tuple3.first)
        && Objects.equals(second, tuple3.second)
        && Objects.equals(third, tuple3.third);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second, third);
  }

  @Override
  public String toString() {
    return "Tuple3{" + "first=" + first + ", second=" + second + ", third=" + third + '}';
  }
}
