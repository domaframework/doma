package org.seasar.doma.internal.apt.processor;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.JavaFileGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;

public abstract class AbstractGeneratingProcessor<M extends TypeElementMeta>
    extends AbstractProcessor {

  protected AbstractGeneratingProcessor(Class<? extends Annotation> supportedAnnotationType) {
    super(supportedAnnotationType);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement a : annotations) {
      final TypeElementMetaFactory<M> factory = createTypeElementMetaFactory();
      for (TypeElement typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(a))) {
        handleTypeElement(
            typeElement,
            t -> {
              M meta = factory.createTypeElementMeta(typeElement);
              if (!meta.isError()) {
                generate(typeElement, meta);
              }
            });
      }
    }
    return true;
  }

  protected abstract TypeElementMetaFactory<M> createTypeElementMetaFactory();

  protected void generate(TypeElement typeElement, M meta) {
    JavaFileGenerator<M> javaFileGenerator =
        new JavaFileGenerator<>(ctx, this::createClassName, this::createGenerator);
    javaFileGenerator.generate(typeElement, meta);
  }

  protected abstract ClassName createClassName(TypeElement typeElement, M meta);

  protected abstract Generator createGenerator(ClassName className, Printer printer, M meta);
}
