package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Consumer;

public class Code {

  private final Consumer<Printer> consumer;

  public Code(Consumer<Printer> consumer) {
    assertNotNull(consumer);
    this.consumer = consumer;
  }

  public void print(Printer p) {
    assertNotNull(p);
    consumer.accept(p);
  }
}
