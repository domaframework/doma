package org.seasar.doma.internal.apt.processor;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.ExternalHolder;
import org.seasar.doma.HolderConverters;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * @since 1.25.0
 */
@SupportedAnnotationTypes({"org.seasar.doma.HolderConverters"})
@SupportedOptions({Options.RESOURCES_DIR, Options.TEST, Options.DEBUG})
public class HolderConvertersProcessor extends AbstractProcessor {

  public HolderConvertersProcessor() {
    super(HolderConverters.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement a : annotations) {
      for (var typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(a))) {
        handleTypeElement(typeElement, this::validate);
      }
    }
    return true;
  }

  private void validate(TypeElement typeElement) {
    var convertersMirror = ctx.getAnnots().newHolderConvertersAnnot(typeElement);
    for (var convType : convertersMirror.getValueValue()) {
      var convElement = ctx.getTypes().toTypeElement(convType);
      if (convElement == null) {
        continue;
      }
      if (convElement.getAnnotation(ExternalHolder.class) == null) {
        throw new AptException(
            Message.DOMA4196,
            typeElement,
            convertersMirror.getAnnotationMirror(),
            new Object[] {convElement.getQualifiedName()});
      }
    }
  }
}
