package org.seasar.doma.internal.apt;

import java.io.Closeable;

/** @author taedium */
public interface Generator extends Closeable {

  void generate();
}
