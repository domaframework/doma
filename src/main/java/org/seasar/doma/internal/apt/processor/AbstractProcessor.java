package org.seasar.doma.internal.apt.processor;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.internal.apt.*;
import org.seasar.doma.message.Message;

public abstract class AbstractProcessor extends javax.annotation.processing.AbstractProcessor {

  protected Class<? extends Annotation> supportedAnnotationType;

  protected AbstractProcessor(Class<? extends Annotation> supportedAnnotationType) {
    this.supportedAnnotationType = supportedAnnotationType;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  protected void handleTypeElement(TypeElement typeElement, Consumer<TypeElement> handler) {
    Annotation annotation = typeElement.getAnnotation(supportedAnnotationType);
    if (annotation == null) {
      return;
    }
    if (Options.isDebugEnabled(processingEnv)) {
      Notifier.debug(
          processingEnv,
          Message.DOMA4090,
          new Object[] {getClass().getName(), typeElement.getQualifiedName()});
    }
    try {
      handler.accept(typeElement);
    } catch (AptException e) {
      Notifier.notify(processingEnv, e);
    } catch (AptIllegalOptionException e) {
      Notifier.notify(processingEnv, Kind.ERROR, e.getMessage(), typeElement);
      throw new AptTypeHandleException(typeElement, e);
    } catch (AptIllegalStateException e) {
      Notifier.notify(processingEnv, Kind.ERROR, Message.DOMA4039, typeElement, new Object[] {});
      throw new AptTypeHandleException(typeElement, e);
    } catch (RuntimeException e) {
      Notifier.notify(processingEnv, Kind.ERROR, Message.DOMA4016, typeElement, new Object[] {});
      throw new AptTypeHandleException(typeElement, e);
    }
    if (Options.isDebugEnabled(processingEnv)) {
      Notifier.debug(
          processingEnv,
          Message.DOMA4091,
          new Object[] {getClass().getName(), typeElement.getQualifiedName()});
    }
  }
}
