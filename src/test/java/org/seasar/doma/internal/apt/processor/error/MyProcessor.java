package org.seasar.doma.internal.apt.processor.error;

import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.apt.processor.AbstractProcessor;

@SupportedAnnotationTypes({"org.seasar.doma.internal.apt.processor.error.MyAnnotation"})
public class MyProcessor extends AbstractProcessor {

  private final Consumer<TypeElement> handler;

  protected MyProcessor(Consumer<TypeElement> handler) {
    super(MyAnnotation.class);
    this.handler = handler;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement a : annotations) {
      for (TypeElement t : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(a))) {
        handleTypeElement(t, handler);
      }
    }
    return true;
  }
}
