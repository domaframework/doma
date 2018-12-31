package org.seasar.doma.internal.apt.processor;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.internal.apt.*;
import org.seasar.doma.message.Message;

public abstract class AbstractProcessor extends javax.annotation.processing.AbstractProcessor {

  protected Class<? extends Annotation> supportedAnnotationType;

  protected Context ctx;

  protected AbstractProcessor(Class<? extends Annotation> supportedAnnotationType) {
    this.supportedAnnotationType = supportedAnnotationType;
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.ctx = new Context(processingEnv);
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
    if (ctx.getOptions().isDebugEnabled()) {
      ctx.getNotifier()
          .debug(
              Message.DOMA4090,
              new Object[] {getClass().getName(), typeElement.getQualifiedName()});
    }
    try {
      handler.accept(typeElement);
    } catch (AptException e) {
      ctx.getNotifier().notify(e);
    } catch (AptIllegalOptionException e) {
      ctx.getNotifier().notify(Kind.ERROR, e.getMessage(), typeElement);
      throw new AptTypeHandleException(typeElement, e);
    } catch (AptIllegalStateException e) {
      ctx.getNotifier().notify(Kind.ERROR, Message.DOMA4039, typeElement, new Object[] {});
      throw new AptTypeHandleException(typeElement, e);
    } catch (RuntimeException e) {
      ctx.getNotifier().notify(Kind.ERROR, Message.DOMA4016, typeElement, new Object[] {});
      throw new AptTypeHandleException(typeElement, e);
    }
    if (ctx.getOptions().isDebugEnabled()) {
      ctx.getNotifier()
          .debug(
              Message.DOMA4091,
              new Object[] {getClass().getName(), typeElement.getQualifiedName()});
    }
  }
}
