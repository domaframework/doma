package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;

public class LazyFormatter {

  private Function<Function<Object, CharSequence>, CharSequence> lazy;

  public LazyFormatter(Function<Function<Object, CharSequence>, CharSequence> lazy) {
    assertNotNull(lazy);
    this.lazy = lazy;
  }

  public CharSequence run(Function<Object, CharSequence> converter) {
    return lazy.apply(converter);
  }
}
