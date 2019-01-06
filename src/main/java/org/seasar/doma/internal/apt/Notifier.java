package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.message.MessageResource;

public final class Notifier {

  private Context ctx;

  public Notifier(Context ctx) {
    this.ctx = ctx;
  }

  public void debug(MessageResource messageResource, Object[] args) {
    assertNotNull(messageResource, args);
    Messager messager = ctx.getEnv().getMessager();
    messager.printMessage(Kind.OTHER, messageResource.getMessage(args));
  }

  public void debug(CharSequence message) {
    assertNotNull(message);
    Messager messager = ctx.getEnv().getMessager();
    messager.printMessage(Kind.OTHER, message);
  }

  public void notify(Kind kind, MessageResource messageResource, Object[] args) {
    assertNotNull(messageResource, args);
    Messager messager = ctx.getEnv().getMessager();
    messager.printMessage(kind, messageResource.getMessage(args));
  }

  public void notify(Kind kind, MessageResource messageResource, Element element, Object[] args) {
    assertNotNull(kind, element, args);
    Messager messager = ctx.getEnv().getMessager();
    messager.printMessage(kind, messageResource.getMessage(args), element);
  }

  public void notify(Kind kind, String message, Element element) {
    assertNotNull(kind, message, element);
    Messager messager = ctx.getEnv().getMessager();
    messager.printMessage(kind, message, element);
  }

  public void notify(AptException e) {
    assertNotNull(e);
    Messager messager = ctx.getEnv().getMessager();
    messager.printMessage(
        e.getKind(),
        e.getMessage(),
        e.getElement(),
        e.getAnnotationMirror(),
        e.getAnnotationValue());
  }
}
