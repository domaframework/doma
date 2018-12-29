package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.message.MessageResource;

/** @author taedium */
public final class Notifier {

  public static void debug(
      ProcessingEnvironment env, MessageResource messageResource, Object[] args) {
    assertNotNull(env, messageResource, args);
    Messager messager = env.getMessager();
    messager.printMessage(Kind.OTHER, messageResource.getMessage(args));
  }

  public static void debug(ProcessingEnvironment env, CharSequence message) {
    assertNotNull(env, message);
    Messager messager = env.getMessager();
    messager.printMessage(Kind.OTHER, message);
  }

  public static void notify(
      ProcessingEnvironment env, Kind kind, MessageResource messageResource, Object[] args) {
    assertNotNull(env, messageResource, args);
    Messager messager = env.getMessager();
    messager.printMessage(kind, messageResource.getMessage(args));
  }

  public static void notify(
      ProcessingEnvironment env,
      Kind kind,
      MessageResource messageResource,
      Element element,
      Object[] args) {
    assertNotNull(env, kind, element, args);
    Messager messager = env.getMessager();
    messager.printMessage(kind, messageResource.getMessage(args), element);
  }

  public static void notify(ProcessingEnvironment env, Kind kind, String message, Element element) {
    assertNotNull(env, kind, message, element);
    Messager messager = env.getMessager();
    messager.printMessage(kind, message, element);
  }

  public static void notify(ProcessingEnvironment env, AptException e) {
    assertNotNull(env, e);
    Messager messager = env.getMessager();
    messager.printMessage(
        e.getKind(),
        e.getMessage(),
        e.getElement(),
        e.getAnnotationMirror(),
        e.getAnnotationValue());
  }
}
