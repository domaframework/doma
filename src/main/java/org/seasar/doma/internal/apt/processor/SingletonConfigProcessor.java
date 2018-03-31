package org.seasar.doma.internal.apt.processor;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.reflection.SingletonConfigReflection;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.message.Message;

/** @author nakamura-to */
@SupportedAnnotationTypes({"org.seasar.doma.SingletonConfig"})
@SupportedOptions({Options.RESOURCES_DIR, Options.TEST, Options.DEBUG})
public class SingletonConfigProcessor extends AbstractProcessor {

  public SingletonConfigProcessor() {
    super(SingletonConfig.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement a : annotations) {
      for (TypeElement typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(a))) {
        handleTypeElement(typeElement, this::validate);
      }
    }
    return true;
  }

  private void validate(TypeElement typeElement) {
    SingletonConfigReflection mirror =
        ctx.getReflections().newSingletonConfigReflection(typeElement);
    if (mirror == null) {
      throw new AptIllegalStateException("mirror must not be null");
    }
    validateClass(typeElement, mirror);
    validateConstructors(typeElement);
    validateMethod(typeElement, mirror.getMethodValue());
  }

  private void validateClass(TypeElement typeElement, SingletonConfigReflection mirror) {
    if (!ctx.getTypes().isAssignable(typeElement.asType(), Config.class)) {
      throw new AptException(Message.DOMA4253, typeElement, mirror.getAnnotationMirror());
    }
  }

  private void validateConstructors(TypeElement typeElement) {
    ElementFilter.constructorsIn(typeElement.getEnclosedElements())
        .stream()
        .filter(c -> !c.getModifiers().contains(Modifier.PRIVATE))
        .findAny()
        .ifPresent(
            c -> {
              throw new AptException(Message.DOMA4256, c);
            });
  }

  private void validateMethod(TypeElement typeElement, String methodName) {
    Optional<ExecutableElement> method =
        ElementFilter.methodsIn(typeElement.getEnclosedElements())
            .stream()
            .filter(m -> m.getModifiers().containsAll(EnumSet.of(Modifier.STATIC, Modifier.PUBLIC)))
            .filter(m -> ctx.getTypes().isAssignable(m.getReturnType(), Config.class))
            .filter(m -> m.getParameters().isEmpty())
            .filter(m -> m.getSimpleName().toString().equals(methodName))
            .findAny();
    if (!method.isPresent()) {
      throw new AptException(Message.DOMA4254, typeElement, new Object[] {methodName});
    }
  }
}
