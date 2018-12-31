package org.seasar.doma.internal.apt.generator;

import java.io.Closeable;

public interface Generator extends Closeable {

  void generate();
}
