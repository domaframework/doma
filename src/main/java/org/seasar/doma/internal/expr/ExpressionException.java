package org.seasar.doma.internal.expr;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.Message;

public class ExpressionException extends DomaException {

  private static final long serialVersionUID = 1L;

  public ExpressionException(Message messageCode, Object... args) {
    super(messageCode, args);
  }

  public ExpressionException(Message messageCode, Throwable cause, Object... args) {
    super(messageCode, cause, args);
  }
}
