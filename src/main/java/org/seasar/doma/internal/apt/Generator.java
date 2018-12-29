package org.seasar.doma.internal.apt;

import java.io.Closeable;

public interface Generator extends Closeable {

  void generate();
}
