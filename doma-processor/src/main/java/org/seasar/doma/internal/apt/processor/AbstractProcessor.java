package org.seasar.doma.internal.apt.processor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.internal.apt.*;
import org.seasar.doma.message.Message;

public abstract class AbstractProcessor extends javax.annotation.processing.AbstractProcessor {

  protected final Class<? extends Annotation> supportedAnnotationType;

  protected Context ctx;

  protected AbstractProcessor(Class<? extends Annotation> supportedAnnotationType) {
    this.supportedAnnotationType = supportedAnnotationType;
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    ctx = new Context(processingEnv);
    ctx.init();
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  protected void handleTypeElement(TypeElement element, Consumer<TypeElement> handler) {
    handleElement(element, handler, () -> element.getQualifiedName().toString());
  }

  protected void handleExecutableElement(
      ExecutableElement element, Consumer<ExecutableElement> handler) {
    handleElement(
        element,
        handler,
        () -> {
          Element owner = element.getEnclosingElement();
          return owner + "#" + element.getSimpleName();
        });
  }

  private <E extends Element> void handleElement(
      E element, Consumer<E> handler, Supplier<String> elementNameSupplier) {
    Annotation annotation = element.getAnnotation(supportedAnnotationType);
    if (annotation == null) {
      return;
    }
    if (ctx.getOptions().isDebugEnabled()) {
      ctx.getReporter()
          .debug(Message.DOMA4090, new Object[] {getClass().getName(), elementNameSupplier.get()});
    }
    try {
      handler.accept(element);
    } catch (AptException e) {
      ctx.getReporter().report(e);
    } catch (AptIllegalOptionException e) {
      ctx.getReporter().report(Kind.ERROR, e.getMessage(), element);
      throw e;
    } catch (AptIllegalStateException e) {
      String stackTrace = getStackTraceAsString(e);
      ctx.getReporter().report(Kind.ERROR, Message.DOMA4039, element, new Object[] {stackTrace});
      throw e;
    } catch (RuntimeException | AssertionError e) {
      String stackTrace = getStackTraceAsString(e);
      ctx.getReporter().report(Kind.ERROR, Message.DOMA4016, element, new Object[] {stackTrace});
      throw e;
    }
    if (ctx.getOptions().isDebugEnabled()) {
      ctx.getReporter()
          .debug(Message.DOMA4091, new Object[] {getClass().getName(), elementNameSupplier.get()});
    }
  }

  private String getStackTraceAsString(Throwable throwable) {
    Writer stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }
}
