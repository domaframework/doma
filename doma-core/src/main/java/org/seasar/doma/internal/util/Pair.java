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
