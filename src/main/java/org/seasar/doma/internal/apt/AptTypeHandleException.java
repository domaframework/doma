package org.seasar.doma.internal.apt;

import javax.lang.model.element.TypeElement;
import org.seasar.doma.message.Message;

public class AptTypeHandleException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AptTypeHandleException(TypeElement typeElement, Throwable cause) {
    super(makeMessage(typeElement), cause);
  }

  private static String makeMessage(TypeElement typeElement) {
    return Message.DOMA4300.getMessage(typeElement.getQualifiedName());
  }
}
