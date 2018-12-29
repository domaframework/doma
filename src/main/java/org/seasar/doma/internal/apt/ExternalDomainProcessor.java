package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.meta.ExternalDomainMeta;
import org.seasar.doma.internal.apt.meta.ExternalDomainMetaFactory;

/** @author taedium */
@SupportedAnnotationTypes({"org.seasar.doma.ExternalDomain"})
@SupportedOptions({Options.VERSION_VALIDATION, Options.RESOURCES_DIR, Options.TEST, Options.DEBUG})
public class ExternalDomainProcessor extends AbstractGeneratingProcessor<ExternalDomainMeta> {

  public ExternalDomainProcessor() {
    super(ExternalDomain.class);
  }

  @Override
  protected ExternalDomainMetaFactory createTypeElementMetaFactory() {
    return new ExternalDomainMetaFactory(processingEnv);
  }

  @Override
  protected Generator createGenerator(TypeElement typeElement, ExternalDomainMeta meta)
      throws IOException {
    assertNotNull(typeElement, meta);
    return new ExternalDomainTypeGenerator(processingEnv, meta.getDomainElement(), meta);
  }
}
