package org.seasar.doma.internal.util;

import java.util.HashSet;
import java.util.Set;

public class Combinations<T> {

  private final Set<Pair<T, T>> pairs = new HashSet<>();

  public boolean contains(Pair<T, T> pair) {
    if (pairs.contains(pair)) {
      return true;
    }
    return pairs.contains(new Pair<>(pair.snd, pair.fst));
  }

  public void add(Pair<T, T> pair) {
    pairs.add(pair);
  }
}
