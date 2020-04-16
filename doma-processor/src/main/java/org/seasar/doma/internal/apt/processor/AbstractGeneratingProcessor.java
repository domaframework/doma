package org.seasar.doma.internal.apt.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.util.Formatter;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.message.Message;

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
    ClassName className = createClassName(typeElement, meta);
    Formatter formatter = null;
    try {
      JavaFileObject file = ctx.getResources().createSourceFile(className, typeElement);
      formatter = new Formatter(new BufferedWriter(file.openWriter()));
      Generator generator = createGenerator(className, new Printer(ctx, formatter), meta);
      generator.generate();
    } catch (IOException | UncheckedIOException e) {
      throw new AptException(
          Message.DOMA4011, typeElement, e, new Object[] {typeElement.getQualifiedName(), e});
    } finally {
      IOUtil.close(formatter);
    }
  }

  protected abstract ClassName createClassName(TypeElement typeElement, M meta);

  protected abstract Generator createGenerator(ClassName className, Printer printer, M meta);
}
