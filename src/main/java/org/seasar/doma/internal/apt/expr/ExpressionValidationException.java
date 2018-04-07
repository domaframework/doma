package org.seasar.doma.internal.apt.expr;

import org.seasar.doma.message.MessageResource;

public class ExpressionValidationException extends RuntimeException {

  private final MessageResource messageResource;

  public ExpressionValidationException(MessageResource messageResource, Object... args) {
    super(messageResource.getMessage(args));
    this.messageResource = messageResource;
  }

  public MessageResource getMessageResource() {
    return messageResource;
  }
}
