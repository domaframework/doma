package org.seasar.doma.internal.apt;

import java.io.Serial;

public class AptIllegalStateException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  public AptIllegalStateException(String message) {
    super(message);
  }
}
