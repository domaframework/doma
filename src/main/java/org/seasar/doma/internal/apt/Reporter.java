package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.message.MessageResource;

public final class Reporter {

  private Messager messager;

  Reporter(ProcessingEnvironment env) {
    assertNotNull(env);
    this.messager = env.getMessager();
  }

  public void debug(MessageResource messageResource, Object[] args) {
    assertNotNull(messageResource, args);
    messager.printMessage(Kind.OTHER, messageResource.getMessage(args));
  }

  public void debug(CharSequence message) {
    assertNotNull(message);
    messager.printMessage(Kind.OTHER, message);
  }

  public void report(Kind kind, MessageResource messageResource, Object[] args) {
    assertNotNull(messageResource, args);
    messager.printMessage(kind, messageResource.getMessage(args));
  }

  public void report(Kind kind, MessageResource messageResource, Element element, Object[] args) {
    assertNotNull(kind, element, args);
    messager.printMessage(kind, messageResource.getMessage(args), element);
  }

  public void report(Kind kind, String message, Element element) {
    assertNotNull(kind, message, element);
    messager.printMessage(kind, message, element);
  }

  public void report(AptException e) {
    assertNotNull(e);
    messager.printMessage(
        e.getKind(),
        e.getMessage(),
        e.getElement(),
        e.getAnnotationMirror(),
        e.getAnnotationValue());
  }
}
