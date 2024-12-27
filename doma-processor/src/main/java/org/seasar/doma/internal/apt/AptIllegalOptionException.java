package org.seasar.doma.internal.apt;

import java.io.Serial;

public class AptIllegalOptionException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  public AptIllegalOptionException(String message) {
    super(message);
  }
}
