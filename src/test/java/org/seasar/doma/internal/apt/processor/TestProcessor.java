package org.seasar.doma.internal.apt.processor;

import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes({"org.seasar.doma.internal.apt.processor.MyBean"})
public class TestProcessor extends AbstractProcessor {

  private final Consumer<TypeElement> handler;

  protected TestProcessor(Consumer<TypeElement> handler) {
    super(MyBean.class);
    this.handler = handler;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement a : annotations) {
      for (var typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(a))) {
        handleTypeElement(typeElement, handler);
      }
    }
    return true;
  }
}
