package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.message.MessageResource;

public final class Notifier {

  private final Messager messager;

  public Notifier(Context ctx) {
    assertNotNull(ctx);
    this.messager = ctx.getEnv().getMessager();
  }

  public void debug(MessageResource messageResource, Object[] args) {
    assertNotNull(messageResource, args);
    messager.printMessage(Kind.OTHER, messageResource.getMessage(args));
  }

  public void send(Kind kind, MessageResource messageResource, Element element, Object[] args) {
    assertNotNull(kind, element, args);
    messager.printMessage(kind, messageResource.getMessage(args), element);
  }

  public void send(Kind kind, String message, Element element) {
    assertNotNull(kind, message, element);
    messager.printMessage(kind, message, element);
  }

  public void send(AptException e) {
    assertNotNull(e);
    messager.printMessage(
        e.getKind(),
        e.getMessage(),
        e.getElement(),
        e.getAnnotationMirror(),
        e.getAnnotationValue());
  }
}
