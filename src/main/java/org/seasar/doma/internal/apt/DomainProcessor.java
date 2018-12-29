package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.meta.DomainMeta;
import org.seasar.doma.internal.apt.meta.DomainMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Domain"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.TEST,
  Options.DEBUG
})
public class DomainProcessor extends AbstractGeneratingProcessor<DomainMeta> {

  public DomainProcessor() {
    super(Domain.class);
  }

  @Override
  protected DomainMetaFactory createTypeElementMetaFactory() {
    return new DomainMetaFactory(processingEnv);
  }

  @Override
  protected Generator createGenerator(TypeElement typeElement, DomainMeta meta) throws IOException {
    assertNotNull(typeElement, meta);
    return new DomainTypeGenerator(processingEnv, typeElement, meta);
  }
}
