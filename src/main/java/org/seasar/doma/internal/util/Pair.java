package org.seasar.doma.internal.util;

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
}
