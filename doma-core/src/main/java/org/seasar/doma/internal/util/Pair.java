package org.seasar.doma.internal.util;

import java.util.Objects;
import java.util.function.Function;

public class Pair<T, U> {

  public final T fst;

  public final U snd;

  public Pair(T fst, U snd) {
    this.fst = fst;
    this.snd = snd;
  }

  public <RT, RU> Pair<RT, RU> map(Function<T, RT> m1, Function<U, RU> m2) {
    return new Pair<>(m1.apply(fst), m2.apply(snd));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(fst, pair.fst) && Objects.equals(snd, pair.snd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fst, snd);
  }
}
